package com.example.spokenwapp.online;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import com.example.spokenwapp.R;
import com.streamaxia.player.StreamaxiaPlayer;
import javax.inject.Inject;

public class SpokenChooseURLViewModel extends ViewModel {

    @Inject
    public SpokenChooseURLViewModel() {
    }

    public static final String URI = "uri";
    public static final String TYPE = "type";

    // Modify this to your desired streams
    public static final String RTMP_URL = "rtsp://192.168.43.118:1935/spokensermon/myStrem.mp4";
    public static final String HLS_URL = "http://192.168.43.118:1935/spokensermon/myStrem/manifest.mpd";
    public static final String DASH_URL = "http://192.168.43.118:1935/spokensermon/myStrem/manifest.mpd";

    public void setRtmpDemoBtn(View view) {
        sendIntent(StreamaxiaPlayer.TYPE_RTMP, view);
    }

    public void setHlsDemoBtn(View view) {
        sendIntent(StreamaxiaPlayer.TYPE_HLS, view);
    }

    public void setDashDemoBtn(View view) {
        sendIntent(StreamaxiaPlayer.TYPE_DASH, view);
    }

    public void sendIntent(int type, View view) {
        Bundle bundle = new Bundle();
        switch (type) {
            case StreamaxiaPlayer.TYPE_RTMP:
                bundle.putString(URI, RTMP_URL);
                bundle.putInt(TYPE, StreamaxiaPlayer.TYPE_RTMP);
                break;
            case StreamaxiaPlayer.TYPE_HLS:
                bundle.putString(URI, HLS_URL);
                bundle.putInt(TYPE, StreamaxiaPlayer.TYPE_HLS);
                break;
            case StreamaxiaPlayer.TYPE_DASH:
                bundle.putString(URI, DASH_URL);
                bundle.putInt(TYPE, StreamaxiaPlayer.TYPE_DASH);
                break;
        }

        Navigation.findNavController(view).navigate(R.id.spokenOnlineService, bundle);

    }

}
