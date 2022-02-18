package com.example.spokenwapp.dacast;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("live_transcoding")
    @Expose
    private boolean live_transcoding;

    @SerializedName("publishing_point_primary")
    @Expose
    private String publishing_point_primary;

    @SerializedName("stream_name")
    @Expose
    private String stream_name;

    @SerializedName("publishing_point_type")
    @Expose
    private String publishing_point_type;

    @SerializedName("publishing_point_backup")
    @Expose
    private String publishing_point_backup;

    @SerializedName("login")
    @Expose
    private String login;

        public void setPublishing_point_type(String publishing_point_type){
            this.publishing_point_type = publishing_point_type;
        }
        public String getPublishing_point_type(){
            return this.publishing_point_type;
        }
        public void setPublishing_point_primary(String publishing_point_primary){
            this.publishing_point_primary = publishing_point_primary;
        }
        public String getPublishing_point_primary(){
            return this.publishing_point_primary;
        }
        public void setPublishing_point_backup(String publishing_point_backup){
            this.publishing_point_backup = publishing_point_backup;
        }
        public String getPublishing_point_backup(){
            return this.publishing_point_backup;
        }
        public void setStream_name(String stream_name){
            this.stream_name = stream_name;
        }
        public String getStream_name(){
            return this.stream_name;
        }
        public void setLogin(String login){
            this.login = login;
        }
        public String getLogin(){
            return this.login;
        }
        public void setPassword(String password){
            this.password = password;
        }
        public String getPassword(){
            return this.password;
        }
        public void setLive_transcoding(boolean live_transcoding){
            this.live_transcoding = live_transcoding;
        }
        public boolean getLive_transcoding(){
            return this.live_transcoding;
        }


}
