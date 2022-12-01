package Program;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class User implements Serializable {

    static final Map<String, Double> currencies = new HashMap<>() {{
        put("USD", 1.00);
        put("EUR", 1.03);
        put("RON", 0.21);
    }};
    static User currentUser;
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    private final HashMap<Reservation, String> completedReservations = new HashMap<>();
    private final ArrayList<Reservation> milestones = new ArrayList<>();
    private final HashMap<Reservation, String> completedMilestones = new HashMap<>();
    private final ArrayList<String> notifications = new ArrayList<>();
    private String username;
    private String password;
    private int id;
    private double balance = 0;
    private String currency;

    User(String username, String password, String currency) {
        this.username = username;
        this.password = password;
        this.currency = currency;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User user)) return false;
        return this.id == user.id;
    }

    @Override
    public String toString() {
        return String.format("Username: %s\nPassword: %s\nId: #%d", this.username, this.password, this.id);
    }

    double getBalance() {
        return this.balance;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    String getUsername() {
        return this.username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getPassword() {
        return this.password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    String getCurrency() {
        return this.currency;
    }

    void setCurrency(String currency) {
        this.currency = currency;
    }

    void addBalance(double value) {
        this.balance += value;
    }

    void removeBalance(double value) {
        this.balance -= value;
    }

    void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
    }

    void addCompletedReservation(Reservation reservation, String completionDate) {
        this.completedReservations.put(reservation, completionDate);
    }

    void addMilestone(Reservation milestone) {
        this.milestones.add(milestone);
    }

    void removeMilestone(Reservation milestone) {
        this.milestones.remove(milestone);
    }

    void addCompletedMilestone(Reservation milestone, String completionDate) {
        this.completedMilestones.put(milestone, completionDate);
    }

    void addNotification(String notification) {
        this.notifications.add(notification);
    }

    void removeNotification(String notification) {
        this.notifications.remove(notification);
    }

    ArrayList<String> getNotifications() {
        return this.notifications;
    }

    ArrayList<Reservation> getMilestones() {
        return this.milestones;
    }

    HashMap<Reservation, String> getCompletedMilestones() {
        return this.completedMilestones;
    }

    int getId() {
        return this.id;
    }

    void setId(int id) {
        this.id = id;
    }

    ArrayList<Reservation> getReservations() {
        return this.reservations;
    }

    HashMap<Reservation, String> getCompletedReservations() {
        return this.completedReservations;
    }


    static class Reservation {
        String name;
        double amount;
        String description;
        String creationDate;

        Reservation(String name) {
            this.name = name;
        }

        Reservation(String name, double amount) {
            this.name = name;
            this.amount = amount;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Reservation)) return false;
            return this.name.equals(((Reservation) obj).name);
        }

    }


}
