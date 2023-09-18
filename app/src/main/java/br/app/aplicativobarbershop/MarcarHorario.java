package br.app.aplicativobarbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MarcarHorario extends AppCompatActivity {
    private TextView txtSlData, txtMarcarHr, txtPrecos, txtPix, txtCredito;
    private ImageView imgCal, imgHora;
    private RadioButton radioBtBarbeiro, radioBtBarbeiro2, radioBtBarbeiro3;
    private CheckBox cBCorte, cBDegrade, cBNavalhado, cBSombrancelha, cBDisfarcado;
    private Button btnMarcar;
    private Calendar calendar;

        //Chaves testes para adicionar pagamentos Stripe
    String SECRET_KEY = "sk_test_51M1bSxDsVT7aSQGas6OVWKtOu6OXUKnfHSelz5Bd5dPRiV8byef9E7tZtsI7o8lzoGSAqXUqWPEn8UbdsVZFSB0900ZL4i6ABx";
    String PUBLISH_KEY = "pk_test_51M1bSxDsVT7aSQGafUMXPSf4WipZsjQRfpPidg1ihIDjnKQ7yCJxaZWj0G7IkoEA17fOWPGWRKOz2i3XhMADdJ3900gDfin6Aj";

    PaymentSheet paymentSheet;

    String customerID;
    String EphericalKey;
    String ClientSecret;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuarioID;

    private String time;
    private String data;

    DatePickerDialog datePickerDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_horario);

        getSupportActionBar().hide();

            // Part2
        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);
        });


        txtSlData = findViewById(R.id.txtSlData);
        txtMarcarHr = findViewById(R.id.txtMarcarHr);

        txtPrecos = findViewById(R.id.txtPrecosProdutos);


        imgCal = findViewById(R.id.imgCal);
        imgHora = findViewById(R.id.imgHora);

        txtPix = findViewById(R.id.txtPix);
        txtCredito = findViewById(R.id.txtCredito);

        radioBtBarbeiro = findViewById(R.id.radioBtBarbeiro);
        radioBtBarbeiro2 = findViewById(R.id.radioBtBarbeiro2);
        radioBtBarbeiro3 = findViewById(R.id.radioBtBarbeiro3);

        cBCorte = findViewById(R.id.cBCorte);
        cBDegrade = findViewById(R.id.cBDegrade);
        cBNavalhado = findViewById(R.id.cBNavalhado);
        cBSombrancelha = findViewById(R.id.cBSombrancelha);
        cBDisfarcado = findViewById(R.id.cBDisfarcado);

        calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar dataSelecionada = Calendar.getInstance();
                dataSelecionada.set(year, month, day);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                txtSlData.setText(format.format(dataSelecionada.getTime()));
                data = format.format(dataSelecionada.getTime());
            }
        }, calendar.get(Calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        imgCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        imgHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar1 = Calendar.getInstance();
                int horas = calendar1.get(Calendar.HOUR_OF_DAY);
                int minutos = calendar1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MarcarHorario.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.setTimeZone(TimeZone.getDefault());
                        SimpleDateFormat format1 = new SimpleDateFormat("H:mm a");
                        time = format1.format(c.getTime());
                        txtMarcarHr.setText(time);
                    }
                }, horas, minutos, true);
                timePickerDialog.show();
            }
        });

        btnMarcar = findViewById(R.id.btnMarcar);
        btnMarcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                salvarDataEHora();
                selecionarOBarberio();
                selecionarCortes();
                onClickPreco(view);
            }
        });

            //Part 3
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    customerID = object.getString("id");

                    Toast.makeText(MarcarHorario.this, customerID, Toast.LENGTH_SHORT).show();

                    getEphericalKey(customerID);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MarcarHorario.this);
        requestQueue.add(stringRequest);

    }

    private void salvarDataEHora() {

        String data = txtSlData.getText().toString();
        String hora = txtMarcarHr.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> horarios = new HashMap<>();
        horarios.put("Data", data);
        horarios.put("Hora", hora);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Data e Hora").document(usuarioID);
        documentReference.set(horarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        });
    }

    private void selecionarOBarberio() {


        String barbeiro = radioBtBarbeiro.getText().toString();
        String barbeiro2 = radioBtBarbeiro2.getText().toString();
        String barbeiro3 = radioBtBarbeiro3.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> barbeiros = new HashMap<>();

        if (radioBtBarbeiro.isChecked()) {
            barbeiros.put("Barbeiros", barbeiro);
        }
        if (radioBtBarbeiro2.isChecked()) {
            barbeiros.put("Barbeiros", barbeiro2);
        }
        if (radioBtBarbeiro3.isChecked())
            barbeiros.put("Barbeiros", barbeiro3);


        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Barbeiros").document(usuarioID);
        documentReference.set(barbeiros).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        });

    }

    public void selecionarCortes() {

        String corte1 = cBCorte.getText().toString();
        String corte2 = cBDegrade.getText().toString();
        String corte3 = cBNavalhado.getText().toString();
        String corte4 = cBSombrancelha.getText().toString();
        String corte5 = cBDisfarcado.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> estilosCortes = new HashMap<>();

        if (cBCorte.isChecked()) {
            estilosCortes.put("Estilos de cortes", corte1);
        }
        if (cBDegrade.isChecked()) {
            estilosCortes.put("Estilos de cortes \n ", corte2);
        }
        if (cBNavalhado.isChecked()) {
            estilosCortes.put(" \n Estilos de cortes \n ", corte3);
        }
        if (cBSombrancelha.isChecked()) {
            estilosCortes.put("Estilos de cortes \n", corte4);
        }
        if (cBDisfarcado.isChecked()) {
            estilosCortes.put("\n Estilos de cortes", corte5);
        }

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Estilos de Cortes").document(usuarioID);
        documentReference.set(estilosCortes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void V) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        });
    }

    public void onClickPreco(View view) {

        int total = 0;
        StringBuilder resultado = new StringBuilder();
        resultado.append("");

        if (cBCorte.isChecked()) {
            resultado.append("");
            total += 35;
        }
        if (cBDegrade.isChecked()) {
            resultado.append("");
            total += 25;
        }
        if (cBNavalhado.isChecked()) {
            resultado.append("");
            total += 30;
        }
        if (cBSombrancelha.isChecked()) {
            resultado.append("");
            total += 10;
        }
        if (cBDisfarcado.isChecked()) {
            resultado.append("");
            total += 20;
        }

        resultado.append("  Total  " + resultado + total + " reais");
        txtPrecos.setText(resultado.toString());
    }

    public void pagamentoDebito(View view) {
        txtPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    public void pagamentoCredito(View view) {
        txtCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentFlow();
            }
        });
    }


        //Part Final..
    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Pagamento concluido com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    EphericalKey =  object.getString("id");

                    Toast.makeText(MarcarHorario.this, EphericalKey, Toast.LENGTH_SHORT).show();

                    getClientSecret(customerID, EphericalKey);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " +SECRET_KEY);
                header.put("Stripe-Version", "2022-08-01");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MarcarHorario.this);
        requestQueue.add(stringRequest);
    }

    private void getClientSecret(String customerID, String ephericalKey) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);

                    ClientSecret =  object.getString("client_secret");
                    Toast.makeText(MarcarHorario.this, ClientSecret, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " +SECRET_KEY);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", "1000" + "00");
                params.put("currency", "brl");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MarcarHorario.this);
        requestQueue.add(stringRequest);

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration("Confraria barber shop"
                        , new PaymentSheet.CustomerConfiguration(customerID, EphericalKey)));
    }
}

