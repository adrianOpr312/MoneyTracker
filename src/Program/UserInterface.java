package Program;

import java.util.Scanner;

public class UserInterface {

    private static class InvalidChoice extends Exception{
        InvalidChoice(){
            super("Invalid choice");
        }

    }

    private static final Scanner in = new Scanner(System.in);
    private static boolean closed = false;

    private static void showDelimiter(){
        System.out.println("\n--------------------------------------------------\n");
    }

    private static void showConnectOptions() throws Exception {
        System.out.println("1 -> Log in");
        System.out.println("2 -> Sign in");
        System.out.println("0 -> Exit");
        System.out.print("\nYour choice -> ");
        byte choice = Byte.parseByte(in.nextLine().trim());
        switch (choice) {
            case 0 -> closed = true;
            case 1 -> logIn();
            case 2 -> signIn();
            default -> throw new InvalidChoice();
        }
    }

    private static void showUserMenu() throws Exception {
        System.out.println("1 -> Display Balance");
        System.out.println("2 -> Display your information");
        System.out.println("3 -> Change username");
        System.out.println("4 -> Change password");
        System.out.println("5 -> Add money to your balance");
        System.out.println("6 -> Remove amount from your balance");
        System.out.println("7 -> Reserve amount for something");
        System.out.println("8 -> Complete a reservation");
        System.out.println("9 -> Remove a reservation");
        System.out.println("10 -> Show reservations");
        System.out.println("11 -> Show completed reservations");
        System.out.println("12 -> Change the currency (don't convert)");
        System.out.println("13 -> Change the currency (convert)");
        System.out.println("14 -> Delete account");
        System.out.println("15 -> Log out");
        System.out.println("0 -> Exit");
        System.out.print("\nYour choice -> ");
        byte choice = Byte.parseByte(in.nextLine().trim());
        switch (choice) {
            case 0 -> closed = true;
            case 1 -> showBalance();
            case 2 -> showInformation();
            case 3 -> changeUsername();
            case 4 -> changePassword();
            case 5 -> addBalance();
            case 6 -> removeBalance();
            case 7 -> makeReservation();
            case 8 -> removeReservation(true);
            case 9 -> removeReservation(false);
            case 10 -> showReservations();
            case 11 -> showCompletedReservations();
            case 12 -> changeCurrency(false);
            case 13 -> changeCurrency(true);
            case 14 -> deleteUser();
            case 15 -> showConnectOptions();
            default -> throw new InvalidChoice();
        }
    }

    private static void showBalance() {
        showDelimiter();
        System.out.printf("Your balance is %,.2f %s%n", User.currentUser.getBalance(), User.currentUser.getCurrency());
        showDelimiter();
    }

    private static void showInformation() {
        showDelimiter();
        System.out.println(User.currentUser);
        showDelimiter();
    }

    private static void showCurrencies() {
        showDelimiter();
        for (String currency : User.currencies.keySet())
            System.out.println(currency);
        showDelimiter();
    }

    private static void showReservations() throws Exception{
        if(User.currentUser.getReservations().size() == 0)
            throw new Exception("You have no reservations");
        showDelimiter();
        for (User.Reservation reservation : User.currentUser.getReservations())
            System.out.println(reservation + "\n");
        showDelimiter();
    }

    private static void showCompletedReservations() throws Exception{
        if(User.currentUser.getCompletedReservations().size() == 0)
            throw new Exception("You have no completed reservations");
        showDelimiter();
        for (User.Reservation reservation: User.currentUser.getCompletedReservations().keySet()){

            System.out.printf("%s\nCompletion date: %s\n", reservation.toString(), User.currentUser.getCompletedReservations().get(reservation));

        }
        showDelimiter();
    }

    private static void logIn() throws Exception {
        showDelimiter();
        System.out.print("Enter your username: ");
        String username = in.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = in.nextLine().trim();
        UserService.logIn(username, password);
        showDelimiter();
        System.out.println("Logged in successfully");
        showDelimiter();
    }

    private static void signIn() throws Exception {
        showDelimiter();
        System.out.print("Enter an username: ");
        String username = in.nextLine().trim();
        System.out.print("Enter a  password: ");
        String password = in.nextLine().trim();
        showCurrencies();
        System.out.print("Enter the name of the currency you want your balance to be in (you can change this later): ");
        String currency = in.nextLine().trim().toUpperCase();
        if (!User.currencies.containsKey(currency)) throw new Exception("That is not a valid currency");
        UserService.signIn(username, password, currency);
        showDelimiter();
        System.out.println("Signed in successfully, you are now logged in");
        showDelimiter();
    }

