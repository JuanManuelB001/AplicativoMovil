package com.example.aplicativomovil.ContactoEmergencia;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class agregarContactoEmergencia extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String getEmail;
    private EditText txtnombreContacto, txttelefonoContacto, txtcorreoContacto;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_contacto_emergencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth= FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

        //CAPTURARA EDIT-TEXT
        txtnombreContacto = findViewById(R.id.txtNombreContacto);
        txttelefonoContacto = findViewById(R.id.txtTelefonoContacto);
        txtcorreoContacto = findViewById(R.id.txtEmailContacto);

        //INICIARLIZAR BOTON
        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(agregarContactoEmergencia.this);
    }

    @Override
    public void onClick(View view) {

        FirebaseUser user = mAuth.getCurrentUser();

        if( view.getId() == R.id.btnAgregar){
            if(!validar()){
                Toast.makeText(agregarContactoEmergencia.this,"ingrese Todos los valores", Toast.LENGTH_SHORT).show();
            }else{
                //OPTENER LOS DATOS DEL EDIT-TEXT
                String nombreContacto = txtnombreContacto.getText().toString().trim();
                String telefonoContacto = txttelefonoContacto.getText().toString().trim();
                String correoContacto = txtcorreoContacto.getText().toString().trim();
                //optener el email
                //getEmail= optenerEmail(user);
                getEmail = "cam@gmail.com";
                agregarNuevoContacto(getEmail,nombreContacto,telefonoContacto,correoContacto);
            }
        }
    }
    public boolean validar(){
        String nombreContacto = txtnombreContacto.getText().toString().trim();
        String telefonoContacto = txttelefonoContacto.getText().toString().trim();
        String correoContacto = txtcorreoContacto.getText().toString().trim();

        if( nombreContacto.isEmpty() || telefonoContacto.isEmpty() || correoContacto.isEmpty()){
            return false;
        }else{
            return true;
        }
    }

    public void agregarNuevoContacto(String getEmail, String nombre, String telefono, String correo) {
        // Crear un mapa para el nuevo contacto de emergencia
        Map<String, Object> nuevoContacto = new HashMap<>();
        nuevoContacto.put("Nombre", nombre);
        nuevoContacto.put("Telefono", telefono);
        nuevoContacto.put("Correo", correo);

        // Buscar el documento del usuario por correo electrónico
        db.collection("Usuarios")
                .whereEqualTo("Correo Electronico", getEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Obtener el primer documento encontrado (asumiendo que el correo electrónico es único)
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        String documentId = document.getId();

                        // Obtener la lista de contactos de emergencia actual
                        List<Map<String, Object>> contactosEmergencia = (List<Map<String, Object>>) document.get("Contacto Emergencia");
                        if (contactosEmergencia == null) {
                            contactosEmergencia = new ArrayList<>();
                        }

                        // Añadir el nuevo contacto de emergencia a la lista
                        contactosEmergencia.add(nuevoContacto);

                        // Actualizar el documento del usuario con la nueva lista de contactos de emergencia
                        db.collection("Usuarios").document(documentId)
                                .update("Contacto Emergencia", contactosEmergencia)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Contacto de emergencia añadido", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al añadir contacto de emergencia", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
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

    public void limpiarCampos(){
        txtnombreContacto.setText("");
        txttelefonoContacto.setText("");
        txtcorreoContacto.setText("");
    }
}