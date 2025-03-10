import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Clock extends JFrame {

    SimpleDateFormat timeFormat;
    SimpleDateFormat dayFormat;
    SimpleDateFormat dateFormat;

    JLabel timeLabel;
    JLabel dayLabel;
    JLabel dateLabel;

    Clock() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Digital Clock");
        this.setLayout(new BorderLayout());
        this.setSize(400, 250);
        this.setResizable(true);

        timeFormat = new SimpleDateFormat("hh:mm:ss a");
        dayFormat = new SimpleDateFormat("EEEE");
        dateFormat = new SimpleDateFormat("dd MMMMM, yyyy");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.setBackground(Color.BLACK);

        timeLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setFont(new Font("SANS_SERIF", Font.PLAIN, 60));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setOpaque(true);

        dayLabel = new JLabel("", SwingConstants.CENTER);
        dayLabel.setFont(new Font("Ink Free", Font.BOLD, 35));
        dayLabel.setForeground(Color.WHITE);
        dayLabel.setBackground(Color.BLACK);
        dayLabel.setOpaque(true);

        dateLabel = new JLabel("", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Ink Free", Font.BOLD, 30));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setBackground(Color.BLACK);
        dateLabel.setOpaque(true);

        panel.add(timeLabel);
        panel.add(dayLabel);
        panel.add(dateLabel);

        this.add(panel, BorderLayout.CENTER);
        this.getContentPane().setBackground(Color.BLACK);
        this.setVisible(true);

        startClock();
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            Calendar calendar = Calendar.getInstance();
            timeLabel.setText(timeFormat.format(calendar.getTime()));
            dayLabel.setText(dayFormat.format(calendar.getTime()));
            dateLabel.setText(dateFormat.format(calendar.getTime()));
        });

        timer.start();
    }

    public static void main(String[] args) {
        new Clock();
    }
}
