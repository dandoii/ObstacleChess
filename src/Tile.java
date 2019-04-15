public class Tile {
    //the number of the tile ranging from 1 to 8
    private int tileNo;
    //file represents the collumn number of the tile from a-h
    private String file;
    //coordinate of the tile
    private String coordinate; // Coordinate of Tile (used as key in lists)
    //colour of the tile
    private ChessBoard.Colour tColour;
    //the piece occupying the tile
    private ChessPiece chessPiece = null;
    //is the tile attacked by white? who knows?this will tell
    private boolean isAttackedWhite = false;
    //same but for black
    private boolean isAttackedBlack = false;
    //these are bools for the special pieces and tests whether they are found on the tile
    private boolean isMine = false;
    private boolean isTrap = false;
    private boolean isTrapOpen = false;
    private boolean westWall = false;
    private boolean southWall = false;

    public Tile(String Co, ChessBoard.Colour colour) {
        coordinate = Co;
        file = String.valueOf(Co.charAt(0));
        tileNo = Integer.valueOf(String.valueOf(Co.charAt(1)));
        setTColour(colour);
    }

    public void setTColour(ChessBoard.Colour colour) { tColour = colour; }

    public ChessBoard.Colour gettColour() { return tColour; }

    public void setChessPiece(ChessPiece chessPiece) {
        this.chessPiece = chessPiece;
    }

    public ChessPiece getPiece() { return chessPiece; }

    public void removeChessPiece() { chessPiece = null; }

    public String getCoord() { return coordinate; }

    public String getFile() { return file; }

    public int gettileNo() { return tileNo; }

    public int gettileNoVal() { return tileNo - 1; }

    public boolean hasPiece() { return (chessPiece != null); }

    public boolean isUnderAttackW() { return isAttackedWhite; }

    public boolean isUnderAttackB() { return isAttackedBlack; }

    public boolean isisAttackedBlack() { return isAttackedBlack; }

    public boolean isisAttackedWhite() { return isAttackedWhite; }

    public void setisAttackedWhite(boolean b) { isAttackedWhite = b; }

    public void setisAttackedBlack(boolean b) { isAttackedBlack = b; }

    public void clearStatus() { isAttackedBlack = false; isAttackedWhite = false; }

    public static int getFileValue(String file) {
        /*
         * Returns an int value based on the file value.
         */
        int fileVal = 0;
        if(file.equals("a")){ fileVal = 0;}
        else if(file.equals("b")){ fileVal = 1;}
        else if(file.equals("c")){ fileVal = 2;}
        else if(file.equals("d")){ fileVal = 3;}
        else if(file.equals("e")){ fileVal = 4;}
        else if(file.equals("f")){ fileVal = 5;}
        else if(file.equals("g")){ fileVal = 6;}
        else if(file.equals("h")){ fileVal = 7;}

        return fileVal;
    }

    public int getFileValue() { return Tile.getFileValue(this.getFile()); }

    public boolean hasisTrapOpen() { return isTrapOpen; }

    public boolean hasisMine() { return isMine; }

    public boolean hasisTrap() { return isTrap; }

    public boolean hasWestWall() { return westWall; }

    public boolean hasSouthWall() { return southWall; }

    public void setWestWall(boolean b) { westWall = b; }

    public void setSouthWall(boolean b) { southWall = b; }

    public void setisMine(boolean b) { isMine = b; }

    public void setisTrap(boolean isTrap) {
        this.isTrap = isTrap;
    }

    public void setisTrapOpen(boolean b) { isTrapOpen = b; }

    public String toString() {
        return this.getCoord();
    }
}
