package com.example.aplicativomovil;

import android.os.Bundle;
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

import com.example.aplicativomovil.DataBase.DataBaseUsuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registrarse extends AppCompatActivity  implements View.OnClickListener{

    private EditText txtnombre,txttelefono,txtcorreo_electronico;

    private Button btnguardar;

    //VARIABLE FIREBASE
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrarse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //INICIALIZAR FIREBASE
        db = FirebaseFirestore.getInstance();

        // TEXTO
        txtnombre = findViewById(R.id.txtNombre);
        txttelefono = findViewById(R.id.txtTelefono);
        txtcorreo_electronico = findViewById(R.id.txtCorreo);
        // BOTON
        btnguardar = findViewById(R.id.btnGuardar);

        //CREAR ACTION LISTENER
        btnguardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String nombre = txtnombre.getText().toString().trim();
        String telefono = txttelefono.getText().toString().trim();
        String correo_electronico = txtcorreo_electronico.getText().toString().trim();



        if(view.getId() == R.id.btnGuardar){
            if(nombre.isEmpty() && telefono.isEmpty() && correo_electronico.isEmpty()){
                Toast.makeText(this, "por favor,ingrese todos  los datos ", Toast.LENGTH_SHORT).show();

            }else{
                postUsuario(nombre,telefono,correo_electronico);
                limpiarCampos();
            }

        }
    }

    private void postUsuario(String nombre, String telefono, String correoElectronico) {
        Map<String, Object> user = new HashMap<>();
        //AGREGAR VALORES AL MAPA CLAVE-VALOR
        user.put("Nombre",nombre);
        user.put("Telefono", telefono);
        user.put("Correo Electronico",correoElectronico);


        //AGREGAR VALORES A LA BASE DE DATOS
        db.collection("Usuarios")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(registrarse.this, "Exito al guardar en la base de datos",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registrarse.this, "Fallo al guar", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void limpiarCampos(){
        txtnombre.setText("");
        txttelefono.setText("");
        txtcorreo_electronico.setText("");
    }
}