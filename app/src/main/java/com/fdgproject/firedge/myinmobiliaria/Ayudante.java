package com.fdgproject.firedge.myinmobiliaria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Firedge on 14/11/2014.
 */
public class Ayudante extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bdinmuebles.sqlite";
    public static final int DATABASE_VERSION = 1;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+Contrato.TablaInmueble.TABLA+
                " ("+ Contrato.TablaInmueble._ID+
                " integer primary key autoincrement, "+
                Contrato.TablaInmueble.LOCALIDAD+" text, "+
                Contrato.TablaInmueble.DIRECCION+" text, "+
                Contrato.TablaInmueble.TIPO+" text, "+
                Contrato.TablaInmueble.PRECIO+" real)";
        Log.v("SQL", sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists "
                + Contrato.TablaInmueble.TABLA;
        db.execSQL(sql);
        onCreate(db);
    }

}
