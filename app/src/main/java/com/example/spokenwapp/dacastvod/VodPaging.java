package com.example.spokenwapp.dacastvod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VodPaging {
    @Expose
    private String self;
    @SerializedName("last")
    @Expose
    private String last;
    @SerializedName("next")
    @Expose
    private Object next;
    @SerializedName("previous")
    @Expose
    private Object previous;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Object getNext() {
        return next;
    }

    public void setNext(Object next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }
}
