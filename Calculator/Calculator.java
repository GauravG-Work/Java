import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.*;

public class Calculator extends JFrame {
    private JTextField display;
    private JLabel historyLabel;
    private double num1 = 0;
    private String operator = "";
    private boolean isNewCalculation = true;

    // Custom colors
    private final Color DARK_BG = new Color(22, 27, 34);
    private final Color DISPLAY_BG = new Color(13, 17, 23);
    private final Color BUTTON_BG = new Color(35, 40, 48);
    private final Color OPERATOR_BG = new Color(56, 118, 229);
    private final Color EQUALS_BG = new Color(52, 168, 83);
    private final Color TEXT_COLOR = new Color(230, 237, 243);
    private final Color HISTORY_COLOR = new Color(139, 148, 158);

    public Calculator() {
        // Set up the frame
        setTitle("Modern Calculator");
        setSize(350, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_BG);
        setUndecorated(true); // Remove window decorations
        setShape(new RoundRectangle2D.Double(0, 0, 350, 550, 20, 20)); // Rounded corners

        // Create a title bar for dragging
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(DARK_BG);
        titleBar.setBorder(new EmptyBorder(10, 15, 0, 15));

        // Add window buttons
        JPanel windowButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        windowButtons.setOpaque(false);

        JButton closeButton = createCircleButton(new Color(237, 88, 88), 15);
        JButton minimizeButton = createCircleButton(new Color(240, 184, 75), 15);

        closeButton.addActionListener(e -> System.exit(0));
        minimizeButton.addActionListener(e -> setState(JFrame.ICONIFIED));

        windowButtons.add(minimizeButton);
        windowButtons.add(closeButton);
        titleBar.add(windowButtons, BorderLayout.EAST);

        JLabel titleLabel = new JLabel("Calculator");
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("SANS_SERIF", Font.BOLD, 14));
        titleBar.add(titleLabel, BorderLayout.WEST);

        // Create display panel
        JPanel displayPanel = new JPanel(new BorderLayout(0, 5));
        displayPanel.setBackground(DARK_BG);
        displayPanel.setBorder(new EmptyBorder(20, 15, 20, 15));

