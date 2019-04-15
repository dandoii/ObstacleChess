import java.awt.*;
import java.util.List;
import static java.lang.System.out;
//class that draws the GUI and updates it with moves made
public class DrawGame {
    private static final String FILE = "abcdefgh";
    private static int clickDrawer = 0;
    private static int mover = 0;
    //counter for walls
    private static int cntWalls1 = 0, cntWalls2 = 0;
    public static String FLAG = null;
    private static boolean isPromotion = false;
    private static Tile promotionTile = null;
    private static ChessPiece.Colour promotionColour;
    private static final int X_DIMENSION = 720, Y_DIMENSION = 720;
    private static final double NUM_TILES_ROW = 8;
    private static final Color BACKGROUND = Color.GRAY;
    private static final Color UI_COLOUR = Color.DARK_GRAY;

    //method showing the board
    public static void show(ChessBoard board, Tile[][] grid,  int count) {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        drawCanvas();
        drawGameLogDock(board, count);
        drawBoard(board, count);
        drawPieces(grid);

        drawButtons(board);

        drawShowTurn(board);
        createInfoWindow();

        createWallLog(board);
        drawisPromotionButtons();
        StdDraw.show();
    }
    //creates the canvas for the drawing
    public static void drawCanvas() {

        StdDraw.setScale();
        StdDraw.setCanvasSize(X_DIMENSION, Y_DIMENSION);
        StdDraw.setPenColor(BACKGROUND);
        StdDraw.filledRectangle(X_DIMENSION/2, Y_DIMENSION/2,
                X_DIMENSION/2, Y_DIMENSION/2);
    }

