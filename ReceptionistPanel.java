import javax.swing.*;
import java.awt.*;

public class ReceptionistPanel extends JFrame {

    StudentManager manager;

    ReceptionistPanel(StudentManager manager) {
        this.manager = manager;

        setTitle("Reception Dashboard");
        setSize(880, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📋  RECEPTIONIST PANEL", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setOpaque(true);
        title.setBackground(new Color(200, 80, 0));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0, 65));
        add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea(22, 55);
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30, 30, 30));

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setForeground(Color.WHITE);
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JTextField idField = new JTextField(10);
        idField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton search = makeBtn("🔍 Search",        new Color(0, 140, 220));
        JButton update = makeBtn("✏️  Update Info",   new Color(0, 160, 100));
        JButton back   = DashboardButtons.backButton(this);

        bottom.add(idLabel);
        bottom.add(idField);
        bottom.add(search);
        bottom.add(update);
        bottom.add(back);

        add(bottom, BorderLayout.SOUTH);

        search.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Student s = manager.searchStudent(id);

                if (s == null) {
                    area.setText("❌ Student Not Found");
                    return;
                }

                area.setText(
                        "========== STUDENT INFO ==========\n\n" +
                        "ID         : " + s.id         + "\n" +
                        "Name       : " + s.name       + "\n" +
                        "Age        : " + s.age        + "\n" +
                        "Department : " + s.dept       + "\n" +
                        "Semester   : " + s.semester   + "\n" +
                        "Section    : " + s.section    + "\n\n" +
                        "Mobile     : " + s.mobile     + "\n" +
                        "Email      : " + s.email      + "\n\n" +
                        "CGPA       : " + s.cgpa       + "\n" +
                        "Attendance : " + String.format("%.2f", s.getAttPer()) + "%\n\n" +
                        "Total Fee  : " + s.totalFee   + "\n" +
                        "Paid Fee   : " + s.paidFee    + "\n" +
                        "Due Fee    : " + s.dueFee()   + "\n\n" +
                        "Status     : " + s.status
                );
            } catch (Exception ex) {
                area.setText("❌ Invalid ID");
            }
        });

        update.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Student s = manager.searchStudent(id);

                if (s == null) {
                    JOptionPane.showMessageDialog(null, "Student Not Found");
                    return;
                }

                String mobile   = JOptionPane.showInputDialog("New Mobile:",   s.mobile);
                String email    = JOptionPane.showInputDialog("New Email:",    s.email);
                String semester = JOptionPane.showInputDialog("New Semester:", s.semester);
                String section  = JOptionPane.showInputDialog("New Section:",  s.section);

                if (mobile   != null) s.mobile   = mobile;
                if (email    != null) s.email    = email;
                if (semester != null) s.semester = semester;
                if (section  != null) s.section  = section;

                manager.saveFile();
                JOptionPane.showMessageDialog(null, "✅ Student Info Updated");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Update Failed");
            }
        });

        setVisible(true);
    }

    JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        return b;
    }
}