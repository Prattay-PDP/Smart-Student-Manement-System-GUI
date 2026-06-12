import javax.swing.*;
import java.awt.*;

public class DashboardButtons {

    // Generic Back Button: dispose current frame and run a custom action
    static JButton backButton(JFrame frame, Runnable onBack) {
        JButton back = new JButton("⬅ BACK");
        back.setBackground(new Color(255, 120, 0));
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.setFocusPainted(false);

        back.addActionListener(e -> {
            frame.dispose();
            onBack.run();
        });

        return back;
    }

    // Back to Main Login (used from ModernLoginUI's feature panels)
    static JButton backButton(JFrame frame) {
        return backButton(frame, () -> AppController.goToLogin());
    }

    // Back to Super Admin Panel
    static JButton backToSuperAdmin(JFrame frame, StudentManager sm, TeacherManager tm) {
        JButton back = new JButton("⬅ BACK TO SUPER ADMIN");
        back.setBackground(new Color(130, 0, 220));
        back.setForeground(Color.WHITE);
        back.setFont(new Font("Arial", Font.BOLD, 16));
        back.setFocusPainted(false);

        back.addActionListener(e -> {
            frame.dispose();
            new SuperAdminPanel(sm, tm);
        });

        return back;
    }
}