import java.util.ArrayList;
import java.util.List;
//class for the bishop piece, inherits from Chesspiece
public class Pawn extends ChessPiece {
    private String passingSquare = null;

    public static Pawn createPawn(Colour colour) {
        return new Pawn(colour);
    }

    private Pawn(Colour colour) {
        super(colour);
        setStringRep(colour == Colour.WHITE ? "P" : "p");
        setvisualRep(colour == Colour.WHITE ? "Chess_plt45.png" : "Chess_pdt45.png");
    }

    public boolean haspassingSquare() {
        return passingSquare != null;
    }

    private void findDiagonalAttackPosition(List<String> validPosition, Tile[][] currentBoardGrid, int i, int j) {
        if (currentBoardGrid[i][j].getPiece().getPieceCol() == Colour.WHITE) {
            try {
                if (!(currentBoardGrid[i + 1][j + 1].hasWestWall()
                        && currentBoardGrid[i + 1][j + 1].hasSouthWall()
                        || currentBoardGrid[i + 1][j].hasSouthWall()
                        && currentBoardGrid[i][j + 1].hasWestWall()
                        || currentBoardGrid[i + 1][j + 1].hasWestWall()
                        && currentBoardGrid[i][j + 1].hasWestWall()
                        || currentBoardGrid[i + 1][j].hasSouthWall()
                        && currentBoardGrid[i + 1][j + 1].hasSouthWall())) {
                    validPosition.add(currentBoardGrid[i + 1][j + 1].getCoord());
                }
            } catch (Exception e) {
            }
            try {
                if (!(currentBoardGrid[i + 1][j].hasSouthWall()
                        && currentBoardGrid[i][j].hasWestWall()
                        || currentBoardGrid[i + 1][j].hasWestWall()
                        && currentBoardGrid[i + 1][j - 1].hasSouthWall()
                        || currentBoardGrid[i + 1][j].hasWestWall()
                        && currentBoardGrid[i][j].hasWestWall()
                        || currentBoardGrid[i + 1][j - 1].hasSouthWall()
                        && currentBoardGrid[i + 1][j].hasSouthWall())) {
                    validPosition.add(currentBoardGrid[i + 1][j - 1].getCoord());
                }
            } catch (Exception e) {
            }
        } else {
            try {
                if (!(currentBoardGrid[i][j].hasSouthWall()
                        && currentBoardGrid[i][j].hasWestWall()
                        || currentBoardGrid[i][j - 1].hasSouthWall()
                        && currentBoardGrid[i - 1][j].hasWestWall()
                        || currentBoardGrid[i][j].hasWestWall()
                        && currentBoardGrid[i - 1][j].hasWestWall()
                        || currentBoardGrid[i][j - 1].hasSouthWall()
                        && currentBoardGrid[i][j].hasSouthWall())) {
                    validPosition.add(currentBoardGrid[i - 1][j - 1].getCoord());
                }
            } catch (Exception e) {
            }
            try {
                if (!(currentBoardGrid[i][j].hasSouthWall()
                        && currentBoardGrid[i][j + 1].hasWestWall()
                        || currentBoardGrid[i][j + 1].hasSouthWall()
                        && currentBoardGrid[i - 1][j + 1].hasWestWall()
                        || currentBoardGrid[i][j + 1].hasWestWall()
                        && currentBoardGrid[i - 1][j + 1].hasWestWall()
                        || currentBoardGrid[i][j].hasSouthWall()
                        && currentBoardGrid[i][j + 1].hasSouthWall())) {
                    validPosition.add(currentBoardGrid[i - 1][j + 1].getCoord());
                }
            } catch (Exception e) {
            }
        }

    }

