package com.fdgproject.firedge.myinmobiliaria;

import android.provider.BaseColumns;

/**
 * Created by Firedge on 14/11/2014.
 */
public class Contrato {

    private Contrato (){
    }

    public static abstract class TablaInmueble implements BaseColumns {
        public static final String TABLA = "inmueble";
        public static final String LOCALIDAD = "localidad";
        public static final String DIRECCION = "direccion";
        public static final String TIPO = "tipo";
        public static final String PRECIO = "precio";
    }

}
