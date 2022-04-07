package BingoParts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles storing and fetching BingoSets to and from storage. The
 * exact location is /bin/BingoParts/Sets/
 * 
 * @author Lucas Schlierf
 */
public class BingoSets {

    /**
     * This class only has static Methods, so it shouldn't be instanciated.
     */
    private BingoSets() {
    }

    /**
     * Fetches all paths of BingoSets in storage.
     * 
     * @return List of all BingoSet paths (as Strings)
     */
    private static List<String> getAllPaths() {
        return Arrays
                .stream(new File(BingoSets.class.getClassLoader().getResource("BingoParts/Sets").getPath()).listFiles())
                .filter(f -> !f.getName().isBlank()).map(f -> f.getAbsolutePath()).toList();
    }

    /**
     * Fetches all names of BingoSets in storage.
     * 
     * @return List of all BingoSet names
     */
    public static List<String> getAllNames() {
        return getAllPaths().stream().map(s -> new File(s).getName().split("\\.")[0]).filter(s -> !s.isBlank())
                .toList();
    }

    /**
     * Fetches all entries of a BingoSet.
     * 
     * @param name the name of the BingoSet
     * @return a List of all lines in the BingoSet (as Strings)
     * @throws IOException if the path does not lead to a BingoSet
     */
    public static List<String> getSet(String name) throws IOException {
        if (!new File(fetchPath(name)).exists()) {
            return new ArrayList<String>();
        }
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fetchPath(name), Charset.forName("UTF-8")));
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        return lines;
    }

    /**
     * Tries to create a new BingoSet with the specified entries.
     * 
     * @param name  the name of the BingoSet
     * @param texts the entries of the BingoSet
     * @return {@code true} if the BingoSet could be created, {@code false}
     *         otherwise
     * @throws IOException if an IO excption occurs
     */
    public static boolean createSet(String name, String... texts) throws IOException {
        File directory = new File(BingoSets.class.getClassLoader().getResource("BingoParts/Sets").getPath());
        File newSet = new File(directory, name + ".txt");
        if (!newSet.createNewFile()) {
            return false;
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(newSet, Charset.forName("UTF-8")));
        for (String s : texts) {
            bw.write(s + System.lineSeparator());
        }
        bw.flush();
        bw.close();
        return true;
    }

    /**
     * Deletes the specified BingoSet.
     * 
     * @param name the name of the BingoSet
     * @return {@code true} if the BingoSet could be deleted, {@code false}
     *         otherwise
     */
    public static boolean deleteSet(String name) {
        return new File(fetchPath(name)).delete();
    }

    /**
     * Throws out all entries in the specified BingoSet and enters the specified
     * entries.
     * 
     * @param name  the name of the BingoSet
     * @param texts the entries to be placed in the BingoSet
     * @return {@code true} if the entries could be changed, {@code false} otherwise
     * @throws IOException if an IO exception occurs
     */
    public static boolean changeSet(String name, String... texts) throws IOException {
        if (!deleteSet(fetchPath(name)))
            return false;
        return createSet(name, texts);
    }

    /**
     * Extracts the file name from the specified path.
     * 
     * @param path the path to the file
     * @return the file name
     */
    private static String getNameFromPath(String path) {
        return new File(path).getName().split("\\.")[0];
    }

    /**
     * Fetches the path of the specified BingoSet.
     * 
     * @param name the name of the BingoSet
     * @return the path of the BingoSet if it exists, or {@code null} otherwise
     */
    private static String fetchPath(String name) {
        return getAllPaths().stream().distinct().filter(p -> getNameFromPath(p).equals(name)).findFirst().orElse(null);
    }

}