    private void findVerticalPosition(ChessBoard board, List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        Tile temporary;
        try {
            if (cT.getPiece().getPieceCol() == Colour.WHITE) {
                if (!currentBoardGrid[i + 1][j].hasSouthWall()) {
                    if (!(temporary = currentBoardGrid[i + 1][j]).hasPiece()) {
                        validPosition.add(temporary.getCoord());
                        if (i == 1) findVerticalPosition(board, validPosition, currentBoardGrid, cT,
                                temporary.gettileNoVal(), temporary.getFileValue());
                    }
                }
            } else {
                if (!currentBoardGrid[i][j].hasSouthWall()) {
                    if (!(temporary = currentBoardGrid[i - 1][j]).hasPiece()) {
                        validPosition.add(temporary.getCoord());
                        if (i == 6) findVerticalPosition(board, validPosition, currentBoardGrid, cT,
                                temporary.gettileNoVal(), temporary.getFileValue());
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void setpassingSquare(String s) {
        passingSquare = s;
    }

    public String getpassingSquare() {
        return passingSquare;
    }

    private void findDiagonalPosition(ChessBoard board, List<String> validPosition, Tile[][] currentBoardGrid, int i, int j) {
        Tile temporary;
        Tile temporaryP;
        String temporaryS;
        passingSquare = null;
        if (currentBoardGrid[i][j].getPiece().getPieceCol() == Colour.WHITE) {
            try {
                if (!(currentBoardGrid[i + 1][j + 1].hasWestWall()
                        && currentBoardGrid[i + 1][j + 1].hasSouthWall()
                        || currentBoardGrid[i + 1][j].hasSouthWall()
                        && currentBoardGrid[i][j + 1].hasWestWall()
                        || currentBoardGrid[i + 1][j + 1].hasWestWall()
                        && currentBoardGrid[i][j + 1].hasWestWall()
                        || currentBoardGrid[i + 1][j].hasSouthWall()
                        && currentBoardGrid[i + 1][j + 1].hasSouthWall())) {
                    if ((temporary = currentBoardGrid[i + 1][j + 1]).hasPiece()
                            && temporary.getPiece().getPieceCol() != Colour.WHITE) {
                        validPosition.add(temporary.getCoord());
                    }
                    if (i == 4 && (temporaryP = currentBoardGrid[i][j + 1]).hasPiece()
                            && temporaryP.getPiece() instanceof Pawn
                            && (temporaryS = board.getlistOfMoves().get(board
                            .getlistOfMoves().size() - 1)).
                            contains(temporaryP.getCoord())
                            && temporaryS.contains("7")) {
                        validPosition.add(temporary.getCoord());
                        passingSquare = temporary.getCoord();
                    }
                }
            } catch (Exception e) {
            }
            try {
                if (!(currentBoardGrid[i + 1][j].hasSouthWall()
                        && currentBoardGrid[i][j].hasWestWall()
                        || currentBoardGrid[i + 1][j].hasWestWall()
                        && currentBoardGrid[i + 1][j - 1].hasSouthWall()
                        || currentBoardGrid[i + 1][j].hasWestWall()
                        && currentBoardGrid[i][j].hasWestWall()
                        || currentBoardGrid[i + 1][j - 1].hasSouthWall()
                        && currentBoardGrid[i + 1][j].hasSouthWall())) {
                    if ((temporary = currentBoardGrid[i + 1][j - 1]).hasPiece()
                            && temporary.getPiece().getPieceCol() != Colour.WHITE) {
                        validPosition.add(temporary.getCoord());
                    }
                    if (i == 4 && (temporaryP = currentBoardGrid[i][j - 1]).hasPiece()
                            && temporaryP.getPiece() instanceof Pawn
                            && (temporaryS = board.getlistOfMoves().get(board
                            .getlistOfMoves().size() - 1)).
                            contains(temporaryP.getCoord())
                            && temporaryS.contains("7")) {
                        validPosition.add(temporary.getCoord());
                        passingSquare = temporary.getCoord();
                    }

                }
            } catch (Exception e) {
            }
        } else {
            try {
                if (!(currentBoardGrid[i][j].hasSouthWall()
                        && currentBoardGrid[i][j].hasWestWall()
                        || currentBoardGrid[i][j - 1].hasSouthWall()
                        && currentBoardGrid[i - 1][j].hasWestWall()
                        || currentBoardGrid[i][j].hasWestWall()
                        && currentBoardGrid[i - 1][j].hasWestWall()
                        || currentBoardGrid[i][j - 1].hasSouthWall()
                        && currentBoardGrid[i][j].hasSouthWall())) {
                    if ((temporary = currentBoardGrid[i - 1][j - 1]).hasPiece()
                            && temporary.getPiece().getPieceCol() != Colour.BLACK) {
                        validPosition.add(temporary.getCoord());
                    }
                    if (i == 3 && (temporaryP = currentBoardGrid[i][j - 1]).hasPiece()
                            && temporaryP.getPiece() instanceof Pawn
                            && (temporaryS = board.getlistOfMoves().get(board
                            .getlistOfMoves().size() - 1)).
                            contains(temporaryP.getCoord())
                            && temporaryS.contains("2")) {
                        validPosition.add(temporary.getCoord());
                        passingSquare = temporary.getCoord();
                    }
                }
            } catch (Exception e) {
            }
            try {
                if (!(currentBoardGrid[i][j].hasSouthWall()
                        && currentBoardGrid[i][j + 1].hasWestWall()
                        || currentBoardGrid[i][j + 1].hasSouthWall()
                        && currentBoardGrid[i - 1][j + 1].hasWestWall()
                        || currentBoardGrid[i][j + 1].hasWestWall()
                        && currentBoardGrid[i - 1][j + 1].hasWestWall()
                        || currentBoardGrid[i][j].hasSouthWall()
                        && currentBoardGrid[i][j + 1].hasSouthWall())) {
                    if ((temporary = currentBoardGrid[i - 1][j + 1]).hasPiece()
                            && temporary.getPiece().getPieceCol() != Colour.BLACK) {
                        validPosition.add(temporary.getCoord());
                    }
                    if (i == 3 && (temporaryP = currentBoardGrid[i][j + 1]).hasPiece()
                            && temporaryP.getPiece() instanceof Pawn
                            && (temporaryS = board.getlistOfMoves().get(board
                            .getlistOfMoves().size() - 1)).
                            contains(temporaryP.getCoord())
                            && temporaryS.contains("2")) {
                        validPosition.add(temporary.getCoord());
                        passingSquare = temporary.getCoord();
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    List<String> getValidAttackPosition(ChessBoard board, Tile currentTile) {
        List<String> validAttackPos = new ArrayList<>(4);
        findDiagonalAttackPosition(validAttackPos, board.getcurrentBoardGrid(),
                currentTile.gettileNoVal(), currentTile.getFileValue());
        return validAttackPos;
    }
    @Override
    public List<String> getvalidPositionition(ChessBoard board, Tile currentTile) {
        List<String> validPosition = new ArrayList<>(4);
        findVerticalPosition(board, validPosition, board.getcurrentBoardGrid(), currentTile,
                currentTile.gettileNoVal(), currentTile.getFileValue());
        findDiagonalPosition(board, validPosition, board.getcurrentBoardGrid(),
                currentTile.gettileNoVal(), currentTile.getFileValue());
        return validPosition;
    }

}