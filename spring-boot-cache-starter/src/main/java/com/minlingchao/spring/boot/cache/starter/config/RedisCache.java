package com.minlingchao.spring.boot.cache.starter.config;

import com.minlingchao.spring.boot.cache.starter.exception.UnsupportedParamerTypeException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Spring Cache默认提供的RedisCache使用了Transaction，NCR不支持
 */
public class RedisCache implements Cache {

  private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

  private RedisTemplate<String, Object> redisTemplate;
  private boolean usePrefix = false;
  private RedisCacheConfig redisCacheConfig = null;

  public RedisCache(RedisCacheConfig redisCacheConfig,
      RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.redisCacheConfig = redisCacheConfig;
  }

  void setUsePrefix(boolean usePrefix) {
    this.usePrefix = usePrefix;
  }

  @Override
  public String getName() {
    return this.redisCacheConfig.getName();
  }

  @Override
  public ValueWrapper get(Object key) {
    if (isMultiGetActive(key)) {
      final Object result = multiGet(key);
      return () -> result;
    }
    final Object result = redisTemplate.opsForValue().get(buildCacheKey(key));
    if (logger.isDebugEnabled()) {
      logger.debug("RedisCache get, key = {}", buildCacheKey(key));
    }
    if (result == null) {
      return null;
    }
    if (result instanceof RedisNullCache) {
      return () -> null;
    }
    return () -> result;
  }

  @Override
  public <T> T get(Object key, Class<T> type) {
    if (isMultiGetActive(key)) {
      Object result = multiGet(key);
      if (result != null && type != null && !type.isInstance(result)) {
        throw new IllegalStateException(
            "Cached value is not of required type [" + type.getName() + "]: " + result);
      }
      return (T) result;
    }
    Object value = redisTemplate.opsForValue().get(buildCacheKey(key));
    if (logger.isDebugEnabled()) {
      logger.debug("RedisCache get, key = {}, class = {}", buildCacheKey(key), type);
    }
    if (value != null && type != null && !type.isInstance(value)) {
      throw new IllegalStateException(
          "Cached value is not of required type [" + type.getName() + "]: " + value);
    }
    if (value instanceof RedisNullCache) {
      return null;
    }
    return (T) value;
  }

  @Override
  public void evict(Object key) {
    if (isMultiEvictActive(key)) {
      multiEvict(key);
      return;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("RedisCache evict, key = {}", buildCacheKey(key));
    }
    redisTemplate.delete(buildCacheKey(key));
  }

