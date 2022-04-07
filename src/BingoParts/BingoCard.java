package BingoParts;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This represents a playing card with a field of BingoTiles that can be marked
 * off.
 * 
 * @author Lucas Schlierf
 */
public class BingoCard {
    private BingoTile[][] tiles;

    /**
     * Creates a new BingoCard form the BinogTile array. Constructor is set to
     * private to insure only square BingoCards are created, use static methods to
     * obtain BingoCards.
     * 
     * @param tiles the BingoTiles you want this BingoCard to hold
     */
    private BingoCard(BingoTile[][] tiles) {
        this.tiles = tiles;
    }

    /**
     * Replaces the center BingoTile with a FreeBingoTile. Only works when the side
     * length is an odd number, so the BingoCard has an actual center.
     */
    public void addFreeTile() {
        if (getSize() % 2 == 1)
            tiles[tiles.length / 2][tiles[0].length / 2] = new FreeBingoTile();
    }

    /**
     * Returns the specified BingoTile.
     * 
     * @param x the x coordinate, from 0 (inclusive), to size (exclusive).
     * @param y the y coordinate, from 0 (inclusive), to size (exclusive).
     * @return the specified BingoTile
     */
    public BingoTile getTile(int x, int y) {
        return tiles[y][x];
    }

    /**
     * Gets the whole BingoTile array.
     * 
     * @return the BingoTile array
     */
    protected BingoTile[][] getTiles() {
        return tiles;
    }

    /**
     * Replaces a BingoTile with the specified one.
     * 
     * @param x     the x coordinate, from 0 (inclusive), to size (exclusive).
     * @param y     the y coordinate, from 0 (inclusive), to size (exclusive).
     * @param field the BingoTile you want to place
     */
    public void setField(int x, int y, BingoTile field) {
        tiles[y][x] = field;
    }

    /**
     * Marks off one BingoTile as solved.
     * 
     * @param x the x coordinate, from 0 (inclusive), to size (exclusive).
     * @param y the y coordinate, from 0 (inclusive), to size (exclusive).
     */
    public void markOffTile(int x, int y) {
        tiles[y][x].setMarkedOff(true);
    }

    /**
     * Returns the size of this BingoCard.
     * 
     * @return the size
     */
    public int getSize() {
        return tiles.length;
    }

    /**
     * Evaluates whether this BingoCard has been completely solved, whether it be
     * vertically, horizontally, or diagonally.
     * 
     * @return {@code true} if this BingoCard is completely solved, {@code false}
     *         otherwise
     */
    public boolean isCompleted() {
        boolean horizontal = true;
        boolean vertical = true;
        boolean diagonalA = true;
        boolean diagonalB = true;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (!(vertical || horizontal))
                    break;
                horizontal &= tiles[i][j].isMarkedOff();
                vertical &= tiles[j][i].isMarkedOff();
            }
            if (vertical || horizontal)
                return true;
            vertical = true;
            horizontal = true;
            diagonalA &= tiles[i][i].isMarkedOff();
            diagonalB &= tiles[i][tiles.length - i - 1].isMarkedOff();
        }
        return diagonalA || diagonalB;
    }

    /**
     * Evaluates how many BingoTiles have been marked off.
     * 
     * @return the number of BingoTiles that have been marked off
     */
    public int numMarkedOff() {
        int result = 0;
        for (BingoTile[] a : tiles) {
            for (BingoTile t : a) {
                if (t.isMarkedOff())
                    result++;
            }
        }
        return result;
    }

    /**
     * Creates a new BingoCard from an array of Strings. Array should be square.
     * 
     * @param texts the Strings from which to create the BingoCard
     * @return the new BingoCard
     */
    public static BingoCard createFromStringArray(String[][] texts) {
        if (texts == null || texts.length < 1 || texts.length != texts[0].length) {
            return null;
        }
        return new BingoCard(
                Arrays.stream(texts).map(a -> Arrays.stream(a).map(s -> new BingoTile(s)).toArray(BingoTile[]::new))
                        .toArray(BingoTile[][]::new));
    }

    /**
     * Creates a new BingoCard from a BingoSet.
     * 
     * @param setName     the name of the BingoSet
     * @param size        the size of the BingoCard
     * @param addFreeTile wether to add a free BingoTile in the middle (only works
     *                    for odd sizes)
     * @return {@code null} if the BingoSet doesn't contain contain enough entries,
     *         or the new BingoCard otherwise.
     * @throws IOException if an IOException occurs while fetching the BingoSet
     */
    public static BingoCard createFromSet(String setName, int size, boolean addFreeTile) throws IOException { // TODO:
                                                                                                              // hande
                                                                                                              // free
                                                                                                              // tile
        addFreeTile &= (size % 2 == 1);
        int minEntries = size * size - (addFreeTile ? 1 : 0);
        List<String> lines = BingoSets.getSet(setName);
        if (lines.size() < minEntries) {
            return null;
        }
        Collections.shuffle(lines, new Random(System.currentTimeMillis()));
        BingoTile[][] tiles = new BingoTile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (addFreeTile && i == size / 2 && j == size / 2)
                    tiles[i][j] = new FreeBingoTile();
                else {
                    String text = lines.get(new Random(System.currentTimeMillis()).nextInt(lines.size()));
                    tiles[i][j] = new BingoTile(text);
                    lines.remove(text);
                }
            }
        }
        return new BingoCard(tiles);
    }

    /**
     * Creates a new BingoCard with size 5 using
     * {@code createFromSet(setName, size)}.
     * 
     * @param setName the name of the BingoSet
     * @return {@code null} if the BingoSet doesn't contain contain enough entries,
     *         or the new BingoCard otherwise.
     * @throws IOException if an IOException occurs while fetching the BingoSet
     */
    public static BingoCard createFromSet(String setName) throws IOException {
        return createFromSet(setName, 5, true);
    }

    @Override
    public String toString() {
        return "BingoCard\nSize: " + getSize()
                + (isCompleted() ? "\nFinished" : "\nNot finished, " + numMarkedOff() + " marked off");
    }

    /**
     * Formats this BingoCard for console print, leaving out whether the BingoTiles
     * have been marked off.
     * 
     * @return a nicely formatted String version of this BingoCard
     */
    public String toConsolePrint() {
        return Printing.toConsolePrint(this);
    }

    /**
     * Formats this BinogCard for console output, also displaying which BingoTiles
     * have been marked off.
     * 
     * @return a nicely formatted String version of this BingoCard
     */
    public String toConsoleOutput() {
        return Printing.toConsoleOutput(this);
    }

}
