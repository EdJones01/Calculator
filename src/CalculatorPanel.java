import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import javax.swing.*;

public class CalculatorPanel extends JPanel implements ActionListener, KeyListener {
    private final JLabel label;
    private String input = "";
    private double answer = 0;
    private final LinkedList<JButton> buttons = new LinkedList<>();
    private final JButton answerButton;

    public CalculatorPanel() {
        setLayout(new BorderLayout());
        addKeyListener(this);
        setFocusable(true);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        label = new JLabel("0");
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(getFont().deriveFont(Font.PLAIN, 40));
        add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 6, 3, 3));

        buttonPanel.add(createButton("7"));
        buttonPanel.add(createButton("8"));
        buttonPanel.add(createButton("9"));
        buttonPanel.add(createButton("/"));
        buttonPanel.add(createButton("DEL"));
        buttonPanel.add(createButton("AC"));

        buttonPanel.add(createButton("4"));
        buttonPanel.add(createButton("5"));
        buttonPanel.add(createButton("6"));
        buttonPanel.add(createButton("*"));
        buttonPanel.add(createButton("("));
        buttonPanel.add(createButton(")"));

        buttonPanel.add(createButton("1"));
        buttonPanel.add(createButton("2"));
        buttonPanel.add(createButton("3"));
        buttonPanel.add(createButton("-"));
        buttonPanel.add(createButton("^"));
        buttonPanel.add(createButton("%"));

        buttonPanel.add(createButton("0"));
        buttonPanel.add(createButton("."));
        buttonPanel.add(createButton("*10^"));
        buttonPanel.add(createButton("+"));

        answerButton = createButton("ANS");
        buttonPanel.add(answerButton);

        buttonPanel.add(createButton("="));

        add(buttonPanel, BorderLayout.CENTER);

        Tools.addKeyBinding(this, KeyEvent.VK_ESCAPE, "clear", (evt) -> clear());
        Tools.addKeyBinding(this, KeyEvent.VK_ENTER, "calculate", (evt) -> calculate());
        Tools.addKeyBinding(this, KeyEvent.VK_BACK_SPACE, "backspace", (evt) -> backspace());
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(getFont().deriveFont(Font.PLAIN, 15));
        button.setActionCommand(text);
        button.setFocusable(false);
        button.addActionListener(this);
        buttons.add(button);
        return button;
    }

    private void setAnswerButtonTooltip() {
        answerButton.setToolTipText(formatDouble("" + answer));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("=")) {
            calculate();
            setAnswerButtonTooltip();
            return;
        }
        if (cmd.equals("ANS")) {
            input += "ANS";
            label.setText(input);
            return;
        }
        if (cmd.equals("AC")) {
            clear();
            return;
        }
        if (cmd.equals("DEL")) {
            backspace();
            return;
        }
        try {
            if (Calculator.isOperator(cmd.toCharArray()[0]) && Double.parseDouble(label.getText()) == answer)
                input += "ANS";
        } catch (Exception ignored) {
        }
        input += (cmd);

        label.setText(input);
    }

    private void backspace() {
        if (input.endsWith("ANS")) {
            input = input.substring(0, input.length() - 2);
        }
        if (input.length() > 1) {
            input = input.substring(0, input.length() - 1);
            label.setText(input);
        } else {
            input = "";
            label.setText("0");
        }
    }

    private void clear() {
        input = "";
        label.setText("0");
    }

    private String formatDouble(String input) {
        if (input.endsWith(".0")) {
            return input.substring(0, input.length() - 2);
        }
        return input;
    }

    private void calculate() {
        double output;
        input = input.replaceAll("ANS", "" + answer);
        input = input.replaceAll("E", "*10^");
        try {
            output = Calculator.evaluateExpression(input);
        } catch (Exception ex) {
            input = "";
            label.setText("ERROR");
            return;
        }
        label.setText(formatDouble("" + output));
        answer = output;
        input = "";
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String key = "" + e.getKeyChar();
        for (JButton button : buttons)
            if (button.getText().equals(key))
                button.doClick();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}