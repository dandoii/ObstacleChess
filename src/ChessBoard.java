import static java.lang.System.out;
import static java.lang.System.err;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private static final int NUM_TILES_ROW = 8; //number of tiles in a row
    private static final String TILE_COL = "abcdefgh"; //collumn that the tile belongs to
    private int countMoves = 0;
    private boolean isDrawClaimed = false;
    private boolean hasResigned = false;
    private boolean blackQAvailable = true;
    private boolean blackKAvailable = true;
    private boolean whiteQAvailable = true;
    private boolean whiteKAvailable = true;
    //counters for the various special pieces
    private static int whiteTrapCounter = 0;
    private static int blackTrapCounter = 0;
    int whiteWallCounter = 0;
    int blackWallCounter = 0;
    private int whiteMineCounter = 0;
    private int blackMineCounter = 0;

    private static int halfMoves = 0;
    private String defSetupLine = "++++";
    private boolean blackFirst = false;

    public enum Colour { WHITE, BLACK } // Enum for tile colours
    private ChessPiece.Colour justPlayed = null;
    private List<String> listOfMoves; // a list that saves all moves made
    private List<Tile[][]> listOfBoardStates; // list that saves board states
    private static Tile[][] currentBoardGrid; // stores all the board tiles
    private static Tile[][] standardTiles; // an initial board that i use to test against

    public static ChessBoard create() { return new ChessBoard(); }

    private ChessBoard() {
        /*
         * Private constructor, taking The top and bottom ChessPiece Colours,
         * sets the pieces, creates the Tiles, and
         * initializes the listOfMoves
         */
        listOfMoves = new ArrayList<>(100);
        listOfBoardStates = new ArrayList<>(100);
        currentBoardGrid = new Tile[NUM_TILES_ROW][NUM_TILES_ROW];
        createTiles(currentBoardGrid);
        setPieces(currentBoardGrid);
        saveState();

        standardTiles = new Tile[NUM_TILES_ROW][NUM_TILES_ROW];
        createTiles(standardTiles);
        setPieces(standardTiles);
    }

    private void createTiles(Tile[][] grid) {
        for (int i = 0; i < NUM_TILES_ROW; i++) {
            for (int j = 0; j < NUM_TILES_ROW; j++) {

                String tCo = String.valueOf(TILE_COL.charAt(j)).
                        concat(String.valueOf(i+1)); // Name of Coordinate

                if (((j+1) % 2 == 0 && ((i+1) % 2) == 0)
                        || ((j+1) % 2 != 0 && ((i+1) % 2) != 0))
                    grid[i][j] = new Tile(tCo, Colour.BLACK);
                else grid[i][j] = new Tile(tCo, Colour.WHITE);
            }
        }
    }

    public void setTileStatus() {
        //out.println("setStatusMet");
        for (int i = 0; i < NUM_TILES_ROW; i++) {
            for (int j = 0; j < NUM_TILES_ROW; j++) {
                currentBoardGrid[i][j].clearStatus();
            }
        }
        for (int i = 0; i < NUM_TILES_ROW; i++) {
            for (int j = 0; j < NUM_TILES_ROW; j++) {
                Tile temporary = currentBoardGrid[i][j];
                try {
                    for (String tC : temporary.getPiece().
                            getValidAttackPosition(this, temporary)) {
                        if (temporary.getPiece().getPieceCol()
                                == ChessPiece.Colour.WHITE) {
                            getTile(tC).setisAttackedWhite(true);
                        } else {
                            getTile(tC).setisAttackedBlack(true);
                        }
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

    private void makeKaboom(Tile sT, Tile eT) {
        int i = eT.gettileNoVal();
        int j = eT.getFileValue();

        sT.removeChessPiece();
        eT.removeChessPiece();

        Tile temporary;
        if (!(temporary = currentBoardGrid[i + 1][j]).hasSouthWall())
            temporary.removeChessPiece();
        if (!(temporary = currentBoardGrid[i][j + 1]).hasWestWall())
            temporary.removeChessPiece();
        temporary = currentBoardGrid[i - 1][j];
        if (!currentBoardGrid[i][j].hasSouthWall())
            temporary.removeChessPiece();
        temporary = currentBoardGrid[i][j - 1];
        if (!currentBoardGrid[i][j].hasWestWall())
            temporary.removeChessPiece();
        temporary = currentBoardGrid[i + 1][j + 1];
        if (!(currentBoardGrid[i + 1][j].hasSouthWall()
                && currentBoardGrid[i][j + 1].hasWestWall()
                || temporary.hasWestWall()
                && temporary.hasSouthWall()
                || currentBoardGrid[i][j+1].hasWestWall()
                && currentBoardGrid[i+1][j+1].hasWestWall()
                || currentBoardGrid[i+1][j].hasSouthWall()
                && currentBoardGrid[i+1][j+1].hasSouthWall()))
            temporary.removeChessPiece();
        temporary = currentBoardGrid[i + 1][j - 1];
        if (!(currentBoardGrid[i + 1][j].hasSouthWall()
                && currentBoardGrid[i][j].hasWestWall()
                || currentBoardGrid[i + 1][j].hasWestWall()
                && temporary.hasSouthWall())
                || currentBoardGrid[i+1][j].hasWestWall()
                && currentBoardGrid[i][j].hasWestWall()
                || currentBoardGrid[i+1][j-1].hasSouthWall()
                && currentBoardGrid[i+1][j].hasSouthWall())
            temporary.removeChessPiece();
        temporary = currentBoardGrid[i - 1][j - 1];
        if (!(currentBoardGrid[i][j].hasSouthWall()
                && currentBoardGrid[i][j].hasWestWall()
                || currentBoardGrid[i][j - 1].hasSouthWall()
                && currentBoardGrid[i - 1][j].hasWestWall()
                || currentBoardGrid[i][j].hasWestWall()
                && currentBoardGrid[i-1][j].hasWestWall()
                || currentBoardGrid[i][j-1].hasSouthWall()
                && currentBoardGrid[i][j].hasSouthWall()))
            temporary.removeChessPiece();
        temporary = currentBoardGrid[i - 1][j + 1];
        if (!(currentBoardGrid[i][j].hasSouthWall()
                && currentBoardGrid[i][j + 1].hasWestWall()
                || currentBoardGrid[i][j + 1].hasSouthWall()
                && temporary.hasWestWall()
                || currentBoardGrid[i][j+1].hasWestWall()
                && currentBoardGrid[i-1][j+1].hasWestWall()
                || currentBoardGrid[i][j].hasSouthWall()
                && currentBoardGrid[i][j+1].hasSouthWall()))
            temporary.removeChessPiece();
    }

    public boolean moveMaker(ChessPiece.Colour col, String move, int mode) {
        boolean pawnMove = false;
        boolean capture = false;
        boolean moveMade = false;
        boolean castl = false;
        boolean obstacleMove = false;
        Tile startTile, endTile = null;
        Tile startTileR, endTileR = null;

        if (!blackFirst && countMoves == 0 && col == ChessPiece.Colour.BLACK) {
            if (mode == 0) {
                err.print("ERROR: illegal move " + move);
                System.exit(54);
            } else {
                DrawGame.FLAG = "ERROR: illegal " +
                        "move " + move;
                DrawGame.createInfoWindow();
            }
        }
        //out.println(justPlayed);
        //out.println(col);
        switch (move) {
            case "0-0-0":
                if (col == ChessPiece.Colour.WHITE) {
                    startTile = getTile("e1");
                    endTile = getTile("c1");
                    startTileR = getTile("a1");
                    endTileR = getTile("d1");
                    if (startTile.getPiece() instanceof King && !((King) startTile
                            .getPiece()).isMoved() && !startTile.isUnderAttackB()) {
                        if (whiteQAvailable) {
                            castl = moveMade = castleMove(startTile,
                                    endTile, startTileR, endTileR);
                            whiteQAvailable = false;
                        }
                    }
                } else {
                    startTile = getTile("e8");
                    endTile = getTile("c8");
                    makePieceMove(startTile, endTile, move);
                    startTileR = getTile("a8");
                    endTileR = getTile("d8");
                    if (startTile.getPiece() instanceof King && !((King) startTile
                            .getPiece()).isMoved() && !startTile.isUnderAttackW()) {
                        if (blackQAvailable) {
                            castl = moveMade = castleMove(startTile, endTile,
                                    startTileR, endTileR);
                            blackQAvailable = false;
                        }
                    }
                }
                break;
            case "0-0":
                if (col == ChessPiece.Colour.WHITE) {
                    startTile = getTile("e1");
                    endTile = getTile("g1");
                    startTileR = getTile("h1");
                    endTileR = getTile("f1");
                    if (startTile.getPiece() instanceof King && !((King) startTile
                            .getPiece()).isMoved() && !startTile.isUnderAttackB()) {
                        if (whiteKAvailable) {
                            castl = moveMade = castleMove(startTile, endTile,
                                    startTileR, endTileR);
                            whiteKAvailable = false;
                        }
                    }
                } else {
                    startTile = getTile("e8");
                    endTile = getTile("g8");
                    startTileR = getTile("h8");
                    endTileR = getTile("f8");
                    if (startTile.getPiece() instanceof King && !((King) startTile
                            .getPiece()).isMoved() && !startTile.isUnderAttackW()) {
                        if (blackKAvailable) {
                            castl = moveMade = castleMove(startTile, endTile,
                                    startTileR, endTileR);
                            blackKAvailable = false;
                        }
                    }
                }
                break;
            default:
                String inChar = String.valueOf(move.charAt(0));
                if (!TILE_COL.contains(inChar)) {
                    obstacleMove = true;
                    moveMade = setObstacle(col, move);
                } else {
                    try {
                        startTile = getTile(move.substring(0, 2));
                        if (startTile.getPiece() instanceof Pawn) {
                            pawnMove = true;
                        }
                        endTile = getTile(move.substring(3));
                        if (endTile.hasPiece() || endTile.hasisMine() ||
                                endTile.hasisTrap()) {
                            capture = true;
                        }
                        if (startTile.getPiece().getPieceCol() == col) {
                            moveMade = makePieceMove(startTile, endTile, move);
                        }
                    } catch (Exception e) { // Ignore
                    }
                }
        }
        if (moveMade) {
            setTileStatus();
            int kingCount = 0;
            for (int i = 0; i < NUM_TILES_ROW; i++) {
                for (int j = 0; j < NUM_TILES_ROW; j++) {
                    if (currentBoardGrid[i][j].hasPiece() && currentBoardGrid[i][j].getPiece()
                            instanceof King) {
                        kingCount++;
                    }
                }
            }
            if (kingCount != 2) {
                if (mode == 0) {
                    out.println("INFO: checkmate");
                    System.exit(2);
                } else {
                    DrawGame.FLAG = "INFO: checkmate";
                    DrawGame.createInfoWindow();
                }
            }
        }
        if (moveMade) {
            setTileStatus();
            ChessPiece temporary;
            for (int i = 0; i < NUM_TILES_ROW; i++) {
                for (int j = 0; j < NUM_TILES_ROW; j++) {
                    try {
                        if ((temporary = currentBoardGrid[i][j].getPiece()).isChecked()
                                && temporary.getPieceCol() != justPlayed
                                && justPlayed == col) {
                            moveMade = false;
                            setState(listOfBoardStates.get(listOfBoardStates.size()-1));
                            setTileStatus();
                            if (mode == 0) {
                                err.print("ERROR: illegal move " + move);
                                System.exit(27);
                            } else {
                                DrawGame.FLAG = "ERROR: illegal " +
                                        "move " + move;
                                DrawGame.
                                        createInfoWindow();
                            }
                        }
                    } catch (Exception e) { // Ignore
                    }
                }
            }
        }
        if (moveMade) {
            if (!obstacleMove) {
                if (castl) {
                    ((King) endTile.getPiece()).setMoved(true);
                    ((Rook) endTileR.getPiece()).setMoved(true);
                } else {
                    if (endTile.getPiece() instanceof King) {
                        ((King) endTile.getPiece()).setMoved(true);
                    } else if (endTile.getPiece() instanceof Rook) {
                        ((Rook) endTile.getPiece()).setMoved(true);
                    }
                }
            }
            for (int i = 0; i < NUM_TILES_ROW; i++) {
                for (int j = 0; j < NUM_TILES_ROW; j++) {
                    if (currentBoardGrid[i][j].hasPiece() && currentBoardGrid[i][j].getPiece
                            () instanceof Pawn && ((Pawn) currentBoardGrid[i][j]
                            .getPiece()).haspassingSquare()) {
                        ((Pawn) currentBoardGrid[i][j].getPiece()).setpassingSquare(null);
                    }
                }
            }
            if (pawnMove || capture) {
                halfMoves = 0;
            } else {
                halfMoves++;
            }
            justPlayed = col;
            countMoves++;
            listOfMoves.add(move);
            saveState();
            setTileStatus();
        } else {
            if (mode == 0) {
                err.print("ERROR: illegal move " + move);
                System.exit(28);
            } else {
                DrawGame.FLAG = "ERROR: illegal move " + move;
                DrawGame.createInfoWindow();
            }
        }
        return moveMade;
    }

    private boolean castleMove(Tile startTile, Tile endTile, Tile startTileR, Tile endTileR) {
        try {
            for (String s :
                    startTile.getPiece().getvalidPositionition(this, startTile)) {
                for (String t :
                        startTileR.getPiece().getvalidPositionition(this, startTileR)) {

                    if (s.equals(t)) {
                        endTile.setChessPiece(startTile.getPiece());
                        startTile.removeChessPiece();
                        endTileR.setChessPiece(startTileR.getPiece());
                        startTileR.removeChessPiece();
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean makePieceMove(Tile sT, Tile eT,String move) {
        boolean moveMade = false;
        for (String validTileCo : sT.getPiece().
                getvalidPositionition(this, sT)) {
            if (validTileCo.equals(eT.getCoord())) {
                if (eT.hasisTrap() || eT.hasisMine()) {
                    if (eT.hasisMine()) {
                        makeKaboom(sT, eT);
                        eT.setisMine(false);
                        moveMade = true;
                    }
                    if (eT.hasisTrap()) {
                        sT.removeChessPiece();
                        eT.setisTrapOpen(true);
                        moveMade = true;
                    }
                } else {
                    // isPromotiontion
                    if (move.length() >= 5 && move.contains("=")
                            && ((eT.gettileNoVal() == 7 && sT.getPiece().getStringRep().equals("P"))
                            || (eT.gettileNoVal() == 0 && sT.getPiece().getStringRep().equals("p")))) {
                        if (sT.getPiece().getStringRep().equals("p")) {
                            switch (String.valueOf(move.charAt(6))) {
                                case "q":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Queen.
                                            create(ChessPiece.Colour.BLACK));
                                    moveMade = true;
                                    break;
                                case "n":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Knight.
                                            create(ChessPiece.Colour.BLACK));
                                    moveMade = true;
                                    break;
                                case "b":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Bishop.
                                            create(ChessPiece.Colour.BLACK));
                                    moveMade = true;
                                    break;
                                case "r":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Rook.
                                            create(ChessPiece.Colour.BLACK));
                                    moveMade = true;
                            }
                        } else {
                            switch (String.valueOf(move.charAt(6))) {
                                case "Q":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Queen.
                                            create(ChessPiece.Colour.WHITE));
                                    moveMade = true;
                                    break;
                                case "N":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Knight.
                                            create(ChessPiece.Colour.WHITE));
                                    moveMade = true;
                                    break;
                                case "B":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Bishop.
                                            create(ChessPiece.Colour.WHITE));
                                    moveMade = true;
                                    break;
                                case "R":
                                    sT.removeChessPiece();
                                    eT.setChessPiece(Rook.
                                            create(ChessPiece.Colour.WHITE));
                                    moveMade = true;
                                    break;
                            }
                        }
                    } else {
                        if (sT.getPiece() instanceof Pawn
                                && ((Pawn) sT.getPiece()).haspassingSquare()
                                && eT.getCoord().
                                equals(((Pawn) sT.getPiece()).getpassingSquare())) {
                            if (sT.getPiece().getPieceCol() == ChessPiece
                                    .Colour.WHITE) {
                                currentBoardGrid[getTile(((Pawn) sT.getPiece())
                                        .getpassingSquare()).gettileNoVal() - 1]
                                        [getTile(((Pawn) sT.getPiece())
                                        .getpassingSquare()).getFileValue()]
                                        .removeChessPiece();
                            } else {
                                currentBoardGrid[getTile(((Pawn) sT.getPiece())
                                        .getpassingSquare()).gettileNoVal() + 1]
                                        [getTile(((Pawn) sT.getPiece())
                                        .getpassingSquare()).getFileValue()]
                                        .removeChessPiece();
                            }
                        }
                        eT.setChessPiece(sT.getPiece());
                        sT.removeChessPiece();
                        moveMade = true;
                    }
                }
            }
        }
        return moveMade;
    }

    private boolean setObstacle(ChessPiece.Colour col, String move) {
        int i = 0;
        boolean moveMade = false;
        Tile temporary = null;
        try {
            temporary = currentBoardGrid
                    [i = Integer.valueOf(String.valueOf(move.charAt(2))) - 1]
                    [Tile.getFileValue(String.valueOf(move.charAt(1)))];
        } catch (Exception e) { // Ignore
        }
        try {
            switch (String.valueOf(move.charAt(0))) {
                case "|":
                    if (temporary.getFileValue() != 0 && !temporary.hasWestWall() &&
                            (col == ChessPiece.Colour.WHITE && whiteWallCounter < 3 ||
                                    col == ChessPiece.Colour.BLACK && blackWallCounter < 3)) {
                        if (col == ChessPiece.Colour.BLACK) blackWallCounter++;
                        else whiteWallCounter++;

                        temporary.setWestWall(true);
                        moveMade = true;
                    }
                    break;
                case "_":
                    if (temporary.gettileNoVal() != 0 && !temporary.hasSouthWall
                            () &&
                            (col == ChessPiece.Colour.WHITE && whiteWallCounter < 3 ||
                                    col == ChessPiece.Colour.BLACK && blackWallCounter < 3)) {
                        if (col == ChessPiece.Colour.BLACK) blackWallCounter++;
                        else whiteWallCounter++;

                        temporary.setSouthWall(true);
                        moveMade = true;
                    }
                    break;
                case "M":
                    if (countMoves >= 5) return false;
                    if ((i == 3 || i == 4)
                            && (col == ChessPiece.Colour.WHITE
                            && whiteMineCounter == 0)
                            || (col == ChessPiece.Colour.BLACK
                            && blackMineCounter == 0)) {
                        if (col == ChessPiece.Colour.BLACK) {
                            blackMineCounter++;
                        } else {
                            whiteMineCounter++;
                        }
                        temporary.setisMine(true);
                        moveMade = true;
                    }
                    break;
                case "D":
                    if (countMoves >= 5) return false;
                    if ((i > 1 && i < 6) &&
                            ((col == ChessPiece.Colour.WHITE && whiteTrapCounter == 0)
                                    || (col == ChessPiece.Colour.BLACK && blackTrapCounter == 0))) {
                        if (col == ChessPiece.Colour.BLACK) {
                            blackTrapCounter++;
                        } else {
                            whiteTrapCounter++;
                        }
                        temporary.setisTrap(true);
                        moveMade = true;
                    }
            }
        } catch (Exception e) { // Ignore
        }
        return moveMade;
    }

    public ChessPiece.Colour readBoard(String fileName) {
        int whitePawnCounter = 8; int blackPawnCounter = 8; int whiteKnightCounter = 2;
        int blackKnightCounter = 2; int whiteRookCounter = 2; int blackRookCounter = 2;
        int whiteBishopCounter = 2; int blackBishopCounter = 2; int whiteQueenCounter = 1;
        int blackQueenCounter = 1; int whiteKingCounter = 1; int blackKingCounter = 1;
        int wallCount = 6; int mineCount = 2; int trapDoorCount = 2;
        int lineCount = 0; int i = 7, j = 0; In reader = null;
        boolean isSetup = false;
        try {
            reader = new In(fileName);
            String t;
            for (int c = 0; c < NUM_TILES_ROW + 2; c++) {
                if (reader.hasNextChar()) {
                    if (!((String.valueOf(reader.readChar())).equals("%"))) {
                        if ((t = reader.readLine()).contains("+")
                                || t.contains("-")) {
                            isSetup = true;
                        } else {
                            lineCount++;
                        }

                    } else {
                        reader.readLine();
                    }
                }
            }
            if (isSetup) {

            } else {
                err.print("ERROR: illegal board at status line");
                System.exit(1);
            }
            reader.close();
        } catch (Exception e) {
            err.print("ERROR: could not read file");
        } finally {
            reader.close();
        }
        reader = null;

        try {
            String tile = "", setup = "";
            String temporaryString, ts;
            String blackString = "pnrbqkODMX.";
            String whiteString = "PNRBQKODMX.";
            String eOL1 = "\r\n";
            String eOL2 = "\r";
            String eOL3 = "\n";
            ChessPiece.Colour col;
            reader = new In(fileName);


            while (reader.hasNextChar()) {
                temporaryString = String.valueOf(reader.readChar());
                if (temporaryString.equals("%")) {
                    reader.readLine();
                } else if (eOL1.equals(temporaryString) || eOL2.equals(temporaryString)
                        || eOL3.equals(temporaryString)) {

                    if (j == 7) {
                        err.print("ERROR: illegal board at "
                                + currentBoardGrid[i][j].getCoord());
                        System.exit(3);
                    }
                    i--; lineCount--; j = 0;
                } else if (i != -1) {

                    if (lineCount == 0 && i != -1 && isSetup) {
                        err.print("ERROR: illegal board at "
                                + currentBoardGrid[i][j].getCoord());
                        System.exit(4);
                    }
                    tile = tile.concat(temporaryString);

                    if (blackString.contains(temporaryString)
                            || whiteString.contains(temporaryString)) {
                        if (tile.matches("([|]?)(_?)([rnbqkpRNBQKP.MDOX])")) {
                            if (j > 7) {
                                err.print("ERROR: illegal board at "
                                        + currentBoardGrid[i][j-(j-7)].getCoord());
                                System.exit(5);
                            }
                            for (int b = 0; b < tile.length(); b++) {
                                Tile temporaryTile = currentBoardGrid[i][j];
                                switch (ts = String.valueOf(tile.charAt(b))) {
                                    case "|":
                                        if (j != 0) {
                                            temporaryTile.setWestWall(true);
                                            --wallCount;
                                        } else {
                                            err.print("ERROR: illegal board" +
                                                    " at "
                                                    + currentBoardGrid[i][j].getCoord());
                                            System.exit(32);
                                        }
                                        break;
                                    case "_":
                                        if (i != 0) {
                                            temporaryTile.setSouthWall(true);
                                            --wallCount;
                                        } else {
                                            err.print("ERROR: illegal board" +
                                                    " at "
                                                    + currentBoardGrid[i][j].getCoord());
                                            System.exit(31);
                                        }
                                        break;
                                    case "M":
                                        if (i > 4 || i < 3) {
                                            err.print("ERROR: illegal board" +
                                                    " at "
                                                    + currentBoardGrid[i][j].getCoord());
                                            System.exit(6);
                                        } else {
                                            temporaryTile.setisMine(true);
                                            --mineCount;
                                        }
                                        break;
                                    case "O":
                                        if (i > 5 || i < 2) {
                                            err.print("ERROR: illegal board" +
                                                    " at "
                                                    + currentBoardGrid[i][j].getCoord());
                                            System.exit(7);
                                        }
                                        temporaryTile.setisTrap(true);
                                        temporaryTile.setisTrapOpen(true);
                                        --trapDoorCount;
                                        break;
                                    case "D":
                                        if (i > 5 || i < 2) {
                                            err.print("ERROR: illegal board" +
                                                    " " +
                                                    "at "
                                                    + currentBoardGrid[i][j].getCoord());
                                            System.exit(8);
                                        }
                                        temporaryTile.setisTrap(true);
                                        temporaryTile.setisTrapOpen(false);
                                        --trapDoorCount;
                                        break;
                                    case "X":
                                        if (i > 4 || i < 3) {
                                            err.print("ERROR: illegal board" +
                                                    " " +
                                                    "at "
                                                    + currentBoardGrid[i][j].getCoord());
                                            System.exit(9);
                                        }
                                        temporaryTile.setisTrap(true);
                                        temporaryTile.setisTrapOpen(false);
                                        temporaryTile.setisTrap(true);
                                        --trapDoorCount;
                                        --mineCount;
                                        break;
                                    case ".":
                                        temporaryTile.removeChessPiece();
                                        break;
                                    case "p":
                                    case "P":
                                        if (ts.equals("p")) {
                                            col = ChessPiece.Colour.BLACK;
                                            blackPawnCounter = blackPawnCounter - 1;
                                        } else {
                                            col = ChessPiece.Colour.WHITE;
                                            --whitePawnCounter;
                                        }
                                        temporaryTile.setChessPiece(Pawn.createPawn(col));
                                        break;
                                    case "n":
                                    case "N":
                                        if (ts.equals("n")) {
                                            col = ChessPiece.Colour.BLACK;
                                            --blackKnightCounter;
                                        } else {
                                            col = ChessPiece.Colour.WHITE;
                                            --whiteKnightCounter;
                                        }
                                        temporaryTile.setChessPiece(Knight.create(col));
                                        break;
                                    case "r":
                                    case "R":
                                        if (ts.equals("r")) {
                                            col = ChessPiece.Colour.BLACK;
                                            --blackRookCounter;
                                        } else {
                                            col = ChessPiece.Colour.WHITE;
                                            --whiteRookCounter;
                                        }
                                        temporaryTile.setChessPiece(Rook.create(col));
                                        break;
                                    case "b":
                                    case "B":
                                        if (ts.equals("b")) {
                                            col = ChessPiece.Colour.BLACK;
                                            --blackBishopCounter;
                                        } else {
                                            --whiteBishopCounter;
                                            col = ChessPiece.Colour.WHITE;
                                        }
                                        temporaryTile.setChessPiece(Bishop.create(col));
                                        break;
                                    case "q":
                                    case "Q":
                                        if (ts.equals("q")) {
                                            col = ChessPiece.Colour.BLACK;
                                            --blackQueenCounter;
                                        } else {
                                            col = ChessPiece.Colour.WHITE;
                                            --whiteQueenCounter;
                                        }
                                        temporaryTile.setChessPiece(Queen.create(col));
                                        break;
                                    case "k":
                                    case "K":
                                        boolean moved = false;
                                        if (ts.equals("k")) {
                                            if (!temporaryTile.getCoord().equals("e8")){
                                                moved = true;
                                            }
                                            col = ChessPiece.Colour.BLACK;
                                            --blackKingCounter;
                                        } else {
                                            if (!temporaryTile.getCoord().equals("e1")){
                                                moved = true;
                                            }
                                            col = ChessPiece.Colour.WHITE;
                                            --whiteKingCounter;
                                        }
                                        temporaryTile.setChessPiece(King.create(col));
                                        if (moved) {
                                            ((King) temporaryTile.getPiece())
                                                    .setMoved(true);
                                        }
                                        break;
                                }
                            }
                            tile = "";
                            j++;

                            if (whitePawnCounter == -1 || blackPawnCounter == -1
                                    || whiteKnightCounter == -1 || blackKnightCounter == -1
                                    || whiteRookCounter == -1 || blackRookCounter == -1
                                    || whiteBishopCounter == -1 || blackBishopCounter == -1
                                    || whiteQueenCounter == -1 || blackQueenCounter == -1
                                    || whiteKingCounter == -1 || blackKingCounter == -1
                                    || wallCount == -1 || mineCount == -1
                                    || trapDoorCount == -1) {
                                err.print("ERROR: illegal board at "
                                        + currentBoardGrid[i][j-1].getCoord());
                                System.exit(10);
                            }
                        } else {
                            err.print("ERROR: illegal board at "
                                    + currentBoardGrid[i][j].getCoord());
                            System.exit(11);
                        }
                    }

                } else if (i == -1) {
                    if (!temporaryString.equals(" ")) setup = setup.concat(temporaryString);

                    if (!reader.hasNextChar()) {
                        if (setup.matches("([w|b])" +
                                "([0-9]{2})([+|\\-]{4})" +
                                "((([a-z])([1-8]))|[\\-])([0-9]{1,2})")) {

                            if (String.valueOf(setup.charAt(7)).equals("-")) {
                                halfMoves = Integer.valueOf(setup.substring(8));
                                setup = setup.substring(0, 8);
                            } else {
                                halfMoves = Integer.valueOf(setup.substring(9));
                                setup = setup.substring(0, 9);
                            }

                            for (int c = 0; c < setup.length(); c++) {
                                ts = String.valueOf(setup.charAt(c));
                                if (c == 0) {
                                    if (ts.equals("w")) {
                                        justPlayed = ChessPiece.Colour.BLACK;
                                    } else {
                                        int samePieceCount = 0;
                                        for (int v = 0; v < NUM_TILES_ROW; v++) {
                                            for (int w = 0; w < NUM_TILES_ROW; w++) {
                                                if (currentBoardGrid[v][w].hasPiece() &&
                                                        standardTiles[v][w]
                                                                .hasPiece()) {
                                                    if (currentBoardGrid[v][w].getPiece()
                                                            .getStringRep().equals
                                                                    (standardTiles[v][w].
                                                                            getPiece().
                                                                            getStringRep())) {
                                                        samePieceCount++;
                                                    }
                                                }
                                            }
                                        }
                                        if (samePieceCount == 32) {
                                            err.print("ERROR: illegal board" +
                                                    " at status line");
                                            System.exit(12);
                                        }
                                        justPlayed = ChessPiece.Colour.WHITE;
                                    }
                                } else if (c == 1) {
                                    if ((Integer.valueOf(ts) + Integer.valueOf
                                            (String.valueOf(setup.charAt(2))))
                                            != wallCount) {
                                        err.print("ERROR: illegal board at " +
                                                "status line");
                                        System.exit(13);
                                    }
                                    whiteWallCounter = 3 - Integer.valueOf(ts);
                                    blackWallCounter = 3 - Integer.valueOf
                                            (String.valueOf(setup.charAt(2)));
                                } else if (c <= 6 && c >= 3) {
                                    if ((ts.equals("-"))) {
                                        switch (c) {
                                            case 3:
                                                whiteQAvailable = false;
                                                break;
                                            case 4:
                                                whiteKAvailable = false;
                                                break;
                                            case 5:
                                                blackQAvailable = false;
                                                break;
                                            case 6:
                                                blackKAvailable = false;
                                                break;
                                        }
                                    }
                                } else if (c > 6) {
                                    if (setup.length() == 9) {
                                        boolean passSet = false;
                                        String passingSquare = setup.substring(7);
                                        Tile temp;
                                        out.println(passingSquare);
                                        for (int a = 0; a < NUM_TILES_ROW; a++) {
                                            for (int b = 0; b < tile.length(); b++) {
                                                if ((temp = currentBoardGrid[a][b]).getPiece()
                                                        instanceof Pawn) {
                                                    if (temp.getPiece()
                                                            .getPieceCol() ==
                                                            ChessPiece.
                                                                    Colour.WHITE) {
                                                        if (currentBoardGrid[a - 1][b]
                                                                .getCoord()
                                                                .equals(passingSquare)) {
                                                            if (currentBoardGrid[a][b - 1]
                                                                    .getPiece()
                                                                    instanceof
                                                                    Pawn) {
                                                                ((Pawn) currentBoardGrid[a][b - 1]
                                                                        .getPiece()).
                                                                        setpassingSquare(passingSquare);
                                                            }
                                                            if (currentBoardGrid[a][b + 1].
                                                                    getPiece()
                                                                    instanceof
                                                                    Pawn) {
                                                                ((Pawn) currentBoardGrid[a][b + 1]
                                                                        .getPiece()).
                                                                        setpassingSquare(passingSquare);
                                                            }
                                                            passSet = true;
                                                        }
                                                    } else {
                                                        if (currentBoardGrid[a + 1][b]
                                                                .getCoord()
                                                                .equals(passingSquare)) {
                                                            if (currentBoardGrid[a][b - 1]
                                                                    .getPiece()
                                                                    instanceof
                                                                    Pawn) {
                                                                ((Pawn) currentBoardGrid[a][b - 1]
                                                                        .getPiece()).
                                                                        setpassingSquare(passingSquare);
                                                            }
                                                            if (currentBoardGrid[a][b + 1].
                                                                    getPiece()
                                                                    instanceof
                                                                    Pawn) {
                                                                ((Pawn) currentBoardGrid[a][b + 1]
                                                                        .getPiece()).
                                                                        setpassingSquare(passingSquare);
                                                            }
                                                            passSet = true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (!passSet) {
                                            err.print("ERROR: illegal board" +
                                                    " at status line");
                                            System.exit(15);
                                        }
                                    }
                                }
                            }
                            defSetupLine = setup.substring(3, 7);
                            for (int a = 0; a < NUM_TILES_ROW; a++) {
                                for (int b = 0; b < NUM_TILES_ROW; b++) {
                                    if (currentBoardGrid[a][b].getPiece() instanceof King) {
                                        if (((King) currentBoardGrid[a][b].getPiece()).isMoved()) {
                                            if (currentBoardGrid[a][b].getPiece()
                                                    .getPieceCol() ==
                                                    ChessPiece.Colour.WHITE) {
                                                for (int n = 3; n <= 4; n++) {
                                                    if (String.valueOf(setup
                                                            .charAt(n)).equals
                                                            ("+")) {
                                                        err.print("illegal board " +
                                                                "at " +
                                                                "status line");
                                                        System.exit(29);
                                                    }
                                                }
                                            } else {
                                                for (int n = 5; n <= 6; n++) {
                                                    if (String.valueOf(setup
                                                            .charAt(n)).equals
                                                            ("+")) {
                                                        err.print("ERROR: illegal " +
                                                                "board at status line");
                                                        System.exit(30);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (!getTile("a1").hasPiece()
                                    && String.valueOf(setup.charAt(3)).equals
                                    ("+")) {
                                err.print("ERROR: illegal board at status line");
                                System.exit(30);
                            } else if (!getTile("a8").hasPiece()
                                    && String.valueOf(setup.charAt(4)).equals
                                    ("+")) {
                                err.print("ERROR: illegal board at status line");
                                System.exit(30);
                            } else if (!getTile("h1").hasPiece()
                                    && String.valueOf(setup.charAt(5)).equals
                                    ("+")) {
                                err.print("ERROR: illegal board at status line");
                                System.exit(30);
                            } else if (!getTile("h8").hasPiece()
                                    && String.valueOf(setup.charAt(6)).equals
                                    ("+")) {
                                err.print("ERROR: illegal board at status line");
                                System.exit(30);
                            }
                        } else {
                            err.print("ERROR: illegal board at status line");
                            System.exit(17);
                        }
                    }
                }
            }

            Tile temp;
            for (int k = NUM_TILES_ROW-1; k >= 0;  k--) {
                for (int g = 0; g < NUM_TILES_ROW; g++) {
                    if ((temp = currentBoardGrid[k][g]).hasPiece() && temp.getPiece()
                            instanceof King) {
                        for (int c = 0; c < 8; c++) {
                            try {
                                switch (c) {
                                    case 0:
                                        if (!currentBoardGrid[k + 1][g].hasSouthWall
                                                ()) {
                                            if (currentBoardGrid[k + 1][g].hasPiece()
                                                    && currentBoardGrid[k + 1][g]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal " +
                                                        "board at " +
                                                        currentBoardGrid[k+1][g].getCoord());
                                                System.exit(18);
                                            }
                                        }
                                        break;
                                    case 1:
                                        if (!currentBoardGrid[k][g + 1].hasWestWall()
                                                ) { // No walls
                                            if (currentBoardGrid[k][g+1].hasPiece()
                                                    && currentBoardGrid[k][g+1]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal board at" +
                                                        " " +
                                                        currentBoardGrid[k][g+1].getCoord());
                                                System.exit(19);
                                            }
                                        }
                                        break;
                                    case 2:
                                        if (!currentBoardGrid[k][j].hasSouthWall()) {

                                            if (currentBoardGrid[k - 1][g].hasPiece()
                                                    && currentBoardGrid[k - 1][g]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal " +
                                                        "board at " +
                                                        currentBoardGrid[k-1][g].getCoord());
                                                System.exit(20);
                                            }
                                        }
                                        break;
                                    case 3:
                                        if (!currentBoardGrid[k][g].hasWestWall()) {

                                            if (currentBoardGrid[k][g-1].hasPiece()
                                                    && currentBoardGrid[k][g-1]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal " +
                                                        "board at " +
                                                        currentBoardGrid[k][g-1].getCoord());
                                                System.exit(21);
                                            }
                                        }
                                        break;
                                    case 4:
                                        if (!(currentBoardGrid[k+1][g+1].hasWestWall()
                                                && currentBoardGrid[k+1][g+1]
                                                .hasSouthWall()
                                                || currentBoardGrid[k+1][g]
                                                .hasSouthWall()
                                                && currentBoardGrid[k][g+1]
                                                .hasWestWall()
                                                || currentBoardGrid[k+1][g+1]
                                                .hasWestWall()
                                                && currentBoardGrid[k][g+1]
                                                .hasWestWall()
                                                || currentBoardGrid[k+1][g]
                                                .hasSouthWall()
                                                && currentBoardGrid[k+1][g+1]
                                                .hasSouthWall())) {

                                            if (currentBoardGrid[k+1][g+1].hasPiece()
                                                    && currentBoardGrid[k+1][g+1]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal " +
                                                        "board at " +
                                                        currentBoardGrid[k+1][g+1].getCoord());
                                                System.exit(22);
                                            }
                                        }
                                        break;
                                    case 5:
                                        if (!(currentBoardGrid[k][g].hasSouthWall()
                                                && currentBoardGrid[k][g].hasWestWall()
                                                || currentBoardGrid[k][g - 1]
                                                .hasSouthWall()
                                                && currentBoardGrid[k - 1][g]
                                                .hasWestWall()
                                                || currentBoardGrid[k][g].hasWestWall()
                                                && currentBoardGrid[k - 1][g]
                                                .hasWestWall()
                                                || currentBoardGrid[k][g - 1]
                                                .hasSouthWall()
                                                && currentBoardGrid[k][g]
                                                .hasSouthWall())) {
                                            // No walls
                                            if (currentBoardGrid[k-1][g-1].hasPiece()
                                                    && currentBoardGrid[k-1][g-1]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal " +
                                                        "board at " +
                                                        currentBoardGrid[k-1][g-1].getCoord());
                                                System.exit(23);
                                            }
                                        }
                                        break;
                                    case 6:
                                        if (!(currentBoardGrid[k + 1][g].hasSouthWall()
                                                && currentBoardGrid[k][g].hasWestWall()
                                                || currentBoardGrid[k + 1][g]
                                                .hasWestWall()
                                                && currentBoardGrid[k + 1][g - 1]
                                                .hasSouthWall()
                                                || currentBoardGrid[k + 1][g]
                                                .hasWestWall()
                                                && currentBoardGrid[k][g].hasWestWall()
                                                || currentBoardGrid[k + 1][g - 1]
                                                .hasSouthWall()
                                                && currentBoardGrid[k + 1][g]
                                                .hasSouthWall())) {

                                            if (currentBoardGrid[k+1][g-1].hasPiece()
                                                    && currentBoardGrid[k+1][g-1]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal " +
                                                        "board at " +
                                                        currentBoardGrid[k+1][g-1].getCoord());
                                                System.exit(24);
                                            }
                                        }
                                        break;
                                    case 7:
                                        if (!(currentBoardGrid[k][g].hasSouthWall()
                                                && currentBoardGrid[k][g + 1]
                                                .hasWestWall()
                                                || currentBoardGrid[k][g + 1]
                                                .hasSouthWall()
                                                && currentBoardGrid[k - 1][g + 1]
                                                .hasWestWall()
                                                || currentBoardGrid[k][g + 1]
                                                .hasWestWall()
                                                && currentBoardGrid[k - 1][g + 1]
                                                .hasWestWall()
                                                || currentBoardGrid[k][g].hasSouthWall()
                                                && currentBoardGrid[k][g + 1]
                                                .hasSouthWall())) {

                                            if (currentBoardGrid[k-1][g+1].hasPiece()
                                                    && currentBoardGrid[k-1][g+1]
                                                    .getPiece() instanceof
                                                    King) {
                                                err.print("ERROR: illegal " +
                                                        "board at " +
                                                        currentBoardGrid[k-1][g+1].getCoord());
                                                System.exit(25);
                                            }
                                        }
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            err.print("ERROR: could not read file");
            e.printStackTrace();
        } finally {
            reader.close();
        }
        return justPlayed;
    }

    public void writeBoard(String fileName) {
        PrintWriter writer = null;
        Tile temporary;
        try {
            writer = new PrintWriter(fileName);
            for (int i = NUM_TILES_ROW - 1; i >= 0; i--) {
                for (int j = 0; j < NUM_TILES_ROW; j++) {
                    temporary = currentBoardGrid[i][j];
                    if (temporary.hasWestWall()) {
                        writer.print("|");
                    }
                    if (temporary.hasSouthWall()) {
                        writer.print("_");
                    }
                    if (temporary.hasisTrap() || temporary.hasisMine()) {
                        if (temporary.hasisTrap() && temporary.hasisMine()) {
                            writer.print("X");
                        } else if (temporary.hasisTrap()) {
                            if (temporary.hasisTrapOpen()) {
                                writer.print("O");
                            } else {
                                writer.print("D");
                            }
                        } else if (temporary.hasisMine()) {
                            writer.print("M");
                        }
                    } else {
                        if (temporary.hasPiece()) {
                            writer.print(temporary.getPiece().getStringRep());
                        } else {
                            writer.print(".");
                        }
                    }
                }
                writer.println();
            }
            writer.print(justPlayed == ChessPiece.Colour.WHITE ? "b " : "w ");
            writer.print((3 - whiteWallCounter) + " " + (3 - blackWallCounter) + " ");
            String castlStringW = null;
            String castlStringB = null;
            for (int i = NUM_TILES_ROW - 1; i >= 0; i--) {
                for (int j = 0; j < NUM_TILES_ROW; j++) {
                    if (currentBoardGrid[i][j].getPiece() instanceof King) {
                        if (currentBoardGrid[i][j].getPiece().getPieceCol() ==
                                ChessPiece.Colour.WHITE) {
                            if (((King) currentBoardGrid[i][j].getPiece()).isMoved()) {
                                castlStringW = "--";
                            }
                        } else {
                            if (((King) currentBoardGrid[i][j].getPiece()).isMoved()) {
                                castlStringB = "--";
                            }
                        }
                    }
                }
            }
            String queenSideW = String.valueOf(defSetupLine.charAt(0)),
                    queenSideB = String.valueOf(defSetupLine.charAt(1)),
                    kingSideW = String.valueOf(defSetupLine.charAt(2)),
                    kingSideB = String.valueOf(defSetupLine.charAt(3));
            for (int i = NUM_TILES_ROW - 1; i >= 0; i--) {
                for (int j = 0; j < NUM_TILES_ROW; j++) {
                    if (currentBoardGrid[i][j].hasPiece()) {
                        if (currentBoardGrid[i][j].getPiece() instanceof Rook) {
                            if (currentBoardGrid[i][j].getPiece().getPieceCol()
                                    == ChessPiece.Colour.WHITE) {
                                if (currentBoardGrid[i][j].getCoord().equals("a1")) {
                                    if (((Rook) currentBoardGrid[i][j].getPiece())
                                            .isMoved()) {
                                        queenSideW = "-";
                                    }
                                } else if (currentBoardGrid[i][j].getCoord().equals
                                        ("a8")) {
                                    if (((Rook) currentBoardGrid[i][j].getPiece())
                                            .isMoved()) {
                                        kingSideW = "-";
                                    }
                                }
                            } else {
                                if (currentBoardGrid[i][j].getCoord().equals("h1")) {
                                    if (((Rook) currentBoardGrid[i][j].getPiece())
                                            .isMoved()) {
                                        queenSideB = "-";
                                    }
                                } else if (currentBoardGrid[i][j].getCoord().equals
                                        ("h8")) {
                                    if (((Rook) currentBoardGrid[i][j].getPiece())
                                            .isMoved()) {
                                        kingSideB = "-";
                                    }
                                }
                            }
                        }
                    }
                }
            }
            writer.print(castlStringW == null ? queenSideW + kingSideW : castlStringW);
            writer.print(castlStringB == null ? queenSideB + kingSideB : castlStringB);
            writer.print(" ");
            String passingSquare = null;
            if (!listOfMoves.isEmpty()) {
                String lastMove = listOfMoves.get(listOfMoves.size() - 1);
                String subS = lastMove.substring(0, 2);
                String subE = lastMove.substring(3);
                if (lastMove.length() == 5) { // >= 5
                    if (!(lastMove.equals("0-0-0") || lastMove.equals("0-0")) &&
                            getTile(subE).hasPiece() && getTile(subE).getPiece()
                            instanceof Pawn) {
                        if (subS.contains("2") && subE.contains("4")) {
                            passingSquare = currentBoardGrid[getTile(subE).gettileNoVal() - 1][getTile
                                    (subE).getFileValue()].getCoord();
                        }
                        if (subS.contains("7") && subE.contains("5")) {
                            passingSquare = currentBoardGrid[getTile(subE).gettileNoVal() + 1][getTile
                                    (subE).getFileValue()].getCoord();
                        }
                    }
                }
            }
            writer.print(passingSquare == null ? "-" : passingSquare);
            writer.print(" ");
            writer.print(halfMoves);
        } catch (Exception e) {
            e.printStackTrace();
            err.print("ERROR: could not write file");
        } finally {
            assert writer != null;
            writer.close();
        }
    }

    public Tile getTile(String tileName) {
        /*
         * Returns a tile only after checking that the file and tileNo of the
         * given coordinate is within the parameters of a standard ChessBoard.
         * If either the file or tileNo is out of bounds, an
         * IndexOutOfBoundsException will be thrown, showing an appropriate,
         * descriptive message.
         */
        String file = String.valueOf(tileName.charAt(0));
        int tileNo = Integer.valueOf(String.valueOf(tileName.charAt(1)));

        if (ChessBoard.TILE_COL.contains(file)) {
            if (1 <= tileNo && tileNo <= 8) return currentBoardGrid[tileNo-1][Tile
                    .getFileValue(file)];
            else throw new IndexOutOfBoundsException("tileNo ranges from 1-8");
        } else throw new IndexOutOfBoundsException("File ranges from a-h");
    }

    public List<String> getlistOfMoves() { return listOfMoves; }

    public Tile[][] getcurrentBoardGrid() { return currentBoardGrid; }

    public ChessPiece.Colour getJustPlayed() { return justPlayed; }

    public void setJustPlayed(ChessPiece.Colour col) { justPlayed = col; }

    public int getblackWallCounter() { return blackWallCounter; }

    public void setblackWallCounter(int count) { blackWallCounter = count; }

    public void setwhiteWallCounter(int count) { whiteWallCounter = count; }

    public int getwhiteWallCounter() { return whiteWallCounter; }

    private void setPieces(Tile[][] grid) {
        for (int i = 0; i < NUM_TILES_ROW; i++) {
            for (int j = 0; j < NUM_TILES_ROW; j++) {
                Tile temporary = grid[i][j];
                setMainPieces(temporary, temporary.getFile());
                setPawns(temporary);
            }
        }
        setTileStatus();
    }

    private void setMainPieces (Tile temporary, String file) {
        ChessPiece.Colour white = ChessPiece.Colour.WHITE;
        ChessPiece.Colour black = ChessPiece.Colour.BLACK;

        if (temporary.gettileNo() == 1 || temporary.gettileNo() == 8) {
            ChessPiece temporaryPiece = null;
            switch (file) {
                case "a": case "h":
                    temporaryPiece = Rook.create(
                            temporary.gettileNo() == 1 ? white : black);
                    break;
                case "b": case "g":
                    temporaryPiece = Knight.create(
                            temporary.gettileNo() == 1 ? white : black);
                    break;
                case "c": case "f":
                    temporaryPiece = Bishop.create(
                            temporary.gettileNo() == 1 ? white : black);
                    break;
                case "d": case "e":
                    setKingAndQueen(temporary);
            }
            if (temporaryPiece != null) temporary.setChessPiece(temporaryPiece);
        }
    }

    private void setPawns(Tile temporary) {
        if (temporary.gettileNo() == 2)
            temporary.setChessPiece(Pawn.
                    createPawn(ChessPiece.Colour.WHITE));
        if (temporary.gettileNo() == 7)
            temporary.setChessPiece(Pawn.
                    createPawn(ChessPiece.Colour.BLACK));
    }

    private void setKingAndQueen(Tile temporary) {
        ChessPiece.Colour white = ChessPiece.Colour.WHITE;
        ChessPiece.Colour black = ChessPiece.Colour.BLACK;

        if (temporary.gettileNo() == 1) {
            if (temporary.getFile().equals("d"))
                temporary.setChessPiece(Queen.create(white));
            else  temporary.setChessPiece(King.create(white));
        } else {
            if (temporary.getFile().equals("d"))
                temporary.setChessPiece(Queen.create(black));
            else temporary.setChessPiece(King.create(black));
        }
    }

    public boolean checkMate() {
        boolean checkmate = false;
        for (int i = 0; i < TILE_COL.length(); i++) {
            for (int j = 0; j < TILE_COL.length(); j++) {
                if (currentBoardGrid[i][j].getPiece() instanceof King) {
                    // Find king

                    if (currentBoardGrid[i][j].getPiece().
                            getvalidPositionition(this, currentBoardGrid[i][j]).isEmpty()) {
                        // Check if king has no where to go
                        int pieceCount = 0;
                        for (int a = 0; a < TILE_COL.length(); a++) {
                            for (int b = 0; b < TILE_COL.length(); b++) {
                                if (currentBoardGrid[a][b].hasPiece() &&
                                        currentBoardGrid[a][b].getPiece().getPieceCol
                                                () == currentBoardGrid[i][j].getPiece
                                                ().getPieceCol()) {
                                    pieceCount++;
                                }
                            }
                        }
                        if (pieceCount == 1) {
                            if (currentBoardGrid[i][j].getPiece().getPieceCol() ==
                                    ChessPiece.Colour.WHITE) {
                                if (whiteWallCounter == 3) {
                                    return true;
                                }
                            } else {
                                if (blackWallCounter == 3) {
                                    return true;
                                }
                            }
                        }
                        // Is he the only piece on the board?

                        if (currentBoardGrid[i][j].getPiece().getPieceCol()
                                != ChessPiece.Colour.WHITE && currentBoardGrid[i][j]
                                .isUnderAttackW()
                                || currentBoardGrid[i][j].getPiece().getPieceCol()
                                != ChessPiece.Colour.BLACK && currentBoardGrid[i][j]
                                .isUnderAttackB()) {

                            for (int c = 0; c < TILE_COL.length(); c++) {
                                for (int v = 0; v < TILE_COL.length(); v++) {
                                    // Find attacking piece
                                    try {
                                        if (currentBoardGrid[c][v].getPiece().isChecked()
                                                && currentBoardGrid[c][v].getPiece()
                                                .getPieceCol() != currentBoardGrid[i][j]
                                                .getPiece().getPieceCol()) {
                                            // Make sure it is attacking this king
                                            checkmate = true;
                                            for (int k = 0;
                                                 k < TILE_COL.length(); k++) {
                                                for (int l = 0; l < TILE_COL.length(); l++) {
                                                    // piece can protect king?
                                                    for (String s : currentBoardGrid[c][v]
                                                            .getPiece().getisCheckedList()) {
                                                        for (String p :
                                                                currentBoardGrid[k][l].getPiece().
                                                                        getvalidPositionition(this,
                                                                                currentBoardGrid[k][l])) {
                                                            if (currentBoardGrid[k][l].getPiece()
                                                                    instanceof Queen
                                                                    || currentBoardGrid[k][l].getPiece()
                                                                    instanceof Bishop
                                                                    || currentBoardGrid[k][l].getPiece()
                                                                    instanceof Rook) {
                                                                if (s.equals(p) || p.equals
                                                                        (currentBoardGrid[c][v].getCoord())) {
                                                                    checkmate = false;
                                                                    break;
                                                                }
                                                            } else if (p.equals(currentBoardGrid[c][v].getCoord())) {
                                                                checkmate = false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) { // Ignore
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return checkmate;
    }

    public void saveState() {
        Tile[][] temporaryGrid = new Tile[NUM_TILES_ROW][NUM_TILES_ROW];
        createTiles(temporaryGrid);
        Tile temporary;
        for (int i = 0; i < NUM_TILES_ROW; i++) {
            for (int j = 0; j < NUM_TILES_ROW; j++) {
                temporary = currentBoardGrid[i][j];
                temporaryGrid[i][j].setWestWall(temporary.hasWestWall());
                temporaryGrid[i][j].setSouthWall(temporary.hasSouthWall());
                temporaryGrid[i][j].setisMine(temporary.hasisMine());
                temporaryGrid[i][j].setisTrap(temporary.hasisTrap());
                temporaryGrid[i][j].setisTrapOpen(temporary.hasisTrapOpen());
                temporaryGrid[i][j].setisAttackedBlack(temporary.isisAttackedBlack());
                temporaryGrid[i][j].setisAttackedWhite(temporary.isisAttackedWhite());
                if (temporary.hasPiece()) {
                    temporaryGrid[i][j].setChessPiece(temporary.getPiece());
                }
            }
        }
        listOfBoardStates.add(temporaryGrid);
    }

    public void setState(Tile[][] grid) {
        Tile temporary;
        createTiles(currentBoardGrid);
        for (int i = 0; i < NUM_TILES_ROW; i++) {
            for (int j = 0; j < NUM_TILES_ROW; j++) {
                temporary = grid[i][j];
                currentBoardGrid[i][j].setWestWall(temporary.hasWestWall());
                currentBoardGrid[i][j].setSouthWall(temporary.hasSouthWall());
                currentBoardGrid[i][j].setisMine(temporary.hasisMine());
                currentBoardGrid[i][j].setisTrap(temporary.hasisTrap());
                currentBoardGrid[i][j].setisTrapOpen(temporary.hasisTrapOpen());
                currentBoardGrid[i][j].setisAttackedBlack(temporary.isisAttackedBlack());
                currentBoardGrid[i][j].setisAttackedWhite(temporary.isisAttackedWhite());
                if (temporary.hasPiece()) {
                    currentBoardGrid[i][j].setChessPiece(temporary.getPiece());
                }
            }
        }
    }

    public void setcountMoves(int i) { countMoves = i; }

    public List<Tile[][]> getlistOfBoardStates() { return listOfBoardStates; }

    public void setBlackFirst() { blackFirst = true; }

    public int getHalfMoves() { return halfMoves; }

    public boolean hasDraw() { return isDrawClaimed; }

    public void setisDrawClaimed(boolean draw) { isDrawClaimed = draw; }

    public boolean hasResigned() { return hasResigned; }

    public void sethasResigned() { hasResigned = true; }

    public boolean isInitialState() {
        int samePieceCount = 0;
        for (int v = 0; v < NUM_TILES_ROW; v++) {
            for (int w = 0; w < NUM_TILES_ROW; w++) {
                if (currentBoardGrid[v][w].hasPiece() &&
                        standardTiles[v][w]
                                .hasPiece()) {
                    if (currentBoardGrid[v][w].getPiece()
                            .getStringRep().equals
                                    (standardTiles[v][w].
                                            getPiece().
                                            getStringRep())) {
                        samePieceCount++;
                    }
                }
            }
        }
        return samePieceCount == 32;
    }
}
