import javax.swing.*;
import java.awt.*;

public class ReadBoardMenu {

    private static void makeDialogue(ChessBoard board) {
        JFrame readFrame = new JFrame("Read in File");
        JLabel infoLabel = new JLabel("    Enter path:    ");
        JTextField pathField = new JTextField();
        JButton readButton = new JButton("Read File");
        // Add listener
        readButton.addActionListener(e -> {
            try {
                board.readBoard(pathField.getText());
                readFrame.setVisible(false);
                DrawGame.show(board, board.getcurrentBoardGrid(), 0);
            } catch (Exception a) {
                infoLabel.setText("    Try Again:    ");
                pathField.setText("");
            }
        });
        // Add components and frame attributes
        readFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        readFrame.setLayout(new GridLayout(1, 3));
        readFrame.add(infoLabel);
        readFrame.add(pathField);
        readFrame.add(readButton);
        readFrame.pack();
        readFrame.setResizable(false);
        readFrame.setVisible(true);
    }

    public static void show(ChessBoard board) {
        makeDialogue(board);
    }
}
