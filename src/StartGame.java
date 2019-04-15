import javax.swing.*;
import java.awt.*;

public class StartGame {

    private static void makeDialogue(ChessBoard board, int count) {
        JFrame startFrame = new JFrame("Obstacle Chess");
        JButton newGameButton = new JButton("New Standard Game");
        JButton readGameButton = new JButton("Read Game From File");
        JButton quitButton = new JButton("Quit Game");
        // Set listeners
        newGameButton.addActionListener(e -> {
            board.setJustPlayed(ChessPiece.Colour.BLACK);
            startFrame.setVisible(false);
            DrawGame.show(board, board.getcurrentBoardGrid(), count);
        });
        readGameButton.addActionListener(e -> {
            startFrame.setVisible(false);
            ReadBoardMenu.show(board);
        });
        quitButton.addActionListener(e -> System.exit(66));
        // Add components and frame attributes
        startFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        startFrame.setLayout(new GridLayout(1,3));
        startFrame.add(newGameButton);
        startFrame.add(readGameButton);
        startFrame.add(quitButton);
        startFrame.pack();
        startFrame.setResizable(false);
        startFrame.setVisible(true);
    }

    public static void show(ChessBoard board, int count) {
        makeDialogue(board, count);
    }
}

