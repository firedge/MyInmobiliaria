package com.fdgproject.firedge.myinmobiliaria;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

public class ImageFragment extends Fragment {

    private ImageView ivFoto;

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);
        ivFoto = (ImageView)v.findViewById(R.id.iv_foto);
        return v;
    }

}
