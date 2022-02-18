package com.example.spokenwapp.dacast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DacastObject {

    @SerializedName("data")
    @Expose
    private List<Data> data;

    @SerializedName("paging")
    @Expose
    private Paging paging;

    @SerializedName("totalCount")
    @Expose
    private String totalCount;

        public void setTotalCount(String totalCount){
            this.totalCount = totalCount;
        }
        public String getTotalCount(){
            return this.totalCount;
        }
        public void setData(List<Data> data){
            this.data = data;
        }
        public List<Data> getData(){
            return this.data;
        }
        public void setPaging(Paging paging){
            this.paging = paging;
        }
        public Paging getPaging(){
            return this.paging;
        }
}
