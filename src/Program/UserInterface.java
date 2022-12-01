package Program;

import java.util.Scanner;

public class UserInterface {

    private static final Scanner in = new Scanner(System.in);
    private static boolean closed = false;

    private static void showDelimiter() {
        System.out.println("\n--------------------------------------------------\n");
    }

    private static void showNotifications() {
        if (!User.currentUser.getNotifications().isEmpty()) showDelimiter();
        for (String notification : User.currentUser.getNotifications())
            System.out.println("!- " + notification + " -!");
        showDelimiter();
    }

    private static void showConnectOptions() throws Exception {
        showDelimiter();
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
        System.out.println("7 -> Add a milestone");
        System.out.println("8 -> Complete a milestone");
        System.out.println("9 -> Remove a milestone");
        System.out.println("10 -> Show milestones");
        System.out.println("11 -> Show completed milestones");
        System.out.println("12 -> Add a reservation");
        System.out.println("13 -> Complete a reservation");
        System.out.println("14 -> Remove a reservation");
        System.out.println("15 -> Show reservations");
        System.out.println("16 -> Show completed reservations");
        System.out.println("17 -> Change the currency (don't convert)");
        System.out.println("18 -> Change the currency (convert)");
        System.out.println("19 -> Delete account");
        System.out.println("20 -> Log out");
        System.out.println("0 -> Exit");
        System.out.print("\nYour choice -> ");
        byte choice = Byte.parseByte(in.nextLine().trim());
        switch (choice) {
            case 0 -> closed = true;
            case 1 -> showBalance(true);
            case 2 -> showInformation(true);
            case 3 -> changeUsername();
            case 4 -> changePassword();
            case 5 -> addBalance();
            case 6 -> removeBalance();
            case 7 -> makeMilestone();
            case 8 -> removeMilestone(true);
            case 9 -> removeMilestone(false);
            case 10 -> showMilestones(true);
            case 11 -> showCompletedMilestones();
            case 12 -> makeReservation();
            case 13 -> removeReservation(true);
            case 14 -> removeReservation(false);
            case 15 -> showReservations(true);
            case 16 -> showCompletedReservations();
            case 17 -> changeCurrency(false);
            case 18 -> changeCurrency(true);
            case 19 -> deleteUser();
            case 20 -> showConnectOptions();
            default -> throw new InvalidChoice();
        }
    }


    private static void showBalance(boolean notifications) {
        if (notifications) showNotifications();
        else showDelimiter();
        System.out.printf("Your balance is %,.2f %s%n", User.currentUser.getBalance(), User.currentUser.getCurrency());
        showDelimiter();
    }

    private static void showInformation(boolean notifications) {
        if (notifications) showNotifications();
        else showDelimiter();
        System.out.println(User.currentUser);
        showDelimiter();
    }

    private static void showCurrencies() {
        showDelimiter();
        for (String currency : User.currencies.keySet())
            System.out.println(currency);
        showDelimiter();
    }

    private static void showReservations(boolean notifications) throws Exception {
        if (User.currentUser.getReservations().isEmpty()) throw new Exception("You have no reservations");
        if (notifications) showNotifications();
        else showDelimiter();
        for (User.Reservation reservation : User.currentUser.getReservations()) {
            System.out.printf("Name: %s\nAmount: %,.2f %s\nDescription: %s\nCreation date: %s%n", reservation.name, reservation.amount, User.currentUser.getCurrency(), reservation.description, reservation.creationDate);
            if (User.currentUser.getReservations().indexOf(reservation) != User.currentUser.getReservations().size() - 1)
                System.out.println();
        }
        showDelimiter();
    }

    private static void showCompletedReservations() throws Exception {
        if (User.currentUser.getCompletedReservations().isEmpty())
            throw new Exception("You have no completed reservations");
        showDelimiter();
        for (User.Reservation reservation : User.currentUser.getCompletedReservations().keySet()) {
            System.out.printf("Name: %s\nAmount: %,.2f %s\nDescription: %s\nCreation date: %s\nCompletion date: %s%n", reservation.name, reservation.amount, User.currentUser.getCurrency(), reservation.description, reservation.creationDate, User.currentUser.getCompletedReservations().get(reservation));
            if (User.currentUser.getReservations().indexOf(reservation) != User.currentUser.getReservations().size() - 1)
                System.out.println();
        }
        showDelimiter();
    }

    private static void showMilestones(boolean notifications) throws Exception {
        if (User.currentUser.getMilestones().isEmpty()) throw new Exception("You have no milestones");
        if (notifications) showNotifications();
        else showDelimiter();
        for (User.Reservation milestone : User.currentUser.getMilestones()) {
            double requiredAmount = UserService.getAmountRequiredToCompleteMilestone(milestone);
            if (requiredAmount > 0)
                System.out.printf("Name: %s\nAmount: %,.2f %s (%,.2f %s required to complete)\nDescription: %s\nCreation date: %s%n", milestone.name, milestone.amount, User.currentUser.getCurrency(), requiredAmount, User.currentUser.getCurrency(), milestone.description, milestone.creationDate);
            else
                System.out.printf("Name: %s\nAmount: %,.2f %s (Acquired!)\nDescription: %s\nCreation date: %s%n", milestone.name, milestone.amount, User.currentUser.getCurrency(), milestone.description, milestone.creationDate);
            if (User.currentUser.getMilestones().indexOf(milestone) != User.currentUser.getMilestones().size() - 1)
                System.out.println();
        }
        showDelimiter();
    }

