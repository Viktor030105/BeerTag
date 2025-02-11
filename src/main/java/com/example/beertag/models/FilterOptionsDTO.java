package com.example.beertag.models;

public class FilterOptionsDTO {

    private String name;
    private Double minAbv;
    private Double maxAbv;
    private Integer styleId;
    private String sortBy;
    private String sortOrder;

    public FilterOptionsDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinAbv() {
        return minAbv;
    }

    public void setMinAbv(Double minAbv) {
        this.minAbv = minAbv;
    }

    public Integer getStyleId() {
        return styleId;
    }

    public void setStyleId(Integer styleId) {
        this.styleId = styleId;
    }

    public Double getMaxAbv() {
        return maxAbv;
    }

    public void setMaxAbv(Double maxAbv) {
        this.maxAbv = maxAbv;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
