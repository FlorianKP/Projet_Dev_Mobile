package iut.dam.projet_dev_mobile.entities;

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

    public Habitat(int id, int floor, double area) {
        this.id = id;
        this.floor = floor;
        this.area = area;
        appliances = new ArrayList<>();
    }
}
