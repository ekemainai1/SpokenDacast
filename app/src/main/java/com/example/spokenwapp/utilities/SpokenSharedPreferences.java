package com.example.spokenwapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SpokenSharedPreferences {

    public static final String SPOKEN_SHARED_PREFERENCES = "spoken_shared_preferences";
    public SharedPreferences sharedPreferences;
    Context context;
    public SpokenSharedPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SPOKEN_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
    }

    public void saveDacastLogsDetails(String date, String time, String title) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Date", date);
        editor.putString("Time", time);
        editor.putString("Title", title);
        editor.apply();
    }

    public String getLogDate() {
        return sharedPreferences.getString("Date", "");
    }

    public String getLogtime() {
        return sharedPreferences.getString("Time", "");
    }

    public String getLogTitle() {
        return sharedPreferences.getString("Title", "");
    }

    public void saveDacastLogsVodDetails(String date, String time, String title) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("VodDate", date);
        editor.putString("VodTime", time);
        editor.putString("VodTitle", title);
        editor.apply();
    }

    public String getLogVodDate() {
        return sharedPreferences.getString("VodDate", "");
    }

    public String getLogVodTime() {
        return sharedPreferences.getString("VodTime", "");
    }

    public String getLogVodTitle() {
        return sharedPreferences.getString("VodTitle", "");
    }

    public void saveMediaData(String mediaId, String uri, String title) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MEDIAID", mediaId);
        editor.putString("URI", uri);
        editor.putString("TITLE", title);
        editor.apply();
    }

    public String getSongMediaId() {
        return sharedPreferences.getString("MEDIAID", "");
    }

    public String getSongMediaUri() {
        return sharedPreferences.getString("URI", "");
    }

    public String getSongMediaTitle() {
        return sharedPreferences.getString("TITLE", "");
    }

    public void saveTheoPlayerState(String playing) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Playing", playing);
        editor.apply();
    }

    public String getStreamPlay() {
        return sharedPreferences.getString("Playing", "audio");
    }

    public void saveSysIPAddress(String ipAddress) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SYSIP", ipAddress);
        editor.apply();
    }

    public String getSysIPAddress() {
        return sharedPreferences.getString("SYSIP", "");
    }

    public void saveChurchOnlineThumbnail(String thumbnail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CTHUMB", thumbnail);
        editor.apply();
    }

    public String getChurchOnlineThumbnail() {
        return sharedPreferences.getString("CTHUMB", "");
    }

    public void saveChurchOnline(boolean churchStatus) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Status", churchStatus);
        editor.apply();
    }

    public boolean getChurchOnline() {
        return sharedPreferences.getBoolean("Status", false);
    }

    public void saveDacastContentTitle(String dacastContentTitle) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CONTENT", dacastContentTitle);
        editor.apply();
    }

    public String getDacastContentTitle() {
        return sharedPreferences.getString("CONTENT", "");
    }

    public void saveMediaMetadataPosition(int pos) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("MEDIAMETADATA", pos);
        editor.apply();
    }

    public int getMediaMetadataPosition() {
        return sharedPreferences.getInt("MEDIAMETADATA", 0);
    }

    public void saveDacastVODOnlineState(int online) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("VODOnlineService", online);
        editor.apply();
    }

    public int getVODOnlineServiceState() {
        return sharedPreferences.getInt("VODOnlineService", 0);
    }

    public void saveDacastAudioOnlineState(int online) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("AudioOnlineService", online);
        editor.apply();
    }

    public int getAudioOnlineServiceState() {
        return sharedPreferences.getInt("AudioOnlineService", 0);
    }

    public void saveDacastSpecOnlineState(int online) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("SpecOnlineService", online);
        editor.apply();
    }

    public int getSpecOnlineServiceState() {
        return sharedPreferences.getInt("SpecOnlineService", 0);
    }


    public static void registerSpokenSharedPreferences(Context context,
        SharedPreferences.OnSharedPreferenceChangeListener listener){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SPOKEN_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterSpokenSharedPreferences(Context context,
        SharedPreferences.OnSharedPreferenceChangeListener listener){
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(SPOKEN_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
