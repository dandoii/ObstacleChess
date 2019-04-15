import static java.lang.System.err;
import static java.lang.System.out;

public class ObstacleCL {
    //ints keeping track of the various modes
    private static int MODE;
    private static final int TER_MODE = 0;//terminal / command line mode
    private static final int GUI_MODE = 1;//graphical mode

    private static String gameFile;
    private static boolean moveMade = false;
    private static In reader = null;
    private static int count = 0;

    public static void setCount(int i) { count = i; }

    public static void main(String[] args) {
        ChessBoard gameRunnerBoard = ChessBoard.create();
        ChessPiece.Colour col = ChessPiece.Colour.BLACK;

        String boardFileOut = "output.out";

        if (args.length == 0) {
            MODE = GUI_MODE;
            StdAudio.play("trailer_loop.wav");
            StartGame.show(gameRunnerBoard, count);
            do {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            } while (gameRunnerBoard.getJustPlayed() == null);
            col = gameRunnerBoard.getJustPlayed() == ChessPiece.Colour.WHITE
                    ? ChessPiece.Colour.BLACK : ChessPiece.Colour.WHITE;

        }  else if (args.length <= 3) {
            MODE = TER_MODE;
            try {
                boardFileOut = args[2];
            } catch (Exception e) {
            }
            col = gameRunnerBoard.readBoard(args[0]) == ChessPiece.Colour
                    .WHITE ? ChessPiece.Colour.BLACK : ChessPiece.Colour.WHITE;
            gameFile = args[1];
        }
        try {
            if (MODE == TER_MODE) {
                reader = new In(gameFile);
            }
            do {
                // alternates between the colour that has to play
                if (moveMade) {
                    count++;
                    if (col == ChessPiece.Colour.WHITE) {
                        col = ChessPiece.Colour.BLACK;
                    } else {
                        col = ChessPiece.Colour.WHITE;
                    }
                }
                // this reads and makes moves
                if (MODE == GUI_MODE) {
                    moveMade = DrawGame.moveMaker(gameRunnerBoard, MODE, count);
                    gameRunnerBoard.writeBoard(boardFileOut);
                    if (gameRunnerBoard.checkMate()) {
                        DrawGame.FLAG = "INFO: checkmate";
                        DrawGame.show(gameRunnerBoard, gameRunnerBoard
                                .getcurrentBoardGrid(), count);
                    }
                } else {
                    if (reader.hasNextLine()) {
                        String temporary = reader.readLine();
                        if (temporary.equals("..."))
                            gameRunnerBoard.setBlackFirst();
                        if (!temporary.contains("%") && !temporary.equals("...")) {
                            moveMade = gameRunnerBoard.moveMaker(col, temporary, MODE);
                            if (gameRunnerBoard.checkMate()) {
                                if (!reader.hasNextLine()) {
                                    gameRunnerBoard.writeBoard(boardFileOut);
                                    out.println("INFO: checkmate");
                                } else {
                                    temporary = reader.readLine();
                                    err.print("ERROR: illegal move at " + temporary);
                                    System.exit(28);
                                }
                            } else {
                                gameRunnerBoard.writeBoard(boardFileOut);
                            }
                        }
                    } else {
                        System.exit(1);
                    }
                }
            } while (!gameRunnerBoard.checkMate() && !gameRunnerBoard.hasDraw
                    () && !gameRunnerBoard.hasResigned());

        } catch (Exception e) {
            err.print("ERROR: That file couldn't be read");
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
    }
}
