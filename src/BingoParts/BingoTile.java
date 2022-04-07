package BingoParts;

/**
 * This represents a single field on a BingoCard that can be marked off as
 * solved.
 * 
 * @author Lucas Schlierf
 */
public class BingoTile {
    private String text;
    private boolean markedOff;

    /**
     * Creates a new BingoTile with the specified text and marks it as not yet
     * solved.
     * 
     * @param text the text for the BingoTile
     */
    public BingoTile(String text) {
        this.text = text;
        markedOff = false;
    }

    public String getText() {
        return text;
    }

    public boolean isMarkedOff() {
        return markedOff;
    }

    public void markOff() {
        markedOff = true;
    }

    protected void setMarkedOff(boolean markedOff) {
        this.markedOff = markedOff;
    }

    @Override
    public String toString() {
        return (markedOff ? "x\n" : "o\n") + text;
    }
}
