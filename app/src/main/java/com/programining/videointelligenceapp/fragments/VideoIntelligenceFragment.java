package com.programining.videointelligenceapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.programining.videointelligenceapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoIntelligenceFragment extends Fragment {

    public static final String API_KEY_BEARER = "Bearer ";
    private static final String API_KEY_URL = "https://videointelligence.googleapis.com/v1/videos:annotate";
    /**
     * use this command to print the TOKEN : gcloud auth application-default print-access-token
     */
    private static final String API_TOKEN = "ya29.c.Ko8Bywd3QQkJoYDO_cts64xl6acVXLjQopz2T_xMr_B441LnpeJmYvIXJXAXdFZAdow0GZXV9EsHiABvIPtvM7bNU_nIDZZ9Tnjh7VkZjue2HYI2eWzJGZO0pIKTxg52-5KTQ3lHfQdWbF6TQcFkF0blNXB3F41KT7OaC_2gg9CBzZA7cqmayletjUhZIQHMH40";
    private TextView tvResponse;
    private ProgressBar progressBar;
    private Context mContext;


    public VideoIntelligenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_video_intelligance, container, false);

        progressBar = parentView.findViewById(R.id.progressBar);
        tvResponse = parentView.findViewById(R.id.tv_response);
        tvResponse.setMovementMethod(new ScrollingMovementMethod());
        Button btnRequest = parentView.findViewById(R.id.btn_request);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestVideoIntelligenceAPI();
            }
        });


        return parentView;
    }

    private void requestVideoIntelligenceAPI() {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(mContext);

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                tvResponse.setText(response.toString());
                progressBar.setVisibility(View.GONE);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvResponse.setText(error.toString());
                progressBar.setVisibility(View.GONE);
            }
        };


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                API_KEY_URL,
                getVideoIntelligenceTranscriptJsonObj(),
                successListener,
                errorListener
        ) {
            //pass token to the webservice!
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", API_KEY_BEARER + API_TOKEN);
                return headers;
            }


        };


        queue.add(request);
    }

    private JSONObject getVideoIntelligenceJsonObj() {

        /**
         *
         {
         "inputUri":"gs://yousuf-dialogflow.appspot.com/Videos/iPhone SE - Snooze Edition.mp4",
         "features": ["LABEL_DETECTION"]
         }
         */

        try {
            JSONObject container = new JSONObject();
            container.put("inputUri", "gs://yousuf-dialogflow.appspot.com/Videos/iPhone SE - Snooze Edition.mp4");
            JSONArray features = new JSONArray();
            features.put("LABEL_DETECTION");
            container.put("features", features);
            return container;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getVideoIntelligenceTranscriptJsonObj() {

        /**
         *
         {
         "inputUri": "input-uri",
         "features": ["SPEECH_TRANSCRIPTION"],
         "videoContext": {
         "speechTranscriptionConfig": {
         "languageCode": "language-code",
         "enableAutomaticPunctuation": true,
         "filterProfanity": true
         }
         }
         }
         */

        try {
            JSONObject container = new JSONObject();
            container.put("inputUri", "gs://yousuf-dialogflow.appspot.com/Videos/iPhone SE - Snooze Edition.mp4");
            JSONArray features = new JSONArray();
            features.put("SPEECH_TRANSCRIPTION");
            container.put("features", features);

            JSONObject videoContext = new JSONObject();
            JSONObject speechConfig = new JSONObject();
            speechConfig.put("languageCode", "en-US");
            speechConfig.put("enableAutomaticPunctuation", "true");
            speechConfig.put("filterProfanity", "true");
            videoContext.put("speechTranscriptionConfig", speechConfig);

            container.put("videoContext", videoContext);

            return container;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
