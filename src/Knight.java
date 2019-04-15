import java.util.ArrayList;
import java.util.List;
//class for the bishop piece, inherits from Chesspiece
public class Knight extends ChessPiece {

    public static Knight create(Colour colour) { return new Knight(colour); }

    private Knight(Colour colour) {
        super(colour);
        setStringRep(colour == Colour.WHITE ? "N" : "n");
        setvisualRep(colour == Colour.WHITE ? "Chess_nlt45.png" : "Chess_ndt45.png");
    }

    @Override
    List<String> getvalidPositionition(ChessBoard board, Tile currentTile) {
        List<String> validPosition = new ArrayList<>(8);
        Tile[][] currentBoardGrid = board.getcurrentBoardGrid();

        for (int i = 0; i < currentBoardGrid.length; i++) {
            for (int j = 0; j < currentBoardGrid[i].length; j++) {
                if (((i == currentTile.gettileNoVal() + 2
                        || i == currentTile.gettileNoVal() - 2)
                        && (j == currentTile.getFileValue() + 1
                        || j == currentTile.getFileValue() - 1))
                        && (!currentBoardGrid[i][j]
                        .hasPiece() || currentBoardGrid[i][j]
                        .getPiece().getPieceCol()
                        != currentTile.getPiece().getPieceCol())) {
                    validPosition.add(currentBoardGrid[i][j].getCoord());
                }
                if ((i == currentTile.gettileNoVal() + 1
                        || i == currentTile.gettileNoVal() - 1)
                        && (j == currentTile.getFileValue() + 2
                        || j == currentTile.getFileValue() - 2)
                        && (!currentBoardGrid[i][j]
                        .hasPiece() || currentBoardGrid[i][j]
                        .getPiece().getPieceCol()
                        != currentTile.getPiece().getPieceCol())) {
                    validPosition.add(currentBoardGrid[i][j].getCoord());
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