import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AdminPanel extends JFrame {

    StudentManager manager;

    static final Color BG      = new Color(15, 20, 45);
    static final Color CARD_BG = new Color(22, 32, 70);
    static final Color ACCENT  = new Color(0, 140, 255);

    public AdminPanel(StudentManager manager) {
        this.manager = manager;

        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 80, 180));
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("👤  ADMIN PANEL  —  USTC");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        header.add(title, BorderLayout.WEST);

        root.add(header, BorderLayout.NORTH);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);

        JPanel grid = new JPanel(new GridLayout(2, 3, 18, 18));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(25, 25, 25, 25));

        grid.add(makeCard("➕", "Add Student",    new Color(0,200,120),  e -> addStudentDialog()));
        grid.add(makeCard("🗑️","Delete Student", new Color(220,60,60),  e -> deleteStudentDialog()));
        grid.add(makeCard("🔍", "Search Student", new Color(0,180,220),  e -> searchStudentDialog()));
        grid.add(makeCard("👥", "All Students",   new Color(130,90,255), e -> viewAllStudents()));
        grid.add(makeCard("📊", "Statistics",     new Color(255,160,0),  e -> showStats()));
        grid.add(makeCard("📢", "Notice Board",   new Color(0,190,150),  e -> editNotice()));

        wrapper.add(grid);
        root.add(wrapper, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(10, 14, 35));
        footer.add(DashboardButtons.backButton(this));
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
        setVisible(true);
    }

    JPanel makeCard(String icon, String label, Color accent, java.awt.event.ActionListener al) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(accent, 2, true),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel iconL = new JLabel(icon, SwingConstants.CENTER);
        iconL.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 34));

        JLabel nameL = new JLabel(label, SwingConstants.CENTER);
        nameL.setForeground(Color.WHITE);
        nameL.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btn = new JButton("OPEN");
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(al);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        nameL.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(nameL);
        center.add(Box.createVerticalStrut(14));
        center.add(btn);

        card.add(iconL,  BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    void addStudentDialog() {
        JTextField[] fields = new JTextField[13];
        String[] labels = {
            "Name", "Age", "Mobile (8801xxxxxxxxx)", "Email",
            "Password", "Department", "Semester", "Section",
            "CGPA", "Total Classes", "Attended", "Total Fee", "Paid Fee"
        };

        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 8, 6));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField(15);
            panel.add(fields[i]);
        }

        int res = JOptionPane.showConfirmDialog(this, panel, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            int id = manager.list.isEmpty() ? 1 :
                     manager.list.stream().mapToInt(s -> s.id).max().getAsInt() + 1;

            Student s = new Student(
                    id, fields[0].getText(), Integer.parseInt(fields[1].getText()),
                    fields[2].getText(), fields[3].getText(), fields[4].getText(),
                    fields[5].getText(), fields[6].getText(), fields[7].getText(),
                    Double.parseDouble(fields[8].getText()),
                    Integer.parseInt(fields[9].getText()),
                    Integer.parseInt(fields[10].getText()),
                    Double.parseDouble(fields[11].getText()),
                    Double.parseDouble(fields[12].getText())
            );

            manager.addStudent(s);
            JOptionPane.showMessageDialog(this, "✅ Student Added!\nID: " + id + "\nName: " + s.name);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid Input. Check all fields.");
        }
    }

    void deleteStudentDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Student ID to Delete:");
        if (input == null) return;

        try {
            int id = Integer.parseInt(input.trim());
            Student s = manager.searchStudent(id);

            if (s == null) {
                JOptionPane.showMessageDialog(this, "Student Not Found!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete Student?\n\nID: " + s.id + "\nName: " + s.name,
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteStudent(id);
                JOptionPane.showMessageDialog(this, "✅ Student Deleted: " + s.name);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    void searchStudentDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (input == null) return;

        try {
            int id = Integer.parseInt(input.trim());
            Student s = manager.searchStudent(id);

            if (s == null) {
                JOptionPane.showMessageDialog(this, "Student Not Found!");
                return;
            }

            String info =
                "ID         : " + s.id         + "\n" +
                "Name       : " + s.name       + "\n" +
                "Age        : " + s.age        + "\n" +
                "Department : " + s.dept       + "\n" +
                "Semester   : " + s.semester   + "\n" +
                "Section    : " + s.section    + "\n" +
                "Mobile     : " + s.mobile     + "\n" +
                "Email      : " + s.email      + "\n\n" +
                "CGPA       : " + s.cgpa       + "\n" +
                "Attendance : " + String.format("%.2f", s.getAttPer()) + "%\n\n" +
                "Total Fee  : " + s.totalFee   + "\n" +
                "Paid Fee   : " + s.paidFee    + "\n" +
                "Due Fee    : " + s.dueFee()   + "\n\n" +
                "Status     : " + s.status;

            JTextArea area = new JTextArea(info);
            area.setFont(new Font("Consolas", Font.PLAIN, 14));
            area.setEditable(false);

            JDialog d = new JDialog(this, "Student Info", true);
            d.setSize(420, 440);
            d.setLocationRelativeTo(this);
            d.add(new JScrollPane(area));
            d.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    void viewAllStudents() {
        JTextArea area = new JTextArea(25, 70);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-20s %-12s %-10s %-6s %-8s %-8s\n",
                "ID", "NAME", "DEPT", "SEMESTER", "CGPA", "ATT%", "DUE"));
        sb.append("=".repeat(80)).append("\n");

        for (Student s : manager.list) {
            sb.append(String.format("%-6d %-20s %-12s %-10s %-6.2f %-8.1f %-8.0f\n",
                    s.id, s.name, s.dept, s.semester, s.cgpa, s.getAttPer(), s.dueFee()));
        }

        if (manager.list.isEmpty()) sb.append("  No students found.");
        area.setText(sb.toString());

        JDialog d = new JDialog(this, "All Students", true);
        d.setSize(900, 500);
        d.setLocationRelativeTo(this);
        d.add(new JScrollPane(area));
        d.setVisible(true);
    }

    void showStats() {
        String stats =
            "Total Students  : " + manager.totalStudents() + "\n" +
            "Average CGPA    : " + String.format("%.2f", manager.avgCGPA()) + "\n" +
            "Low Attendance  : " + manager.lowAttendance() + " students\n" +
            "Total Due Fee   : " + String.format("%.2f", manager.totalDue()) + " ৳\n\n" +
            (manager.topper() != null ?
             "Top Student     : " + manager.topper().name + " (CGPA: " + manager.topper().cgpa + ")" : "No students yet");

        JOptionPane.showMessageDialog(this, stats, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    void editNotice() {
        JTextArea ta = new JTextArea(NoticeBoard.notice, 4, 40);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        int res = JOptionPane.showConfirmDialog(this, new JScrollPane(ta), "Edit Notice Board", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            NoticeBoard.saveNotice(ta.getText().trim());
            JOptionPane.showMessageDialog(this, "✅ Notice Updated!");
        }
    }
}