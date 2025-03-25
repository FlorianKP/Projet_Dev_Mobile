package iut.dam.projet_dev_mobile.entities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Habitat {

    public int id;
    public int floor;
    public double area;
    public User user;
    public List<Appliance> appliances;

    public Habitat() {
        appliances = new ArrayList<>();
    }

    public Habitat(int id, int floor, double area , User user) {
        this.id = id;
        this.floor = floor;
        this.area = area;
        appliances = new ArrayList<>();
        this.user = user ;
    }


    public static Habitat getFromJson(String json){
        Gson gson = new Gson();
        Habitat obj = gson.fromJson(json, Habitat.class);
        return obj;
    }
    public static List<Habitat> getListFromJson(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Habitat>>(){}.getType();
        List<Habitat> list = gson.fromJson(json, type);

        for (Habitat h : list) {
            if (h.user != null) {
                Log.d("DEBUG_JSON", "Habitat ID: " + h.id + " - Utilisateur: " + h.user.firstName + " " + h.user.lastName);
            } else {
                Log.d("DEBUG_JSON", "Habitat ID: " + h.id + " - Aucun utilisateur chargé !");
            }
        }
        return list;
    }


    public boolean hasAppliance(String appliance){
        for (Appliance a:appliances){
            if(a.name.equals(appliance)) return true;
        }
        return false;
    }

    public void addAppliance(Appliance appliance){
        appliances.add(appliance);
    }
}
