package com.example.spokenwapp.dacast;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.example.spokenwapp.R;
import com.google.gson.Gson;
import com.theoplayer.android.api.THEOplayerView;
import com.theoplayer.android.api.source.SourceDescription;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

class ContentId{
    public int broadcasterId, mediaId;
    public String contentType;

    private ContentId(){}

    public static ContentId parse(String contentIdStr) throws Exception{
        ContentId contentId = new ContentId();

        String[] split = contentIdStr.split("_", 3);
        contentId.broadcasterId = Integer.parseInt(split[0]);
        contentId.mediaId = Integer.parseInt(split[2]);

        if(!split[1].equals("c") && !split[1].equals("f") && !split[1].equals("l") && !split[1].equals("p")){
            throw new Exception("invalid content type: " + split[1]);
        }
        contentId.contentType = split[1];

        return contentId;
    }
}

class ContentInfo{
    public String m3u8Link;
    public String posterLink;
    public String adUrl;

    public ContentInfo(String m3u8Link, String posterLink, String adUrl){
        this.m3u8Link = m3u8Link;
        this.posterLink = posterLink;
        this.adUrl = adUrl;
    }
}

class JsonResponse{
    JsonResponseContentInfo contentInfo;
}

class JsonResponseContentInfo{
    JsonResponseFeatures features;
    String splashscreenUrl;
}

class JsonResponseFeatures{
    JsonResponseWatermark watermark;
}

class JsonResponseWatermark {
    String imageUrl;
}

class ServiceResponse{
    String hls;
}

class PlayerHandler extends Handler {
    public static final int SET_CONTENT_INFO = 1;

    THEOplayerView theoplayer;

    public PlayerHandler(THEOplayerView theoplayer){
        this.theoplayer = theoplayer;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case SET_CONTENT_INFO:
                ContentInfo contentInfo = (ContentInfo)msg.obj;
                SourceDescription.Builder sourceDescription = SourceDescription
                        .Builder.sourceDescription(contentInfo.m3u8Link)
                        .poster(contentInfo.posterLink);

                if(contentInfo.adUrl != null){
                    sourceDescription.ads(contentInfo.adUrl);
                }

                theoplayer.getPlayer().setSource(sourceDescription.build());
                break;
        }
    }
}

public class DacastPlayer {

    private static String TAG = "DacastPlayer";
    private static String JSON_URL_BASE = "https://playback.dacast.com/content/info";
    private static String SERVICE_URL_BASE = "https://playback.dacast.com/content/access";

    private THEOplayerView theoplayer;
    private PlayerHandler handler;
    private RelativeLayout layout;
    private ImageView watermarkImage;
    Context context;

    public DacastPlayer(Activity activity, String contentIdStr) {
        this(activity, contentIdStr, null);
    }

    public DacastPlayer(Activity activity, String contentId, String adUrl) {
        theoplayer = new THEOplayerView(activity);
        handler = new PlayerHandler(theoplayer);
        watermarkImage = new ImageView(activity);
        layout = new RelativeLayout(activity);
        watermarkImage.setClickable(false);
        watermarkImage.setFocusable(false);

        ConstraintLayout.LayoutParams paramsPlayer = new ConstraintLayout
                .LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT);
        paramsPlayer.leftMargin = 0;
        paramsPlayer.topMargin = 0;
        layout.addView(theoplayer, paramsPlayer);
        layout.setBackgroundColor(Color.TRANSPARENT);

        ConstraintLayout.LayoutParams paramsImage = new ConstraintLayout
                .LayoutParams(300, 300);
        paramsImage.leftMargin = 50;
        paramsImage.topMargin = 10;
        layout.addView(watermarkImage, paramsImage);
        watermarkImage.setImageAlpha(90);
        fetchVideoInfo(contentId, adUrl);
    }

    public View getView(){
        return layout;
    }

    public THEOplayerView getTHEOplayer(){
        return theoplayer;
    }

    public void onPause() {
        theoplayer.onPause();
    }

    public void onResume() {
        theoplayer.onResume();
    }

    public void onDestroy() {
        theoplayer.onDestroy();
    }

    private void fetchVideoInfo(final String contentId, final String adUrl){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String provider = "vzaar";
                    if(contentId.contains("_")){
                        provider = "dacast";
                    }

                    String rawJson = httpGet( JSON_URL_BASE + "?contentId="  + contentId + "&provider=" + provider);
                    String rawToken = httpGet(SERVICE_URL_BASE + "?contentId="  + contentId + "&provider=" + provider);

                    Gson gson = new Gson();
                    JsonResponse json = gson.fromJson(rawJson, JsonResponse.class);
                    ServiceResponse token = gson.fromJson(rawToken, ServiceResponse.class);

                    ContentInfo contentInfo = new ContentInfo(token.hls, json.contentInfo.splashscreenUrl, adUrl);
                    handler.sendMessage(handler.obtainMessage(PlayerHandler.SET_CONTENT_INFO, contentInfo));

                    if(json.contentInfo.features.watermark.imageUrl != null){
                        new DownloadImageTask(watermarkImage)
                                .execute("https:" + json.contentInfo.features.watermark.imageUrl);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static String httpGet(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }
}
