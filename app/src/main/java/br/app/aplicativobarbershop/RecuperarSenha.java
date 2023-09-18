package br.app.aplicativobarbershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenha extends AppCompatActivity {

    private EditText edtRedefinirSenha;
    private TextView txtRetornar;
    private Button btnRedefinir;

    private String email;
    String[] mensagens = {"Preencha os campos"};

    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        edtRedefinirSenha = findViewById(R.id.edtRedefinirSenha);

        txtRetornar = findViewById(R.id.txtRetornar);
        txtRetornar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecuperarSenha.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        });

        btnRedefinir = findViewById(R.id.btnRedefinir);
        btnRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarData(view);
            }
        });
    }

    private void validarData(View v) {
        email = edtRedefinirSenha.getText().toString();
        if (email.isEmpty()) {
            Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }else {
            esqueceuSenha();
        }
    }

    private void esqueceuSenha() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RecuperarSenha.this, "Verifique seu email", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RecuperarSenha.this, FormLogin.class));
                    finish();
                }else {
                    Toast.makeText(RecuperarSenha.this, "Erro : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}