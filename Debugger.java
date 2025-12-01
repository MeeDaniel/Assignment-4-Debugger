class Debugger {
    // Foreground codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Background codes
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // Style Code
    public static final String ANSI_BOLD   = "\u001B[1m";

    /**
     * Prints board with its entities. Adjusts grid and colors insects
     * @param boardData
     * @param boardSize
     */
    public static void logBoard(Map<String, BoardEntity> boardData, int boardSize) {
        // Process of grid adjustment. letterSize is the length of the every number and grid entry string.
        // Should be at least 2, then at least length of the greatest number in the grid
        // (including indexes and food points)
        int maxLetterSize = Math.max(String.valueOf(boardSize).length(), 2);

        for (String key : boardData.keySet()) {
            BoardEntity entity = boardData.get(key);

            if (entity instanceof FoodPoint) {
                FoodPoint food = (FoodPoint) entity;
                int size = String.valueOf(food.getValue()).length();

                if (size > maxLetterSize) {
                    maxLetterSize = size;
                }
            }
        }

        int letterSize = maxLetterSize + 1;

        // Header line: "   |1  |2  |3  |...|n  "
        System.out.print(" ".repeat(letterSize) + "|");

        for (int i = 1; i <= boardSize; i++) {
            System.out.print(left(String.valueOf(i), ' ', letterSize));

            if (i != boardSize) {
                System.out.print("|");
            }
        }
        System.out.println();

        // Body
        for (int y = 1; y <= boardSize; y++) {
            // Separating line: "---+---+---+...+---"
            System.out.println(("-".repeat(letterSize) + "+").repeat(boardSize) + "-".repeat(letterSize));

            // Row of the grid: "row|   | G |10 |...|   "
            System.out.print(left(String.valueOf(y), ' ', letterSize) + "|");

            for (int x = 1; x <= boardSize; x++) {
                String key = x + " " + y;

                if (!(boardData.containsKey(key))) {
                    // If there is nothing on this grid cell, then it is empty in the terminal
                    System.out.print(" ".repeat(letterSize));
                } else {
                    BoardEntity entity = boardData.get(key);

                    if (entity instanceof FoodPoint food) {
                        // If there is a food point on this grid cell, we just print it in the center of the cell
                        System.out.print(center(String.valueOf(food.getValue()), ' ', letterSize));
                    } else if (entity instanceof Insect insect) {
                        // If there is an insect on this grid cell, we color background in color of the insect and
                        // prints a letter, corresponding to the class name of the insect
                        InsectColor iColor = insect.getColor();
                        String sColor = switch (iColor) {
                            case InsectColor.RED -> ANSI_RED_BACKGROUND;
                            case InsectColor.GREEN -> ANSI_GREEN_BACKGROUND;
                            case InsectColor.BLUE -> ANSI_BLUE_BACKGROUND;
                            default -> ANSI_YELLOW_BACKGROUND;
                        };

                        System.out.print(ANSI_BOLD + sColor + ANSI_BLACK);

                        switch (insect) {
                            case Ant ant -> System.out.print(center("A", ' ', letterSize));
                            case Butterfly butterfly -> System.out.print(center("B", ' ', letterSize));
                            case Spider spider -> System.out.print(center("S", ' ', letterSize));
                            case Grasshopper grasshopper -> System.out.print(center("G", ' ', letterSize));
                            default -> System.out.print(center("?", ' ', letterSize));
                        }

                        System.out.print(ANSI_RESET);
                    }
                }

                if (x != boardSize) {
                    System.out.print("|");
                }
            }

            System.out.println();
        }
    }

    /**
     * Places a <i>string</i> in the middle of <i>c</i> chars. Length of resulting string is <i>width</i>
     * @param string
     * @param c
     * @param width
     * @return
     */
    private static String center(String string, char c, int width) {
        if (string.length() >= width) {
            return string;
        }

        int left = (width - string.length()) / 2;
        int right = (width - string.length()) - left;

        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(c).repeat(left));
        sb.append(string);
        sb.append(String.valueOf(c).repeat(right));

        return sb.toString();
    }

    /**
     * Places a <i>string</i> to the left bound of <i>c</i> chars. Length of resulting string is <i>width</i>
     * @param string
     * @param c
     * @param width
     * @return
     */
    private static String left(String string, char c, int width) {
        if (string.length() >= width) {
            return string;
        }

        int toFill = width - string.length();

        StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(String.valueOf(c).repeat(toFill));

        return sb.toString();
    }
}
