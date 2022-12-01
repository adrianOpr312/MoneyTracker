import Program.UserInterface;

public class Main {
    public static void main(String[] args){
        try {
            UserInterface.run();
        }catch(Exception e){
            System.out.println("One of the save files corrupted or not found");
        }
    }
}