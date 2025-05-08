package com.minecraft.minepay.util.query;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Query {

    public static class Search<T> {

        private final Result result;
        private final List<T> foundData;

        private Search(Result result) {
            this.result = result;
            this.foundData = Collections.emptyList();
        }

        private Search(Result result, T data) {
            this.result = result;
            this.foundData = Collections.singletonList(data);
        }

        private Search(Result result, List<T> data) {
            this.result = result;
            this.foundData = List.copyOf(data);
        }

        public Result result() {
            return result;
        }

        public boolean isError() {
            return result.equals(Result.ERROR);
        }

        public boolean isEmpty() {
            return result.equals(Result.NOT_FOUND);
        }

        public List<T> all() {
            return foundData;
        }

        public T first() {
            return foundData.isEmpty() ? null : foundData.get(0);
        }

        public static <T> Search<T> notFound() {
            return new Search<T>(Result.NOT_FOUND);
        }

        public static <T> Search<T> foundOne(T data) {
            return new Search<T>(Result.FOUND_ONE, data);
        }

        public static <T> Search<T> foundMultiple(List<T> data) {
            return new Search<T>(Result.FOUND_MULTIPLE, data);
        }

        public static <T> Search<T> foundAuto(List<T> data) {
            return new Search<T>(
                    data.size() > 1 ? Result.FOUND_MULTIPLE : data.isEmpty() ? Result.NOT_FOUND : Result.FOUND_ONE,
                    data);
        }

        public static <T> Search<T> error() {
            return new Search<T>(Result.ERROR);
        }

        public static <T> Search<T> error(T data) {
            return new Search<T>(Result.ERROR, data);
        }
    }

    public static class MapSearch<K, V> {

        private final Result result;
        private final Map<K, V> foundData;

        private MapSearch(Result result) {
            this.result = result;
            this.foundData = Collections.emptyMap();
        }

        private MapSearch(Result result, K key, V value) {
            this.result = result;
            this.foundData = Collections.singletonMap(key, value);
        }

        private MapSearch(Result result, Map<K, V> data) {
            this.result = result;
            this.foundData = Map.copyOf(data);
        }

        public Result result() {
            return result;
        }

        public Map<K, V> all() {
            return foundData;
        }

        public Entry<K, V> first() {
            return foundData.isEmpty() ? null : foundData.entrySet().iterator().next();
        }

        public static <K, V> MapSearch<K, V> notFound() {
            return new MapSearch<K, V>(Result.NOT_FOUND);
        }

        public static <K, V> MapSearch<K, V> foundOne(K key, V value) {
            return new MapSearch<K, V>(Result.FOUND_ONE, key, value);
        }

        public static <K, V> MapSearch<K, V> foundMultiple(Map<K, V> data) {
            return new MapSearch<K, V>(Result.FOUND_MULTIPLE, data);
        }

        public static <K, V> MapSearch<K, V> foundAuto(Map<K, V> data) {
            return new MapSearch<K, V>(
                    data.size() > 1 ? Result.FOUND_MULTIPLE : data.isEmpty() ? Result.NOT_FOUND : Result.FOUND_ONE,
                    data);
        }

        public static <K, V> MapSearch<K, V> error() {
            return new MapSearch<K, V>(Result.ERROR);
        }
    }

    public enum Result {
        NOT_FOUND, FOUND_ONE, FOUND_MULTIPLE, ERROR, MOJANG_NOT_FOUND;
    }
}

