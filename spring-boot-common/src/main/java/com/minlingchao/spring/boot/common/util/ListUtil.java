package com.minlingchao.spring.boot.common.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Author:minlingchao
 * Date: 2017/11/23 10:30
 * Description: list工具类
 */
public class ListUtil {

    public static <T> List<List<T>> subBatchList(List<T> t, Integer limit) {


        if (CollectionUtils.isEmpty(t) || t.size() == 0) {
            return new ArrayList<>();
        }

        List<List<T>> result = Lists.newArrayList();

        int total = t.size();


        //判断是否有必要分批
        if (limit < total) {

            int cycelTotal = total / limit;

            for (int i = 0; i < cycelTotal; i++) {
                List<T> tempList = Lists.newArrayList();
                tempList.addAll(t.subList(0, limit));
                result.add(tempList);
                t.subList(0, limit).clear();
            }

            if (!t.isEmpty()) {
                result.add(t);
            }
        } else {
            result.add(t);
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> all = Lists.newArrayList();

        for (int i = 0; i < 156666; i++) {
            all.add(i + "");
        }

        List<List<String>> r = ListUtil.subBatchList(all, 100);

        for (List<String> s : r) {
            System.out.println(s.size());
        }
    }
}
