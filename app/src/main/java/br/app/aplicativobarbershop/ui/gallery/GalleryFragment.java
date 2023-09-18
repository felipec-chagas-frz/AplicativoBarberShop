package br.app.aplicativobarbershop.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.app.aplicativobarbershop.Produtos;
import br.app.aplicativobarbershop.R;
import br.app.aplicativobarbershop.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    Activity context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }

    public void onStart() {
        super.onStart();

    }

}