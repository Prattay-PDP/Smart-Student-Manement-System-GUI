import javax.swing.*;
import java.awt.*;

public class TeacherPanel extends JFrame {

    public TeacherPanel() {
        setTitle("Teacher Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📚  TEACHER DASHBOARD", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setOpaque(true);
        title.setBackground(new Color(30, 30, 30));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0, 65));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(4, 1, 10, 10));
        center.setBackground(new Color(45, 45, 45));
        center.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton uploadMarks  = makeBtn("📊  Upload / Edit Marks");
        JButton attendance   = makeBtn("📅  Manage Attendance");
        JButton viewStudents = makeBtn("👨‍🎓  View Students");
        JButton notice       = makeBtn("📢  View Notice");

        center.add(uploadMarks);
        center.add(attendance);
        center.add(viewStudents);
        center.add(notice);

        add(center, BorderLayout.CENTER);

        uploadMarks.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Student ID to Edit Marks:");
            if (id != null) {
                String marks = JOptionPane.showInputDialog("Enter New Marks:");
                JOptionPane.showMessageDialog(null, "Marks updated for ID: " + id + "\nMarks: " + marks);
            }
        });

        attendance.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Student ID:");
            if (id != null) {
                String att = JOptionPane.showInputDialog("Enter Attendance %:");
                JOptionPane.showMessageDialog(null, "Attendance updated for ID: " + id + "\nAttendance: " + att + "%");
            }
        });

        viewStudents.addActionListener(e ->
                JOptionPane.showMessageDialog(null, "Student list feature coming soon!")
        );

        notice.addActionListener(e ->
                JOptionPane.showMessageDialog(null, NoticeBoard.notice)
        );

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(30, 30, 30));
        bottom.add(DashboardButtons.backButton(this));
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    JButton makeBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 17));
        b.setBackground(new Color(0, 120, 220));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }
}