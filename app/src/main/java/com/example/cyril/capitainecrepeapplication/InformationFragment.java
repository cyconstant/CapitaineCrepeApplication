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
public class InformationFragment extends Fragment {

    private TextView textViewInformation;
    private String information = "";

    public InformationFragment() {
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

        View v = inflater.inflate(R.layout.fragment_information, container, false);
        textViewInformation = (TextView) v.findViewById(R.id.textViewInformation);
        Button buttonFermer = (Button) v.findViewById(R.id.buttonFermer);
        afficherInformation(information);
        buttonFermer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fermer();
            }
        });
        return v;
    }

    public void afficherInformation(String information) {
        this.information = information;
        textViewInformation.setText(information);
    }

    public void fermer() {
        super.onDestroyView();
        Activity activity = this.getActivity();
        System.out.println("InformationFragment.fermer()");
        activity.finish();
    }

}
