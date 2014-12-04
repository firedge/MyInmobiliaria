package com.fdgproject.firedge.myinmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class MainActivity extends FragmentActivity {

    private GestorInmueble gi;
    private Adaptador adp;
    private final int FOTO = 1, ACTIVIDADFOTOS = 2;
    private ListView lv;
    private int IDinm = 0;

    /********************************************************************************************/
    /*                                                                                          */
    /*                                   Metodos on...                                          */
    /*                                                                                          */
    /********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gi = new GestorInmueble(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Cursor
        gi.open();
        Cursor c = gi.getCursor(null, null, null);

        //Lista
        adp = new Adaptador(this, c);
        lv = (ListView)findViewById(R.id.lv_principal);
        lv.setAdapter(adp);
        final ImageFragment f = (ImageFragment)getFragmentManager().findFragmentById(R.id.fragment3);
        final boolean horizontal = (f != null && f.isInLayout());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor)lv.getItemAtPosition(i);
                Inmueble inm = gi.getRow(cursor);
                if(horizontal){
                    lanzarImagenes(cogerFotos(inm.getId()));
                } else {
                    Intent intent = new Intent(MainActivity.this, ActividadDetalle.class);
                    intent.putExtra("id", inm.getId());
                    intent.putExtra("fotos", cogerFotos(inm.getId()));
                    startActivityForResult(intent, ACTIVIDADFOTOS);

                }
            }
        });
        registerForContextMenu(lv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gi.close();
    }

    /******************************  Menu principal  *********************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_nuevo) {
            Intent i = new Intent(this, Formulario.class);
            Bundle b = new Bundle();
            b.putInt("indice", -1);
            i.putExtras(b);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /******************************  Menu contextual  *********************************/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        int index = info.position;
        if(id == R.id.action_foto){
            Cursor cursor = (Cursor)lv.getItemAtPosition(index);
            Inmueble inm = gi.getRow(cursor);
            IDinm = inm.getId();
            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(i, FOTO);
            return true;
        } else if(id == R.id.action_editar){
            Intent i = new Intent(this, Formulario.class);
            Bundle b = new Bundle();
            Cursor cursor = (Cursor)lv.getItemAtPosition(index);
            Inmueble inm = gi.getRow(cursor);
            b.putInt("indice", inm.getId());
            i.putExtras(b);
            startActivity(i);
            return true;
        } else if(id == R.id.action_borrar){
            Cursor cursor = (Cursor)lv.getItemAtPosition(index);
            Inmueble inm = gi.getRow(cursor);
            gi.delete(inm);
            adp.changeCursor(gi.getCursor(null,null,null));
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /****************************************************************************************/

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
       if (resultCode == RESULT_OK && requestCode == FOTO) {
            Bitmap foto = (Bitmap)data.getExtras().get("data");
            FileOutputStream salida;
            String ruta = getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/" + generaNombre();
            try {
                salida = new FileOutputStream(ruta);
                foto.compress(Bitmap.CompressFormat.JPEG, 90,salida);
            } catch (FileNotFoundException e) {
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == ACTIVIDADFOTOS) {
           int id = (Integer) data.getExtras().get("resul");
           lanzarImagenes(cogerFotos(id));
           //lanzarImagenes((ArrayList<String>) data.getSerializableExtra("resul"));
       }
    }

    /********************************************************************************************/
    /*                                                                                          */
    /*                                Metodos auxiliares                                        */
    /*                                                                                          */
    /********************************************************************************************/

    public String generaNombre(){
        String s = "inmueble_"+IDinm+"_";
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String formatteDate = df.format(date);
        s+=formatteDate+".jpg";
        IDinm = 0;
        return s;
    }

    private ArrayList<String> cogerFotos(int i){
        ArrayList<String> list = new ArrayList<String>();
        File f = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM).toString());
        File [] lf = f.listFiles();
        String [] trozos;
        int id;
        for(File foto:lf){
            trozos = foto.getName().split("_");
            id = Integer.parseInt(trozos[1]);
            if(id == i)
                list.add(foto.getPath());
        }
        return list;
    }

    private int image_index, MAX_IMAGE_COUNT;
    private void lanzarImagenes(final ArrayList<String> fotos){
        Button btnPrevious = (Button) findViewById(R.id.bt_prev);
        Button btnNext = (Button) findViewById(R.id.bt_next);
        if(fotos.size()>0) {
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
        } else {
            ImageView ivFoto = (ImageView) findViewById(R.id.iv_foto);
            ivFoto.setImageResource(R.drawable.nofoto);
            btnNext.setOnClickListener(null);
            btnPrevious.setOnClickListener(null);
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
}
