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
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlatsDispoActivityFragment extends ListFragment {

    private TextView textViewPlatsDispo;
    private String platsDispo = "";

    // new thing
    public String lePlatACommander;
    int tailleTableauValues ;
    private boolean commandeDeCrepe = false;
    private TextView lePlatFrag;
    // end new thing

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

        // new thing
        String values[] = platsDispo.split("\n");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        tailleTableauValues = values.length;
        setListAdapter(adapter);
        // end new thing

        textViewPlatsDispo.setText(platsDispo);
    }

    // new things :
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        System.out.println("clic sur element de la liste : ");
        String selection = l.getItemAtPosition(position).toString();
        int positionObjet = position ;

        System.out.println("crepe : " + selection + " position : " + position);
        // reparation du String :
        String selectionNew = selection.substring(2);
        lePlatFrag = (TextView) getActivity().findViewById(R.id.nomDuPlatACommander);

        if (positionObjet > 0 && positionObjet <= tailleTableauValues) {
            lePlatACommander = "";
            lePlatACommander = selectionNew;
            lePlatFrag.setText(lePlatACommander);
            // faire un set du String lePlat de CommandeActivity
            commandeDeCrepe = true ;
            System.out.println("lePlatACommander : " + lePlatACommander);
        }

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
    public void setLePlatACommander(String s){
        lePlatACommander = s ;
    }
// mettre le string dans un TextView devant le bouton valider et laisser
    // l'utilisateur appuyer sur le bouton.
    // end new things.

}
