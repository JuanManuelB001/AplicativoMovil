package com.example.aplicativomovil.ContactoEmergencia;

import android.annotation.SuppressLint;
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

import com.example.aplicativomovil.MainActivity;
import com.example.aplicativomovil.R;
import com.example.aplicativomovil.entidades.ContactoEmergencia;
import com.example.aplicativomovil.registrarse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class contancoEmergenciaActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button btnGuardar;
    //VARIABLE FIREBASE
    private FirebaseFirestore db;
    //AUTENTIFICACION CUENTA
    @SuppressLint("RestrictedApi")
    private FirebaseAuth mAuth;
    public String documentId;
    private Button btnAgregar;
    private EditText txtnombreContacto, txttelefonoContacto, txtcorreoContacto;

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

        //CONFIGURACION EDIT-TEXT
        txtnombreContacto = findViewById(R.id.txtNombreContacto);
        txttelefonoContacto = findViewById(R.id.txtTelefonoContacto);
        txtcorreoContacto = findViewById(R.id.txtEmailContacto);

        //BOTON AGREGAR
        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(this);

        //CONFIGURACION BOTON
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(contancoEmergenciaActivity.this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnGuardar){
            if(!validar()){
                Toast.makeText(contancoEmergenciaActivity.this,"Ingrese todo los valores", Toast.LENGTH_SHORT).show();
            }else{
                btnGuardar.setEnabled(false);
                //INSTANCIAR VALORES DEL EDITTEXT DE ACTIVITY_REGISTRARSE
                String getNombre = getIntent().getStringExtra("nombreUsuario");
                String getTelefono = getIntent().getStringExtra("telefono");
                String getCorreo = getIntent().getStringExtra("correo");
                //VALORES CONTACTO DE EMERGENCIA
                String nombreContacto = txtnombreContacto.getText().toString().trim();
                String telefonoContacto = txttelefonoContacto.getText().toString().trim();
                String correoContacto = txtcorreoContacto.getText().toString().trim();

                //IR AL METODO PARA GUARDAR LOS DATOS EN LA BASE DE FIREBASE
                postUsuario(getNombre, getTelefono, getCorreo,nombreContacto, telefonoContacto,correoContacto);
            }
        }else if(view.getId() == R.id.btnAgregar){
            Toast.makeText(contancoEmergenciaActivity.this,"Número "+documentId,Toast.LENGTH_SHORT).show();
            agregarNuevoContactoEmergencia("usuario_3", "Pedro", "555", "pedro@example.com");
        }
    }
    public boolean validar(){
        String nombre = txtnombreContacto.getText().toString().trim();
        String telefono = txttelefonoContacto.getText().toString().trim();
        String correo = txtcorreoContacto.getText().toString().trim();

        if(nombre.isEmpty() || telefono.isEmpty() ||correo.isEmpty()){
            return false;
        }else{
            return  true;
        }
    }
    public interface CountCallback {
        void onCountReady(int count);
    }
    private void totalDatos(registrarse.CountCallback callback) {
        db.collection("Usuarios").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            count++;
                        }
                        //Toast.makeText(contancoEmergenciaActivity.this, "Total documentos: " + count, Toast.LENGTH_SHORT).show();
                        callback.onCountReady(count);  // Llama al callback con el conteo
                    } else {
                        Log.w("Error", "Error obteniendo documentos.", task.getException());
                        callback.onCountReady(0);  // En caso de error, retorna 0
                    }
                });
    }

    private void postUsuario(String nombre, String telefono, String correoElectronico,String nombreContacto,String telefonoContacto,String correoContacto) {
        Map<String, Object> user = new HashMap<>();
        List<Map<String, Object>> contacto_emergencia = new ArrayList<>();
        ContactoEmergencia cont = new ContactoEmergencia(nombreContacto, telefonoContacto, correoContacto);
        ContactoEmergencia cont2 = new ContactoEmergencia("juanito","333", "jaunit@hotmail.com");

        //GUARDAR LOS DATOS DEL CONTACTO DE EMERGENCIA
        Map<String, Object> contacto = new HashMap<>();
        contacto.put("NombreContacto", cont.getNombreContacto());
        contacto.put("TelefonoContacto", cont.getTelefonoContacto());
        contacto.put("CorreoContacto", cont.getCorreoContacto());
        //SEGUNDA ARREGACIÓN
        Map<String, Object> contacto2 = new HashMap<>();
        contacto2.put("NombreContacto", cont2.getNombreContacto());
        contacto2.put("TelefonoContacto", cont2.getTelefonoContacto());
        contacto2.put("CorreoContacto", cont2.getCorreoContacto());

        contacto_emergencia.add(contacto);
        contacto_emergencia.add(contacto2);

        // AGREGAR VALORES AL MAPA CLAVE-VALOR
        user.put("Nombre", nombre);
        user.put("Telefono", telefono);
        user.put("Correo Electronico", correoElectronico);
        user.put("Contacto Emergencia", contacto_emergencia);
        totalDatos(count -> {
            // Puedes usar 'count' para establecer un ID o cualquier lógica
            documentId = "usuario_" + count;  // Ejemplo: crear un ID basado en el conteo

            // AGREGAR VALORES A LA BASE DE DATOS con el ID específico
            db.collection("Usuarios")
                    .document(documentId)  // Usa el ID específico
                    .set(user)  // Usar 'set' en lugar de 'add'
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(contancoEmergenciaActivity.this, "Se ha registrado el Usuario", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(contancoEmergenciaActivity.this, MainActivity.class);
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


    //////////
    // Función para agregar un nuevo contacto de emergencia a un usuario existente
    private void agregarNuevoContactoEmergencia(String documentId, String nombreContacto, String telefonoContacto, String correoContacto) {
        // Crear el nuevo contacto a agregar
        Map<String, Object> nuevoContacto = new HashMap<>();
        nuevoContacto.put("NombreContacto", nombreContacto);
        nuevoContacto.put("TelefonoContacto", telefonoContacto);
        nuevoContacto.put("CorreoContacto", correoContacto);

        // Obtener el documento del usuario
        db.collection("Usuarios").document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Leer la lista actual de contactos de emergencia
                        List<Map<String, Object>> contactosActuales = (List<Map<String, Object>>) documentSnapshot.get("Contacto Emergencia");
                        if (contactosActuales == null) {
                            contactosActuales = new ArrayList<>();
                        }

                        // Agregar el nuevo contacto a la lista
                        contactosActuales.add(nuevoContacto);

                        // Actualizar el campo "Contacto Emergencia" en Firestore
                        db.collection("Usuarios").document(documentId)
                                .update("Contacto Emergencia", contactosActuales)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(contancoEmergenciaActivity.this, "Contacto de emergencia agregado", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(contancoEmergenciaActivity.this, "Error al actualizar contactos", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(contancoEmergenciaActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(contancoEmergenciaActivity.this, "Error al obtener el documento", Toast.LENGTH_SHORT).show();
                });
    }
    ///////////
}