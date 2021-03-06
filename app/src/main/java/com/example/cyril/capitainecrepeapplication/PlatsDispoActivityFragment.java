package com.example.cyril.capitainecrepeapplication;

//import android.support.v4.app.Fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlatsDispoActivityFragment extends ListFragment {

    private String platsDispo = "";
    public String lePlatACommander;
    int tailleTableauValues ;
    private boolean commandeDeCrepe = false;

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
        afficherPlatsDispo(platsDispo);

        return v;
    }

    public void afficherPlatsDispo(String platsDispo) {
        this.platsDispo = platsDispo;
        String values[] = platsDispo.split("\n");
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, values);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.mylist, values);
        tailleTableauValues = values.length;
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println("clic sur element de la liste : ");
        String selection = l.getItemAtPosition(position).toString();

        System.out.println("crepe : " + selection + " position : " + position);
        /* enlever les 2 espaces */
        String selectionNew = selection.substring(2);
        TextView lePlatFrag = (TextView) getActivity().findViewById(R.id.nomDuPlatACommander);

        lePlatACommander = "";
        lePlatACommander = selectionNew;
        lePlatFrag.setText(lePlatACommander);
        commandeDeCrepe = true;
        System.out.println("lePlatACommander : " + lePlatACommander);

        super.onListItemClick(l, v, position, id);
    }

    public boolean commandeCrepe(){
        return commandeDeCrepe;
    }

    public void resetCommandeCrepe(){
        commandeDeCrepe = false;
    }

    public String getLePlatACommander(){
        return lePlatACommander;
    }


}
