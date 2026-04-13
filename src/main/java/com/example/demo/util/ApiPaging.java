package com.example.demo.util;

public final class ApiPaging {
    private ApiPaging() {
    }

    public static boolean isPagedRequest(String q, Integer page, Integer size) {
        return page != null || size != null || (q != null && !q.trim().isEmpty());
    }

    public static int pageOrZero(Integer page) {
        return page != null ? Math.max(0, page) : 0;
    }

    public static int sizeBounded(Integer size, int defaultSize) {
        int s = size != null ? size : defaultSize;
        return Math.min(Math.max(s, 1), 100);
    }
}
