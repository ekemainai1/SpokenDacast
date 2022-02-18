package com.example.spokenwapp.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import com.example.spokenwapp.R;

public class SpokenOnlineDialogFragment extends DialogFragment {

    View root;
    public static final String TAG = "OnlineDialogFragment";
    private static String KEY_TITLE = "KEY_TITLE";
    AppCompatTextView compatTextViewDown, compatTextViewShare, compatTextViewDetails;

    public SpokenOnlineDialogFragment() {

    }

    public static SpokenOnlineDialogFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        SpokenOnlineDialogFragment fragment = new SpokenOnlineDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_spoken_online_dialog, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        compatTextViewDown = root.findViewById(R.id.download);
        compatTextViewShare = root.findViewById(R.id.shareAudio);
        compatTextViewDetails = root.findViewById(R.id.audioDetails);

        compatTextViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        compatTextViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        compatTextViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


}
