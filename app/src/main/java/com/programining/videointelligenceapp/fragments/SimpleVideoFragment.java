package com.programining.videointelligenceapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.programining.videointelligenceapp.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleVideoFragment extends Fragment {

    private static final int KEY_REQUEST_VIDEO = 100;
    private VideoView videoView;
    private ImageButton ibPlay;
    private ImageView ivPoster;
    private Button btnBrowse;
    private ImageButton ibFullScreen;
    private Context mContext;
    private FullScreenListener mListener;
    private boolean isFullScreen;
    private View parentView;

    public SimpleVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        parentView = inflater.inflate(R.layout.fragment_simple_video, container, false);

        videoView = parentView.findViewById(R.id.video_view);
        ibPlay = parentView.findViewById(R.id.ib_play);
        ivPoster = parentView.findViewById(R.id.iv_poster);
        btnBrowse = parentView.findViewById(R.id.btn_browse);
        ibFullScreen = parentView.findViewById(R.id.ib_full_screen);
        isFullScreen = false;
        ibFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupFullScreen();
            }
        });
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectVideo();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                ibPlay.setVisibility(View.VISIBLE);
                ivPoster.setVisibility(View.VISIBLE);
                ibPlay.setImageResource(R.drawable.ic_replay);
            }
        });

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibPlay.setVisibility(View.GONE);
                ivPoster.setVisibility(View.GONE);
                videoView.start();
            }
        });


        return parentView;
    }

    private void setupFullScreen() {
        if (mListener != null) {
            mListener.onFullScreenClick();
            isFullScreen = !isFullScreen;
            if (isFullScreen) {
                ibFullScreen.setImageResource(R.drawable.ic_fullscreen_exit);
                btnBrowse.setVisibility(View.GONE);
            } else {
                ibFullScreen.setImageResource(R.drawable.ic_fullscreen);
                btnBrowse.setVisibility(View.VISIBLE);
            }
        }


    }

    private void selectVideo() {
        Intent i = new Intent(Intent.ACTION_PICK);/*
        i.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(i, KEY_REQUEST_VIDEO);*/

        i.setType("video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Video"), KEY_REQUEST_VIDEO);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Uri vidUri;
        if (resultCode == RESULT_OK && requestCode == KEY_REQUEST_VIDEO && data != null) {
            vidUri = data.getData();

            //set the video path
            videoView.setVideoURI(vidUri);

            //media controller
            MediaController vidControl = new MediaController(mContext);
            vidControl.setAnchorView(videoView);
            videoView.setMediaController(vidControl);

            ibPlay.setVisibility(View.VISIBLE);
            ivPoster.setVisibility(View.VISIBLE);
            ibPlay.setImageResource(R.drawable.ic_play);
            Glide.with(mContext).load(vidUri).into(ivPoster);


        } else {
            Toast.makeText(mContext, "An error happened will selecting the video!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public void setupFullScreenListener(FullScreenListener listener) {
        mListener = listener;
    }

    public interface FullScreenListener {
        void onFullScreenClick();
    }
}
