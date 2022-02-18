package com.example.spokenwapp.dacast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("next")
    @Expose
    private String next;

    @SerializedName("last")
    @Expose
    private String last;

    @SerializedName("previous")
    @Expose
    private String previous;

    @SerializedName("self")
    @Expose
    private String self;

        public void setSelf(String self){
            this.self = self;
        }
        public String getSelf(){
            return this.self;
        }
        public void setLast(String last){
            this.last = last;
        }
        public String getLast(){
            return this.last;
        }
        public void setNext(String next){
            this.next = next;
        }
        public String getNext(){
            return this.next;
        }
        public void setPrevious(String previous){
            this.previous = previous;
        }
        public String getPrevious(){
            return this.previous;
        }

}
