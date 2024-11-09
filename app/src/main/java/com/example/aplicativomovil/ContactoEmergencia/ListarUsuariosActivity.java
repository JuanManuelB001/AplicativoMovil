package com.example.aplicativomovil.ContactoEmergencia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicativomovil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ListarUsuariosActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnagregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar_usuarios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //INICIALIZAR BOTON
        btnagregar = findViewById(R.id.btnAgregar);
        btnagregar.setOnClickListener(ListarUsuariosActivity.this);

    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if( view.getId() == R.id.btnAgregar){
            intent = new Intent(ListarUsuariosActivity.this, agregarContactoEmergencia.class);
            startActivity(intent);

        }
    }
}