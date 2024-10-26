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

public class IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText txtcorreo, txtcontrasena;
    private Button btniniciar;
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

        //INICIARLIZAR TEXT VIEW
        txtcorreo = findViewById(R.id.txtUsuario);
        txtcontrasena = findViewById(R.id.txtCon);

        btniniciar = findViewById(R.id.btnIniciarSesion);
        btniniciar.setOnClickListener(this);

    }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btnIniciarSesion) {
                String email = txtcorreo.getText().toString();
                String password = txtcontrasena.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(IniciarSesionActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        }
    private void updateUI(FirebaseUser user) {
        if (user != null) {

            Toast.makeText(IniciarSesionActivity.this,"Usuario autenticado: " + user.getEmail(),Toast.LENGTH_LONG).show();
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

    }