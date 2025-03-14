import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUILedger extends JFrame {
    private final JTextField descriptionField;
    private final JTextField amountField;
    private final DefaultTableModel tableModel;
    private final JLabel balanceLabel;
    private double totalBalance = 0.0;

    public GUILedger() {
        setTitle("Simple Ledger");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        JButton addButton = new JButton("Add Transaction");
        inputPanel.add(addButton);
        add(inputPanel, BorderLayout.NORTH);

        // Table to display transactions
        String[] columnNames = { "Description", "Amount" };
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable ledgerTable = new JTable(tableModel);
        add(new JScrollPane(ledgerTable), BorderLayout.CENTER);

        // Balance Label
        balanceLabel = new JLabel("Total Balance: ₹0.00", SwingConstants.CENTER);
        add(balanceLabel, BorderLayout.SOUTH);

        // Add Button Functionality
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        setVisible(true);
    }

    private void addTransaction() {
        String description = descriptionField.getText();
        String amountText = amountField.getText();

        try {
            double amount = Double.parseDouble(amountText);
            tableModel.addRow(new Object[] { description, "₹" + amount });
            totalBalance += amount;
            balanceLabel.setText("Total Balance: ₹" + totalBalance);
            descriptionField.setText("");
            amountField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new GUILedger();
    }
}
