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
public class PlatsDispoActivityFragment extends Fragment {

    private TextView textViewPlatsDispo;
    private String platsDispo = "";

    public PlatsDispoActivityFragment() {
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
        View v = inflater.inflate(R.layout.fragment_plats_dispo, container, false);
        textViewPlatsDispo = (TextView) v.findViewById(R.id.textViewPlatsDispo);

        textViewPlatsDispo.setText("");
        afficherPlatsDispo(platsDispo);

        return v;
    }

    public void afficherPlatsDispo(String platsDispo) {
        this.platsDispo = platsDispo;
        textViewPlatsDispo.setText(platsDispo);
    }
}
