import java.util.ArrayList;
import java.util.List;
//class for the bishop piece, inherits from Chesspiece
public class Queen extends ChessPiece {

    public static Queen create(Colour colour) {
        return new Queen(colour);
    }

    private Queen(ChessPiece.Colour colour) {
        super(colour);
        setStringRep(colour == Colour.WHITE ? "Q" : "q");
        setvisualRep(colour == Colour.WHITE ?
                "Chess_qlt45.png" : "Chess_qdt45.png");
    }
    @Override
    List<String> getValidAttackPosition(ChessBoard board, Tile currentTile) {
        return getvalidPositionition(board, currentTile);
    }

    @Override
    List<String> getvalidPositionition(ChessBoard board, Tile currentTile) {
        List<String> validPosition = new ArrayList<>(32);
        findDiagLinMoves(validPosition, board, currentTile);
        return validPosition;
    }


}