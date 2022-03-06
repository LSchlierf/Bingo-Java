package ConsoleGame;

import java.io.IOException;

import BingoParts.*;

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
        try {
            chosen = System.in.read() - 49;
            System.in.skip(System.in.available());
            while(chosen >= BingoSets.getAllNames().size() || chosen < 0){
                System.out.println("\nChoose one of the available sets:\n");
                i = 0;
                for(String n : BingoSets.getAllNames()){
                    System.out.println(++i + ": " + n);
                }
                chosen = System.in.read() - 49;
                System.in.skip(System.in.available());
            }
            playingCard = BingoCard.createFromSet(BingoSets.getAllNames().get(chosen), 3);
            System.out.println("\nYou chose: " + BingoSets.getAllNames().get(chosen));
            while(!playingCard.isCompleted()){
                System.out.println(playingCard.toConsoleOutput());
                System.out.println("Select the Field you want to mark off:");
                System.out.println("X: ");
                int x = System.in.read() - 48;
                System.in.skip(System.in.available());
                System.out.println("Y: ");
                int y = System. in.read() - 48;
                System.in.skip(System.in.available());
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
