package br.app.aplicativobarbershop.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.app.aplicativobarbershop.HorarioCliente;
import br.app.aplicativobarbershop.MarcarHorario;
import br.app.aplicativobarbershop.Produtos;
import br.app.aplicativobarbershop.R;
import br.app.aplicativobarbershop.TelaPrincipal;


public class HomeFragment extends Fragment {

    Activity context;

    private ViewFlipper slidesImagens;
    private ImageView imgMarcarHr, imgHoraMarcada, imgContato, imgProdutos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    public void onStart() {
        super.onStart();

        slidesImagens =  context.findViewById(R.id.slideImagens);

        int imagens [] = {R.drawable.ft_fachada_barbearia, R.drawable.ft_barbearia_interior, R.drawable.ft_produtos_barbearia, R.drawable.ft_produtos_barbearia02};

        for (int image: imagens) {
            slideImagem(image);
        }

        imgMarcarHr = context.findViewById(R.id.imgMarcarHr);
        imgMarcarHr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MarcarHorario.class);
                startActivity(intent);
            }
        });

        imgHoraMarcada = context.findViewById(R.id.imgHrMarcada);
        imgHoraMarcada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HorarioCliente.class);
                startActivity(intent);
            }
        });

        imgContato = context.findViewById(R.id.imgContato);
        imgContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=+5551993742795";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        imgProdutos = context.findViewById(R.id.imgProdutos);
        imgProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Produtos.class);
                startActivity(intent);
            }
        });
    }
    public void slideImagem (int image) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(image);

        slidesImagens.addView(imageView);
        slidesImagens.setFlipInterval(4000);
        slidesImagens.setAutoStart(true);

        slidesImagens.setInAnimation(context, android.R.anim.slide_in_left);
        slidesImagens.setOutAnimation(context, android.R.anim.slide_out_right);

    }
}
