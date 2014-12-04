package com.fdgproject.firedge.myinmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ActividadDetalle extends FragmentActivity {

    private ArrayList<String> fotos;
    private int image_index, MAX_IMAGE_COUNT;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_detalle);
        id = getIntent().getExtras().getInt("id");
        fotos = (ArrayList) getIntent().getExtras().getParcelableArrayList("fotos");
        if(fotos.size()>0) {
            Button btnPrevious = (Button) findViewById(R.id.bt_prev);
            btnPrevious.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image_index--;
                    if (image_index == -1) {
                        image_index = MAX_IMAGE_COUNT - 1;
                    }
                    ponerFoto(fotos.get(image_index));
                }
            });

            Button btnNext = (Button) findViewById(R.id.bt_next);
            btnNext.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image_index++;
                    if (image_index == MAX_IMAGE_COUNT) {
                        image_index = 0;
                    }
                    ponerFoto(fotos.get(image_index));
                }
            });

            image_index = 0;
            MAX_IMAGE_COUNT = fotos.size();
            ponerFoto(fotos.get(image_index));
        }
    }

    private void ponerFoto(String ruta){
        ImageView ivFoto = (ImageView) findViewById(R.id.iv_foto);
        File imgFile = new  File(ruta);
        if(imgFile.exists())
        {
            ivFoto.setImageURI(Uri.fromFile(imgFile));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int v = getResources().getConfiguration().orientation;

        switch(v){
            case Configuration.ORIENTATION_LANDSCAPE:
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("resul", id);
                i.putExtras(bundle);
                setResult(Activity.RESULT_OK, i);
                finish();
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
    }
}
