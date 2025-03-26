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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iut.dam.projet_dev_mobile.entities.Appliance;
import iut.dam.projet_dev_mobile.entities.Habitat;
import iut.dam.projet_dev_mobile.entities.User;

public class HabitatAdapter extends ArrayAdapter<Habitat> {
    Activity activity;
    int itemResourceId;
    List<Habitat> items;

    public HabitatAdapter(Activity activity, int itemResourceId, List<Habitat> items) {
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
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResourceId, parent, false);
        }

        Log.d("DEBUG_LISTVIEW", "Affichage de l'élément position: " + position);

        if (items == null || items.isEmpty()) {
            Log.d("DEBUG_LISTVIEW", "Aucune donnée reçue pour Habitat !");
            return layout; // Retourne une vue vide
        }

        TextView nameTV = layout.findViewById(R.id.tvName);
        TextView nbEquipTV = layout.findViewById(R.id.tvNbEquipements);
        TextView floorTV = layout.findViewById(R.id.tvFloor);
        TextView userTV = layout.findViewById(R.id.tvName); // 🔹 Ajout pour afficher l'unique résident

        ImageView icon1 = layout.findViewById(R.id.icon1);
        ImageView icon2 = layout.findViewById(R.id.icon2);
        ImageView icon3 = layout.findViewById(R.id.icon3);
        ImageView icon4 = layout.findViewById(R.id.icon4);
        ImageView icon5 = layout.findViewById(R.id.icon5);
        ImageView icon6 = layout.findViewById(R.id.icon6);


        Habitat habitat = items.get(position);

        String nbEquipements = habitat.appliances.size() + " équipement" + (habitat.appliances.size() < 2 ? "" : "s");
        nbEquipTV.setText(nbEquipements);
        String floor = "Étage " + habitat.floor;
        floorTV.setText(floor);

        // 🔹 Affichage du résident
        String userName;
        if (habitat.user != null) {
            Log.d("DEBUG_USER", "Utilisateur détecté : " + habitat.user.firstName + " " + habitat.user.lastName);
            userName = habitat.user.firstName + " " + habitat.user.lastName;
        } else {
            Log.d("DEBUG_USER", "Aucun utilisateur détecté dans Habitat ID : " + habitat.id);
            userName = "Aucun résident";
        }
        userTV.setText(userName);
        Log.d("DEBUG_LISTVIEW", "Affichage de l'habitat ID: " + habitat.id + " | Étage: " + habitat.floor);
        if (habitat.appliances != null && !habitat.appliances.isEmpty()) {
            Log.d("DEBUG_EQUIPEMENTS", "Habitat ID " + habitat.id + " : " + habitat.appliances.size() + " équipements détectés.");
            for (Appliance appliance : habitat.appliances) {
                Log.d("DEBUG_EQUIPEMENTS", " → " + appliance.name);
            }
        } else {
            Log.d("DEBUG_EQUIPEMENTS", "Habitat ID " + habitat.id + " : Aucun équipement détecté.");
        }



        Log.d("DEBUG_LISTVIEW", "Résident: " + userName);


        /*
        // 🔹 Gestion des icônes d'équipements
        List<Integer> equipIcons = new ArrayList<>();
        icon1.setImageDrawable(null);
        icon2.setImageDrawable(null);
        icon3.setImageDrawable(null);

        if (habitat.hasAppliance("Machine_a_laver")) {
            equipIcons.add(R.drawable.machine_a_laver);
        }
        if (habitat.hasAppliance("Aspirateur")) {
            equipIcons.add(R.drawable.aspirateur);
        }
        if (habitat.hasAppliance("Fer_a_repasser")) {
            equipIcons.add(R.drawable.fer_a_repasser);
        }

        if (equipIcons.size() > 0) {
            icon1.setImageResource(equipIcons.get(0));
        }
        if (equipIcons.size() > 1) {
            icon2.setImageResource(equipIcons.get(1));
        }
        if (equipIcons.size() > 2) {
            icon3.setImageResource(equipIcons.get(2));
        }
        if (equipIcons.size() > 0) {
            icon1.setImageResource(equipIcons.get(0));
            icon1.setVisibility(View.VISIBLE);
        }
        if (equipIcons.size() > 1) {
            icon2.setImageResource(equipIcons.get(1));
            icon2.setVisibility(View.VISIBLE);
        }
        if (equipIcons.size() > 2) {
            icon3.setImageResource(equipIcons.get(2));
            icon3.setVisibility(View.VISIBLE);
        }
*/
        Map<String, Integer> drawableMap = new HashMap<>();
        drawableMap.put("Aspirateur", R.drawable.aspirateur);
        drawableMap.put("Machine_a_laver", R.drawable.machine_a_laver);
        drawableMap.put("Fer_a_repasser", R.drawable.fer_a_repasser);

        ImageView[] imageViews = {icon1, icon2, icon3, icon4, icon5, icon6}; // Référence tes ImageView ici

        displayAppliancesIcons(habitat, imageViews, drawableMap);


        return layout;
    }

    public void displayAppliancesIcons(Habitat habitat, ImageView[] imageViews, Map<String, Integer> drawableMap) {
        List<Integer> iconsToShow = new ArrayList<>();

        // Liste des appareils à vérifier
        String[] applianceNames = {"Aspirateur", "Machine_a_laver", "Fer_a_repasser"};

        // Remplir la liste des icônes à afficher
        for (String appliance : applianceNames) {
            int count = habitat.howManyAppliances(appliance);
            for (int i = 0; i < count; i++) {
                if (iconsToShow.size() < imageViews.length) { // Max 6 icônes
                    iconsToShow.add(drawableMap.get(appliance));
                } else {
                    break;
                }
            }
        }

        // Afficher les icônes dans les ImageView
        for (int i = 0; i < imageViews.length; i++) {
            if (i < iconsToShow.size()) {
                imageViews[i].setImageResource(iconsToShow.get(i));
                imageViews[i].setVisibility(View.VISIBLE);
            } else {
                imageViews[i].setVisibility(View.INVISIBLE); // Cacher les ImageView inutilisées
            }
        }
    }

}
