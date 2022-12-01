package Program;

import Program.CRUD.FileRepository;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class UserService {

    public static final FileRepository<User> repository = new FileRepository<>("src/Database/UserData.dat");

    private static void updateCurrentUser() {
        repository.updateEntity(repository.findEntity(User.currentUser), User.currentUser);
    }

    private static int findByUsernameAndPassword(String username, String password) {
        for (User user : repository.getEntities())
            if (user.getUsername().equals(username) && user.getPassword().equals(password))
                return repository.findEntity(user);
        return -1;
    }

    private static double convertToCurrency(double amount, String newCurrency) {
        return amount * User.currencies.get(User.currentUser.getCurrency()) / User.currencies.get(newCurrency);
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

    private static boolean reservationExists(User.Reservation reservation) {
        return User.currentUser.getReservations().contains(reservation);
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

    static void logIn(String username, String password) throws Exception{
        int index = findByUsernameAndPassword(username, password);
        if(index == -1)
            throw new Exception("Invalid username or password");
        User.currentUser = getUsers().get(index);
    }

    static void changeUsername(String newUsername) {
        User.currentUser.setUsername(newUsername);
        updateCurrentUser();
    }

    static void changePassword(String newPassword) {
        User.currentUser.setPassword(newPassword);
        updateCurrentUser();
    }

    static void changeBalance(double amount, boolean add) {
        if (add) User.currentUser.addBalance(amount);
        else User.currentUser.removeBalance(amount);
        updateCurrentUser();
    }

    static void changeCurrency(String newCurrency, boolean convert) {
        if (convert) {
            User.currentUser.setBalance(convertToCurrency(User.currentUser.getBalance(), newCurrency));
            for (User.Reservation reservation : User.currentUser.getReservations()) {
                reservation.amount = convertToCurrency(reservation.amount, newCurrency);
            }
        }
        User.currentUser.setCurrency(newCurrency);
        for (User.Reservation reservation : User.currentUser.getReservations()) {
            reservation.amount = convertToCurrency(reservation.amount, newCurrency);
        }
        updateCurrentUser();
    }

    static void addReservation(String name, double amount) {
        User.Reservation reservation = new User.Reservation(name, amount);
        reservation.description = "No description";
        reservation.creationDate = getDate();
        User.currentUser.addReservation(reservation);
        User.currentUser.removeBalance(amount);
        updateCurrentUser();
    }

    static void addReservation(String name, double amount, String description) throws Exception{
        User.Reservation reservation = new User.Reservation(name, amount);
        if(reservationExists(reservation))
            throw new Exception("The reservation with the name " + name + " already exists");
        reservation.description = description;
        reservation.creationDate = getDate();
        User.currentUser.addReservation(reservation);
        User.currentUser.removeBalance(amount);
        updateCurrentUser();
    }


    static void removeReservation(String name, boolean completed) throws Exception{
        if(!(reservationExists(new User.Reservation(name))))
            throw new Exception("The reservation with the name " + name + " doesn't exist");
        User.Reservation reservation = User.currentUser.getReservations().get(User.currentUser.getReservations().indexOf(new User.Reservation(name)));
        if (completed) User.currentUser.addCompletedReservation(reservation, getDate());
        else changeBalance(reservation.amount, true);
        User.currentUser.removeReservation(new User.Reservation(name, 0));
        updateCurrentUser();
    }

    static void deleteCurrentUser() {
        repository.deleteEntity(repository.getEntities().indexOf(User.currentUser));
    }


}
