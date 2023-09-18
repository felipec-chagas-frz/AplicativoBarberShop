package br.app.aplicativobarbershop.ui.slideshow;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.app.aplicativobarbershop.R;
import br.app.aplicativobarbershop.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    Activity context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        return root;
    }

    public void onStart() {
        super.onStart();

    }
}