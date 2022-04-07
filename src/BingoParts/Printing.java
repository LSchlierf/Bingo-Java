package BingoParts;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Provides several methods for formatting BingoCards and printing them to a PDF
 * or the console
 * 
 * @author Lucas Schlierf
 */
public class Printing {

    private static final int[] X_VALUES = { 20, 320 };
    private static final int[] Y_VALUES = { 770, 510, 250 };
    private static final int PAGE_SIZE_X = 550;
    private static final int PAGE_SIZE_Y = 780;

    private static final String PATH = Printing.class.getClassLoader().getResource("BingoParts/PrintOutput").getPath();

    /**
     * This class only has static Methods, so it shouldn't be instanciated.
     */
    private Printing() {
    }

    /**
     * Formats the BingoCard for console printing, using
     * {@code formatForConsoleOutput()}. Leaves out whether the BingoTiles are
     * marked off.
     * 
     * @param card the BingoCard to format
     * @return a formatted String, ready for console printing
     */
    public static String toConsolePrint(BingoCard card) {
        return formatForConsoleOutput(Arrays.stream(card.getTiles())
                .map(a -> Arrays.stream(a).map(t -> t.getText().replaceAll(" ", "\n")).toArray(String[]::new))
                .toArray(String[][]::new));
    }

    /**
     * Formats the BingoCard for console output, using
     * {@code formatForConsoleOutput()}, also displaying which BingoTiles have been
     * marked off
     * 
     * @param card the BingoCard to format
     * @return a formatted String, ready for console output
     */
    public static String toConsoleOutput(BingoCard card) {
        return formatForConsoleOutput(Arrays.stream(card.getTiles())
                .map(a -> Arrays.stream(a).map(t -> t.toString().replaceAll(" ", "\n")).toArray(String[]::new))
                .toArray(String[][]::new));
    }

    /**
     * Formats the given String array for console output. Only looks good with
     * monospaced font.
     * 
     * @param texts the Strings to print, should be a square Array.
     * @return a formatted String, ready for console printing.
     */
    protected static String formatForConsoleOutput(String[][] texts) {
        StringBuilder lineSeparator = new StringBuilder();
        String[] allTexts = Arrays.stream(texts).reduce(new String[0], (a, b) -> mergeTwoArrays(a, b));
        int maxRows = Arrays.stream(allTexts)
                .mapToInt(s -> Arrays.stream(s.split("\n")).mapToInt(w -> w.length()).max().getAsInt()).max()
                .getAsInt();
        texts = formatTexts(texts, maxRows);
        allTexts = Arrays.stream(texts).reduce(new String[0], (a, b) -> mergeTwoArrays(a, b));
        int maxLines = 1
                + Arrays.stream(allTexts).mapToInt(s -> s.length() - s.replaceAll("\n", "").length()).max().getAsInt();
        lineSeparator.append("├");
        for (int i = 0; i < texts.length; i++) {
            for (int j = 0; j < maxRows; j++) {
                lineSeparator.append("─");
            }
            lineSeparator.append("┼");
        }
        lineSeparator.deleteCharAt(lineSeparator.length() - 1);
        lineSeparator.append("┤");
        String middleLine = lineSeparator.toString();
        String topLine = middleLine.replaceAll("├", "┌").replaceAll("┤", "┐").replaceAll("┼", "┬");
        String bottomLine = topLine.replaceAll("┌", "└").replaceAll("┐", "┘").replaceAll("┬", "┴");

        StringBuilder result = new StringBuilder(topLine + "\n");
        for (int i = 0; i < texts.length; i++) {
            for (int j = 0; j < maxLines; j++) {
                StringBuilder currentLine = new StringBuilder();
                currentLine.append("│");
                for (int k = 0; k < texts.length; k++) {
                    String[] currentText = texts[i][k].split("\n");
                    if (j < currentText.length) {
                        currentLine.append(currentText[j]);
                    } else {
                        currentLine.append(" ");
                    }
                    while (currentLine.length() % (maxRows + 1) != 0) {
                        currentLine.append(" ");
                    }
                    currentLine.append("│");
                }
                currentLine.append("\n");
                result.append(currentLine);
            }
            if (i + 1 < texts.length)
                result.append(middleLine + "\n");
            else
                result.append(bottomLine);
        }
        return result.toString();
    }

    private static String[][] formatTexts(String[][] input, int maxLength) {
        for (int i = 0; i < input.length; i++) {
            String[] a = input[i];
            for (int j = 0; j < a.length; j++) {
                String[] words = a[j].split("\n");
                for (int k = 0; k < words.length - 1; k++) {
                    if (words[k] == null)
                        continue;
                    if (words[k].length() + words[k + 1].length() < maxLength) {
                        words[k] = words[k] + " " + words[k + 1];
                        words[k + 1] = null;
                    }
                }
                input[i][j] = words[0];
                for (int k = 1; k < words.length; k++) {
                    if (words[k] != null)
                        input[i][j] = input[i][j] + "\n" + words[k];
                }
            }
        }
        return input;
    }

