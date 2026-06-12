import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LibrarianPanel extends JFrame {

    ArrayList<String> books       = new ArrayList<>();
    ArrayList<String> issuedBooks = new ArrayList<>();

    DefaultListModel<String> model    = new DefaultListModel<>();
    JList<String>            bookList = new JList<>(model);

    public LibrarianPanel() {
        setTitle("Library Dashboard");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📖  LIBRARIAN PANEL", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(0, 100, 160));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0, 60));
        add(title, BorderLayout.NORTH);

        bookList.setFont(new Font("Consolas", Font.PLAIN, 14));
        add(new JScrollPane(bookList), BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setBackground(new Color(30, 30, 30));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row1.setBackground(new Color(30, 30, 30));
        JTextField bookField = new JTextField(30);
        bookField.setFont(new Font("Arial", Font.PLAIN, 14));
        bookField.setPreferredSize(new Dimension(400, 32));

        JLabel bookLabel = new JLabel("Book Name:");
        bookLabel.setForeground(Color.WHITE);
        bookLabel.setFont(new Font("Arial", Font.BOLD, 13));

        row1.add(bookLabel);
        row1.add(bookField);
        south.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        row2.setBackground(new Color(30, 30, 30));

        JButton addBtn    = makeBtn("➕ Add Book",    new Color(0, 180, 100));
        JButton deleteBtn = makeBtn("🗑 Delete",       new Color(200, 60, 60));
        JButton searchBtn = makeBtn("🔍 Search",       new Color(0, 140, 220));
        JButton issueBtn  = makeBtn("📤 Issue Book",   new Color(180, 80, 255));
        JButton backBtn   = makeBtn("⬅ Back to Login", new Color(255, 120, 0));

        row2.add(addBtn);
        row2.add(deleteBtn);
        row2.add(searchBtn);
        row2.add(issueBtn);
        row2.add(backBtn);
        south.add(row2);

        add(south, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            String book = bookField.getText().trim();
            if (!book.isEmpty()) {
                books.add(book);
                model.addElement(book);
                bookField.setText("");
            }
        });

        deleteBtn.addActionListener(e -> {
            int index = bookList.getSelectedIndex();
            if (index != -1) {
                books.remove(index);
                model.remove(index);
            }
        });

        searchBtn.addActionListener(e -> {
            String key = bookField.getText().trim().toLowerCase();
            model.clear();
            for (String b : books) {
                if (b.toLowerCase().contains(key)) model.addElement(b);
            }
        });

        issueBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Student ID:"));
                Student s = AppController.manager.searchStudent(id);
                if (s == null) {
                    JOptionPane.showMessageDialog(null, "Student Not Found");
                    return;
                }
                String book = JOptionPane.showInputDialog("Book Name:");
                if (book == null || book.isEmpty()) return;
                String date = java.time.LocalDate.now().toString();
                String info = "ID: " + s.id + " | Name: " + s.name + " | Dept: " + s.dept + " | Book: " + book + " | Date: " + date;
                issuedBooks.add(info);
                model.addElement(info);
                JOptionPane.showMessageDialog(null, "✅ Book Issued Successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Invalid Input");
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new ModernLoginUI(AppController.manager);
        });

        setVisible(true);
    }

    JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(140, 34));
        return b;
    }
}