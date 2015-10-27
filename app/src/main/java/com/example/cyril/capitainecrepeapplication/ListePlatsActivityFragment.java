package com.example.cyril.capitainecrepeapplication;

//import android.support.v4.app.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListePlatsActivityFragment extends Fragment {

    private TextView textViewListeDesPlats;
    private String listeDesPlats = "";

    public ListePlatsActivityFragment() {
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

        View v = inflater.inflate(R.layout.fragment_listeplats, container, false);
        textViewListeDesPlats = (TextView) v.findViewById(R.id.textViewListeDesPlats);
        Button buttonFermer = (Button) v.findViewById(R.id.buttonFermer);
        afficherLesPlats(listeDesPlats);
        buttonFermer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fermer();
            }
        });
        return v;
    }

    public void afficherLesPlats(String listeDesPlats) {
        this.listeDesPlats = listeDesPlats;
        textViewListeDesPlats.setText(listeDesPlats);
    }

    public void fermer() {
        super.onDestroyView();
        Activity activity = this.getActivity();
        System.out.println("Activity.onfinish via Fragment");
        activity.finish();


    }
}
