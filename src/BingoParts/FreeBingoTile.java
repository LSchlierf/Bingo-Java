package BingoParts;

/**
 * This represents a BingoField that is always marked off.
 * @author Lucas Schlierf
 */
public class FreeBingoTile extends BingoTile {

    /**
     * Creates a BingoField that displays the text "FREE" and is always marked off.
     */
    public FreeBingoTile(){
        super("FREE");
    }

    @Override
    public boolean isMarkedOff() {
        return true;
    }

    @Override
    public String toString() {
        return "FREE";
    }
}