    private static void showCompletedMilestones() throws Exception {
        if (User.currentUser.getCompletedMilestones().isEmpty())
            throw new Exception("You have no completed milestones");
        showDelimiter();
        int index = 0;
        for (User.Reservation milestone : User.currentUser.getCompletedMilestones().keySet()) {
            System.out.printf("Name: %s\nAmount: %,.2f %s\nDescription: %s\nCreation date: %s\nCompletion date: %s%n", milestone.name, milestone.amount, User.currentUser.getCurrency(), milestone.description, milestone.creationDate, User.currentUser.getCompletedMilestones().get(milestone));
            if (index != User.currentUser.getCompletedMilestones().keySet().size() - 1) System.out.println();
            index++;
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
        showNotifications();
        System.out.println("Username changed successfully");
        showDelimiter();
    }

    private static void changePassword() {
        showDelimiter();
        System.out.print("Enter a new password: ");
        String newPassword = in.nextLine().trim();
        UserService.changePassword(newPassword);
        showNotifications();
        System.out.println("Password changed successfully");
        showDelimiter();
    }

    private static void addBalance() throws Exception {
        showBalance(false);
        System.out.print("Enter the amount to add: ");
        double amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount < 0) throw new Exception("Amounts can't be negative");
        UserService.changeBalance(amount, true);
        showNotifications();
        System.out.println("Balanced added successfully");
        showDelimiter();
    }

    private static void removeBalance() throws Exception {
        showBalance(false);
        System.out.print("Enter the amount to remove: ");
        double amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount > User.currentUser.getBalance())
            throw new Exception("You can't remove an amount grater than your balance");
        if (amount < 0) throw new Exception("Amounts can't be negative");
        UserService.changeBalance(amount, false);
        showNotifications();
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
        if (choice == 1) {
            System.out.print("Enter the description: ");
            String description = in.nextLine().trim();
            UserService.addReservation(name, amount, description);
        } else if (choice == 0) UserService.addReservation(name, amount);
        else throw new InvalidChoice();
        showNotifications();
        System.out.println("Reservation added successfully");
        showDelimiter();

    }

    private static void removeReservation(boolean complete) throws Exception {
        showReservations(false);
        System.out.print("Enter the name of the reservation: ");
        String name = in.nextLine().trim();
        UserService.removeReservation(name, complete);
        String prompt = "Successfully %s the reservation%n";
        showNotifications();
        if (complete) System.out.printf(prompt, "completed");
        else System.out.printf(prompt, "removed");
        showDelimiter();
    }

    private static void changeCurrency(boolean convert) throws Exception {
        showCurrencies();
        System.out.print("Enter the name of the new currency: ");
        String currency = in.nextLine().trim().toUpperCase();
        if (!User.currencies.containsKey(currency)) throw new Exception("That is not a valid currency");
        UserService.changeCurrency(currency, convert);
        showNotifications();
        System.out.println("Currency changed successfully");
        showDelimiter();

    }

    private static void makeMilestone() throws Exception {
        showDelimiter();
        System.out.print("Enter a name for the milestone: ");
        String name = in.nextLine().trim();
        System.out.print("Enter the milestone amount: ");
        double amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        System.out.print("Enter 1 if you want to set a description or 0 if you don't: ");
        byte choice = Byte.parseByte(in.nextLine().trim());
        if (choice == 1) {
            System.out.print("Enter the description: ");
            String description = in.nextLine().trim();
            UserService.addMilestone(name, amount, description);
        } else if (choice == 0) UserService.addMilestone(name, amount);
        else throw new InvalidChoice();
        showNotifications();
        System.out.println("Successfully added milestone");
        showDelimiter();
    }

    private static void removeMilestone(boolean complete) throws Exception {
        showMilestones(false);
        System.out.print("Enter the name of the milestone: ");
        String name = in.nextLine().trim();
        UserService.removeMilestone(name, complete);
        showNotifications();
        String prompt = "Successfully %s the milestone%n";
        if (complete) System.out.printf(prompt, "completed");
        else System.out.printf(prompt, "removed");
        showDelimiter();
    }

    private static void deleteUser() throws Exception {
        showInformation(false);
        System.out.print("Enter your id to confirm: ");
        int id = Integer.parseInt(in.nextLine().replace('#', ' ').trim());
        if (id != User.currentUser.getId()) throw new Exception("Invalid id");
        UserService.deleteCurrentUser();
        showDelimiter();
        System.out.println("User deleted successfully");
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
            } catch (Exception e) {
                showDelimiter();
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            try {
                showUserMenu();
                UserService.updateNotifications();
                if (closed) {
                    UserService.saveUsers();
                    break;
                }
            } catch (NumberFormatException e) {
                showNotifications();
                System.out.println("Invalid number");
                showDelimiter();
            } catch (Exception e) {
                showNotifications();
                System.out.println(e.getMessage());
                showDelimiter();
            }
        }
    }

    private static class InvalidChoice extends Exception {
        InvalidChoice() {
            super("Invalid choice");
        }

    }

}
