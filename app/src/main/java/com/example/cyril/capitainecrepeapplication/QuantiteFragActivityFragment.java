package com.example.cyril.capitainecrepeapplication;

//import android.support.v4.app.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuantiteFragActivityFragment extends Fragment {

    private TextView textViewQuantite;
    private String quantitePlats = "";

    public QuantiteFragActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Memoriser les données du fragment
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quantite, container, false);
        textViewQuantite = (TextView) v.findViewById(R.id.textViewQuantite);
        textViewQuantite.setText("");
        afficherPlatsDispo(quantitePlats);
        return v;
    }

    public void afficherPlatsDispo(String quantitePlats) {
        this.quantitePlats = quantitePlats;
        textViewQuantite.setText(quantitePlats);
    }
}
