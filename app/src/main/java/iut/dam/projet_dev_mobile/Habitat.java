package iut.dam.projet_dev_mobile;

import java.util.ArrayList;
import java.util.List;

public class Habitat {
    int id;
    String residentName;
    int floor;
    double area;
    List<Appliance> appliances;

    public Habitat(int id, String name, int floor){
        this.id=id;
        this.residentName = name;
        this.floor = floor;
        this.area = area;
        this.appliances = new ArrayList<>();
    }

    public void addAppliance(Appliance appliance){
        appliances.add(appliance);
    }

    public boolean hasAppliance(String appliance){
        for (Appliance a:appliances){
            if(a.name.equals(appliance)) return true;
        }
        return false;
    }

}
