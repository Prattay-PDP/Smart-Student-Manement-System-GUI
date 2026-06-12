import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class SuperAdminPanel extends JFrame {

    StudentManager studentManager;
    TeacherManager teacherManager;

    static final Color BG       = new Color(8, 12, 30);
    static final Color CARD_BG  = new Color(15, 22, 50);
    static final Color GOLD     = new Color(255, 200, 0);
    static final Color ACCENT   = new Color(0, 200, 255);
    static final Color RED      = new Color(220, 60, 60);
    static final Color GREEN    = new Color(0, 200, 120);
    static final Color TEXT_W   = Color.WHITE;
    static final Color TEXT_SUB = new Color(160, 190, 230);

    public SuperAdminPanel(StudentManager studentManager, TeacherManager teacherManager) {
        this.studentManager = studentManager;
        this.teacherManager = teacherManager;

        setTitle("⭐ Super Admin Panel");
        setSize(1250, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        setVisible(true);
    }

    JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(80, 0, 160));
        p.setPreferredSize(new Dimension(0, 90));
        p.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("⭐  SUPER ADMIN  —  USTC");
        title.setForeground(GOLD);
        title.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel sub = new JLabel("Full System Control · All Access");
        sub.setForeground(new Color(200, 180, 255));
        sub.setFont(new Font("Arial", Font.PLAIN, 13));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title);
        left.add(sub);

        JLabel clock = new JLabel();
        clock.setForeground(GOLD);
        clock.setFont(new Font("Arial", Font.BOLD, 16));

        javax.swing.Timer t = new javax.swing.Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            clock.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
        });
        t.start();

        p.add(left,  BorderLayout.WEST);
        p.add(clock, BorderLayout.EAST);
        return p;
    }

    JPanel buildCenter() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG);

        JPanel grid = new JPanel(new GridLayout(5, 3, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(20, 25, 20, 25));

        grid.add(makeCard("👥", "All Students",     "View full student list",     ACCENT,               e -> viewAllStudents()));
        grid.add(makeCard("🔍", "Search Student",   "Search by ID",               new Color(0,180,200), e -> searchStudent()));
        grid.add(makeCard("➕", "Add Student",       "Register new student",       GREEN,                e -> addStudentDialog()));

        grid.add(makeCard("📋", "All Teachers",     "View all teachers",          new Color(130,90,255),e -> viewAllTeachers()));
        grid.add(makeCard("👨‍🏫", "Add Teacher",    "Register new teacher",       new Color(100,200,80),e -> addTeacherDialog()));
        grid.add(makeCard("🗑️", "Delete Teacher",  "Remove a teacher",           RED,                  e -> deleteTeacherDialog()));

        grid.add(makeCard("💵", "Fees Collection",  "Collect & view fee history", new Color(0,180,100), e -> new FeesPanel(studentManager)));
        grid.add(makeCard("💰", "Income/Expense",   "Manage finance",             new Color(0,150,255), e -> new FinancePanel()));
        grid.add(makeCard("📈", "Reports",          "View system reports",        new Color(255,140,0), e -> new ReportPanel(studentManager)));

        grid.add(makeCard("💻", "Online Course",    "Manage online courses",      new Color(0,180,220), e -> { dispose(); new OnlineCoursePanel(true, studentManager, teacherManager); }));
        grid.add(makeCard("📝", "Online Exam",      "Create & manage exams",      new Color(180,80,255),e -> { dispose(); new OnlineExamPanel(true, studentManager, teacherManager); }));
        grid.add(makeCard("📋", "Behaviour/Result", "Add results & behaviour",    new Color(255,100,150),e -> new BehaviourResultPanel(studentManager)));

        grid.add(makeCard("🧑‍💼", "HR / Staff",     "Staff info & attendance",   new Color(100,100,220),e -> new StaffPanel()));
        grid.add(makeCard("📄", "CV/Certificates",  "Generate CV & certificates",  new Color(0,150,150), e -> new CVCertificatePanel(studentManager)));
        grid.add(makeCard("📅", "Annual Calendar",  "Manage school calendar",      new Color(180,120,0), e -> { dispose(); new AnnualCalendarPanel(true, studentManager, teacherManager); }));
        wrapper.add(grid);

        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.add(wrapper, BorderLayout.CENTER);
        outer.add(buildSystemRow(), BorderLayout.SOUTH);

        return outer;
    }

    JPanel buildSystemRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 16));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(0, 25, 20, 25));

        row.add(makeCard("📢", "Notice Board", "Set system notice", new Color(255,150,0), e -> editNotice()));
        row.add(makeCard("📊", "Statistics",   "System overview",   GOLD,                 e -> showStats()));
        row.add(makeCard("⬅️", "Logout",       "Back to login",     new Color(100,100,120), e -> logout()));

        return row;
    }

    JPanel makeCard(String icon, String label, String desc, Color accent, java.awt.event.ActionListener al) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
                new LineBorder(accent, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel iconL = new JLabel(icon, SwingConstants.CENTER);
        iconL.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JLabel nameL = new JLabel(label, SwingConstants.CENTER);
        nameL.setForeground(TEXT_W);
        nameL.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel descL = new JLabel(desc, SwingConstants.CENTER);
        descL.setForeground(TEXT_SUB);
        descL.setFont(new Font("Arial", Font.PLAIN, 10));

        JButton btn = new JButton("OPEN");
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.addActionListener(al);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        nameL.setAlignmentX(Component.CENTER_ALIGNMENT);
        descL.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(nameL);
        center.add(Box.createVerticalStrut(3));
        center.add(descL);
        center.add(Box.createVerticalStrut(8));
        center.add(btn);

        card.add(iconL,  BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout());
        p.setBackground(new Color(5, 8, 20));
        JLabel l = new JLabel("⭐ Super Admin — Highest Access Level — USTC 2026");
        l.setForeground(new Color(150, 120, 200));
        p.add(l);
        return p;
    }

    void viewAllStudents() {
        JTextArea area = new JTextArea(25, 60);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-20s %-12s %-10s %-6s %-10s %-8s\n",
                "ID", "NAME", "DEPT", "SEMESTER", "CGPA", "ATT%", "DUE"));
        sb.append("=".repeat(80)).append("\n");

        for (Student s : studentManager.list) {
            sb.append(String.format("%-6d %-20s %-12s %-10s %-6.2f %-10.1f %-8.0f\n",
                    s.id, s.name, s.dept, s.semester, s.cgpa, s.getAttPer(), s.dueFee()));
        }

        if (studentManager.list.isEmpty()) sb.append("  No students found.");

        area.setText(sb.toString());

        JDialog d = new JDialog(this, "All Students", true);
        d.setSize(900, 500);
        d.setLocationRelativeTo(this);
        d.add(new JScrollPane(area));
        d.setVisible(true);
    }

    void searchStudent() {
        String input = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (input == null) return;

        try {
            int id = Integer.parseInt(input.trim());
            Student s = studentManager.searchStudent(id);

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
            d.setSize(450, 480);
            d.setLocationRelativeTo(this);
            d.add(new JScrollPane(area));
            d.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
    }

    void addStudentDialog() {
        JTextField[] fields = new JTextField[13];
        String[] labels = {
            "Name", "Age", "Mobile (8801xxxxxxxxx)", "Email",
            "Password", "Department", "Semester", "Section",
            "CGPA", "Total Classes", "Attended", "Total Fee", "Paid Fee"
        };

        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 8, 6));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField(15);
            panel.add(fields[i]);
        }

        int res = JOptionPane.showConfirmDialog(this, panel, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            int id = studentManager.list.isEmpty() ? 1 :
                     studentManager.list.stream().mapToInt(s -> s.id).max().getAsInt() + 1;

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

            studentManager.addStudent(s);
            JOptionPane.showMessageDialog(this, "✅ Student Added!\nID: " + id);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid Input. Check all fields.");
        }
    }

    void viewAllTeachers() {
        JTextArea area = new JTextArea(20, 55);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-20s %-12s %-16s %-20s %-15s\n",
                "ID", "NAME", "DEPT", "MOBILE", "EMAIL", "SUBJECT"));
        sb.append("=".repeat(90)).append("\n");

        for (Teacher t : teacherManager.list) {
            sb.append(String.format("%-5d %-20s %-12s %-16s %-20s %-15s\n",
                    t.id, t.name, t.dept, t.mobile, t.email, t.subject));
        }

        if (teacherManager.list.isEmpty()) sb.append("  No teachers found.");

        area.setText(sb.toString());

        JDialog d = new JDialog(this, "All Teachers", true);
        d.setSize(900, 450);
        d.setLocationRelativeTo(this);
        d.add(new JScrollPane(area));
        d.setVisible(true);
    }

    void addTeacherDialog() {
        JTextField[] fields = new JTextField[6];
        String[] labels = {
            "Name", "Department", "Mobile (8801xxxxxxxxx)",
            "Email", "Password", "Subject"
        };

        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 8, 6));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField(15);
            panel.add(fields[i]);
        }

        int res = JOptionPane.showConfirmDialog(this, panel, "Add New Teacher", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        try {
            int id = teacherManager.nextId();

            Teacher t = new Teacher(
                    id, fields[0].getText(), fields[1].getText(), fields[2].getText(),
                    fields[3].getText(), fields[4].getText(), fields[5].getText()
            );

            teacherManager.addTeacher(t);
            JOptionPane.showMessageDialog(this, "✅ Teacher Added!\nID: " + id + "\nName: " + t.name);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Invalid Input. Check all fields.");
        }
    }

    void deleteTeacherDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter Teacher ID to Delete:");
        if (input == null) return;

        try {
            int id = Integer.parseInt(input.trim());
            Teacher t = teacherManager.searchTeacher(id);

            if (t == null) {
                JOptionPane.showMessageDialog(this, "Teacher Not Found!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete Teacher?\n\nID: " + t.id + "\nName: " + t.name + "\nDept: " + t.dept,
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                teacherManager.deleteTeacher(id);
                JOptionPane.showMessageDialog(this, "✅ Teacher Deleted: " + t.name);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID!");
        }
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

    void showStats() {
        String stats =
            "Total Students   : " + studentManager.totalStudents() + "\n" +
            "Total Teachers   : " + teacherManager.totalTeachers() + "\n\n" +
            "Average CGPA     : " + String.format("%.2f", studentManager.avgCGPA()) + "\n" +
            "Low Attendance   : " + studentManager.lowAttendance() + " students\n" +
            "Total Due Fee    : " + String.format("%.2f", studentManager.totalDue()) + " ৳\n\n" +
            (studentManager.topper() != null ?
             "Top Student      : " + studentManager.topper().name + " (CGPA: " + studentManager.topper().cgpa + ")" : "No students yet") + "\n";

        JTextArea area = new JTextArea(stats);
        area.setFont(new Font("Consolas", Font.PLAIN, 15));
        area.setEditable(false);

        JDialog d = new JDialog(this, "Statistics", true);
        d.setSize(450, 320);
        d.setLocationRelativeTo(this);
        d.add(new JScrollPane(area));
        d.setVisible(true);
    }

    void logout() {
        dispose();
        AppController.goToLogin();
    }
}