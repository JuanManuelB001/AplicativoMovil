package com.example.aplicativomovil;

import com.google.android.gms.tasks.Task;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText txtcorreo, txtcontrasena;
    private String getEmail;
    private Button btniniciar,btnregistrarse, btnrestauracontrasena;
    private static final String TAG = "IniciarSesionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_sesion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //INICIALIZAR AUTENTIFICACION
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //INICIARLIZAR TEXT VIEW
        txtcorreo = findViewById(R.id.txtUsuario);
        txtcontrasena = findViewById(R.id.txtCon);

        //INICIARLIZAR BOTONES
        btniniciar = findViewById(R.id.btnIniciarSesion);
        btniniciar.setOnClickListener(this);

        btnregistrarse = findViewById(R.id.btnRegistrarse);
        btnregistrarse.setOnClickListener(IniciarSesionActivity.this);

        btnrestauracontrasena = findViewById(R.id.btnRestaurarContrasena);
        btnrestauracontrasena.setOnClickListener(IniciarSesionActivity.this);
    }

        @Override
        public void onClick(View view) {
        Intent intent = new Intent();

            if (view.getId() == R.id.btnIniciarSesion) {
                String email = txtcorreo.getText().toString();
                String password = txtcontrasena.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (email.isEmpty() || password.isEmpty()) {
                                    Toast.makeText(IniciarSesionActivity.this, "Ingrese Correo y Contraseña", Toast.LENGTH_LONG).show();
                                } else {
                                    if (task.isSuccessful()) {


                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);


                                    } else {
                                        Toast.makeText(IniciarSesionActivity.this, "Usuario o Contraseña Incorrectos.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            }
                        });
            }
            /*else if(view.getId() == R.id.btnRestaurarContrasena){
                intent = new Intent(IniciarSesionActivity.this, restaurarContrasenaActivity.class);
                startActivity(intent);
            }
            */else if(view.getId() == R.id.btnRegistrarse){

                intent = new Intent(IniciarSesionActivity.this, registrarse.class);
                startActivity(intent);
            }
        }




    private void updateUI(FirebaseUser user) {
        if (user != null) {

            //METODO PARA OPTENER EL NOMBRE
            getNombre(optenerEmail(user));
            Intent intent = new Intent(IniciarSesionActivity.this, homeActivity.class);
            startActivity(intent);
            // startActivity(intent);
            // finish(); // Opcional, si no deseas regresar a esta actividad
        } else {
            // Si el usuario no está autenticado, puedes mostrar un mensaje o resetear campos
            Log.d(TAG, "No hay usuario autenticado");
            // Por ejemplo, limpiar los campos de texto
            txtcorreo.setText("");
            txtcontrasena.setText("");
        }
    }
    private void getNombre(String getEmail) {
        // Buscar el documento del usuario por correo electrónico
        db.collection("Usuarios")
                .whereEqualTo("Correo Electronico", getEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Obtener el primer documento encontrado (asumiendo que el correo electrónico es único)
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = document.getId();

                        //OPTENER EL NOMBRE DEL USUARIO
                        String nombreUsuario = document.getString("Nombre");
                        if(nombreUsuario != null){
                            // Usar el nombre del usuario según sea necesario
                            Toast.makeText(this, "Bienvenido " + nombreUsuario, Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                });
    }

    public String optenerEmail(FirebaseUser user){
        String getEmail;
        getEmail = user.getEmail();
        return getEmail;
    }

    }