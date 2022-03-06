package ConsoleGame;

import java.io.IOException;
import java.util.Scanner;

import BingoParts.*;

/**
 * @author Lucas Schlierf
 */
public class ConsoleGame {
    /**
     * Launches a simple console version of a bingo game. You can mark off fields as prompted.
     */
    public static void main(String[] args) {
        BingoCard playingCard;
        int i = 0;
        int chosen;
        System.out.println("Available sets:\n");
        for(String n : BingoSets.getAllNames()){
            System.out.println(++i + ": " + n);
        }
        try(Scanner scan = new Scanner(System.in)) {
            chosen = scan.nextInt();
            while(chosen >= BingoSets.getAllNames().size() || chosen < 0){
                System.out.println("\nChoose one of the available sets:\n");
                i = 0;
                for(String n : BingoSets.getAllNames()){
                    System.out.println(++i + ": " + n);
                }
                chosen = scan.nextInt();
            }
            playingCard = BingoCard.createFromSet(BingoSets.getAllNames().get(chosen), 3);
            System.out.println("\nYou chose: " + BingoSets.getAllNames().get(chosen));
            while(!playingCard.isCompleted()){
                System.out.println(playingCard.toConsoleOutput());
                System.out.println("Select the Field you want to mark off:");
                System.out.println("X: ");
                int x = scan.nextInt();
                System.out.println("Y: ");
                int y = scan.nextInt();
                if(x < 1 || y < 1 || x > playingCard.getSize() || y > playingCard.getSize()){
                    System.out.println("Please select a value between 1 and " + playingCard.getSize());
                    continue;
                }
                playingCard.markOffTile(x - 1, y - 1);
            }
            System.out.println(playingCard.toConsoleOutput());
            System.out.println("Bingo!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
