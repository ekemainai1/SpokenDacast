package com.example.spokenwapp.dacastvod;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VodPictures {

        @SerializedName("thumbnail")
        @Expose
        private List<String> thumbnail = null;
        @SerializedName("splashscreen")
        @Expose
        private List<String> splashscreen = null;

        public List<String> getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(List<String> thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<String> getSplashscreen() {
            return splashscreen;
        }

        public void setSplashscreen(List<String> splashscreen) {
            this.splashscreen = splashscreen;
        }
}
