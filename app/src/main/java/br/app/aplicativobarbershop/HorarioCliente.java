package br.app.aplicativobarbershop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HorarioCliente extends AppCompatActivity {
    private TextView txtNomeUser, txtNomeUser2, txtNomeUser3, txtNomeUser5;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario_cliente);

        getSupportActionBar().hide();


        txtNomeUser2 = findViewById(R.id.txtNomeUser2);
        txtNomeUser3 = findViewById(R.id.txtNomeUser3);
        txtNomeUser5 = findViewById(R.id.txtNomeUser5);
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                if (document != null) {

                }
            }
        });

        DocumentReference documentReference01 = db.collection("Barbeiros").document(usuarioID);
        documentReference01.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                if (document != null) {
                    txtNomeUser2.setText(document.getString("Barbeiros"));
                }
            }
        });

        DocumentReference documentReference02 = db.collection("Data e Hora").document(usuarioID);
        documentReference02.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                if (document != null) {
                    txtNomeUser3.setText(document.getString("Data"));
                    txtNomeUser5.setText(document.getString("Hora"));
                }
            }
        });
    }
}