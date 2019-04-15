import java.util.ArrayList;
import java.util.List;
//class for the bishop piece, inherits from Chesspiece
public class King  extends ChessPiece {
    private boolean moved = false;

    public static King create(Colour colour) { return new King(colour); }

    private King(Colour colour) {
        super(colour);
        setStringRep(colour == Colour.WHITE ? "K" : "k");
        setvisualRep(colour == Colour.WHITE ? "Chess_klt45.png" : "Chess_kdt45.png");
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved() {
        return moved;
    }

    @Override
    List<String> getvalidPositionition(ChessBoard board, Tile currentTile) {
        List<String> validPosition = new ArrayList<>(9);
        Tile[][] currentBoardGrid = board.getcurrentBoardGrid();

        int i = currentTile.gettileNoVal(), j = currentTile.getFileValue();
        Tile temporary = null;
        for (int c = 0; c < 8; c++) {
            try {
                    if(c == 0){
                        if (!currentBoardGrid[i+1][j].hasSouthWall()) {
                            temporary = currentBoardGrid[i + 1][j];
                        }
                    }
                    else if(c == 1) {
                        if (!currentBoardGrid[i][j + 1].hasWestWall()) {
                            temporary = currentBoardGrid[i][j + 1];
                        }
                    }
                    else if(c == 2) {
                        if (!currentBoardGrid[i][j].hasSouthWall()) {
                            temporary = currentBoardGrid[i - 1][j];
                        }
                    }
                    else if(c == 3) {
                        if (!currentBoardGrid[i][j].hasWestWall()) {
                            temporary = currentBoardGrid[i][j - 1];
                        }
                    }
                    else if(c == 4) {
                        if (!(currentBoardGrid[i + 1][j + 1].hasWestWall()
                                && currentBoardGrid[i + 1][j + 1].hasSouthWall()
                                || currentBoardGrid[i + 1][j].hasSouthWall()
                                && currentBoardGrid[i][j + 1].hasWestWall()
                                || currentBoardGrid[i + 1][j + 1].hasWestWall()
                                && currentBoardGrid[i][j + 1].hasWestWall()
                                || currentBoardGrid[i + 1][j].hasSouthWall()
                                && currentBoardGrid[i + 1][j + 1].hasSouthWall())) {

                            temporary = currentBoardGrid[i + 1][j + 1];
                        }
                    }
                    else if(c == 5) {
                        if (!(currentBoardGrid[i][j].hasSouthWall()
                                && currentBoardGrid[i][j].hasWestWall()
                                || currentBoardGrid[i][j - 1].hasSouthWall()
                                && currentBoardGrid[i - 1][j].hasWestWall()
                                || currentBoardGrid[i][j].hasWestWall()
                                && currentBoardGrid[i - 1][j].hasWestWall()
                                || currentBoardGrid[i][j - 1].hasSouthWall()
                                && currentBoardGrid[i][j].hasSouthWall())) {

                            temporary = currentBoardGrid[i - 1][j - 1];
                        }
                    }
                    else if(c == 6) {
                        if (!(currentBoardGrid[i + 1][j].hasSouthWall()
                                && currentBoardGrid[i][j].hasWestWall()
                                || currentBoardGrid[i + 1][j].hasWestWall()
                                && currentBoardGrid[i + 1][j - 1].hasSouthWall()
                                || currentBoardGrid[i + 1][j].hasWestWall()
                                && currentBoardGrid[i][j].hasWestWall()
                                || currentBoardGrid[i + 1][j - 1].hasSouthWall()
                                && currentBoardGrid[i + 1][j].hasSouthWall())) {

                            temporary = currentBoardGrid[i + 1][j - 1];
                        }
                    }
                    else if(c == 7) {
                        if (!(currentBoardGrid[i][j].hasSouthWall()
                                && currentBoardGrid[i][j + 1].hasWestWall()
                                || currentBoardGrid[i][j + 1].hasSouthWall()
                                && currentBoardGrid[i - 1][j + 1].hasWestWall()
                                || currentBoardGrid[i][j + 1].hasWestWall()
                                && currentBoardGrid[i - 1][j + 1].hasWestWall()
                                || currentBoardGrid[i][j].hasSouthWall()
                                && currentBoardGrid[i][j + 1].hasSouthWall())) {

                            temporary = currentBoardGrid[i - 1][j + 1];
                        }
                    }

                if (currentTile.getPiece().getPieceCol() == Colour.WHITE) {
                    if (!temporary.isUnderAttackB() && !temporary.hasPiece()
                            || temporary.getPiece().getPieceCol() != Colour.WHITE) {
                        validPosition.add(temporary.getCoord());
                    }
                } else {
                    if (!temporary.isUnderAttackW() && !temporary.hasPiece()
                            || temporary.getPiece().getPieceCol() != Colour.BLACK) {
                        validPosition.add(temporary.getCoord());
                    }
                }
            } catch (Exception e) {

            }
        }

        if (i == 0 && j == 4 && currentTile.getPiece().getPieceCol() == Colour.WHITE) {
            if (currentBoardGrid[i][j-4].hasPiece() && currentBoardGrid[i][j-4]
                    .getPiece() instanceof Rook) {
                if (!((Rook) currentBoardGrid[i][j-4].getPiece()).isMoved()) {
                    boolean cast = false;
                    for (String s : currentBoardGrid[i][j-4].getPiece().getvalidPositionition(board,
                            currentBoardGrid[i][j-4])) {
                        if (s.equals(currentBoardGrid[i][j-1].getCoord())) {
                            cast = true;
                        }
                    }
                    if (cast) {
                        validPosition.add(currentBoardGrid[i][j - 2].getCoord());
                    }
                }
            }
        }
        if (i == 7 && j == 4 && currentTile.getPiece().getPieceCol() == Colour.BLACK) {
            if (currentBoardGrid[i][j-4].hasPiece() && currentBoardGrid[i][j-4]
                    .getPiece() instanceof Rook) {
                if (!((Rook) currentBoardGrid[i][j-4].getPiece()).isMoved()) {
                    boolean cast = false;
                    for (String s : currentBoardGrid[i][j-4].getPiece().getvalidPositionition(board,
                            currentBoardGrid[i][j-4])) {
                        if (s.equals(currentBoardGrid[i][j-1].getCoord())) {
                            cast = true;
                        }
                    }
                    if (cast) {
                        validPosition.add(currentBoardGrid[i][j - 2].getCoord());
                    }
                }
            }
        }
        if (i == 7 && j == 4 && currentTile.getPiece().getPieceCol() == Colour.BLACK) {
            if (currentBoardGrid[i][j+3].hasPiece() && currentBoardGrid[i][j+3]
                    .getPiece() instanceof Rook) {
                if (!((Rook) currentBoardGrid[i][j+3].getPiece()).isMoved()) {
                    boolean cast = false;
                    for (String s : currentBoardGrid[i][j+3].getPiece().getvalidPositionition(board,
                            currentBoardGrid[i][j+3])) {
                        if (s.equals(currentBoardGrid[i][j+1].getCoord())) {
                            cast = true;
                        }
                    }
                    if (cast) {
                        validPosition.add(currentBoardGrid[i][j + 2].getCoord());
                    }
                }
            }
        }
        if (i == 0 && j == 4 && currentTile.getPiece().getPieceCol() == Colour.WHITE) {
            if (currentBoardGrid[i][j+3].hasPiece() && currentBoardGrid[i][j+3]
                    .getPiece() instanceof Rook) {
                if (!((Rook) currentBoardGrid[i][j+3].getPiece()).isMoved()) {
                    boolean cast = false;
                    for (String s : currentBoardGrid[i][j+3].getPiece().getvalidPositionition(board,
                            currentBoardGrid[i][j+3])) {
                        if (s.equals(currentBoardGrid[i][j+1].getCoord())) {
                            cast = true;
                        }
                    }
                    if (cast) {
                        validPosition.add(currentBoardGrid[i][j + 2].getCoord());
                    }
                }
            }
        }


        return validPosition;
    }

    @Override
    List<String> getValidAttackPosition(ChessBoard board, Tile cT) {
        return getvalidPositionition(board, cT);
    }
}
