package iut.dam.projet_dev_mobile.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeSlot {

    public int id;
    public Date begin;
    public Date end;
    public int maxWattage;
    private int availableWattage;
    public float consumptionPercentage;
    public List<Booking> bookings;

    public TimeSlot() {
        bookings = new ArrayList<>();
    }

    public TimeSlot(int id, Date begin, Date end, int maxWattage) {
        this.id = id;
        this.begin = begin;
        this.end = end;
        this.maxWattage = maxWattage;
        bookings = new ArrayList<>();
        this.availableWattage = maxWattage;
        this.consumptionPercentage=-1;
    }
    public int getAvailableWattage() {
        return this.availableWattage;
    }

    public void setAvailableWattage(int availableWattage) {
        this.availableWattage = availableWattage;
    }

    // Méthode pour calculer la puissance disponible
    public void calculateAvailableWattage() {
        int usedWattage = bookings.stream()
                .mapToInt(b -> b.getAppliance().getWattage())
                .sum();
        this.availableWattage = maxWattage - usedWattage;
    }
}
