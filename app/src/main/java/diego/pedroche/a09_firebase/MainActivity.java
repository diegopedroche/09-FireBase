package diego.pedroche.a09_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView lbFrase;
    private EditText txtFrase;
    private Button btnGuardar;

    private FirebaseDatabase database;

    private DatabaseReference refFrase;
    private DatabaseReference refPersona;
    private DatabaseReference refPersonas;

    private ArrayList<Persona> personas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lbFrase = findViewById(R.id.lbFrase);
        txtFrase = findViewById(R.id.txtFrase);
        btnGuardar = findViewById(R.id.btnGuardar);
        personas = new ArrayList<>();
        crearPersonas();

        database = FirebaseDatabase.getInstance("https://fir-f8c69-default-rtdb.europe-west1.firebasedatabase.app");

        // Referencias
        refFrase = database.getReference("frase");
        refPersona = database.getReference("persona");
        refPersonas = database.getReference("personas");

        // Escribir
        Persona p = new Persona("Diego",20);
        //refPersona.setValue(p);
        //refPersonas.setValue(personas);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refFrase.setValue(txtFrase.getText().toString());
            }
        });

        // Lecturas
        refPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    GenericTypeIndicator< ArrayList<Persona> > gti = new GenericTypeIndicator< ArrayList<Persona> >() {
                    };
                    ArrayList<Persona> personas = snapshot.getValue(gti);
                    Toast.makeText(MainActivity.this, "Descargados: "+personas.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Persona p = snapshot.getValue(Persona.class);
                    Toast.makeText(MainActivity.this, p.getNombre(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refFrase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String frase = snapshot.getValue(String.class);
                    lbFrase.setText(frase);
                }else {
                    Toast.makeText(MainActivity.this, "NO EXISTE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void crearPersonas() {
        for (int i = 0; i < 1000; i++) {
            personas.add(new Persona("Nombre"+ i,20));
        }
    }
}