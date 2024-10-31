import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        boolean gameIsRunning = true;
        Scanner scanner = new Scanner(System.in);
        int round = 1;
        Character you = new Character(3,3,3);
        while (gameIsRunning) {
            System.out.println("YOU: " + you.toString());
            //gen enemy
            Character enemy = Character.generateEnemy(round);
            System.out.println("ENEMY: " + enemy);
            System.out.println("Round: " + round + ", type c to continue, s to stop");
            String option = scanner.next();
            switch (option) {
                /*case "i":
                case "info":
                    //stuff
                    break;*/
                case "c":
                case "continue":
                    System.out.println("cont");

                    boolean won = Character.fight(you, enemy);
                    if (won) {
                        round++;
                        System.out.println("after fight YOU: " + you.toString());
                        System.out.println("You won");
                        you.atkUp();
                        you.hpUp();
                        System.out.println("new YOU: " + you.toString());
                    } else {
                        gameIsRunning = false;
                        System.out.println("you lost");
                        System.out.println("Game has terminated");
                    }

                    //stuff
                    break;
                case "s":
                case "stop":
                    gameIsRunning = false;
                    System.out.println("Game has terminated");
                    break;

            }
        }
    }
}
