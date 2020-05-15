package com.mygdx.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidxFragmentApplication;

public class GameFragment extends AndroidxFragmentApplication {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return the GLSurfaceView on which libgdx is drawing game stuff
        return initializeForView(new Core());
    }
}

