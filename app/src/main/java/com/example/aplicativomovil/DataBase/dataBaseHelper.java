package com.example.aplicativomovil.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class dataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE= "Usuario.db";
    public static final String TABLE_USUARIOS="table_usuarios";


    public dataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //EJECUTAR AL MOMENTO DE LLAMAR A LA CLASE REGISTRARSE
        //CREAR BASE DE DATOS
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_USUARIOS +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"nombre TEXT NOT NULL," +
                "telefono TEXT NOT NULL," +
                "correo_electronico TEXT"+
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //EJECUTA CUANDO CAMBIE LA VERSION DE LA BASE DE DATOS
        //AGREGAR CAMPO DE DB
        sqLiteDatabase.execSQL("DROP TABLE "+TABLE_USUARIOS);//ELIMINAR TABLA
        onCreate(sqLiteDatabase);


    }
}
