package com.example.numad22sp_yuesun.link_collector;

import androidx.annotation.NonNull;

public class LinkItem {
    private final String name;
    private final String URL;

    public LinkItem(@NonNull String name, @NonNull String URL) {
        this.name = name;
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }
}
