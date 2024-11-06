package com.example.aplicativomovil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicativomovil.entidades.Usuarios;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class contancoEmergenciaActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button btnGuardar;
    //VARIABLE FIREBASE
    private FirebaseFirestore db;
    //AUTENTIFICACION CUENTA
    @SuppressLint("RestrictedApi")
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contanco_emergencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // INICIALIZAR FIREBASE
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //INSTANCIAR VALORES DEL EDITTEXT DE ACTIVITY_REGISTRARSE
        String getNombre = getIntent().getStringExtra("nombreUsuario");
        String getTelefono = getIntent().getStringExtra("telefono");
        String getCorreo = getIntent().getStringExtra("correo");

        Toast.makeText(contancoEmergenciaActivity.this,getTelefono+ " "+ getCorreo , Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnGuardar){
            totalDatos();

        }
    }
    private void totalDatos(registrarse.CountCallback callback) {
        db.collection("Usuarios").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            count++;
                        }
                        Toast.makeText(contancoEmergenciaActivity.this, "Total documentos: " + count, Toast.LENGTH_SHORT).show();
                        callback.onCountReady(count);  // Llama al callback con el conteo
                    } else {
                        Log.w("Error", "Error obteniendo documentos.", task.getException());
                        callback.onCountReady(0);  // En caso de error, retorna 0
                    }
                });
    }

    private void postUsuario(String nombre, String telefono, String correoElectronico) {
        Map<String, Object> user = new HashMap<>();
        Map<String, Object> contacto_emergencia = new HashMap<>();
        // AGREGAR VALORES AL MAPA CLAVE-VALOR
        user.put("Nombre", nombre);
        user.put("Telefono", telefono);
        user.put("Correo Electronico", correoElectronico);
        user.put("Contacto Emergencia", contacto_emergencia);

        //CREAR USUARIO
        Usuarios usuario = new Usuarios(nombre,telefono,correoElectronico);


        totalDatos(count -> {
            // Puedes usar 'count' para establecer un ID o cualquier lógica
            String documentId = "usuario_" + count;  // Ejemplo: crear un ID basado en el conteo

            // AGREGAR VALORES A LA BASE DE DATOS con el ID específico
            db.collection("Usuarios")
                    .document(documentId)  // Usa el ID específico
                    .set(user)  // Usar 'set' en lugar de 'add'
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(contancoEmergenciaActivity.this, contancoEmergenciaActivity.class);
                            intent.putExtra("nombreUsuario",nombre);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(contancoEmergenciaActivity.this, "Fallo al guardar", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}