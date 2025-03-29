package iut.dam.projet_dev_mobile.entities;

import java.util.Date;

public class Booking {

    public int id;
    public int order;
    public Date bookedAt;
    public Appliance appliance;
    public TimeSlot timeSlot;
    private User user;
    public Booking() {
    }

    public Booking(int id, int order, Date bookedAt) {
        this();
        this.id = id;
        this.order = order;
        this.bookedAt = bookedAt;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Appliance getAppliance() {
        return this.appliance;
    }
}
