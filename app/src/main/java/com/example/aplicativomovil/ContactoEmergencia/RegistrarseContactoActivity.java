package com.example.aplicativomovil.ContactoEmergencia;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicativomovil.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarseContactoActivity extends AppCompatActivity  implements View.OnClickListener{

    private Button btnGuardar;
    //VARIABLE FIREBASE
    private FirebaseFirestore db;
    //AUTENTIFICACION CUENTA
    @SuppressLint("RestrictedApi")
    private FirebaseAuth mAuth;
    public String documentId;
    private Button btnAgregar;
    private EditText txtnombreContacto, txttelefonoContacto, txtcorreoContacto;
    public String getId;
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

        //INSTANCIAR VALORES DEL EDITTEXT DE ACTIVITY_REGISTRARSE
        getId = getIntent().getStringExtra("id");

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
        btnGuardar.setOnClickListener(RegistrarseContactoActivity.this);


        Toast.makeText(RegistrarseContactoActivity.this,"numero--> "+getId,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnGuardar){
            if(!validar()){
                Toast.makeText(RegistrarseContactoActivity.this,"Ingrese todo los valores", Toast.LENGTH_SHORT).show();
            }else{
                btnGuardar.setEnabled(false);

                //Toast.makeText(contactoEmergenciaActivity.this, "id "+getId,Toast.LENGTH_SHORT).show();

                //VALORES CONTACTO DE EMERGENCIA
                String nombreContacto = txtnombreContacto.getText().toString().trim();
                String telefonoContacto = txttelefonoContacto.getText().toString().trim();
                String correoContacto = txtcorreoContacto.getText().toString().trim();

                //IR AL METODO PARA GUARDAR LOS DATOS EN LA BASE DE FIREBASE
                agregarNuevoContactoEmergencia(getId,nombreContacto,telefonoContacto,correoContacto);
            }
        }else if(view.getId() == R.id.btnAgregar){
            String getNombre = txtnombreContacto.getText().toString().trim();
            String getTelefono = txttelefonoContacto.getText().toString().trim();
            String getCorreo = txtcorreoContacto.getText().toString().trim();

            if(getNombre.isEmpty() || getTelefono.isEmpty() || getCorreo.isEmpty()){
                Toast.makeText(RegistrarseContactoActivity.this,"Por favor ingrese todos los valores", Toast.LENGTH_SHORT).show();
            }else{
                agregarNuevoContactoEmergencia(getId, getNombre, getTelefono,getCorreo);
                limpiarCampos();
            }

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
                                    Toast.makeText(RegistrarseContactoActivity.this, "Contacto de emergencia agregado", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegistrarseContactoActivity.this, "Error al actualizar contactos", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(RegistrarseContactoActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegistrarseContactoActivity.this, "Error al obtener el documento", Toast.LENGTH_SHORT).show();
                });
    }
    public void limpiarCampos(){
        txtnombreContacto.setText("");
        txttelefonoContacto.setText("");
        txtcorreoContacto.setText("");
    }
}