    private static String[] mergeTwoArrays(String[] a, String[] b) {
        String[] result = new String[a.length + b.length];
        int index = 0;
        for (String s : a) {
            result[index++] = s;
        }
        for (String s : b) {
            result[index++] = s;
        }
        return result;
    }

    private static String formatForPDF(String input) {
        return input.replaceAll("[┼├┤┬┴┌┐└┘]", "+").replaceAll("│", "|").replaceAll("─", "-");
    }

    /**
     * Creates a PDF from the provided BingoCard.
     * 
     * @param card    the card you want to print
     * @param setName the name of the set the card was created from
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printToPDF(BingoCard card, String setName) throws IOException {
        printToPDF(card, setName, PATH);
    }

    /**
     * Creates a PDF from the provided BingoCard and saves it at the provided path.
     * 
     * @param card    the card you want to print
     * @param setName the name of the set the card was created from
     * @param path    the path to save the PDF to
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printToPDF(BingoCard card, String setName, String path) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDFont font = PDType1Font.COURIER;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(20, 770);
        for (String s : formatForPDF(toConsolePrint(card)).split("\n")) {
            contentStream.showText(s);
            contentStream.newLineAtOffset(0, -14);
        }
        contentStream.endText();
        contentStream.close();

        document.save(path + "/BingoCard-" + System.currentTimeMillis() + ".pdf");
        document.close();
    }

    /**
     * Creates a PDF from a newly created BingoCard from the specified BingoSet,
     * using {@code printToPDF()}. Defaults to size 3, using a free tile if
     * possible, and the standard output Path.
     * 
     * @param setName the BingoSet to use for the BingoCard
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printOne(String setName) throws IOException {
        printOne(setName, 3);
    }

    /**
     * Creates a PDF from a newly created BingoCard from the specified BingoSet,
     * using {@code printToPDF()}. Defaults to using a free tile if possible, and
     * the standard output Path.
     * 
     * @param setName the BingoSet to use for the BingoCard
     * @param size    the size of the BingoCard
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printOne(String setName, int size) throws IOException {
        printOne(setName, size, true);
    }

    /**
     * Creates a PDF from a newly created BingoCard from the specified BingoSet,
     * using {@code printToPDF()}. Defaults to the standard output Path.
     * 
     * @param setName     the BingoSet to use for the BingoCard
     * @param size        the size of the BingoCard
     * @param addFreeTile wether to use a free tile (only works with odd sizes)
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printOne(String setName, int size, boolean addFreeTile) throws IOException {
        printToPDF(BingoCard.createFromSet(setName, size, addFreeTile), setName);
    }

    /**
     * Creates a PDF from a newly created BingoCard from the specified BingoSet,
     * using {@code printToPDF()}.
     * 
     * @param setName     the BingoSet to use for the BingoCard
     * @param size        the size of the BingoCard
     * @param addFreeTile wether to use a free tile (only works with odd sizes)
     * @param path        the path to save the PDF to
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printOne(String setName, int size, boolean addFreeTile, String path) throws IOException {
        printToPDF(BingoCard.createFromSet(setName, size, addFreeTile), setName, path);
    }

    /**
     * Creates six new BingoCards with size 3 from the specified BingoSet, and then
     * creates a new PDF from them.
     * Defaults to size 3, using a free tile if possible, and the standard output path.
     * 
     * @param setName the BingoSet to use for the BingoCards
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printSix(String setName) throws IOException {
        printSix(setName, 3);
    }

    /**
     * Creates six new BingoCards with the specified size form the specified
     * BingoSet, an then creates a new PDF from them.
     * Defaults to using a free tile if possible, and the standard output path.
     * 
     * @param setName the BingoSet to use for the BingoCards
     * @param size    the size for the BingoCards
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printSix(String setName, int size) throws IOException {
        printSix(setName, size, true);
    }

    /**
     * Creates six new BingoCards with the specified size form the specified
     * BingoSet, an then creates a new PDF from them.
     * Defaults to the standard output path.
     * 
     * @param setName     the BingoSet to use for the BingoCards
     * @param size        the size for the BingoCards
     * @param addFreeTile wether to add a free BingoTile (only works for odd sizes)
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printSix(String setName, int size, boolean addFreeTile) throws IOException {
        printSix(setName, size, addFreeTile, PATH);
    }

    /**
     * Creates six new BingoCards with the specified size form the specified
     * BingoSet, an then creates a new PDF from them.
     * 
     * @param setName     the BingoSet to use for the BingoCards
     * @param size        the size for the BingoCards
     * @param addFreeTile wether to add a free BingoTile (only works for odd sizes)
     * @param path        the path to save the PDF to
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printSix(String setName, int size, boolean addFreeTile, String path) throws IOException {
        BingoCard[] cards = new BingoCard[6];
        for (int i = 0; i < 6; i++) {
            cards[i] = BingoCard.createFromSet(setName, size, addFreeTile);
        }
        String[] formatted = new String[6];
        for (int i = 0; i < 6; i++) {
            formatted[i] = formatForPDF(cards[i].toConsolePrint());
        }
        int[] widths = new int[6];
        for (int i = 0; i < 6; i++) {
            widths[i] = formatted[i].split("\n")[0].length();
        }
        int[] heights = new int[6];
        for (int i = 0; i < 6; i++) {
            heights[i] = formatted[i].split("\n").length;
        }
        int maxWidth = findmax(widths);
        int maxHeight = findmax(heights);
        int widthLimit = (int) ((PAGE_SIZE_X / (maxWidth * 1)) * 0.8f);
        int heightLimit = (int) ((PAGE_SIZE_Y / (maxHeight * 2)) * 0.65f);
        int fontSize = Math.min(widthLimit, heightLimit);

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDFont font = PDType1Font.COURIER;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, fontSize);

        for (int i = 0; i < 6; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(X_VALUES[i % 2], Y_VALUES[i % 3]);
            for (String s : formatForPDF(toConsolePrint(cards[i])).split("\n")) {
                contentStream.showText(s);
                contentStream.newLineAtOffset(0, -fontSize);
            }
            contentStream.endText();
        }

        contentStream.close();

        document.save(path + "/" + setName + "-BingoCards-Size" + size + "-" + System.currentTimeMillis() + ".pdf");
        document.close();
    }

    private static int findmax(int[] vals) {
        if (vals.length == 0)
            return -1;
        int max = vals[0];
        for (int i : vals) {
            if (i > max)
                max = i;
        }
        return max;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            String setName = null;
            int size = 0;
            Boolean addFreeTile = null;
            Boolean provideOwnPath = null;
            String path = null;
            Boolean one = null;
            int i = 0;
            int chosen = 0;
            try (Scanner scan = new Scanner(System.in)) {
                System.out.println("Please choose a set to print from.");
                while (setName == null) {
                    System.out.println("Available sets: ");
                    for (String n : BingoSets.getAllNames()) {
                        System.out.println(++i + ": " + n);
                    }
                    chosen = scan.nextInt() - 1;
                    if (chosen >= 0 && chosen < BingoSets.getAllNames().size()) {
                        setName = BingoSets.getAllNames().get(chosen);
                    } else {
                        i = 0;
                        System.out.println("Please choose one of the available sets.");
                    }
                }
                chosen = 0;
                int max = (int) Math.sqrt(BingoSets.getSet(setName).size());
                if (max < 3) {
                    System.out.println("That set is too small to create BingoCards.");
                    return;
                }
                if (max == 3) {
                    size = 3;
                }
                while (size == 0) {
                    System.out.println("Please choose a size. Minimum size is 3, maximum size is " + max + ".");
                    chosen = scan.nextInt();
                    if (chosen > 2 && chosen <= max) {
                        size = chosen;
                    }
                }
                if (size % 2 == 0) {
                    addFreeTile = true;
                }
                scan.nextLine();
                while (addFreeTile == null) {
                    System.out.println("Do you want to use a free tile? (Y/N)");
                    String choice = scan.nextLine();
                    if (choice.matches("(?i)y"))
                        addFreeTile = true;
                    if (choice.matches("(?i)n"))
                        addFreeTile = false;
                }
                while (provideOwnPath == null) {
                    System.out.println("Do you want to provide your own path? (Y/N)");
                    String choice = scan.nextLine();
                    if (choice.matches("(?i)y"))
                        provideOwnPath = true;
                    if (choice.matches("(?i)n"))
                        provideOwnPath = false;
                }
                if (provideOwnPath) {
                    System.out.println("Please enter the path:");
                    path = scan.nextLine();
                }
                while (one == null) {
                    System.out.println("Do you want to print one card or six?");
                    String choice = scan.nextLine();
                    if (choice.matches("(?i)one"))
                        one = true;
                    if (choice.matches("(?i)six"))
                        one = false;
                    if (one == null) {
                        System.out.println("Please enter either 'one' or 'six'.");
                    }
                }
                if (one) {
                    if (path == null) {
                        printOne(setName, size, addFreeTile);
                        System.out.println("Card printed at " + PATH);
                    } else {
                        printOne(setName, size, addFreeTile, path);
                        System.out.println("Card printed at " + path);
                    }
                } else {
                    if (path == null) {
                        printSix(setName, size, addFreeTile);
                        System.out.println("Cards printed at " + PATH);
                    } else {
                        printSix(setName, size, addFreeTile, path);
                        System.out.println("Cards printed at " + path);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String setName = args[1];
                int size = Integer.parseInt(args[2]);
                String path = args[3];
                boolean addFreeTile = true;
                if (args.length > 4 && args[4].equals("--no-free-tile"))
                    addFreeTile = false;
                if (args[0].matches("(?i)one")) {
                    printOne(setName, size, addFreeTile, path);
                    System.out.println("Created a PDF at" + path);
                } else if (args[0].matches("(?i)six")) {
                    printSix(setName, size, addFreeTile, path);
                    System.out.println("Created a PDF at " + path);
                } else {
                    System.out.println("Usage: 'one'|'six', path, size, [--no-free-tile]");
                }

            } catch (Exception e) {
                System.out.println("Usage: 'one'|'six', setName, size, path, [--no-free-tile]");
            }

        }
    }

}
