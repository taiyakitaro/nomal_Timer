import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.LocalDateTime;

public class SimpleTimer extends JFrame {
    private JTextField inputField;
    private JLabel timerLabel;
    private JLabel stopwatchLabel;
    private JButton startButton;
    private JButton stopwatchStartButton;
    private JButton stopwatchStopButton;
    private JButton resetButton;
    private Timer timer;
    private int remainingSeconds;
    private LocalDateTime stopwatchStartTime;
    private Timer stopwatchTimer;

    public SimpleTimer() {
        setTitle("簡易タイマーとストップウォッチ");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // タイマー機能
        inputField = new JTextField(10);
        add(new JLabel("タイマー秒数を入力:"));
        add(inputField);

        timerLabel = new JLabel("タイマー残り: 00:00:00");
        add(timerLabel);

        startButton = new JButton("タイマー開始");
        add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    remainingSeconds = Integer.parseInt(inputField.getText());
                    startTimer();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SimpleTimer.this, "有効な数字を入力してください。", "エラー", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ストップウォッチ機能
        stopwatchLabel = new JLabel("ストップウォッチ: 00:00:00");
        add(stopwatchLabel);

        stopwatchStartButton = new JButton("ストップウォッチ開始");
        add(stopwatchStartButton);

        stopwatchStopButton = new JButton("ストップウォッチ停止");
        add(stopwatchStopButton);
        stopwatchStopButton.setEnabled(false);

        stopwatchStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startStopwatch();
            }
        });

        stopwatchStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStopwatch();
            }
        });

        // リセットボタン
        resetButton = new JButton("リセット");
        add(resetButton);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetAll();
            }
        });
    }

    private void startTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (remainingSeconds > 0) {
                    timerLabel.setText("タイマー残り: " + formatTime(remainingSeconds));
                    remainingSeconds--;
                } else {
                    timer.stop();
                    timerLabel.setText("タイマーが終了しました！");
                    JOptionPane.showMessageDialog(SimpleTimer.this, "タイマーが終了しました！", "完了", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        timer.start();
    }

    private void startStopwatch() {
        stopwatchStartTime = LocalDateTime.now();
        stopwatchStartButton.setEnabled(false);
        stopwatchStopButton.setEnabled(true);

        stopwatchTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Duration elapsed = Duration.between(stopwatchStartTime, LocalDateTime.now());
                stopwatchLabel.setText("ストップウォッチ: " + formatTime((int) elapsed.getSeconds()));
            }
        });
        stopwatchTimer.start();
    }

    private void stopStopwatch() {
        if (stopwatchTimer != null && stopwatchTimer.isRunning()) {
            stopwatchTimer.stop();
        }
        stopwatchStartButton.setEnabled(true);
        stopwatchStopButton.setEnabled(false);
    }

    private void resetAll() {
        // タイマーのリセット
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timerLabel.setText("タイマー残り: 00:00:00");
        inputField.setText("");

        // ストップウォッチのリセット
        if (stopwatchTimer != null && stopwatchTimer.isRunning()) {
            stopwatchTimer.stop();
        }
        stopwatchLabel.setText("ストップウォッチ: 00:00:00");
        stopwatchStartButton.setEnabled(true);
        stopwatchStopButton.setEnabled(false);
    }

    private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleTimer frame = new SimpleTimer();
            frame.setVisible(true);
        });
    }
}
