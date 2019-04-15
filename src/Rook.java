import java.util.ArrayList;
import java.util.List;
//class for the bishop piece, inherits from Chesspiece
public class Rook extends ChessPiece {
    private boolean moved = false;
    private Tile startTile = null;

    public static Rook create(Colour colour, Tile... start) {
        return new Rook(colour, start);
    }

    private Rook(Colour colour, Tile... start) {
        super(colour);
        setStringRep(colour == Colour.WHITE ? "R" : "r");
        setvisualRep(colour == Colour.WHITE ?
               "Chess_rlt45.png" : "Chess_rdt45.png");
        if (start.length != 0) startTile = start[0];
    }
    public Tile getStartTile() { return startTile; }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean isMoved() {
        return moved;
    }

    @Override
    List<String> getvalidPositionition(ChessBoard board, Tile currentTile) {
        List<String> validPosition = new ArrayList<>(16);
        findLinMoves(validPosition, board, currentTile);
        return validPosition;
    }

    @Override
    List<String> getValidAttackPosition(ChessBoard board, Tile currentTile) {
        return getvalidPositionition(board, currentTile);
    }
}