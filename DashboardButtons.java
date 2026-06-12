import javax.swing.*;
import java.awt.*;

public class DashboardButtons {

    static JButton makeButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 18));
        b.setFocusPainted(false);
        return b;
    }

    static JButton line() {
        JButton l = new JButton("--------------");
        l.setEnabled(false);
        return l;
    }

    static JButton backButton(JFrame frame, StudentManager manager) {
        JButton back = new JButton("⬅ BACK TO LOGIN");
        back.setBackground(new Color(255, 120, 0));
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.setFocusPainted(false);
        back.addActionListener(e -> {
            frame.dispose();
            AppController.goToLogin();
        });
        return back;
    }

    static JButton backButton(JFrame frame) {
        JButton back = new JButton("⬅ BACK TO LOGIN");
        back.setBackground(new Color(255, 120, 0));
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.setFocusPainted(false);
        back.addActionListener(e -> {
            frame.dispose();
            AppController.goToLogin();
        });
        return back;
    }
}