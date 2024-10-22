package com.example.aplicativomovil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicativomovil.DataBase.dataBaseHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnmapa;
    private Button btncrearusuario;
    private Button btniniciarsesion;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //BOTON IR AL MAPA
        btnmapa = findViewById(R.id.btnMapa);
        btnmapa.setOnClickListener(this);

        //BOTON USUARIO
        btncrearusuario= findViewById(R.id.btnCrearUsuario);
        btncrearusuario.setOnClickListener(this);

        //BOTON INIVIAR SESION
        btniniciarsesion= findViewById(R.id.btnIniciarSesion);
        btniniciarsesion.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if(view.getId() == R.id.btnMapa){
            intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
        else if (view.getId() == R.id.btnCrearUsuario) {
            intent = new Intent(MainActivity.this, registrarse.class);
            dataBaseHelper dbHelper = new dataBaseHelper(MainActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //CREACION BASE DE DATOS
            if (db != null) {
                Toast.makeText(MainActivity.this, "La Base de Datos Se Ha Creado ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Error no se ha podido Crear La Base de Datos...", Toast.LENGTH_LONG).show();
            }
            startActivity(intent);
        }
    }


}