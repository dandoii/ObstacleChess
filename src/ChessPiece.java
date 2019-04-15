import java.util.ArrayList;
import java.util.List;
//super class for all the normal pieces
public abstract class ChessPiece {
    public enum Colour { WHITE, BLACK }
    private Colour pColour;
    private String stringRep;
    private String visualRep;
    private boolean isChecked, isCheckedtemporaryorary = false;
    private List<String> isCheckedDownRight, isCheckedDownLeft, isCheckedUp, isCheckedDown, isCheckedRight, isCheckedLeft, isCheckedList,isCheckedUpRight, isCheckedUpLeft;

    public ChessPiece(Colour colour) {
        pColour = colour;
        isCheckedList = new ArrayList<>(16);
        isCheckedUp = new ArrayList<>(16);
        isCheckedDown = new ArrayList<>(16);
        isCheckedRight = new ArrayList<>(16);
        isCheckedLeft = new ArrayList<>(16);
        isCheckedUpRight = new ArrayList<>(16);
        isCheckedUpLeft = new ArrayList<>(16);
        isCheckedDownRight = new ArrayList<>(16);
        isCheckedDownLeft = new ArrayList<>(16);

    }

    public Colour getPieceCol() { return pColour; }

    public void setvisualRep(String rep) { visualRep = rep; }

    public String getvisualRep() { return visualRep; }

    public void setStringRep(String rep) { stringRep = rep; }

    public String getStringRep() { return stringRep; }

    abstract List<String> getvalidPositionition(ChessBoard board, Tile cT);

    abstract List<String> getValidAttackPosition(ChessBoard board, Tile cT);

    public boolean isChecked() { return isChecked; }

    public List<String> getisCheckedList() { return isCheckedList; }

