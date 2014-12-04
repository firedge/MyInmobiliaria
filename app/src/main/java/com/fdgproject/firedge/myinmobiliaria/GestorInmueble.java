package com.fdgproject.firedge.myinmobiliaria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Firedge on 17/11/2014.
 */
public class GestorInmueble {
    private Ayudante abd;
    private SQLiteDatabase bd;

    public GestorInmueble(Context c) {
        abd= new Ayudante(c);
    }
    public void open() {
        bd= abd.getWritableDatabase();
    }
    public void openRead() {
        bd= abd.getReadableDatabase();
    }
    public void close() {
        abd.close();
    }

    //INSERTAR
    public long insert(Inmueble objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION,objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO,objeto.getTipo());
        valores.put(Contrato.TablaInmueble.PRECIO,objeto.getPrecio());
        long id = bd.insert(Contrato.TablaInmueble.TABLA,null, valores);
        return id;
    }

    //BORRAR
    public int delete(Inmueble objeto) {
        String condicion= Contrato.TablaInmueble._ID+ " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.delete(Contrato.TablaInmueble.TABLA, condicion,argumentos);
        return cuenta;
    }

    //EDITAR
    public int update(Inmueble objeto) {
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.LOCALIDAD, objeto.getLocalidad());
        valores.put(Contrato.TablaInmueble.DIRECCION, objeto.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO,objeto.getTipo());
        valores.put(Contrato.TablaInmueble.PRECIO,objeto.getPrecio());
        String condicion= Contrato.TablaInmueble._ID+ " = ?";
        String[] argumentos = { objeto.getId() + "" };
        int cuenta = bd.update(Contrato.TablaInmueble.TABLA, valores,condicion, argumentos);
        return cuenta;
    }

    //CONSULTAR
    public List<Inmueble> select(String condicion, String[] parametros, String orden) {
        List<Inmueble> ls = new ArrayList<Inmueble>();
        Cursor cursor= bd.query(Contrato.TablaInmueble.TABLA,null,
                condicion, parametros, null, null, orden); //select * from jugador where condicion order by orden
        cursor.moveToFirst();
        Inmueble objeto;
        while(!cursor.isAfterLast()) {
            objeto = getRow(cursor);
            ls.add(objeto);
            cursor.moveToNext();
        }
        cursor.close();
        return ls;
    }

    public ArrayList<Inmueble> select(){
        return (ArrayList)select(null,null,null);
    }

    public Inmueble getRow(Cursor c) {
        Inmueble objeto= new Inmueble();
        objeto.setId(c.getInt(0));
        objeto.setLocalidad(c.getString(1));
        objeto.setDireccion(c.getString(2));
        objeto.setTipo(c.getString(3));
        objeto.setPrecio(c.getDouble(4));
        return objeto;
    }

    public Inmueble getRow(long id){
        List<Inmueble> jg = select(Contrato.TablaInmueble._ID + " = ?", new String[]{id + ""}, null);
        if(!jg.isEmpty())
            return jg.get(0);
        return null;
    }

    public Cursor getCursor(String condicion, String[] parametros, String orden) {
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, null, condicion, parametros, null, null, orden);
        return cursor;
    }
}
