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


        //BOTON USUARIO
        btncrearusuario= findViewById(R.id.btnCrearUsuario);
        btncrearusuario.setOnClickListener(this);

        //BOTON INICIAR SESION
        btniniciarsesion= findViewById(R.id.btnIniciarSesion);
        btniniciarsesion.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
         if (view.getId() == R.id.btnCrearUsuario) {
            intent = new Intent(MainActivity.this, registrarse.class);
            startActivity(intent);

        }else if(view.getId() == R.id.btnIniciarSesion){
            intent = new Intent(MainActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
        }
    }


}