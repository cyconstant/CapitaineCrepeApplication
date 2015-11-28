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
public class QuantiteActivityFragment extends ListFragment {

    private String quantitePlats = "";
    private String listeNomString = "";
    private String[] listeNom = new String[40];
    private String renvoi;
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
        afficherPlatsDispo(quantitePlats);
        return v;
    }

    public void afficherPlatsDispo(String newQuantity) {
        String[] separated = newQuantity.split("\n");

        for (int i = 1; i < separated.length - 1; i += 2) {
            // String avec Quantite et Nom pour Affichage
            quantitePlats += " " + separated[i+1] + " " + separated[i] + "\n";
            // String avec le nom seul pour Ajout
            listeNomString += separated[i] + "\n";
        }
        String[] listeQuantiteNom ;
        listeNom = null;

        listeQuantiteNom = quantitePlats.split("\n");
        listeNom = listeNomString.split("\n");

        quantitePlats = "";
        listeNomString = "";

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.mylist, listeQuantiteNom);
        tailleTableauQuantiteNom = listeQuantiteNom.length;
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        TextView quantitePlatFrag = (TextView) getActivity().findViewById(R.id.nomDuPlatAAjouter);

        System.out.println("clic sur element de la liste : ");
        if ( dernierElementClique == position || dernierElementClique == 1000) {
            dernierElementClique = position;
            compteurDeClics++;
            renvoi = Integer.toString(compteurDeClics)+" "+listeNom[position];
        }else {
            System.out.println("changement d'item detecte");
            compteurDeClics = 1;
            renvoi = Integer.toString(compteurDeClics)+" "+listeNom[position];
            dernierElementClique = position;
            quantitePlatFrag.setText("renvoi");
        }

        if (position >= 0 && position <= tailleTableauQuantiteNom) {
            quantitePlatFrag.setText(renvoi);
            ajoutDeCrepe = true ;
        }
        super.onListItemClick(l, v, position, id);
    }

    public boolean ajoutDeCrepe(){
        return ajoutDeCrepe;
    }

    public void resetAjoutDeCrepe(){
        ajoutDeCrepe = false;
    }

    public String getRenvoi(){
        return renvoi;
    }

    public void setCompteurDeClics(int i){
        compteurDeClics = i;
    }

}
