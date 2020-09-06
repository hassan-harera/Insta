package com.example.insta;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.MediaRouteButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SecondFragment extends Fragment {

    private FloatingActionButton fab;
    private View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null){
            fab.setVisibility(View.INVISIBLE);
        }
    }
}