package BingoParts;

import java.util.Arrays;

/**
 * @author Lucas Schlierf
 */
public class Printing { //TODO: add printing support to pdf.

    /**
     * This class only has static Methods,
     * so it shouldn't be instanciated.
     */
    private Printing(){}

    /**
     * Formats the BingoCard for console printing, using {@code formatForConsoleOutput()}. Leaves out whether the BingoTiles are marked off.
     * @param card the BingoCard to format
     * @return a formatted String, readyy for console printing.
     */
    public static String toConsolePrint(BingoCard card){
        return formatForConsoleOutput(Arrays.stream(card.getTiles()).map(a -> Arrays.stream(a).map(t -> t.getText().replaceAll(" ", "\n")).toArray(String[]::new)).toArray(String[][]::new));
    }

    /**
     * Formats the BingoCard for console output, using {@code formatForConsoleOutput()}, also displaying which BingoTiles have been marked off
     * @param card
     * @return
     */
    public static String toConsoleOutput(BingoCard card){
        return formatForConsoleOutput(Arrays.stream(card.getTiles()).map(a -> Arrays.stream(a).map(t -> t.toString().replaceAll(" ", "\n")).toArray(String[]::new)).toArray(String[][]::new));
    }

    /**
     * Formats the given String array for console output. Only looks good with monospaced font.
     * @param texts the Strings to print, should be a square Array.
     * @return a formatted String, ready for console printing.
     */
    public static String formatForConsoleOutput(String[][] texts){
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
        int maxLines = 1 + Arrays.stream(allTexts).mapToInt(s -> s.length() - s.replaceAll("\n", "").length()).max().getAsInt();
        int maxRows = Arrays.stream(allTexts).mapToInt(s -> Arrays.stream(s.split("\n")).mapToInt(w -> w.length()).max().getAsInt()).max().getAsInt();
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
    
}
