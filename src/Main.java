import Program.CRUD.FileRepository;
import Program.UserInterface;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            UserInterface.run();
        } catch (Exception e) {
            Scanner in = new Scanner(System.in);
            String choice;
            System.out.print("One or more of the save files is corrupted or not found, enter 0 if you want to exit or any other key if you want to repair them (WARNING: this will erase all the data previously saved): ");
            choice = in.nextLine().trim();
            if (!choice.equals("0")) {
                FileRepository.saveObject(null, "src/Database/UserData.dat");
                UserInterface.run();
            }
        }
    }
}