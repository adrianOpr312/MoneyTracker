package Program;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

class User implements Serializable {

    static User currentUser;
    private final ArrayList<Allocation> reservations = new ArrayList<>();
    private final HashMap<Allocation, String> completedReservations = new HashMap<>();
    private final ArrayList<Allocation> milestones = new ArrayList<>();
    private final HashMap<Allocation, String> completedMilestones = new HashMap<>();
    private final ArrayList<String> notifications = new ArrayList<>();
    private final HashMap<String, Double> currencies = new HashMap<>() {{
        put("USD", 1.00);
        put("GBP", 1.23);
        put("EUR", 1.03);
        put("RON", 0.21);
    }};
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
        if (!(obj instanceof User)) return false;
        return this.id == ((User) obj).id;
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

    void addCurrency(String currency, double conversionRate) {
        this.currencies.put(currency, conversionRate);
    }

    void removeCurrency(String currency) {
        this.currencies.remove(currency);
    }

    HashMap<String, Double> getCurrencies() {
        return this.currencies;
    }

    void addBalance(double value) {
        this.balance += value;
    }

    void removeBalance(double value) {
        this.balance -= value;
    }

    void addReservation(Allocation reservation) {
        this.reservations.add(reservation);
    }

    void removeReservation(Allocation reservation) {
        this.reservations.remove(reservation);
    }

    void addCompletedReservation(Allocation reservation, String completionDate) {
        this.completedReservations.put(reservation, completionDate);
    }

    void addMilestone(Allocation milestone) {
        this.milestones.add(milestone);
    }

    void removeMilestone(Allocation milestone) {
        this.milestones.remove(milestone);
    }

    void addCompletedMilestone(Allocation milestone, String completionDate) {
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

    ArrayList<Allocation> getMilestones() {
        return this.milestones;
    }

    HashMap<Allocation, String> getCompletedMilestones() {
        return this.completedMilestones;
    }

    int getId() {
        return this.id;
    }

    void setId(int id) {
        this.id = id;
    }

    ArrayList<Allocation> getReservations() {
        return this.reservations;
    }

    HashMap<Allocation, String> getCompletedReservations() {
        return this.completedReservations;
    }


    static class Allocation {
        String name;
        double amount;
        String description;
        String creationDate;

        Allocation(String name) {
            this.name = name;
        }

        Allocation(String name, double amount) {
            this.name = name;
            this.amount = amount;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Allocation)) return false;
            return this.name.equals(((Allocation) obj).name);
        }

    }


}