        // History display
        historyLabel = new JLabel(" ");
        historyLabel.setForeground(HISTORY_COLOR);
        historyLabel.setFont(new Font("SANS_SERIF", Font.PLAIN, 16));
        historyLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Main display
        display = new JTextField("0");
        display.setBackground(DISPLAY_BG);
        display.setForeground(TEXT_COLOR);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("SANS_SERIF", Font.PLAIN, 60));
        display.setEditable(false);
        display.setBorder(new EmptyBorder(10, 15, 10, 15));

        displayPanel.add(historyLabel, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.CENTER);

        // Create button panel with spacing
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 12, 12));
        buttonPanel.setBackground(DARK_BG);
        buttonPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Make the window draggable
        MouseAdapter dragAdapter = new MouseAdapter() {
            private int offsetX, offsetY;

            @Override
            public void mousePressed(MouseEvent e) {
                offsetX = e.getX();
                offsetY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - offsetX, e.getYOnScreen() - offsetY);
            }
        };

        titleBar.addMouseListener(dragAdapter);
        titleBar.addMouseMotionListener(dragAdapter);

        // Add components to the frame
        add(titleBar, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Create and add buttons
        String[] buttonLabels = {
                "C", "±", "%", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "←", "="
        };

        for (String label : buttonLabels) {
            JButton button = createStyledButton(label);
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }
    }

    private JButton createCircleButton(Color color, int size) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(size, size));
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(button.getBackground());
                g2.fillOval(0, 0, button.getWidth(), button.getHeight());
                g2.dispose();
            }
        });

        return button;
    }

    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("SANS_SERIF", Font.PLAIN, 24));
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set button colors based on type
        if (label.equals("=")) {
            button.setBackground(EQUALS_BG);
        } else if (label.equals("+") || label.equals("-") || label.equals("×") || label.equals("÷")) {
            button.setBackground(OPERATOR_BG);
        } else {
            button.setBackground(BUTTON_BG);
        }

        // Custom button UI for rounded corners
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paint button background with rounded corners
                g2.setColor(button.getBackground());
                g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 16, 16);

                // Draw highlighted border when pressed
                if (getModel(button).isPressed()) {
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, button.getWidth() - 2, button.getHeight() - 2, 16, 16);
                }

                // Draw text
                FontMetrics fm = g2.getFontMetrics(button.getFont());
                Rectangle textRect = new Rectangle(0, 0, button.getWidth(), button.getHeight());
                String text = SwingUtilities.layoutCompoundLabel(
                        button, fm, button.getText(), null,
                        button.getVerticalAlignment(), button.getHorizontalAlignment(),
                        button.getVerticalTextPosition(), button.getHorizontalTextPosition(),
                        textRect, new Rectangle(), textRect, 0);

                g2.setColor(button.getForeground());
                g2.setFont(button.getFont());
                int x = (button.getWidth() - fm.stringWidth(text)) / 2;
                int y = (button.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, x, y);
                g2.dispose();
            }

            private ButtonModel getModel(JButton button) {
                return button.getModel();
            }
        });

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (label.equals("=")) {
                    button.setBackground(EQUALS_BG.brighter());
                } else if (label.equals("+") || label.equals("-") || label.equals("×") || label.equals("÷")) {
                    button.setBackground(OPERATOR_BG.brighter());
                } else {
                    button.setBackground(BUTTON_BG.brighter());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (label.equals("=")) {
                    button.setBackground(EQUALS_BG);
                } else if (label.equals("+") || label.equals("-") || label.equals("×") || label.equals("÷")) {
                    button.setBackground(OPERATOR_BG);
                } else {
                    button.setBackground(BUTTON_BG);
                }
            }
        });

        return button;
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            // Handle digit buttons
            if ((command.charAt(0) >= '0' && command.charAt(0) <= '9') || command.equals(".")) {
                if (isNewCalculation) {
                    display.setText(command);
                    isNewCalculation = false;
                } else {
                    if (display.getText().equals("0") && !command.equals(".")) {
                        display.setText(command);
                    } else {
                        // Don't allow multiple decimal points
                        if (command.equals(".") && display.getText().contains(".")) {
                            return;
                        }
                        display.setText(display.getText() + command);
                    }
                }
            }
            // Handle operator buttons
            else if (command.equals("+") || command.equals("-") ||
                    command.equals("×") || command.equals("÷")) {
                String actualOperator = command;
                if (command.equals("×"))
                    actualOperator = "*";
                if (command.equals("÷"))
                    actualOperator = "/";

                // Update history display
                if (!operator.isEmpty()) {
                    // If an operator was already selected, calculate the result first
                    calculate();
                }
                num1 = Double.parseDouble(display.getText());
                operator = actualOperator;
                historyLabel.setText(formatResult(num1) + " " + command + " ");
                isNewCalculation = true;
            }
            // Handle equals button
            else if (command.equals("=")) {
                if (!operator.isEmpty()) {
                    double num2 = Double.parseDouble(display.getText());
                    historyLabel.setText(historyLabel.getText() + formatResult(num2) + " = ");
                    calculate();
                    operator = "";
                }
            }
            // Handle clear button
            else if (command.equals("C")) {
                display.setText("0");
                historyLabel.setText(" ");
                num1 = 0;
                operator = "";
                isNewCalculation = true;
            }
            // Handle backspace button
            else if (command.equals("←")) {
                String currentText = display.getText();
                if (currentText.length() > 1) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                } else {
                    display.setText("0");
                    isNewCalculation = true;
                }
            }
            // Handle sign change button
            else if (command.equals("±")) {
                double value = Double.parseDouble(display.getText());
                value = -value;
                display.setText(formatResult(value));
            }
            // Handle percentage button
            else if (command.equals("%")) {
                double value = Double.parseDouble(display.getText());
                value = value / 100;
                display.setText(formatResult(value));
            }
        }
    }

    private void calculate() {
        if (operator.isEmpty()) {
            return;
        }

        double num2 = Double.parseDouble(display.getText());
        double result = 0;

        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    display.setText("Error");
                    isNewCalculation = true;
                    return;
                }
                break;
        }

        display.setText(formatResult(result));
        num1 = result;
        isNewCalculation = true;
    }

    private String formatResult(double result) {
        // Format number with appropriate decimal places
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            String formatted = String.valueOf(result);
            // Remove trailing zeros if present
            if (formatted.contains(".")) {
                formatted = formatted.replaceAll("0*$", "").replaceAll("\\.$", "");
            }
            return formatted;
        }
    }

    public static void main(String[] args) {
        // Set system look and feel with fallback
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Calculator calculator = new Calculator();
            calculator.setVisible(true);
        });
    }
}