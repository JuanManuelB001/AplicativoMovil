package com.example.aplicativomovil;

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
import com.example.aplicativomovil.DataBase.DataBaseUsuario;

public class registrarse extends AppCompatActivity  implements View.OnClickListener{

    private EditText txtnombre,txttelefono,txtcorreo_electronico;

    private Button btnguardar;

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
        DataBaseUsuario dbUsuario = new DataBaseUsuario(registrarse.this);

        if(view.getId() == R.id.btnGuardar){
            dbUsuario.insertarTabla(txtnombre.getText().toString()
                    ,txttelefono.getText().toString()
                    ,txtcorreo_electronico.getText().toString());
            Toast.makeText(registrarse.this, "SE ha registrado el usuario satisfactoriamente",Toast.LENGTH_LONG).show();
            limpiarCampos();
        }
    }
    private void limpiarCampos(){
        txtnombre.setText("");
        txttelefono.setText("");
        txtcorreo_electronico.setText("");
    }
}