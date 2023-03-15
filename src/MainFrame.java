import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        CalculatorPanel panel = new CalculatorPanel();
        panel.setPreferredSize(new Dimension(450, 300));
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }
}