  @Override
  public void put(Object key, Object value) {
    if (value == null) {
      if (redisCacheConfig.isCacheNull()) {
        value = RedisNullCache.INSTANCE;
      } else {
        return;
      }
    }
    if (redisCacheConfig != null && redisCacheConfig.getExpire() > 0) {
      if (logger.isDebugEnabled()) {
        logger.debug("RedisCache put, key = {}, expire = {}, unit = {}", buildCacheKey(key),
            redisCacheConfig.getExpire(), redisCacheConfig.getTimeUnit());
      }
      redisTemplate.opsForValue().set(buildCacheKey(key), value, redisCacheConfig.getExpire(),
          redisCacheConfig.getTimeUnit());
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("RedisCache put, key = {}, no expire", buildCacheKey(key));
      }
      redisTemplate.opsForValue().set(buildCacheKey(key), value);
    }
  }

  private String buildCacheKey(Object key) {
    if (usePrefix) {
      return redisCacheConfig.getName() + ":" + String.valueOf(key);
    } else {
      return String.valueOf(key);
    }
  }

  private String deBuildCacheKey(String cacheKey) {
    if (usePrefix) {
      int index = redisCacheConfig.getName().length();
      return cacheKey.substring(index);
    } else {
      return cacheKey;
    }
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {

    ValueWrapper value = get(key);
    if (value != null) {
      return (T) value.get();
    }

    if (isMultiGetActive(key)) {
      return multiGet(key);
    }

    String lockKey = buildCacheKey(key) + "~lock";
    try {
      T result;
      int retry = redisCacheConfig.getSyncRetry();
      long timeout = redisCacheConfig.getSyncExpire();
      boolean lock = redisTemplate.opsForValue().increment(lockKey, 1) == 1;
      redisTemplate.expire(lockKey, timeout, TimeUnit.SECONDS);
      if (logger.isDebugEnabled()) {
        logger.debug("RedisCache sync get, key = {}, lock = {}", lockKey, lock);
      }
      if (lock) {
        result = valueLoader.call();
        put(key, result);
        redisTemplate.delete(lockKey);
        return result;
      } else {
        while (retry-- > 0) {
          ValueWrapper valueWrapper = get(key);
          if (valueWrapper != null) {
            return (T) valueWrapper.get();
          }
          try {
            Thread.sleep(redisCacheConfig.getSyncSleep());
          } catch (InterruptedException ignore) {
          }
        }
        result = valueLoader.call();
        put(key, result);
        redisTemplate.delete(lockKey);
        return result;
      }
    } catch (Exception e) {
      redisTemplate.delete(lockKey);
      return null;
    }
  }


  private boolean isMultiEvictActive(Object key) {
    if (!redisCacheConfig.isEnhanceEnable()) {
      return false;
    }
    String k = String.valueOf(key);
    if (!k.startsWith(RedisCacheEnhanceTag.MEVICT)) {
      return false;
    }
    String[] split = k.split("\\|");
    if (split.length < 3) {
      return false;
    }
    if (!split[2].contains("#")) {
      return false;
    }
    int listIndex;
    try {
      listIndex = Integer.parseInt(split[1]);
    } catch (NumberFormatException e) {
      return false;
    }
    MethodInvocation invocation = RedisCacheThreadLocal.invocationThreadLocal.get();
    if (invocation == null) {
      return false;
    }
    if (!(invocation instanceof ReflectiveMethodInvocation)) {
      return false;
    }
    Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
    if (parameterTypes == null || parameterTypes.length <= listIndex) {
      return false;
    }
    if (!List.class.isAssignableFrom(parameterTypes[listIndex])) {
      return false;
    }
    return true;
  }


  private boolean isMultiGetActive(Object key) {
    if (!redisCacheConfig.isEnhanceEnable()) {
      return false;
    }
    String k = String.valueOf(key);
    if (!k.startsWith(RedisCacheEnhanceTag.MGET)) {
      return false;
    }
    String[] split = k.split("\\|");
    if (split.length < 4) {
      return false;
    }
    if (!split[2].contains("#")) {
      return false;
    }
    if (!split[3].contains("#")) {
      return false;
    }
    int listIndex;
    try {
      listIndex = Integer.parseInt(split[1]);
    } catch (NumberFormatException e) {
      return false;
    }
    MethodInvocation invocation = RedisCacheThreadLocal.invocationThreadLocal.get();
    if (invocation == null) {
      return false;
    }
    if (!(invocation instanceof ReflectiveMethodInvocation)) {
      return false;
    }
    Class<?>[] parameterTypes = invocation.getMethod().getParameterTypes();
    if (parameterTypes == null || parameterTypes.length <= listIndex) {
      return false;
    }
    if (!Collection.class.isAssignableFrom(parameterTypes[listIndex])) {
      return false;
    }
    Class<?> returnType = invocation.getMethod().getReturnType();
    if (returnType == null) {
      return false;
    }
    if (!List.class.isAssignableFrom(returnType)) {
      return false;
    }
    return true;
  }


  private <T> T multiGet(Object key) {
    try {
      String k = String.valueOf(key);
      String[] split = k.split("\\|");
      Integer listIndex = Integer.parseInt(split[1]);
      String getExpression = split[2];//mget表达式，根据该表达式生成一组key，去cache中批量拿
      String putExpression = split[3];//mput表达式，cache没有命中的情况下，穿透到DB，查询结果回调cache

      ReflectiveMethodInvocation invocation = (ReflectiveMethodInvocation) RedisCacheThreadLocal.invocationThreadLocal
          .get();
      Object[] arguments = invocation.getArguments();
      Collection listObj = (Collection) arguments[listIndex];
      //生成一组key
      List<String> toGetKeys = assembleCacheKeyList(getExpression, listObj);

      //返回
      List<Object> result = new ArrayList<>();

      if (logger.isDebugEnabled()) {
        logger.debug("RedisCache multiGet, keys = {}", toGetKeys);
      }
      if (toGetKeys.isEmpty()) {
        return (T) result;
      }

      Collection<Object> missIds;
      if (List.class.isAssignableFrom(listObj.getClass())) {
        missIds = new ArrayList<>();
      } else if (Set.class.isAssignableFrom(listObj.getClass())) {
        missIds = new LinkedHashSet<>();
      } else {
        throw new UnsupportedParamerTypeException("Unsupported Type: " + listObj.getClass());
      }
      Set<String> missKeys = new HashSet<>();

      //cache中批量拿，分批拿
      List<Object> cacheResult = new ArrayList<>();
      List<List<String>> keysList = splitKeys(toGetKeys, 500);
      for (List<String> keys : keysList) {
        List<Object> subCacheResult = redisTemplate.opsForValue().multiGet(keys);
        cacheResult.addAll(subCacheResult);
      }

      Iterator iterator = listObj.iterator();
      for (int i = 0; iterator.hasNext(); i++) {
        Object obj = cacheResult.get(i);
        Object id = iterator.next();
        if (obj != null) {//拿到了说明命中
          if (!(obj instanceof RedisNullCache)) {
            result.add(obj);
          }
        } else {//没拿到，计入miss
          missIds.add(id);
          missKeys.add(toGetKeys.get(i));
        }
      }
      if (!missIds.isEmpty()) {
        //如果miss不为空，篡改参数，发起db查询
        invocation.getArguments()[listIndex] = missIds;
        Object proceed = invocation.proceed();
        List dbResult = (List) proceed;
        if (!dbResult.isEmpty()) {
          //db查询结果放到返回中
          result.addAll(dbResult);
        }
        //db查询结果，回填到cache
        List<String> toPutKeys = assembleCacheKeyList(putExpression, dbResult);
        for (int i = 0; i < toPutKeys.size(); i++) {
          put(toPutKeys.get(i), dbResult.get(i));
        }
        //db查询不到的，回填null到cache
        if (missIds.size() > dbResult.size()) {
          missKeys.removeAll(toPutKeys);
          for (String missKey : missKeys) {
            put(deBuildCacheKey(missKey), null);
          }
        }
      }
      return (T) result;
    } catch (Throwable throwable) {
      logger.error("multiGet error", throwable);
      throw new RuntimeException(throwable);
    }
  }

  private void multiEvict(Object key) {
    try {
      String k = String.valueOf(key);
      String[] split = k.split("\\|");
      Integer listIndex = Integer.parseInt(split[1]);
      String expression = split[2];//表达式

      ReflectiveMethodInvocation invocation = (ReflectiveMethodInvocation) RedisCacheThreadLocal.invocationThreadLocal
          .get();

      Object[] arguments = invocation.getArguments();

      List listObj = (List) arguments[listIndex];

      //生成一组key
      List<String> toEvictKeys = assembleCacheKeyList(expression, listObj);
//            List<String> toEvictKeys = new ArrayList<>();
//            for (String evictKey : list) {
//                toEvictKeys.add(buildCacheKey(evictKey));
//            }

      if (logger.isDebugEnabled()) {
        logger.debug("RedisCache multiEvict, keys = {}", toEvictKeys);
      }
      //批量删除cache，分批删
      if (!toEvictKeys.isEmpty()) {
        List<List<String>> keysList = splitKeys(toEvictKeys, 500);
        for (List<String> keys : keysList) {
          redisTemplate.delete(keys);
        }
      }
    } catch (Throwable throwable) {
      logger.error("multiEvict error", throwable);
      throw new RuntimeException(throwable);
    }
  }

  //(xxxxx)(#.*)(yyyy)(#.id)
  private List<String> assembleCacheKeyList(String expression, Collection list)
      throws NoSuchFieldException, IllegalAccessException {
    List<String> items = parseBracket(expression);
    List<String> keys = new ArrayList<>();
    for (Object obj : list) {
      StringBuilder key = new StringBuilder("");
      for (String item : items) {
        if (item.startsWith("#.")) {
          String keyField = item.substring(2);
          if (keyField.equals("*")) {
            key.append(obj);
          } else {
            Object value = getValue(obj, keyField);
            key.append(value);
          }
        } else {
          key.append(item);
        }
      }
      keys.add(buildCacheKey(key.toString()));
    }
    return keys;
  }

  //把(xx)(yy)(zz)解析成[xx,yy,zz]，此处会做严格的语法检查
  private List<String> parseBracket(String expression) {
    List<String> result = new ArrayList<>();
    StringBuilder item = new StringBuilder();
    char startChar = expression.charAt(0);
    if (startChar != '(') {
      throw new RuntimeException("not start with '('");
    }
    boolean inner = false;
    for (int i = 0; i < expression.length(); i++) {
      char c = expression.charAt(i);
      if (c == '(') {
        if (inner) {
          throw new RuntimeException("dumplicate '('");
        }
        item = new StringBuilder();
        inner = true;
      } else if (c == ')') {
        if (!inner) {
          throw new RuntimeException("dumplicate ')'");
        }
        if (item.length() > 0) {
          result.add(item.toString());
          item = new StringBuilder();
        }
        inner = false;
      } else {
        if (!inner) {
          throw new RuntimeException("missing '('");
        }
        item.append(c);
      }
    }
    if (item.length() > 0) {
      throw new RuntimeException("missing ')'");
    }
    return result;
  }

  private Object getValue(Object obj, String keyField)
      throws NoSuchFieldException, IllegalAccessException {
    Class<?> clazz = obj.getClass();

    Field field = null;

    for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
      try {
        field = clazz.getDeclaredField(keyField);
        field.setAccessible(true);
        return field.get(obj);
      } catch (Exception e) {
      }
    }
    throw new NoSuchFieldException(keyField);
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    ValueWrapper wrapper = get(key);
    if (wrapper == null) {
      put(key, value);
      return null;
    } else {
      return wrapper;
    }
  }

  @Override
  public Object getNativeCache() {
    return redisTemplate;
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  private static List<List<String>> splitKeys(List<String> collection, int splitSize) {
    if (collection == null) {
      return null;
    }
    if (collection.isEmpty()) {
      return new ArrayList<>();
    }
    List<List<String>> res = new ArrayList<>();
    if (collection.size() < splitSize) {
      res.add(new ArrayList<>(collection));
      return res;
    } else {
      List<String> tmp = new ArrayList<>();
      for (String t : collection) {
        tmp.add(t);
        if (tmp.size() == splitSize) {
          res.add(tmp);
          tmp = new ArrayList<>();
        }
      }
      if (!tmp.isEmpty()) {
        res.add(tmp);
      }
      return res;
    }
  }
}
