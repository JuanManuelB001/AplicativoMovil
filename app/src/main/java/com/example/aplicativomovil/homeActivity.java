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

public class homeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnmapa,btnrestaurarContrasena;
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


    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if(view.getId() == R.id.btnMapa){
            intent = new Intent(homeActivity.this, MapsActivity.class);
            startActivity(intent);
        }else if(view.getId() == R.id.btnRestaurarContrasena){
            intent = new Intent(homeActivity.this, restaurarContrasenaActivity.class);
            startActivity(intent);

        }
    }
}