    private static void changeUsername() {
        showDelimiter();
        System.out.print("Enter a new username: ");
        String newUsername = in.nextLine().trim();
        UserService.changeUsername(newUsername);
        showDelimiter();
        System.out.println("Username changed successfully");
        showDelimiter();
    }

    private static void changePassword() {
        showDelimiter();
        System.out.print("Enter a new password: ");
        String newPassword = in.nextLine().trim();
        UserService.changePassword(newPassword);
        showDelimiter();
        System.out.println("Password changed successfully");
        showDelimiter();
    }

    private static void addBalance() throws Exception {
        showDelimiter();
        System.out.print("Enter the amount to add: ");
        double amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount < 0) throw new Exception("Amounts can't be negative");
        UserService.changeBalance(amount, true);
        showDelimiter();
        System.out.println("Balanced added successfully");
        showDelimiter();
    }

    private static void removeBalance() throws Exception {
        showDelimiter();
        System.out.print("Enter the amount to remove: ");
        double amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount > User.currentUser.getBalance())
            throw new Exception("You can't remove an amount grater than your balance");
        if (amount < 0) throw new Exception("Amounts can't be negative");
        UserService.changeBalance(amount, false);
        showDelimiter();
        System.out.print("Balanced removed successfully");
        showDelimiter();
    }

    private static void makeReservation() throws Exception {
        showDelimiter();
        System.out.print("Enter the name of the reservation: ");
        String name = in.nextLine().trim();
        System.out.print("Enter the amount you want to set for this reservation (will be removed from your balance): ");
        double amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount > User.currentUser.getBalance())
            throw new Exception("You can't remove an amount grater than your balance");
        System.out.print("Enter 1 if you want to set a description for the reservation, or 0 if you don't: ");
        byte choice = Byte.parseByte(in.nextLine().trim());
        if (choice != 0 && choice != 1) throw new Exception("Invalid choice");
        else if (choice == 1) {
            System.out.print("Enter the description: ");
            String description = in.nextLine().trim();
            UserService.addReservation(name, amount, description);
        } else UserService.addReservation(name, amount);
        showDelimiter();
        System.out.println("Reservation added successfully");
        showDelimiter();

    }

    private static void removeReservation(boolean complete) throws Exception {
        showReservations();
        System.out.print("Enter the name of the reservation: ");
        String name = in.nextLine().trim();
        UserService.removeReservation(name, complete);
        String prompt = "Reservation %s successfully";
        showDelimiter();
        if (complete) System.out.printf(prompt + "\n", "completed");
        else System.out.printf(prompt + "\n", "removed");
        showDelimiter();
    }

    private static void changeCurrency(boolean convert) throws Exception {
        showCurrencies();
        System.out.print("Enter the name of the new currency: ");
        String currency = in.nextLine().trim().toUpperCase();
        if (!User.currencies.containsKey(currency)) throw new Exception("That is not a valid currency");
        UserService.changeCurrency(currency, convert);
        showDelimiter();
        System.out.println("Currency changed successfully");
        showDelimiter();

    }

    private static void deleteUser() throws Exception {
        showInformation();
        System.out.print("Enter your id to confirm: ");
        int id = Integer.parseInt(in.nextLine().replace('#', ' ').trim());
        if (id != User.currentUser.getId()) throw new Exception("Invalid id");
        UserService.deleteCurrentUser();
        showDelimiter();
        System.out.println("User deleted successfully");
        showDelimiter();
        showConnectOptions();
    }

    public static void run() throws Exception {
        UserService.loadUsers();
        while (true) {
            try {
                showConnectOptions();
                if (closed) {
                    UserService.saveUsers();
                    return;
                }
                break;
            } catch (NumberFormatException | InvalidChoice e) {
                showDelimiter();
                System.out.println("Invalid choice");
                showDelimiter();
            } catch (Exception e) {
                showDelimiter();
                System.out.println(e.getMessage());
                showDelimiter();
            }
        }
        while (true) {
            try {
                showUserMenu();
                if (closed) {
                    UserService.saveUsers();
                    break;
                }
            } catch (NumberFormatException | InvalidChoice e) {
                showDelimiter();
                System.out.println("Invalid choice");
                showDelimiter();
            } catch (Exception e) {
                showDelimiter();
                System.out.println(e.getMessage());
                showDelimiter();
            }
        }
    }

}
