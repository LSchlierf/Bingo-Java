package BingoParts;

import java.io.IOException;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * @author Lucas Schlierf
 */
public class Printing {

    private static final int[] X_VALUES = {20, 320};
    private static final int[] Y_VALUES = {770, 510, 250};

    /**
     * This class only has static Methods,
     * so it shouldn't be instanciated.
     */
    private Printing(){}

    /**
     * Formats the BingoCard for console printing, using {@code formatForConsoleOutput()}. 
     * Leaves out whether the BingoTiles are marked off.
     * @param card the BingoCard to format
     * @return a formatted String, ready for console printing
     */
    public static String toConsolePrint(BingoCard card){
        return formatForConsoleOutput(Arrays.stream(card.getTiles())
                                            .map(a -> Arrays.stream(a)
                                                            .map(t -> t.getText().replaceAll(" ", "\n"))
                                                            .toArray(String[]::new))
                                            .toArray(String[][]::new));
    }

    /**
     * Formats the BingoCard for console output, using {@code formatForConsoleOutput()}, 
     * also displaying which BingoTiles have been marked off
     * @param card the BingoCard to format
     * @return a formatted String, ready for console output
     */
    public static String toConsoleOutput(BingoCard card){
        return formatForConsoleOutput(Arrays.stream(card.getTiles())
                                            .map(a -> Arrays.stream(a)
                                                            .map(t -> t.toString().replaceAll(" ", "\n"))
                                                            .toArray(String[]::new))
                                            .toArray(String[][]::new));
    }

    /**
     * Formats the given String array for console output. Only looks good with monospaced font.
     * @param texts the Strings to print, should be a square Array.
     * @return a formatted String, ready for console printing.
     */
    protected static String formatForConsoleOutput(String[][] texts){
        StringBuilder lineSeparator = new StringBuilder();
        String[] allTexts = Arrays.stream(texts).reduce(new String[0], (a, b) -> {
            String[] newArray = new String[a.length + b.length];
            int i = 0;
            for(String s : a){
                newArray[i] = s;
                i++;
            }
            for(String s : b){
                newArray[i] = s;
                i++;
            }
            return newArray;
        });
        int maxLines = 1 + Arrays.stream(allTexts).mapToInt(
                                        s -> s.length() - s.replaceAll("\n", "").length()
                                        ).max().getAsInt();
        int maxRows = Arrays.stream(allTexts).mapToInt(
                                        s -> Arrays.stream(s.split("\n")).mapToInt(w -> w.length()).max().getAsInt()
                                        ).max().getAsInt();
        lineSeparator.append("├");
        for(int i = 0; i < texts.length; i++){
            for(int j = 0; j < maxRows; j++){
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
        for(int i = 0; i < texts.length; i++){
            for(int j = 0; j < maxLines; j++){
                StringBuilder currentLine = new StringBuilder();
                currentLine.append("│");
                for(int k = 0; k < texts.length; k++){
                    String[] currentText = texts[i][k].split("\n");
                    if(j < currentText.length){
                        currentLine.append(currentText[j]);
                    }
                    else{
                        currentLine.append(" ");
                    }
                    while(currentLine.length() % (maxRows + 1) != 0){
                        currentLine.append(" ");
                    }
                    currentLine.append("│");
                }
                currentLine.append("\n");
                result.append(currentLine);
            }
            if(i + 1 < texts.length) result.append(middleLine + "\n");
            else result.append(bottomLine);
        }
        return result.toString();
    }

    private static String formatForPDF(String input){
        return input.replaceAll("[┼├┤┬┴┌┐└┘]", "+").replaceAll("│", "|").replaceAll("─", "-");
    }

    /**
     * Creates a PDF from the provided BingoCard.
     * @param card the card you want to print
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printToPDF(BingoCard card) throws IOException{
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDFont font = PDType1Font.COURIER;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(20, 770);
        for(String s : formatForPDF(toConsolePrint(card)).split("\n")){
            contentStream.showText(s);
            contentStream.newLineAtOffset(0, -14);
        }
        contentStream.endText();
        contentStream.close();

        document.save(Printing.class.getClassLoader().getResource("BingoParts/PrintOutput").getPath()
                                                    + "/BingoCard-" + System.currentTimeMillis() + ".pdf");
        document.close();
    }

    /**
     * Creates a PDF from a newly created BingoCard from the specified BingoSet, using {@code printToPDF()}.
     * @param setName the BingoSet to use for the BingoCard
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printOne(String setName) throws IOException{
        printToPDF(BingoCard.createFromSet(setName, 3));
    }

    /**
     * Creates six new BingoCards from the specified BingoSet, and then creates a new PDF from them.
     * @param setName the BingoSet to use for the BingoCards
     * @throws IOException if an IO exception occurs while creating the PDF
     */
    public static void printSix(String setName) throws IOException{
        BingoCard[] cards = new BingoCard[6];
        for(int i = 0; i < 6; i++){
            cards[i] = BingoCard.createFromSet(setName, 3);
        }

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDFont font = PDType1Font.COURIER;

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(font, 12);
        
        for(int i = 0; i < 6; i++){
            contentStream.beginText();
            contentStream.newLineAtOffset(X_VALUES[i%2], Y_VALUES[i%3]);
            for(String s : formatForPDF(toConsolePrint(cards[i])).split("\n")){
                contentStream.showText(s);
                contentStream.newLineAtOffset(0, -14);
            }
            contentStream.endText();
        }

        contentStream.close();

        document.save(Printing.class.getClassLoader().getResource("BingoParts/PrintOutput").getPath()
                                                    + "/" + setName + "-BingoCards-" + System.currentTimeMillis() + ".pdf");
        document.close();
    }
    
}
