package com.fdgproject.firedge.myinmobiliaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Formulario extends Activity {

    private int indice;
    private String tipo = "";
    private Inmueble inmueble = null;
    private EditText etLocalidad, etDireccion, etPrecio;
    private GestorInmueble gi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Bundle b = getIntent().getExtras();
        indice = b.getInt("indice");

        gi = new GestorInmueble(this);
        gi.open();

        inmueble = gi.getRow(indice);

        final String [] tipos = getResources().getStringArray(R.array.tipos);

        Spinner spTipo= (Spinner)findViewById(R.id.sp_tipo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(adapter);
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipo = tipos[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tipo = tipos[0];
            }
        });

        etLocalidad = (EditText)findViewById(R.id.et_localidad);
        etDireccion = (EditText)findViewById(R.id.et_direccion);
        etPrecio = (EditText)findViewById(R.id.et_precio);

        if(inmueble != null){
            etLocalidad.setText(inmueble.getLocalidad());
            etDireccion.setText(inmueble.getDireccion());
            etPrecio.setText(Double.toString(inmueble.getPrecio()));
            int i = 0;
            while(!tipos[i].equals(inmueble.getTipo()))
                i++;
            spTipo.setSelection(i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gi.close();
    }

    public void aceptar_bt(View v){
        String localidad = etLocalidad.getText().toString();
        String direccion = etDireccion.getText().toString();
        double precio;
        try {
            precio = Double.parseDouble(etPrecio.getText().toString());
        }catch (Exception ex){
            precio = -1;
        }
        if(precio != -1 && !localidad.isEmpty() && !direccion.isEmpty()) {
            if (indice == -1) {
                inmueble = new Inmueble(localidad, direccion, tipo, precio);
                gi.insert(inmueble);
                Toast.makeText(this, getString(R.string.formulario_alta), Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                inmueble.setLocalidad(localidad);
                inmueble.setDireccion(direccion);
                inmueble.setTipo(tipo);
                inmueble.setPrecio(precio);
                gi.update(inmueble);
                Toast.makeText(this, getString(R.string.formulario_edicion), Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        } else {
            Toast.makeText(this, getString(R.string.formulario_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelar_bt(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
