package com.programining.videointelligenceapp.activites;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.programining.videointelligenceapp.R;
import com.programining.videointelligenceapp.fragments.SimpleVideoFragment;
import com.programining.videointelligenceapp.fragments.VideoIntelligenceFragment;
import com.programining.videointelligenceapp.interfaces.MediatorInterface;

public class MainActivity extends AppCompatActivity implements MediatorInterface {

    private boolean isFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragmentTo(new VideoIntelligenceFragment(), VideoIntelligenceFragment.class.getSimpleName());
    }

    private void displaySimpleVideoFragment() {
        SimpleVideoFragment fragment = new SimpleVideoFragment();
        fragment.setupFullScreenListener(new SimpleVideoFragment.FullScreenListener() {
            @Override
            public void onFullScreenClick() {
                shouldDisplayFullScreen();
            }
        });
        changeFragmentTo(fragment, SimpleVideoFragment.class.getSimpleName());
    }

    private void shouldDisplayFullScreen() {
        if (isFullScreen) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().show();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }
        isFullScreen = !isFullScreen;

    }


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fl_host);
    }


    /**
     * enables changing fragments dynamically
     *
     * @param fragmentToDisplay : allow you to pass the fragment you want to display
     * @param fragmentTag       : allow to pass the fragment tag
     */
    @Override
    public void changeFragmentTo(Fragment fragmentToDisplay, String fragmentTag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(R.id.fl_host, fragmentToDisplay, fragmentTag);
        if (fm.findFragmentByTag(fragmentTag) == null) {
            ft.addToBackStack(fragmentTag);
        }
        ft.commit();
    }


    /**
     * enables fragments to call dynamically!
     */
    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof SimpleVideoFragment)
            finish();
        else
            super.onBackPressed();
    }


}
