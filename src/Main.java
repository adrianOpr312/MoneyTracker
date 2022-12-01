import Program.UserInterface;

public class Main {
    public static void main(String[] args){
        try {
            UserInterface.run();
        }catch(Exception e){
            System.out.println("Something went wrong");
        }
    }
}