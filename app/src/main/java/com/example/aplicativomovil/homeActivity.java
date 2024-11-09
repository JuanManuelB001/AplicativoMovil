package com.example.aplicativomovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicativomovil.ContactoEmergencia.ListarUsuariosActivity;
import com.example.aplicativomovil.Mapa.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnmapa,btnrestaurarContrasena;
    private Button btnusuario;
    private Button btnConecDIs;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //INICIALIZAR BOTON MAPA
        btnmapa = findViewById(R.id.btnMapa);
        btnrestaurarContrasena = findViewById(R.id.btnRestaurarContrasena);
        //BUTTON LISTENER
        btnmapa.setOnClickListener(homeActivity.this);
        btnrestaurarContrasena.setOnClickListener(this);

        //BOTON USUARIO
        btnusuario = findViewById(R.id.btnListaContactos);
        btnusuario.setOnClickListener(this);

        //BOTON CONEXION BLUETOOTH
        btnConecDIs = findViewById(R.id.btnConecDIs);
        btnConecDIs.setOnClickListener(homeActivity.this);

        //FIREBASE AUTHENTIFICATION
        mAuth= FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        String auth = String.valueOf(mAuth.getCurrentUser());
        Intent intent = new Intent();
        if(view.getId() == R.id.btnMapa){
            intent = new Intent(homeActivity.this, MapsActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.btnRestaurarContrasena){
            intent = new Intent(homeActivity.this, restaurarContrasenaActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.btnConecDIs){
            intent = new Intent(homeActivity.this, bluetoothConexion.class);
            startActivity(intent);
        }else if(view.getId() == R.id.btnListaContactos){
            intent = new Intent(homeActivity.this, ListarUsuariosActivity.class);
            startActivity(intent);
        }
    }
}