    //draws the actual board
    public static void drawBoard(ChessBoard board, int count) {
        StdDraw.setScale();
        // Background
        StdDraw.picture(0.5, 0.5, "black_Square.png",
                0.75, 0.75);
        StdDraw.picture(0.5, 0.5, "white_Square.png",
                0.71, 0.71);
        StdDraw.picture(0.5, 0.5, "black_Square.png",
                0.65, 0.65);
        // Tiles
        StdDraw.setScale(-0.28, 1.28);
        StdDraw.setPenColor(StdDraw.RED);
        for (double i = 0; i < NUM_TILES_ROW; i++) {
            for (double j = 0; j < NUM_TILES_ROW; j++) {
                if ((i+1) % 2 == 0 && (j+1) % 2 != 0
                        || (i+1) % 2 != 0 && (j+1) % 2 == 0) {
                    StdDraw.picture((1.0/(NUM_TILES_ROW))*(j+1)-1.0/NUM_TILES_ROW/2,
                            ((1.0/(NUM_TILES_ROW)))*(i+1)-1.0/NUM_TILES_ROW/2,
                            "white_Square.png",
                            1.0/NUM_TILES_ROW,
                            1.0/NUM_TILES_ROW);
                }
                if (board.isInitialState()) {
                    if (count < 1) {
                        if (i >= 2 && i <= 5) {
                            StdDraw.filledCircle((1.0 / (NUM_TILES_ROW)) * (j + 1)
                                            - 1.0 / NUM_TILES_ROW / 2,
                                    ((1.0 / (NUM_TILES_ROW))) * (i + 1)
                                            - 1.0 / NUM_TILES_ROW / 2,
                                    0.03);
                        }
                    } else if (count < 3) {
                        if (i >= 3 && i <= 4) {
                            StdDraw.filledCircle((1.0 / (NUM_TILES_ROW)) * (j + 1)
                                            - 1.0 / NUM_TILES_ROW / 2,
                                    ((1.0 / (NUM_TILES_ROW))) * (i + 1)
                                            - 1.0 / NUM_TILES_ROW / 2,
                                    0.03);
                        }
                    }
                }
                if (board.getcurrentBoardGrid()[(int) i][(int) j].hasisTrapOpen()) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledCircle((1.0 / (NUM_TILES_ROW)) * (j + 1)
                                    - 1.0 / NUM_TILES_ROW / 2,
                            ((1.0 / (NUM_TILES_ROW))) * (i + 1)
                                    - 1.0 / NUM_TILES_ROW / 2,
                            0.03);
                    StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                }
            }
        }
        StdDraw.setScale(-0.235, 1.235);
        StdDraw.setPenColor(Color.black);
        // Indices
        for (double i = 0; i < NUM_TILES_ROW; i++) {
            StdDraw.text((1.0/(NUM_TILES_ROW))*(i+1)-1.0/NUM_TILES_ROW/2,
                    0.000002, String.valueOf(FILE.charAt((int) i)));
        }
        for (double i = 0; i < NUM_TILES_ROW; i++) {
            StdDraw.text(0.002,
                    (1.0/(NUM_TILES_ROW))*(i+1)-1.0/NUM_TILES_ROW/2,
                    String.valueOf((int) i+1));
        }
    }

    private static void drawButtons(ChessBoard board){
        createUndoButton();
        drawRedoMove();
        drawResignButton();
        askDraw(board);
    }
    //draws all pieces in
    public static void drawPieces(Tile[][] grid) {
        StdDraw.setScale(-0.28, 1.28);
        for (double i = 0; i < NUM_TILES_ROW; i++) {
            for (double j = 0; j < NUM_TILES_ROW; j++) {
                if (grid[(int) i][(int) j].hasPiece()) {
                    StdDraw.picture((1.0/(NUM_TILES_ROW))*(j+1)-1.0/NUM_TILES_ROW/2,
                            ((1.0/(NUM_TILES_ROW)))*(i+1)-1.0/NUM_TILES_ROW/2,
                            grid[(int) i][(int) j].getPiece().getvisualRep(),
                            1.0/NUM_TILES_ROW,
                            1.0/NUM_TILES_ROW);
                }
                if (grid[(int) i][(int) j].hasSouthWall()) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(
                            (1.0/(NUM_TILES_ROW))*(j+1)-1.0/NUM_TILES_ROW/2,
                            ((1.0/(NUM_TILES_ROW)))*(i+1)-1.0/NUM_TILES_ROW,
                            1.0/NUM_TILES_ROW/2, 0.006);
                }
                if (grid[(int) i][(int) j].hasWestWall()) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledRectangle(
                            (1.0/(NUM_TILES_ROW))*(j+1)-1.0/NUM_TILES_ROW,
                            ((1.0/(NUM_TILES_ROW)))*(i+1)-1.0/NUM_TILES_ROW/2,
                            0.006, 1.0/NUM_TILES_ROW/2);
                }
            }
        }
    }

    public static int roundClick(double d) {
        int i = 0;
        if (0.177 <= d && d <= 0.259) { i = 0; }
        else if (0.259 <= d && d <= 0.34) { i = 1; }
        else if (0.34 <= d && d <= 0.419) { i = 2; }
        else if (0.419 <= d && d <= 0.5) { i = 3; }
        else if (0.5 <= d && d <= 0.5805) { i = 4; }
        else if (0.5805 <= d && d <= 0.661) { i = 5; }
        else if (0.661 <= d && d <= 0.7402) { i = 6; }
        else if (0.7402 <= d && d <= 0.8208) { i = 7; }
        return i;
    }
    //draws the button that moves back one move
    public static void createUndoButton() {
        StdDraw.setScale();
        StdDraw.setPenColor(UI_COLOUR);
        StdDraw.filledRectangle(0.5 - 0.11, 0.085,
                0.05, 0.02);
        StdDraw.picture(0.5 - 0.11, 0.085, "back.png");
    }

    public static void drawRedoMove() {
        StdDraw.setScale();
        StdDraw.setPenColor(UI_COLOUR);
        StdDraw.filledRectangle(0.5 + 0.11, 0.085,
                0.05, 0.02);
        StdDraw.picture(0.5 + 0.11, 0.085, "forward.png");
    }

    public static void drawShowTurn(ChessBoard board) {
        StdDraw.setScale();
        StdDraw.setPenColor(UI_COLOUR);
        StdDraw.filledCircle(0.85, 0.925, 0.021);

        if (board.getJustPlayed() == ChessPiece.Colour.WHITE) {
            StdDraw.setPenColor(StdDraw.BLACK);
        } else {
            StdDraw.setPenColor(StdDraw.WHITE);
        }
        StdDraw.filledCircle(0.85, 0.925, 0.015);
    }

    public static void drawResignButton() {
        StdDraw.setScale();
        StdDraw.setPenColor(UI_COLOUR);
        StdDraw.filledRectangle(0.2, 0.085,
                0.05, 0.02);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.2, 0.084, "Give Up");
    }

    public static void createInfoWindow() {
        StdDraw.setScale();
        StdDraw.setPenColor(UI_COLOUR);
        StdDraw.filledRectangle(0.5, 0.925, 0.25, 0.02);
        if (FLAG != null) {
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(0.5, 0.924, FLAG);
        }
    }

    public static void askDraw(ChessBoard board) {
        StdDraw.setScale();
        if (board.getHalfMoves() >= 50 || clickDrawer == 1) {
            StdDraw.setPenColor(StdDraw.BOOK_RED);
        } else {
            StdDraw.setPenColor(UI_COLOUR);
        }
        StdDraw.filledRectangle(0.8, 0.085,
                0.05, 0.02);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.8, 0.084, "Draw?");
    }

    public static void drawisPromotionButtons() {
        StdDraw.setScale();
        if (isPromotion) {
            StdDraw.setPenColor(StdDraw.GREEN);
        } else {
            StdDraw.setPenColor(UI_COLOUR);

        }
        StdDraw.filledSquare(0.925, 0.85, 0.015);
        StdDraw.filledSquare(0.925, 0.85-0.023*2, 0.015);
        StdDraw.filledSquare(0.925, 0.85-0.023*4, 0.015);
        StdDraw.filledSquare(0.925, 0.85-0.023*6, 0.015);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.925, 0.85, "Q");
        StdDraw.text(0.925, 0.85-0.023*2, "B");
        StdDraw.text(0.925, 0.85-0.023*4, "R");
        StdDraw.text(0.925, 0.85-0.023*6, "N");
    }

    public static void createWallLog(ChessBoard board) {
        StdDraw.setScale();
        StdDraw.setPenColor(UI_COLOUR);
        StdDraw.filledRectangle(0.925, 0.5, 0.03, 0.15);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.925, 0.58,
                String.valueOf(6 - board.whiteWallCounter - board.blackWallCounter));
        StdDraw.filledRectangle(0.925, 0.5,
                0.002, 0.025);
        StdDraw.filledRectangle(0.925, 0.4,
                0.015, 0.002);
    }

    public static void drawGameLogDock(ChessBoard board, int count) {
        StdDraw.setScale();
        StdDraw.setPenColor(UI_COLOUR);
        StdDraw.filledRectangle(0.07, 0.5,
                0.055, 0.375);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(0.07, 0.5,
                0.05, 0.37);
        StdDraw.setPenColor(StdDraw.BLACK);
        List<String> list = board.getlistOfMoves();
        double offset;
        for (double i = list.size()-1; i >= list.size()-27; i--) {
            try {
                if (count == i) StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
                offset = list.size()-27 < 0 ? 0 : list.size() - 27;
                StdDraw.text(0.07, 0.825 - Math.abs(i - offset) * 0.025,
                        (list.get((int) i).contains("M") || list.get((int) i)
                                .contains("D") ? "---" : list.get((int) i)));
                StdDraw.setPenColor(StdDraw.BLACK);
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    public static boolean moveMaker(ChessBoard board, int mode, int count) {
        boolean moveMade = false;
        String move = "";
        double sX, sY, eX = 0, eY = 0;
        int siX, siY, eiX, eiY;
        // Get coordinates
        while (true) {
            if (StdDraw.isMousePressed()) {
                sX = StdDraw.mouseX(); sY = StdDraw.mouseY();
                while (StdDraw.isMousePressed()) {
                    eX = StdDraw.mouseX(); eY = StdDraw.mouseY();
                }
                break;
            }
        }

        // Take action
        if (isPromotion) {
            if (sX >= 0.9089 && sX <= 0.9388 && sY >= 0.6985 && sY <= 0.7283) {
                promotionTile.setChessPiece(Knight.create(promotionColour));
            } else if (sX >= 0.9089 && sX <= 0.9388 && sY >= 0.7447 && sY <=
                    0.77313) {
                promotionTile.setChessPiece(Rook.create(promotionColour));
            } else if (sX >= 0.9089 && sX <= 0.9388 && sY >= 0.79104 && sY <=
                    0.8208) {
                promotionTile.setChessPiece(Bishop.create(promotionColour));
            } else if (sX >= 0.9089 && sX <= 0.9388  && sY >= 0.8373 && sY <=
                    0.8671) {
                promotionTile.setChessPiece(Queen.create(promotionColour));
            }
            isPromotion = false;
            board.setTileStatus();
            show(board, board.getcurrentBoardGrid(), count);
        } else if (sX >= 0.149 && sX <= 0.249 && sY >= 0.0671 && sY <= 0.1059) {
            FLAG = "INFO: " + (board.getJustPlayed() == ChessPiece
                    .Colour.WHITE ? "black" : "white") + " resigned";
            board.sethasResigned();
            show(board, board.getcurrentBoardGrid(), count);
        } else if (sX >= 0.749 && sX <= 0.849 && sY >= 0.0656 && sY <= 0.1059) {
            clickDrawer++;
            if (board.getHalfMoves() >= 50 || clickDrawer == 2) {
                board.setisDrawClaimed(true);
                FLAG = "INFO: draw";
            }
            show(board, board.getcurrentBoardGrid(), count);
        } else if (count < 4 && board.isInitialState()) {
            clickDrawer = 0;
            if (sX >= 0.177 && sX <= 0.8208 && sY >= 0.177 && sY <= 0.8208) {
                if (eX >= 0.177 && eX <= 0.8208 && eY >= 0.177 && eY <= 0.8208) {
                    siX = roundClick(sX); eiX = roundClick(eX);
                    siY = roundClick(sY); eiY = roundClick(eY);
                    if (siX == eiX && siY == eiY) {
                        if (count < 2) {
                            move = move.concat("D").
                                    concat(String.valueOf(FILE.charAt(eiX)))
                                    .concat(String.valueOf(eiY + 1));
                            moveMade = board.moveMaker((board.getJustPlayed() ==
                                    ChessPiece.Colour.WHITE ?
                                    ChessPiece.Colour.BLACK :
                                    ChessPiece.Colour.WHITE), move, mode);
                        } else {
                            move = move.concat("M").
                                    concat(String.valueOf(FILE.charAt(eiX)))
                                    .concat(String.valueOf(eiY + 1));
                            moveMade = board.moveMaker((board.getJustPlayed() ==
                                    ChessPiece.Colour.WHITE ?
                                    ChessPiece.Colour.BLACK :
                                    ChessPiece.Colour.WHITE), move, mode);
                        }
                        if (moveMade) FLAG = null;
                        show(board, board.getcurrentBoardGrid(), count);
                    }
                }
            }
        } else if (sX >= 0.177 && sX <= 0.8208 && sY >= 0.177 && sY <= 0.8208) {
            if (eX >= 0.177 && eX <= 0.8208 && eY >= 0.177 && eY <= 0.8208) {
                siX = roundClick(sX); eiX = roundClick(eX);
                siY = roundClick(sY); eiY = roundClick(eY);
                if (mover != 0) {
                    board.setState(board.getlistOfBoardStates().get(board
                            .getlistOfBoardStates().size() - 1 - mover));
                    board.setcountMoves(count - mover);
                    ObstacleCL.setCount(count - mover);
                    int counter = 0;
                    String temporary;
                    for (int i = 0; i < mover; i++) {
                        board.getlistOfBoardStates().remove(board.getlistOfBoardStates()
                                .size() - 1);
                        if (counter++ % 2 == 0) {
                            if ((temporary = board.getlistOfMoves().get(board
                                    .getlistOfBoardStates()
                                    .size() - 1)).contains("|") || temporary
                                    .contains("_")) {
                                cntWalls1++;
                            }
                        } else {
                            if ((temporary = board.getlistOfMoves().get(board
                                    .getlistOfBoardStates()
                                    .size() - 1)).contains("|") || temporary
                                    .contains("_")) {
                                cntWalls2++;
                            }
                        }
                        board.getlistOfMoves().remove(board.getlistOfBoardStates()
                                .size() - 1);
                    }
                    if (board.getJustPlayed() == ChessPiece.Colour.WHITE) {
                        board.setwhiteWallCounter(board.getwhiteWallCounter() -
                                cntWalls1);
                        board.setblackWallCounter(board.getblackWallCounter() -
                                cntWalls2);
                    } else {
                        board.setwhiteWallCounter(board.getwhiteWallCounter() -
                                cntWalls2);
                        board.setblackWallCounter(board.getblackWallCounter() -
                                cntWalls1);
                    }
                    cntWalls2 = cntWalls1 = 0;
                    mover = 0;
                }
                if (siX == eiX && siY == eiY) {
                    if (board.getcurrentBoardGrid()[siY][siX].hasPiece()) {
                        drawTutorial(board, siX, siY, count);
                    }
                } else {
                    clickDrawer = 0;
                    move = move.concat(String.valueOf(FILE.charAt(siX)))
                            .concat(String.valueOf(siY + 1))
                            .concat("-")
                            .concat(String.valueOf(FILE.charAt(eiX)))
                            .concat(String.valueOf(eiY + 1));
                    moveMade = board.moveMaker((board.getJustPlayed() ==
                            ChessPiece.Colour.WHITE ?
                            ChessPiece.Colour.BLACK :
                            ChessPiece.Colour.WHITE), move, mode);
                    if (moveMade) {
                        FLAG = null;
                        Tile temporary;
                        if (move.substring(3).contains("8") && board
                                .getJustPlayed() == ChessPiece.Colour.WHITE) {
                            if ((temporary = board.getTile(move.substring(3)))
                                    .hasPiece()
                                    && temporary.getPiece() instanceof Pawn) {
                                isPromotion = true;
                                promotionTile = temporary;
                                promotionColour = ChessPiece.Colour.WHITE;
                            }
                        } else if (move.substring(3).contains("1") && board
                                .getJustPlayed() == ChessPiece.Colour.BLACK) {
                            if ((temporary = board.getTile(move.substring(3)))
                                    .hasPiece()
                                    && temporary.getPiece() instanceof Pawn) {
                                isPromotion = true;
                                promotionTile = temporary;
                                promotionColour = ChessPiece.Colour.BLACK;
                            }
                        }
                    }
                    show(board, board.getcurrentBoardGrid(), count);
                }
            }
        } else if (sX >= 0.895 && sX <= 0.955 && sY >= 0.385 && sY <= 0.422) {
            clickDrawer = 0;
            if (eX >= 0.177 && eX <= 0.8208 && eY >= 0.177 && eY <= 0.8208) {
                eiX = roundClick(eX); eiY = roundClick(eY);
                move = move.concat("_")
                        .concat(String.valueOf(FILE.charAt(eiX)))
                        .concat(String.valueOf(eiY+1));
                moveMade = board.moveMaker((board.getJustPlayed() ==
                        ChessPiece.Colour.WHITE ?
                        ChessPiece.Colour.BLACK :
                        ChessPiece.Colour.WHITE), move, mode);
                if (moveMade) FLAG = null;
                show(board, board.getcurrentBoardGrid(), count);
            }
        } else if (sX >= 0.895 && sX <= 0.953 && sY >= 0.4611 && sY <= 0.547) {
            clickDrawer = 0;
            if (eX >= 0.177 && eX <= 0.8208 && eY >= 0.177 && eY <= 0.8208) {
                eiX = roundClick(eX); eiY = roundClick(eY);
                move = move.concat("|")
                        .concat(String.valueOf(FILE.charAt(eiX)))
                        .concat(String.valueOf(eiY+1));
                moveMade = board.moveMaker((board.getJustPlayed() ==
                        ChessPiece.Colour.WHITE ?
                        ChessPiece.Colour.BLACK :
                        ChessPiece.Colour.WHITE), move, mode);
                if (moveMade) FLAG = null;
                show(board,board.getcurrentBoardGrid(), count);
            }
        } else if (sX >= 0.34 && sX <= 0.4388 && sY >= 0.0656 && sY <= 0.1044) {
            if (mover < board.getlistOfBoardStates().size()-5) {
                mover++;
                if (board.getJustPlayed() == ChessPiece.Colour.WHITE) {
                    board.setJustPlayed(ChessPiece.Colour.BLACK);
                } else {
                    board.setJustPlayed(ChessPiece.Colour.WHITE);
                }
                show(board, board.getlistOfBoardStates()
                        .get(board.getlistOfBoardStates().size() - 1 - mover), count);
            }
        } else if (sX >= 0.449 && sX <= 0.549 && sY >= 0.0567 && sY <= 0.1164) {
            mover = 0;
            show(board, board.getcurrentBoardGrid(), count);
        } else if (sX >= 0.559 && sX <= 0.658 && sY >= 0.0671 && sY <= 0.1059) {
            if (mover != 0) {
                mover--;
                if (board.getJustPlayed() == ChessPiece.Colour.WHITE) {
                    board.setJustPlayed(ChessPiece.Colour.BLACK);
                } else {
                    board.setJustPlayed(ChessPiece.Colour.WHITE);
                }
                show(board, board.getlistOfBoardStates()
                        .get(board.getlistOfBoardStates().size() - 1 - mover), count);
            }
        } else {
            mover = 0;
        }
        return moveMade;
    }

    private static void drawTutorial(ChessBoard board, int x, int y, int count) {
        double i, j;
        StdDraw.clear();
        drawCanvas();
        drawGameLogDock(board, count);
        drawBoard(board, count);

        Tile temporary;
        List<String> valPos =
                (temporary = board.getcurrentBoardGrid()[y][x]).getPiece().getvalidPositionition(board, temporary);
        StdDraw.setScale(-0.28, 1.28);
        StdDraw.setPenColor(Color.LIGHT_GRAY);
        for (String p : valPos) {
            temporary = board.getTile(p);
            i = temporary.gettileNoVal() + 1;
            j = temporary.getFileValue() + 1;

            StdDraw.filledCircle((1.0/(NUM_TILES_ROW))*(j)-1.0/NUM_TILES_ROW/2,
                    ((1.0/(NUM_TILES_ROW)))*(i)-1.0/NUM_TILES_ROW/2, 0.03);
        }
        drawPieces(board.getcurrentBoardGrid());

        drawButtons(board);

        drawShowTurn(board);
        FLAG = null;
        createInfoWindow();

        createWallLog(board);
        StdDraw.show();
    }




}
