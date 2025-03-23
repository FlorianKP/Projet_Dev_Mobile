package iut.dam.projet_dev_mobile;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import iut.dam.projet_dev_mobile.entities.Habitat;

public class HabitatAdapter extends ArrayAdapter<Habitat> {
    Activity activity;
    int itemResourceId;
    List<Habitat> items;
    public HabitatAdapter(Activity activity, int itemResourceId,List<Habitat> items) {
        super(activity, itemResourceId, items);
        this.activity = activity;
        this.itemResourceId = itemResourceId;
        this.items = items;
        Log.d("DEBUG_ADAPTER", "Nombre d'éléments dans l'adapter: " + items.size());
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View layout = convertView;
        if (convertView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResourceId, parent, false);
        }
        Log.d("DEBUG_LISTVIEW", "Affichage de l'élément position: " + position);
        if (items == null || items.isEmpty()) {
            System.out.println("Aucune donnée reçue pour Habitat !");
            return layout; // Retourne la vue vide
        }
        TextView nameTV =  layout.findViewById(R.id.tvName);
        TextView nbEquipTV = layout.findViewById(R.id.tvNbEquipements);
        TextView floorTV = layout.findViewById(R.id.tvFloor);
        ImageView icon1 = layout.findViewById(R.id.icon1);
        ImageView icon2 = layout.findViewById(R.id.icon2);
        ImageView icon3 = layout.findViewById(R.id.icon3);
        String nbEquipements = items.get(position).appliances.size()+" équipement" + (items.get(position).appliances.size()<2?"":"s");
        nbEquipTV.setText(nbEquipements);
        String floor =  "Étage " +String.valueOf(items.get(position).floor) ;
        floorTV.setText(floor);


        // Liste des équipements possédés
        List<Integer> equipIcons = new ArrayList<>();

        icon1.setImageDrawable(null);
        icon2.setImageDrawable(null);
        icon3.setImageDrawable(null);

        if (items.get(position).hasAppliance("machine à laver")) {
            equipIcons.add(R.drawable.machine_a_laver);
        }
        if (items.get(position).hasAppliance("aspirateur")) {
            equipIcons.add(R.drawable.aspirateur);
        }
        if (items.get(position).hasAppliance("fer à repasser")) {
            equipIcons.add(R.drawable.fer_a_repasser);
        }

        // On remplit seulement les ImageView nécessaires
        if (equipIcons.size() > 0) {
            icon1.setImageResource(equipIcons.get(0));
        }
        if (equipIcons.size() > 1) {
            icon2.setImageResource(equipIcons.get(1));
        }

        if (equipIcons.size() > 2) {
            icon3.setImageResource(equipIcons.get(2));
        }
        return layout;
    }
}
