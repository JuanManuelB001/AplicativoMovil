package com.example.aplicativomovil;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

import java.util.HashMap;
import java.util.Map;

public class registrarse extends AppCompatActivity  implements View.OnClickListener{

    private EditText txtnombre,txttelefono,txtcorreo_electronico;
    private EditText txtcontrasena,txtconfirmarcontrasena;
    private Button btnguardar;

    //VARIABLE FIREBASE
    private FirebaseFirestore db;
    //AUTENTIFICACION CUENTA
    @SuppressLint("RestrictedApi")
    private FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();

        // TEXTO
        txtnombre = findViewById(R.id.txtNombre);
        txttelefono = findViewById(R.id.txtTelefono);
        txtcorreo_electronico = findViewById(R.id.txtCorreo);
        //CONTRASENA
        txtcontrasena = findViewById(R.id.txtcontrasena);
        txtconfirmarcontrasena = findViewById(R.id.txtConfirmarContrasena);

        // BOTON
        btnguardar = findViewById(R.id.btnGuardar);

        //CREAR ACTION LISTENER PARA EL BOTON
        btnguardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //CONVERTIR VALORES A STRING DE EDIT TEXT
        String nombre = txtnombre.getText().toString().trim();
        String telefono = txttelefono.getText().toString().trim();
        String correo_electronico = txtcorreo_electronico.getText().toString().trim();
        String contrasena = txtcontrasena.getText().toString().trim();
        String confirContrasena = txtconfirmarcontrasena.getText().toString().trim();


        if(view.getId() == R.id.btnGuardar){//ESCUCHAR EL EVENTO BOTON GUARDAR
            //REVISAR QUE LOS EDIT TEXT NO ESTEN VACIOS
            
            if(nombre.isEmpty() && telefono.isEmpty() && correo_electronico.isEmpty()){
                Toast.makeText(this, "por favor,ingrese todos  los datos ", Toast.LENGTH_SHORT).show();


            }else{
                if(!correo_electronico.isEmpty() ){
                    if( contrasena.equals(confirContrasena)){
                        Toast.makeText(this, "Contraseña coincide ", Toast.LENGTH_SHORT).show();
                        crearUsuario(correo_electronico, contrasena);
                        limpiarCampos();//LIMPIAR CAMPOS DEL EDIT TEXT
                    }else {
                        Toast.makeText(registrarse.this, "Contraseña no Coincide", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(registrarse.this, "Por favor ingrese Correo Electronico", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
    private void crearUsuario(String correo, String cont){
        mAuth.createUserWithEmailAndPassword(correo, cont)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(registrarse.this,"Bienvenido ", Toast.LENGTH_LONG).show();
                            // Ahora que el usuario fue creado, guarda sus datos en Firestore
                            postUsuario(txtnombre.getText().toString().trim(), txttelefono.getText().toString().trim(), correo);
                            Intent intent = new Intent(registrarse.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(registrarse.this, "Error: no se pudo crear el usuario", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(registrarse.this,"Ha ocurrido un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void postUsuario(String nombre, String telefono, String correoElectronico) {
        //FUNCION PARA GUARDAR LOS VALORES  A LA BASE DE DATOS

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