import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//class for the bishop piece, inherits from Chesspiece
public class Bishop extends ChessPiece {

    public static Bishop create(Colour colour) {
        return new Bishop(colour);
    }

    private Bishop(Colour colour) {
        super(colour);
        setStringRep(colour == Colour.WHITE ? "B" : "b");
        setvisualRep(colour == Colour.WHITE ? "Chess_blt45.png" : "Chess_bdt45.png");
    }

    @Override
    List<String> getvalidPositionition(ChessBoard board, Tile currentTile) {
        List<String> validPosition = new ArrayList<>(16);
        findDiagonalMoves(validPosition, board, currentTile);
        return validPosition;
    }

    @Override
    List<String> getValidAttackPosition(ChessBoard board, Tile cT) {
        return getvalidPositionition(board, cT);
    }
}