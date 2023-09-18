package br.app.aplicativobarbershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.Normalizer;

public class FormLogin extends AppCompatActivity {
    private EditText editEmail, editsenha;
    private TextView txtCadastro, txtEsqueceuSenha;
    private Button btnEntrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Preencha todos os campos"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();

        editEmail = findViewById(R.id.editEmail);
        editsenha = findViewById(R.id.editSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        progressBar = findViewById(R.id.progressBar);

        txtEsqueceuSenha = findViewById(R.id.txtEsqueceuSenha);
        txtEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, RecuperarSenha.class);
                startActivity(intent);
            }
        });


        txtCadastro = findViewById(R.id.txtCadastro);
        txtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString();
                String senha = editsenha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else {
                    autenticarUsuario(v);
                }
            }
        });
    }

    private void autenticarUsuario(View view) {
        String email = editEmail.getText().toString();
        String senha = editsenha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaPrincipal();
                        }
                    },3000);
                }else {
                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e) {
                        erro = "Erro ao logar usuário";
                    }
                    Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null) {
            telaPrincipal();
        }
    }

    private void telaPrincipal() {
        Intent intent = new Intent(FormLogin.this,TelaPrincipal.class);
        startActivity(intent);
        finish();
    }
}