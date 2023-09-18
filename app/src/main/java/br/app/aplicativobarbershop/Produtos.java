package br.app.aplicativobarbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Produtos extends AppCompatActivity {

    private TextView txtPrecosProdutos;
    private CheckBox cbProduto, cbProduto2, cbProduto3;
    private Button btnComprar;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuarioID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        getSupportActionBar().hide();

        txtPrecosProdutos = findViewById(R.id.txtPrecosProdutos);

        cbProduto = findViewById(R.id.cbProduto);
        cbProduto2 = findViewById(R.id.cbProduto2);
        cbProduto3 = findViewById(R.id.cbProduto3);

        btnComprar = findViewById(R.id.btnComprar);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarProdutos();
                onClickProdutos(view);
            }
        });
    }

    public void selecionarProdutos() {

        String produto1 = cbProduto.getText().toString();
        String produto2 = cbProduto2.getText().toString();
        String produto3 = cbProduto3.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> produtos = new HashMap<>();

        if (cbProduto.isChecked()) {
            produtos.put("Produto", produto1);
        }
        if (cbProduto2.isChecked()) {
            produtos.put("Produto \n ", produto2);
        }
        if (cbProduto3.isChecked()) {
            produtos.put(" \n Produto", produto3);
        }

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Produto").document(usuarioID);
        documentReference.set(produtos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void V) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        });
    }

    public void onClickProdutos(View view) {

        int total = 0;
        StringBuilder resultado = new StringBuilder();
        resultado.append("");

        if (cbProduto.isChecked()) {
            resultado.append("");
            total += 50;
        }
        if (cbProduto2.isChecked()) {
            resultado.append("");
            total += 30;
        }
        if (cbProduto3.isChecked()) {
            resultado.append("");
            total += 60;
        }

        resultado.append("  Total  " + resultado + total + " reais");
        txtPrecosProdutos.setText(resultado.toString());
    }
}