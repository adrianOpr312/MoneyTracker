package Program;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private static final Scanner in = new Scanner(System.in);
    private static final String currencyChangePrompt = "You have successfully %sd %,.2f %s %s your balance%n";
    private static boolean closed = false;

    private static void showDelimiter() {
        System.out.println("\n--------------------------------------------------\n");
    }

    private static void showNotifications() {
        if (!User.currentUser.getNotifications().isEmpty()) showDelimiter();
        for (String notification : User.currentUser.getNotifications())
            System.out.println("   !!!!   " + notification.toUpperCase() + "   !!!   ");
        showDelimiter();
    }

    private static void runConnectMenu() {
        while (!closed) {
            try {
                showConnectOptions();
                break;
            } catch (NumberFormatException e) {
                showDelimiter();
                System.out.println("Invalid number");
            } catch (Exception e) {
                showDelimiter();
                System.out.println(e.getMessage());
            }
        }
    }

    public static void runUserMenu() throws Exception {
        while (!closed) {
            try {
                showDelimiter();
                showUserMenu();
                UserService.updateNotifications();
            } catch (NumberFormatException e) {
                showNotifications();
                System.out.println("Invalid number");
            } catch (Exception e) {
                showNotifications();
                System.out.println(e.getMessage());
            }
        }
        UserService.updateCurrentUser();
        UserService.saveUsers();
    }

    private static void showConnectOptions() throws Exception {
        byte choice;
        showDelimiter();
        System.out.println("1 -> Log in");
        System.out.println("2 -> Sign in");
        System.out.println("0 -> Exit");
        System.out.print("\nYour choice -> ");
        choice = Byte.parseByte(in.nextLine().trim());
        switch (choice) {
            case 0 -> closed = true;
            case 1 -> logIn();
            case 2 -> signIn();
            default -> throw new Exception("Invalid choice");
        }
    }

    private static void showUserMenu() throws Exception {
        byte choice;
        System.out.println("1 -> Display Balance");
        System.out.println("2 -> Display your information");
        System.out.println("3 -> Change username");
        System.out.println("4 -> Change password");
        System.out.println("5 -> Add money to your balance");
        System.out.println("6 -> Remove amount from your balance");
        System.out.println("7 -> Add money to your balance from another currency");
        System.out.println("8 -> Remove money from your balance from another currency");
        System.out.println("9 -> Add a milestone");
        System.out.println("10 -> Complete a milestone");
        System.out.println("11 -> Remove a milestone");
        System.out.println("12 -> Show milestones");
        System.out.println("13 -> Show completed milestones");
        System.out.println("14 -> Add a reservation");
        System.out.println("15 -> Complete a reservation");
        System.out.println("16 -> Remove a reservation");
        System.out.println("17 -> Show reservations");
        System.out.println("18 -> Show completed reservations");
        System.out.println("19 -> Change the currency (don't convert)");
        System.out.println("20 -> Change the currency (convert)");
        System.out.println("21 -> Add a new currency");
        System.out.println("22 -> Remove a currency");
        System.out.println("23 -> Delete account");
        System.out.println("24 -> Log out");
        System.out.println("0 -> Exit");
        System.out.print("\nYour choice -> ");
        choice = Byte.parseByte(in.nextLine().trim());
        switch (choice) {
            case 0 -> closed = true;
            case 1 -> showBalance(true);
            case 2 -> showInformation(true);
            case 3 -> changeUsername();
            case 4 -> changePassword();
            case 5 -> changeBalance(true);
            case 6 -> changeBalance(false);
            case 7 -> changeBalanceFromOtherCurrency(true);
            case 8 -> changeBalanceFromOtherCurrency(false);
            case 9 -> makeMilestone();
            case 10 -> removeMilestone(true);
            case 11 -> removeMilestone(false);
            case 12 -> showMilestones(true);
            case 13 -> showCompletedMilestones();
            case 14 -> makeReservation();
            case 15 -> removeReservation(true);
            case 16 -> removeReservation(false);
            case 17 -> showReservations(true);
            case 18 -> showCompletedReservations();
            case 19 -> changeCurrency(false);
            case 20 -> changeCurrency(true);
            case 21 -> addCurrency();
            case 22 -> removeCurrency();
            case 23 -> deleteUser();
            case 24 -> logOut();
            default -> throw new Exception("Invalid choice");
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
    }

    private static void showCurrencies(String... args) {
        List<String> ignoredCurrencies = List.of(args);
        showDelimiter();
        for (String currency : User.currentUser.getCurrencies().keySet()) {
            if (!ignoredCurrencies.contains(currency)) {
                System.out.printf("%s: %,.2f USD%n", currency, User.currentUser.getCurrencies().get(currency));
            }
        }
        showDelimiter();
    }

    private static void showBasicCurrencies() {
        showDelimiter();
        for(String currency: User.basicCurrencies.keySet())
            System.out.printf("%s: %,.2f USD%n", currency, User.basicCurrencies.get(currency));
        showDelimiter();
    }

    private static void showReservations(boolean notifications) throws Exception {
        if (User.currentUser.getReservations().isEmpty()) throw new Exception("You have no reservations");
        if (notifications) showNotifications();
        else showDelimiter();
        for (User.Allocation reservation : User.currentUser.getReservations()) {
            System.out.printf("Name: %s\nAmount: %,.2f %s\nDescription: %s\nCreation date: %s%n", reservation.name, reservation.amount, User.currentUser.getCurrency(), reservation.description, reservation.creationDate);
            if (User.currentUser.getReservations().indexOf(reservation) != User.currentUser.getReservations().size() - 1)
                System.out.println();
        }
    }

    private static void showCompletedReservations() throws Exception {
        if (User.currentUser.getCompletedReservations().isEmpty())
            throw new Exception("You have no completed reservations");
        showDelimiter();
        for (User.Allocation reservation : User.currentUser.getCompletedReservations().keySet()) {
            System.out.printf("Name: %s\nAmount: %,.2f %s\nDescription: %s\nCreation date: %s\nCompletion date: %s%n", reservation.name, reservation.amount, User.currentUser.getCurrency(), reservation.description, reservation.creationDate, User.currentUser.getCompletedReservations().get(reservation));
            if (User.currentUser.getReservations().indexOf(reservation) != User.currentUser.getReservations().size() - 1)
                System.out.println();
        }
    }

    private static void showMilestones(boolean notifications) throws Exception {
        if (User.currentUser.getMilestones().isEmpty()) throw new Exception("You have no milestones");
        if (notifications) showNotifications();
        else showDelimiter();
        for (User.Allocation milestone : User.currentUser.getMilestones()) {
            double requiredAmount = milestone.amount - User.currentUser.getBalance();
            if (requiredAmount > 0)
                System.out.printf("Name: %s\nAmount: %,.2f %s (%,.2f %s required to complete)\nDescription: %s\nCreation date: %s%n", milestone.name, milestone.amount, User.currentUser.getCurrency(), requiredAmount, User.currentUser.getCurrency(), milestone.description, milestone.creationDate);
            else
                System.out.printf("Name: %s\nAmount: %,.2f %s (Acquired!)\nDescription: %s\nCreation date: %s%n", milestone.name, milestone.amount, User.currentUser.getCurrency(), milestone.description, milestone.creationDate);
            if (User.currentUser.getMilestones().indexOf(milestone) != User.currentUser.getMilestones().size() - 1)
                System.out.println();
        }
    }

    private static void showCompletedMilestones() throws Exception {
        int index;
        if (User.currentUser.getCompletedMilestones().isEmpty())
            throw new Exception("You have no completed milestones");
        showDelimiter();
        index = 0;
        for (User.Allocation milestone : User.currentUser.getCompletedMilestones().keySet()) {
            System.out.printf("Name: %s\nAmount: %,.2f %s\nDescription: %s\nCreation date: %s\nCompletion date: %s%n", milestone.name, milestone.amount, User.currentUser.getCurrency(), milestone.description, milestone.creationDate, User.currentUser.getCompletedMilestones().get(milestone));
            if (index != User.currentUser.getCompletedMilestones().keySet().size() - 1) System.out.println();
            index++;
        }
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
    }

    private static void signIn() throws Exception {
        String username;
        String password;
        String currency;
        showDelimiter();
        System.out.print("Enter an username: ");
        username = in.nextLine().trim();
        System.out.print("Enter a  password: ");
        password = in.nextLine().trim();
        showBasicCurrencies();
        System.out.print("Enter the name of the currency you want your balance to be in (you can add other ones and change to them later): ");
        currency = in.nextLine().trim().toUpperCase();
        if (!List.of("USD", "GBP", "EUR", "RON").contains(currency))
            throw new Exception("That is not a valid currency");
        UserService.signIn(username, password, currency);
        showDelimiter();
        System.out.println("Signed in successfully, you are now logged in");
    }

    private static void changeUsername() {
        String newUsername;
        showDelimiter();
        System.out.print("Enter a new username: ");
        newUsername = in.nextLine().trim();
        User.currentUser.setUsername(newUsername);
        showNotifications();
        System.out.println("Username changed successfully");
    }

    private static void changePassword() {
        String newPassword;
        showDelimiter();
        System.out.print("Enter a new password: ");
        newPassword = in.nextLine().trim();
        User.currentUser.setPassword(newPassword);
        showNotifications();
        System.out.println("Password changed successfully");
    }

    private static void changeBalance(boolean add) throws Exception {
        String operation;
        String process;
        double amount;
        if (add) {
            operation = "add";
            process = "to";
        } else {
            operation = "remove";
            process = "from";
        }
        showBalance(false);
        System.out.print("Enter the amount to " + operation + ": ");
        amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount < 0) throw new Exception("Amounts can't be negative");
        UserService.changeBalance(amount, add);
        showNotifications();
        if (add) operation += "e";
        System.out.printf(currencyChangePrompt, operation, amount, User.currentUser.getCurrency(), process);
    }

    private static void changeBalanceFromOtherCurrency(boolean add) throws Exception {
        String operation;
        String process;
        String currency;
        double amount;
        if (add) {
            operation = "add";
            process = "to";
        } else {
            operation = "remove";
            process = "from";
        }
        showCurrencies();
        System.out.print("Enter the name of the currency you want the amount to be in: ");
        currency = in.nextLine().trim().toUpperCase();
        if (!User.currentUser.getCurrencies().containsKey(currency)) throw new Exception("Invalid currency");
        showBalance(false);
        System.out.print("Enter the amount you want to " + operation + ": ");
        amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount < 0) throw new Exception("The amount can't be negative");
        UserService.changeBalanceWithConversion(amount, currency, add);
        showNotifications();
        if (add) operation += "e";
        System.out.printf(currencyChangePrompt, operation, amount, currency, process);
    }

    private static void makeReservation() throws Exception {
        String name;
        double amount;
        byte choice;
        showDelimiter();
        System.out.print("Enter the name of the reservation: ");
        name = in.nextLine().trim();
        System.out.print("Enter the amount you want to set for this reservation (will be removed from your balance): ");
        amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        if (amount > User.currentUser.getBalance())
            throw new Exception("You can't remove an amount grater than your balance");
        System.out.print("Enter 1 if you want to set a description for the reservation, or 0 if you don't: ");
        choice = Byte.parseByte(in.nextLine().trim());
        if (choice == 1) {
            String description;
            System.out.print("Enter the description: ");
            description = in.nextLine().trim();
            UserService.addReservation(name, amount, description);
        } else if (choice == 0) UserService.addReservation(name, amount);
        else throw new Exception("Invalid choice");
        showNotifications();
        System.out.println("Allocation added successfully");
    }

    private static void removeReservation(boolean complete) throws Exception {
        String prompt = "Successfully %s the reservation%n";
        String name;
        showReservations(false);
        System.out.print("Enter the name of the reservation: ");
        name = in.nextLine().trim();
        UserService.removeReservation(name, complete);
        showNotifications();
        if (complete) System.out.printf(prompt, "completed");
        else System.out.printf(prompt, "removed");
    }

    private static void changeCurrency(boolean convert) throws Exception {
        String currency;
        showCurrencies();
        System.out.print("Enter the name of the new currency: ");
        currency = in.nextLine().trim().toUpperCase();
        if (!User.currentUser.getCurrencies().containsKey(currency))
            throw new Exception("That is not a valid currency");
        UserService.changeCurrency(currency, convert);
        showNotifications();
        System.out.println("Currency changed successfully");
    }

    private static void addCurrency() throws Exception {
        String currency;
        double conversionRate;
        showDelimiter();
        System.out.print("Enter the abbreviation of the new currency (Ex: USD): ");
        currency = in.nextLine().trim().toUpperCase();
        if (currency.length() != 3) throw new Exception("Abbreviations must be 3 characters long");
        System.out.print("Enter the conversion rate based on USD ( how much USD is in 1 " + currency + "): ");
        conversionRate = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        User.currentUser.addCurrency(currency, conversionRate);
        showNotifications();
        System.out.println("Currency added successfully");
    }

    private static void removeCurrency() throws Exception {
        String currency;
        if (User.currentUser.getCurrencies().size() == 0)
            throw new Exception("YOU MUST HAVE AT LEAST AN AVAILABLE CURRENCY!");
        showCurrencies();
        System.out.print("Enter the abbreviation of the currency: ");
        currency = in.nextLine().trim().toUpperCase();
        if (!User.currentUser.getCurrencies().containsKey(currency))
            throw new Exception("The currency \"" + currency + "\" does not exist");
        if (currency.equals(User.currentUser.getCurrency())) {
            String choice;
            String newCurrency;
            showDelimiter();
            System.out.print("WARNING: The currency you chose to delete is your current currency, enter 0 to abort or anything else to change your currency ( the price won't be converted ): ");
            choice = in.nextLine().trim();
            if (choice.equals("0")) {
                showNotifications();
                System.out.println("Removal aborted successfully");
                showDelimiter();
                return;
            }
            showCurrencies(currency);
            System.out.print("Enter the abbreviation of the new currency: ");
            newCurrency = in.nextLine().trim().toUpperCase();
            if (newCurrency.equals(currency))
                throw new Exception("You can't pick your current currency as the new currency in this context");
            if (!User.currentUser.getCurrencies().containsKey(newCurrency))
                throw new Exception("The currency \"" + newCurrency + "\" does not exist");
            UserService.changeCurrency(newCurrency, false);
        }
        User.currentUser.removeCurrency(currency);
        showNotifications();
        System.out.println("The currency \"" + currency + "\" has been removed successfully");
    }

    private static void makeMilestone() throws Exception {
        String name;
        double amount;
        byte choice;
        showDelimiter();
        System.out.print("Enter a name for the milestone: ");
        name = in.nextLine().trim();
        System.out.print("Enter the milestone amount: ");
        amount = Double.parseDouble(in.nextLine().replace(',', '.').trim());
        System.out.print("Enter 1 if you want to set a description or 0 if you don't: ");
        choice = Byte.parseByte(in.nextLine().trim());
        if (choice == 1) {
            String description;
            System.out.print("Enter the description: ");
            description = in.nextLine().trim();
            UserService.addMilestone(name, amount, description);
        } else if (choice == 0) UserService.addMilestone(name, amount);
        else throw new Exception("Invalid choice");
        showNotifications();
        System.out.println("Successfully added milestone");
    }

    private static void removeMilestone(boolean complete) throws Exception {
        String prompt = "Successfully %s the milestone%n";
        String name;
        showMilestones(false);
        System.out.print("Enter the name of the milestone: ");
        name = in.nextLine().trim();
        UserService.removeMilestone(name, complete);
        showNotifications();
        if (complete) System.out.printf(prompt, "completed");
        else System.out.printf(prompt, "removed");
    }

    private static void deleteUser() throws Exception {
        int id;
        showInformation(false);
        System.out.print("\nEnter your id to confirm: ");
        id = Integer.parseInt(in.nextLine().replace('#', ' ').trim());
        if (id != User.currentUser.getId()) throw new Exception("Invalid id");
        UserService.deleteCurrentUser();
        showDelimiter();
        System.out.println("User deleted successfully");
        runConnectMenu();
    }

    private static void logOut() {
        UserService.updateCurrentUser();
        runConnectMenu();
    }

    public static void run() throws Exception {
        UserService.loadUsers();
        showConnectOptions();
        if (!closed)
            runUserMenu();
    }

}
