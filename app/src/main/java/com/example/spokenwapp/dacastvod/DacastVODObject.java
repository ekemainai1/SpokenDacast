package com.example.spokenwapp.dacastvod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DacastVODObject {

    @SerializedName("totalCount")
    @Expose
    private String totalCount;
    @SerializedName("data")
    @Expose
    private List<VodData> data = null;
    @SerializedName("paging")
    @Expose
    private VodPaging paging;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<VodData> getData() {
        return data;
    }

    public void setData(List<VodData> data) {
        this.data = data;
    }

    public VodPaging getPaging() {
        return paging;
    }

    public void setPaging(VodPaging paging) {
        this.paging = paging;
    }

}