    public void findDiagLinMoves(List<String> validPosition, ChessBoard board, Tile cT) {
        isCheckedList.clear();
        isChecked = false;
        findPosUp(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedUp); isCheckedtemporaryorary = false;
        }
        isCheckedUp.clear();
        findPosDown(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedDown); isCheckedtemporaryorary = false;
        }
        isCheckedDown.clear();
        findPosLeft(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedLeft); isCheckedtemporaryorary = false;
        }
        isCheckedLeft.clear();
        findPosRight(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedRight); isCheckedtemporaryorary = false;
        }
        isCheckedRight.clear();
        findDiagonalPosUpLeft(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedUpLeft); isCheckedtemporaryorary = false;
        }
        isCheckedUpLeft.clear();
        findDiagonalPosUpRight(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedUpRight); isCheckedtemporaryorary = false;
        }
        isCheckedUpRight.clear();
        findDiagonalPosDownLeft(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedDownLeft); isCheckedtemporaryorary = false;
        }
        isCheckedDownLeft.clear();
        findDiagonalPosDownRight(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedDownRight); isCheckedtemporaryorary = false;
        }
        isCheckedDownRight.clear();
    }

    public void findLinMoves(List<String> validPosition, ChessBoard board, Tile cT) {
        isCheckedList.clear();
        isChecked = false;
        findPosUp(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedUp); isCheckedtemporaryorary = false;
        }
        isCheckedUp.clear();
        findPosDown(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedDown); isCheckedtemporaryorary = false;
        }
        isCheckedDown.clear();
        findPosLeft(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedLeft); isCheckedtemporaryorary = false;
        }
        isCheckedLeft.clear();
        findPosRight(validPosition, board.getcurrentBoardGrid(), cT,
                cT.gettileNoVal(), cT.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedRight); isCheckedtemporaryorary = false;
        }
        isCheckedRight.clear();
    }

    public void findDiagonalMoves(List<String> validPosition, ChessBoard board, Tile currentTile) {
        isCheckedList.clear();
        isChecked = false;
        findDiagonalPosUpLeft(validPosition, board.getcurrentBoardGrid(), currentTile,
                currentTile.gettileNoVal(), currentTile.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedUpLeft); isCheckedtemporaryorary = false;
        }
        isCheckedUpLeft.clear();
        findDiagonalPosUpRight(validPosition, board.getcurrentBoardGrid(), currentTile,
                currentTile.gettileNoVal(), currentTile.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedUpRight); isCheckedtemporaryorary = false;
        }
        isCheckedUpRight.clear();
        findDiagonalPosDownLeft(validPosition, board.getcurrentBoardGrid(), currentTile,
                currentTile.gettileNoVal(), currentTile.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedDownLeft); isCheckedtemporaryorary = false;
        }
        isCheckedDownLeft.clear();
        findDiagonalPosDownRight(validPosition, board.getcurrentBoardGrid(), currentTile,
                currentTile.gettileNoVal(), currentTile.getFileValue());
        if (isCheckedtemporaryorary) {
            isCheckedList.addAll(isCheckedDownRight); isCheckedtemporaryorary = false;
        }
        isCheckedDownRight.clear();
    }

    private void findDiagonalPosDownLeft(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tI = i - 1;
        int tJ = j - 1;
        try {
            if (!(currentBoardGrid[i][j].hasSouthWall()
                    && currentBoardGrid[i][j].hasWestWall()
                    || currentBoardGrid[i][j-1].hasSouthWall()
                    && currentBoardGrid[i-1][j].hasWestWall()
                    || currentBoardGrid[i][j].hasWestWall()
                    && currentBoardGrid[i-1][j].hasWestWall()
                    || currentBoardGrid[i][j-1].hasSouthWall()
                    && currentBoardGrid[i][j].hasSouthWall())) {
                if (!currentBoardGrid[tI][tJ].hasPiece()) {
                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    findDiagonalPosDownLeft(validPosition, currentBoardGrid, cT, tI, tJ);
                } else if (currentBoardGrid[tI][tJ].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    if (currentBoardGrid[tI][tJ].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedDownLeft.add(currentBoardGrid[tI][tJ].getCoord());
            }
        } catch (Exception e) {

        }
    }

    private void findDiagonalPosDownRight(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tI = i - 1;
        int tJ = j + 1;
        try {
            if (!(currentBoardGrid[i][j].hasSouthWall()
                    && currentBoardGrid[i][j+1].hasWestWall()
                    || currentBoardGrid[i][j+1].hasSouthWall()
                    && currentBoardGrid[i-1][j+1].hasWestWall()
                    || currentBoardGrid[i][j+1].hasWestWall()
                    && currentBoardGrid[i-1][j+1].hasWestWall()
                    || currentBoardGrid[i][j].hasSouthWall()
                    && currentBoardGrid[i][j+1].hasSouthWall())) {
                if (!currentBoardGrid[tI][tJ].hasPiece()) {
                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    findDiagonalPosDownRight(validPosition, currentBoardGrid, cT, tI, tJ);
                } else if (currentBoardGrid[tI][tJ].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    if (currentBoardGrid[tI][tJ].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedDownRight.add(currentBoardGrid[tI][tJ].getCoord());
            }
        } catch (Exception e) {

        }
    }

    private void findDiagonalPosUpLeft(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tI = i + 1;
        int tJ = j - 1;

        try {
            if (!(currentBoardGrid[i+1][j].hasSouthWall()
                    && currentBoardGrid[i][j].hasWestWall()
                    || currentBoardGrid[i+1][j].hasWestWall()
                    && currentBoardGrid[i+1][j-1].hasSouthWall()
                    || currentBoardGrid[i+1][j].hasWestWall()
                    && currentBoardGrid[i][j].hasWestWall()
                    || currentBoardGrid[i+1][j-1].hasSouthWall()
                    && currentBoardGrid[i+1][j].hasSouthWall())) {

                if (!currentBoardGrid[tI][tJ].hasPiece()) {

                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    findDiagonalPosUpLeft(validPosition, currentBoardGrid, cT, tI, tJ);

                } else if (currentBoardGrid[tI][tJ].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    if (currentBoardGrid[tI][tJ].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedUpLeft.add(currentBoardGrid[tI][tJ].getCoord());
            }
        } catch (Exception e) {

        }
    }

    private void findDiagonalPosUpRight(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tI = i + 1;
        int tJ = j + 1;
        try {
            if (!(currentBoardGrid[i+1][j+1].hasWestWall()
                    && currentBoardGrid[i+1][j+1].hasSouthWall()
                    || currentBoardGrid[i+1][j].hasSouthWall()
                    && currentBoardGrid[i][j+1].hasWestWall()
                    || currentBoardGrid[i+1][j+1].hasWestWall()
                    && currentBoardGrid[i][j+1].hasWestWall()
                    || currentBoardGrid[i+1][j].hasSouthWall()
                    && currentBoardGrid[i+1][j+1].hasSouthWall())) {
                if (!currentBoardGrid[tI][tJ].hasPiece()) {
                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    findDiagonalPosUpRight(validPosition, currentBoardGrid, cT, tI, tJ);
                } else if (currentBoardGrid[tI][tJ].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[tI][tJ].getCoord());
                    if (currentBoardGrid[tI][tJ].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedUpRight.add(currentBoardGrid[tI][tJ].getCoord());
            }
        } catch (Exception e) {

        }
    }

    private void findPosLeft(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tJ = j - 1;
        try {
            if (!currentBoardGrid[i][j].hasWestWall()) {
                if (!currentBoardGrid[i][tJ].hasPiece()) {
                    validPosition.add(currentBoardGrid[i][tJ].getCoord());
                    findPosLeft(validPosition, currentBoardGrid, cT, i, tJ);
                } else if (currentBoardGrid[i][tJ].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[i][tJ].getCoord());
                    if (currentBoardGrid[i][tJ].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedLeft.add(currentBoardGrid[i][tJ].getCoord());
            }
        } catch (Exception e) {

        }
    }

    private void findPosRight(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tJ = j + 1;
        try {
            if (!currentBoardGrid[i][tJ].hasWestWall()) {
                if (!currentBoardGrid[i][tJ].hasPiece()) {
                    validPosition.add(currentBoardGrid[i][tJ].getCoord());
                    findPosRight(validPosition, currentBoardGrid, cT, i, tJ);
                } else if (currentBoardGrid[i][tJ].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[i][tJ].getCoord());
                    if (currentBoardGrid[i][tJ].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedRight.add(currentBoardGrid[i][tJ].getCoord());
            }
        } catch (Exception e) {

        }
    }

    private void findPosUp(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tI = i + 1;
        try {
            if (!currentBoardGrid[tI][j].hasSouthWall()) {
                if (!currentBoardGrid[tI][j].hasPiece()) {
                    validPosition.add(currentBoardGrid[tI][j].getCoord());
                    findPosUp(validPosition, currentBoardGrid, cT, tI, j);
                } else if (currentBoardGrid[tI][j].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[tI][j].getCoord());
                    if (currentBoardGrid[tI][j].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedUp.add(currentBoardGrid[tI][j].getCoord());
            }
        } catch (Exception e) {

        }
    }

    private void findPosDown(List<String> validPosition, Tile[][] currentBoardGrid, Tile cT, int i, int j) {
        int tI = i - 1;
        try {
            if (!currentBoardGrid[i][j].hasSouthWall()) {
                if (!currentBoardGrid[tI][j].hasPiece()) {
                    validPosition.add(currentBoardGrid[tI][j].getCoord());
                    findPosDown(validPosition, currentBoardGrid, cT, tI, j);
                } else if (currentBoardGrid[tI][j].getPiece().getPieceCol()
                        != cT.getPiece().getPieceCol()) {
                    validPosition.add(currentBoardGrid[tI][j].getCoord());
                    if (currentBoardGrid[tI][j].getPiece() instanceof King) {
                        isChecked = isCheckedtemporaryorary = true;
                    }
                }
                isCheckedDown.add(currentBoardGrid[tI][j].getCoord());
            }
        } catch (Exception e) {

        }
    }

}
