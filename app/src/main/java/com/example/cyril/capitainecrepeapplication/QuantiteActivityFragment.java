package com.example.cyril.capitainecrepeapplication;

//import android.support.v4.app.Fragment;

import android.app.Fragment;
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
public class QuantiteActivityFragment extends ListFragment {

    private TextView quantitePlatFrag ;
    private String quantitePlats = "";

    private String listeQuantiteNom[];
    private String listeNom[];
    private String renvoi;
    private String lePlatAAjouter;
    private boolean ajoutDeCrepe = false;
    private int tailleTableauQuantiteNom ;
    private int compteurDeClics;
    private int dernierElementClique = 1000;

    public QuantiteActivityFragment() {
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
        //textViewQuantite = (TextView) v.findViewById(R.id.textViewQuantite);
        //textViewQuantite.setText("");
        afficherPlatsDispo(quantitePlats);
        return v;
    }

    public void afficherPlatsDispo(String newQuantity) {
        String[] separated = newQuantity.split("\n");
        //quantitePlats = separated[0] + "\n";
        quantitePlats = "Quantite disponible : " + "\n";
        // on met sur une seule ligne le nom et la quantite.
        for (int i = 1; i < separated.length - 1; i += 2) {
            // quantitePlats += " " + separated[i] + " : " + separated[i + 1] + "\n";
            listeQuantiteNom[i] = separated[i+1] + " " + separated[i];
            // on liste les noms seuls
            listeNom[i] = separated[i];
        }
        // on peut afficher la liste
        // textViewQuantite.setText(quantitePlats);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.mylist, listeQuantiteNom);
        tailleTableauQuantiteNom = listeQuantiteNom.length;
        setListAdapter(adapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println("clic sur element de la liste : ");
        String selection = l.getItemAtPosition(position).toString();
        if ( dernierElementClique == position || dernierElementClique == 1000) {
            dernierElementClique = position;
            compteurDeClics++;
            renvoi = Integer.toString(compteurDeClics)+" "+listeNom[position];
        }
        System.out.println(renvoi);
        // reparation du String :
        // String selectionNew = selection.substring(2);
        quantitePlatFrag = (TextView) getActivity().findViewById(R.id.nomDuPlatAAjouter);

        //if (position == 0){
        //    lePlatACommander = "";
        //    lePlatFrag.setText("");
        //}

        if (position >= 0 && position <= tailleTableauQuantiteNom) {
            lePlatAAjouter = "";
            lePlatAAjouter = renvoi;
            quantitePlatFrag.setText(renvoi);
            ajoutDeCrepe = true ;
            System.out.println("lePlatAAjouter : " + lePlatAAjouter);
        }
        super.onListItemClick(l, v, position, id);
    }

    public boolean ajoutDeCrepe(){
        return ajoutDeCrepe;
    }

    public void resetajoutDeCrepe(){
        ajoutDeCrepe = false;
    }

    public String getRenvoi(){
        return renvoi;
    }

    public void setRenvoi(String s){
        renvoi = s ;
    }

}
