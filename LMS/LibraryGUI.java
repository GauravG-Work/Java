import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Book {
    String title;
    String author;
    String genre;
    boolean isBorrowed;

    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void borrowBook() {
        isBorrowed = true;
    }

    public void returnBook() {
        isBorrowed = false;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (" + genre + ")";
    }
}

public class LibraryGUI {
    private JFrame frame;
    private DefaultListModel<String> bookListModel;
    private JList<String> bookList;
    private ArrayList<Book> books;
    private JLabel statusLabel;
    private Color primaryColor = new Color(51, 51, 102); // Dark blue
    private Color accentColor = new Color(255, 204, 0);  // Gold
    private Color bgColor = new Color(240, 240, 245);    // Light gray-blue
    private final ImageIcon logoIcon = createLogoIcon();

    public LibraryGUI() {
        books = new ArrayList<>();
        setupMainFrame();
        setupComponents();
        addSampleBooks();
        frame.setVisible(true);
    }

    private void setupMainFrame() {
        frame = new JFrame("Comic Book Library");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(bgColor);

        // Custom title bar with logo
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(primaryColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Comic Book Library", logoIcon, JLabel.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setIconTextGap(15);

        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Add search field to title panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton searchButton = createStyledButton("Search", new Color(220, 220, 220));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        titlePanel.add(searchPanel, BorderLayout.EAST);
        frame.add(titlePanel, BorderLayout.NORTH);
    }

    private void setupComponents() {
        // Center panel with book list and details
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(bgColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Book list
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        bookList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bookList.setBackground(Color.WHITE);
        bookList.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
        bookList.setSelectionBackground(new Color(230, 230, 250));
        bookList.setSelectionForeground(primaryColor);

        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Book details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryColor, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel detailsTitle = new JLabel("Book Details");
        detailsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        detailsTitle.setForeground(primaryColor);

        JPanel detailsContent = new JPanel(new GridLayout(4, 1, 5, 5));
        detailsContent.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Title: Select a book to view details");
        JLabel authorLabel = new JLabel("Author: ");
        JLabel genreLabel = new JLabel("Genre: ");
        JLabel statusLabel = new JLabel("Status: ");

        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        authorLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        genreLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        detailsContent.add(titleLabel);
        detailsContent.add(authorLabel);
        detailsContent.add(genreLabel);
        detailsContent.add(statusLabel);

        detailsPanel.add(detailsTitle, BorderLayout.NORTH);
        detailsPanel.add(detailsContent, BorderLayout.CENTER);

        // Split pane to divide book list and details
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                scrollPane,
                detailsPanel
        );
        splitPane.setDividerLocation(400);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(5);
        splitPane.setBorder(BorderFactory.createEmptyBorder());

        centerPanel.add(splitPane, BorderLayout.CENTER);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(bgColor);

        JButton addBookButton = createStyledButton("Add New Book", accentColor);
        JButton borrowBookButton = createStyledButton("Borrow Book", new Color(102, 204, 102));
        JButton returnBookButton = createStyledButton("Return Book", new Color(102, 153, 255));

        buttonPanel.add(addBookButton);
        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);

        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(primaryColor);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        this.statusLabel = new JLabel("Ready | Total Books: 0");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);

        statusPanel.add(statusLabel, BorderLayout.WEST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Add action listeners
        addBookButton.addActionListener(e -> addBook());
        borrowBookButton.addActionListener(e -> borrowBook());
        returnBookButton.addActionListener(e -> returnBook());

        // Selection listener to update details panel
        bookList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = bookList.getSelectedIndex();
                if (index >= 0 && index < books.size()) {
                    Book book = books.get(index);
                    titleLabel.setText("Title: " + book.getTitle());
                    authorLabel.setText("Author: " + book.getAuthor());
                    genreLabel.setText("Genre: " + book.getGenre());
                    statusLabel.setText("Status: " + (book.isBorrowed() ? "Borrowed" : "Available"));
                }
            }
        });
    }

    private JButton createStyledButton(String text, Color buttonColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(buttonColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Button hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(buttonColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(buttonColor);
            }
        });

        return button;
    }

    private ImageIcon createLogoIcon() {
        // Create a simple comic book icon
        int size = 32;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw comic book shape
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(2, 2, size-4, size-4, 5, 5);

        g2d.setColor(accentColor);
        g2d.fillOval(size/4, size/4, size/2, size/2);

        g2d.setColor(primaryColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(2, 2, size-4, size-4, 5, 5);

        g2d.dispose();
        return new ImageIcon(img);
    }

    private void addSampleBooks() {
        addBookToLibrary("Watchmen", "Alan Moore", "Graphic Novel");
        addBookToLibrary("Batman: The Dark Knight Returns", "Frank Miller", "Superhero");
        addBookToLibrary("Saga", "Brian K. Vaughan", "Sci-Fi");
        addBookToLibrary("Maus", "Art Spiegelman", "Historical");
        updateStatusBar();
    }

    private void addBookToLibrary(String title, String author, String genre) {
        books.add(new Book(title, author, genre));
        bookListModel.addElement(title + " (Available)");
    }

    private void addBook() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();

        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Author:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("Genre:"));
        inputPanel.add(genreField);

        int result = JOptionPane.showConfirmDialog(
                frame,
                inputPanel,
                "Add New Comic Book",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();

            if (!title.isEmpty() && !author.isEmpty()) {
                if (genre.isEmpty()) genre = "Unknown";
                addBookToLibrary(title, author, genre);
                statusLabel.setText("Book added: " + title);
                updateStatusBar();
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Title and author are required!",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void borrowBook() {
        int index = bookList.getSelectedIndex();
        if (index != -1) {
            Book book = books.get(index);
            if (!book.isBorrowed()) {
                book.borrowBook();
                bookListModel.set(index, book.getTitle() + " (Borrowed)");
                statusLabel.setText("Book borrowed: " + book.getTitle());
                updateStatusBar();
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "This book is already borrowed!",
                        "Borrow Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a book to borrow!",
                    "Selection Required",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void returnBook() {
        int index = bookList.getSelectedIndex();
        if (index != -1) {
            Book book = books.get(index);
            if (book.isBorrowed()) {
                book.returnBook();
                bookListModel.set(index, book.getTitle() + " (Available)");
                statusLabel.setText("Book returned: " + book.getTitle());
                updateStatusBar();
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "This book is not borrowed!",
                        "Return Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please select a book to return!",
                    "Selection Required",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void updateStatusBar() {
        int total = books.size();
        int borrowed = 0;
        for (Book book : books) {
            if (book.isBorrowed()) borrowed++;
        }
        statusLabel.setText(
                String.format("Total Books: %d | Available: %d | Borrowed: %d",
                        total, total - borrowed, borrowed)
        );
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LibraryGUI());
    }
}