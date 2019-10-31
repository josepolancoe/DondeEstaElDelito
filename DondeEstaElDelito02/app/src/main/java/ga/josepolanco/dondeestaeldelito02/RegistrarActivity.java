package ga.josepolanco.dondeestaeldelito02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ga.josepolanco.dondeestaeldelito02.R;

public class RegistrarActivity extends AppCompatActivity {
//    CollectionReference estudianteColeccion;
    Button btn_guardar;
    EditText codigo, nombres, apellidos, carreraP, edad;
    TextView data_estudiante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setTitle("Registrar Usuario");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);



    }
}