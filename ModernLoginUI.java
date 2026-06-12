import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class ModernLoginUI extends JFrame {

    StudentManager manager;

    static final Color BG_DARK    = new Color(10, 18, 42);
    static final Color BG_CARD    = new Color(18, 30, 65);
    static final Color BG_SIDEBAR = new Color(8, 14, 35);
    static final Color ACCENT     = new Color(0, 140, 255);
    static final Color TEXT_W     = Color.WHITE;
    static final Color TEXT_SUB   = new Color(160, 190, 230);

    static final Color C_ADMIN         = new Color(0, 140, 255);
    static final Color C_STUDENT       = new Color(0, 200, 120);
    static final Color C_TEACHER       = new Color(130, 90, 255);
    static final Color C_PARENT        = new Color(255, 100, 160);
    static final Color C_LIBRARIAN     = new Color(0, 190, 200);
    static final Color C_RECEPTIONIST  = new Color(255, 150, 0);
    static final Color C_ACCOUNTANT    = new Color(200, 80, 255);
    static final Color C_EXIT          = new Color(220, 60, 60);

    public ModernLoginUI(StudentManager manager) {
        this.manager = manager;
        NoticeBoard.loadNotice();

        setTitle("USTC Student Management System");
        setSize(1150, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        setVisible(true);
    }

    JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(0, 60, 150));
        p.setPreferredSize(new Dimension(0, 90));
        p.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel("USTC STUDENT MANAGEMENT SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel sub = new JLabel("University of Science and Technology Chittagong");
        sub.setForeground(new Color(200, 220, 255));
        sub.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title);
        left.add(sub);

        JLabel time = new JLabel();
        time.setForeground(Color.WHITE);
        time.setFont(new Font("Arial", Font.BOLD, 16));

        Timer t = new Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            time.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
        });
        t.start();

        p.add(left, BorderLayout.WEST);
        p.add(time, BorderLayout.EAST);
        return p;
    }

    JPanel buildCenter() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(BG_DARK);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(new EmptyBorder(20, 30, 10, 30));

        JPanel superRow = new JPanel(new GridLayout(1, 1, 0, 0));
        superRow.setOpaque(false);
        superRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        superRow.setBorder(new EmptyBorder(0, 0, 16, 0));
        superRow.add(makeSuperAdminCard());
        wrapper.add(superRow);

        JLabel divider = new JLabel("── Other Roles ──", SwingConstants.CENTER);
        divider.setForeground(new Color(100, 130, 180));
        divider.setFont(new Font("Arial", Font.PLAIN, 12));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);
        divider.setBorder(new EmptyBorder(0, 0, 10, 0));
        wrapper.add(divider);

        JPanel grid = new JPanel(new GridLayout(2, 4, 16, 16));
        grid.setOpaque(false);

        grid.add(makeCard("👤",  "Admin",         "Full system control",     C_ADMIN,        e -> adminLogin()));
        grid.add(makeCard("🎓",  "Student",        "Student dashboard",       C_STUDENT,      e -> studentLogin()));
        grid.add(makeCard("📚",  "Teacher",        "Academic control",        C_TEACHER,      e -> teacherLogin()));
        grid.add(makeCard("👨‍👩‍👧", "Parent",       "Track student progress",  C_PARENT,       e -> parentLogin()));
        grid.add(makeCard("📖",  "Librarian",      "Library management",      C_LIBRARIAN,    e -> librarianLogin()));
        grid.add(makeCard("📋",  "Receptionist",   "Front desk operations",   C_RECEPTIONIST, e -> receptionistLogin()));
        grid.add(makeCard("💰",  "Accountant",     "Manage student accounts", C_ACCOUNTANT,   e -> accountantLogin()));
        grid.add(makeCard("⏻",   "Exit",           "Close application",       C_EXIT,         e -> System.exit(0)));

        wrapper.add(grid);

        return new JPanel(new BorderLayout()) {{
            setBackground(BG_DARK);
            add(wrapper, BorderLayout.CENTER);
        }};
    }

    JPanel makeSuperAdminCard() {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(new Color(25, 10, 60));
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 150, 255), 2, true),
                new EmptyBorder(14, 24, 14, 24)
        ));

        JLabel icon = new JLabel("⭐");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 38));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("Super Admin");
        name.setForeground(new Color(255, 220, 80));
        name.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel desc = new JLabel("Highest Access Level · View All Students & Teachers · Add / Delete Teachers · Manage Everything");
        desc.setForeground(new Color(200, 180, 255));
        desc.setFont(new Font("Arial", Font.PLAIN, 12));

        textPanel.add(name);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(desc);

        JButton btn = new JButton("LOGIN AS SUPER ADMIN");
        btn.setBackground(new Color(130, 0, 220));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(220, 40));
        btn.addActionListener(e -> superAdminLogin());

        card.add(icon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(btn, BorderLayout.EAST);

        return card;
    }

    JPanel makeCard(String icon, String label, String desc, Color accent, ActionListener al) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(accent, 2, true),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel name = new JLabel(label, SwingConstants.CENTER);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel details = new JLabel(desc, SwingConstants.CENTER);
        details.setForeground(TEXT_SUB);
        details.setFont(new Font("Arial", Font.PLAIN, 11));

        JButton btn = new JButton("LOGIN");
        btn.setBackground(accent);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(al);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        details.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(name);
        center.add(Box.createVerticalStrut(4));
        center.add(details);
        center.add(Box.createVerticalStrut(12));
        center.add(btn);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    JPanel buildFooter() {
        JPanel p = new JPanel(new FlowLayout());
        p.setBackground(BG_SIDEBAR);
        JLabel l = new JLabel("© 2026 USTC Student Management System");
        l.setForeground(new Color(120, 150, 200));
        p.add(l);
        return p;
    }

    String[] loginMethod() {
        String[] op = {"Mobile", "Email"};
        String method = (String) JOptionPane.showInputDialog(
                null, "Choose Login Method", "Login",
                JOptionPane.QUESTION_MESSAGE, null, op, op[0]);
        if (method == null) return null;

        String input;
        if (method.equals("Mobile")) {
            JPanel panel = new JPanel(new FlowLayout());
            JLabel prefix = new JLabel("8801 - ");
            JTextField field = new JTextField(10);
            panel.add(prefix);
            panel.add(field);

            int res = JOptionPane.showConfirmDialog(null, panel, "Enter Mobile Number", JOptionPane.OK_CANCEL_OPTION);
            if (res != JOptionPane.OK_OPTION) return null;

            String raw = field.getText().trim();
            if (!raw.matches("\\d{9}")) {
                JOptionPane.showMessageDialog(null, "Enter 9 digits only!");
                return null;
            }
            input = "8801" + raw;
        } else {
            input = JOptionPane.showInputDialog("Enter Email:");
            if (input == null) return null;
        }

        JPasswordField pf = new JPasswordField();
        int r = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return null;

        return new String[]{input, new String(pf.getPassword())};
    }

    void superAdminLogin() {
        String[] data = loginMethod();
        if (data == null) return;

        if ((data[0].equals("8801962367310") || data[0].equals("superadmin@ustc.ac.bd")) && data[1].equals("super1234")) {
            new SuperAdminPanel(manager, AppController.teacherManager);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Super Admin Credentials!");
        }
    }

    void adminLogin() {
        String[] data = loginMethod();
        if (data == null) return;

        if ((data[0].equals("8801962367310") || data[0].equals("admin@gmail.com")) && data[1].equals("1234")) {
            new AdminPanel(manager);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Admin Credentials!");
        }
    }

    void studentLogin() {
        String[] data = loginMethod();
        if (data == null) return;

        Student s = manager.login(data[0], data[1]);
        if (s != null) {
            new StudentPanel(s, manager);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Student Credentials!");
        }
    }

    void teacherLogin() {
        String[] data = loginMethod();
        if (data == null) return;

        Teacher t = AppController.teacherManager.login(data[0], data[1]);
        if (t != null) {
            new TeacherPanel();
            dispose();
        } else if (data[0].equals("8801962367310") && data[1].equals("teacher")) {
            new TeacherPanel();
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Teacher Credentials!");
        }
    }

    void parentLogin() {
        try {
            String idText = JOptionPane.showInputDialog("Enter Student ID");
            if (idText == null) return;

            int id = Integer.parseInt(idText);
            Student s = manager.searchStudent(id);

            if (s != null) {
                new ParentPanel(s);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Student Not Found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid Input");
        }
    }

    void librarianLogin() {
        String[] data = loginMethod();
        if (data == null) return;

        if (data[0].equals("8801962367310") && data[1].equals("library")) {
            new LibrarianPanel();
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Librarian Credentials!");
        }
    }

    void receptionistLogin() {
        String[] data = loginMethod();
        if (data == null) return;

        if (data[0].equals("8801962367310") && data[1].equals("1234")) {
            new ReceptionistPanel(manager);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Receptionist Credentials!");
        }
    }

    void accountantLogin() {
        String[] data = loginMethod();
        if (data == null) return;

        if (data[0].equals("8801962367310") && data[1].equals("1234")) {
            new AccountantPanel(manager);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Wrong Accountant Credentials!");
        }
    }
}