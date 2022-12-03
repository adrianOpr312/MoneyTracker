package Program;

import Program.CRUD.FileRepository;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class UserService {

    public static final FileRepository<User> repository = new FileRepository<>("src/Database/UserData.dat");

    static void updateCurrentUser() {
        repository.updateEntity(repository.findEntity(User.currentUser), User.currentUser);
    }

    private static int findByUsernameAndPassword(String username, String password) {
        for (User user : repository.getEntities())
            if (user.getUsername().equals(username) && user.getPassword().equals(password))
                return repository.findEntity(user);
        return -1;
    }

    private static double convertToCurrency(double amount, String newCurrency) {
        return amount * User.currentUser.getCurrencies().get(User.currentUser.getCurrency()) / User.currentUser.getCurrencies().get(newCurrency);
    }

    private static int generateId() {
        Random random = new Random();
        int id = 0;
        for (byte i = 0; i < 5; i++) {
            id = id * 10 + random.nextInt(10);
        }
        return id;

    }

    private static boolean idExists(int id) {
        for (User u : getUsers())
            if (u.getId() == id) {
                return true;
            }
        return false;
    }

    private static void assignId(User user) {
        int id = generateId();
        while (idExists(id)) id = generateId();
        user.setId(id);

    }

    private static String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss O");
        return formatter.format(ZonedDateTime.now());
    }

    private static String getNotification(String milestoneName) {
        for (String notification : User.currentUser.getNotifications())
            if (notification.contains(String.format("\"%s\"", milestoneName))) return notification;
        return null;
    }

    private static boolean reservationExists(User.Allocation reservation) {
        return User.currentUser.getReservations().contains(reservation);
    }

    private static boolean milestoneExists(User.Allocation milestone) {
        return User.currentUser.getMilestones().contains(milestone);
    }

    static void saveUsers() throws IOException {
        repository.saveToFile();
    }

    static void loadUsers() throws IOException, ClassNotFoundException {
        repository.loadFromFile();
    }

    private static List<User> getUsers() {
        return repository.getEntities();
    }

    static void signIn(String username, String password, String currency) {
        User newUser = new User(username, password, currency);
        assignId(newUser);
        User.currentUser = newUser;
        repository.addEntity(newUser);
    }

    static void logIn(String username, String password) throws Exception {
        int index = findByUsernameAndPassword(username, password);
        if (index == -1) throw new Exception("Invalid username or password");
        User.currentUser = getUsers().get(index);
    }

    static void changeBalance(double amount, boolean add) throws Exception {
        if (add) User.currentUser.addBalance(amount);
        else if (amount > User.currentUser.getBalance())
            throw new Exception("You can't remove an amount greater than your balance");
        else User.currentUser.removeBalance(amount);
    }

    static void changeBalanceWithConversion(double amount, String currency, boolean add) throws Exception {
        double amountInCurrentCurrency = amount * User.currentUser.getCurrencies().get(currency) / User.currentUser.getCurrencies().get(User.currentUser.getCurrency());
        if (add) User.currentUser.addBalance(amountInCurrentCurrency);
        else if (amountInCurrentCurrency > User.currentUser.getBalance())
            throw new Exception("The converted amount to remove can't be greater than your balance");
        else User.currentUser.removeBalance(amountInCurrentCurrency);
    }

    static void changeCurrency(String newCurrency, boolean convert) {
        if (convert) {
            User.currentUser.setBalance(convertToCurrency(User.currentUser.getBalance(), newCurrency));
            for (User.Allocation reservation : User.currentUser.getReservations())
                reservation.amount = convertToCurrency(reservation.amount, newCurrency);
            for (User.Allocation milestone : User.currentUser.getMilestones())
                milestone.amount = convertToCurrency(milestone.amount, newCurrency);
        }
        User.currentUser.setCurrency(newCurrency);
    }

    static void addReservation(String name, double amount) {
        User.Allocation reservation = new User.Allocation(name, amount);
        reservation.description = "No description";
        reservation.creationDate = getDate();
        User.currentUser.addReservation(reservation);
        User.currentUser.removeBalance(amount);
    }

    static void addReservation(String name, double amount, String description) throws Exception {
        User.Allocation reservation = new User.Allocation(name, amount);
        if (reservationExists(reservation))
            throw new Exception("The reservation with the name " + name + " already exists");
        reservation.description = description;
        reservation.creationDate = getDate();
        User.currentUser.addReservation(reservation);
        User.currentUser.removeBalance(amount);
    }


    static void removeReservation(String name, boolean complete) throws Exception {
        User.Allocation reservation = User.currentUser.getReservations().get(User.currentUser.getReservations().indexOf(new User.Allocation(name)));
        if (!reservationExists(reservation))
            throw new Exception("The reservation with the name " + name + " doesn't exist");
        if (complete) User.currentUser.addCompletedReservation(reservation, getDate());
        else changeBalance(reservation.amount, true);
        User.currentUser.removeReservation(reservation);
    }

    static void addMilestone(String name, double amount) throws Exception {
        User.Allocation milestone = new User.Allocation(name, amount);
        if (milestoneExists(milestone)) throw new Exception("The milestone with the name " + name + " already exists");
        milestone.description = "No description";
        milestone.creationDate = getDate();
        User.currentUser.addMilestone(milestone);
    }

    static void addMilestone(String name, double amount, String description) throws Exception {
        User.Allocation milestone = new User.Allocation(name, amount);
        if (milestoneExists(milestone)) throw new Exception("The milestone with the name " + name + " already exists");
        milestone.description = description;
        milestone.creationDate = getDate();
        User.currentUser.addMilestone(milestone);
    }

    static void removeMilestone(String name, boolean complete) throws Exception {
        User.Allocation milestone = User.currentUser.getMilestones().get(User.currentUser.getMilestones().indexOf(new User.Allocation(name)));
        if (!milestoneExists(milestone)) throw new Exception("The milestone with the name " + name + " doesn't exist");
        if (complete) {
            if (User.currentUser.getBalance() < milestone.amount)
                throw new Exception("You can't complete this milestone yet");
            User.currentUser.removeBalance(milestone.amount);
            User.currentUser.addCompletedMilestone(milestone, getDate());
        }
        User.currentUser.removeMilestone(milestone);
        User.currentUser.removeNotification(getNotification(name));
    }

    static void updateNotifications() {
        for (User.Allocation milestone : User.currentUser.getMilestones()) {
            String notification = "You have enough balance to complete the milestone with the name \"" + milestone.name + "\"";
            if (User.currentUser.getBalance() >= milestone.amount && !User.currentUser.getNotifications().contains(notification))
                User.currentUser.addNotification(notification);
        }
    }

    static void deleteCurrentUser() {
        repository.deleteEntity(repository.getEntities().indexOf(User.currentUser));
    }

}
