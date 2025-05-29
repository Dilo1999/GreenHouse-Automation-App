package com.example.gh_app;

import java.util.List;

public class GrowthData {
    private String imageUrl;
    private String timestamp;
    private List<Double> beanLengths;

    public GrowthData(String imageUrl, String timestamp, List<Double> beanLengths) {
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.beanLengths = beanLengths;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Double> getBeanLengths() {
        return beanLengths;
    }
}
