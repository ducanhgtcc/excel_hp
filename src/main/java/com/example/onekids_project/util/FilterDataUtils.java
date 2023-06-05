package com.example.onekids_project.util;

import org.eclipse.jetty.util.ConcurrentHashSet;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * date 2021-02-17 12:59
 * 
 * @author lavanviet
 */
public class FilterDataUtils {
    /**
     * filter object by properties
     * @param f
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctBy(Function<? super T, ?> f) {
        Set<Object> objects = new ConcurrentHashSet<>();
        return t -> objects.add(f.apply(t));
    }
}
