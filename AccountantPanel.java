import javax.swing.*;
import java.awt.*;

public class AccountantPanel extends JFrame {

    StudentManager manager;

    AccountantPanel(StudentManager manager) {
        this.manager = manager;

        setTitle("Accountant Dashboard");
        setSize(880, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("💰  ACCOUNTANT PANEL", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setOpaque(true);
        title.setBackground(new Color(100, 0, 160));
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

        JButton search = makeBtn("🔍 Search",       new Color(0, 140, 220));
        JButton pay    = makeBtn("💵 Add Payment",  new Color(0, 180, 100));
        JButton back   = DashboardButtons.backButton(this);

        bottom.add(idLabel);
        bottom.add(idField);
        bottom.add(search);
        bottom.add(pay);
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
                        "=================================\n" +
                        "        STUDENT ACCOUNT\n" +
                        "=================================\n\n" +
                        "ID         : " + s.id       + "\n" +
                        "Name       : " + s.name     + "\n" +
                        "Department : " + s.dept     + "\n\n" +
                        "Total Fee  : " + s.totalFee + "\n" +
                        "Paid Fee   : " + s.paidFee  + "\n" +
                        "Due Fee    : " + s.dueFee() + "\n"
                );
            } catch (Exception ex) {
                area.setText("❌ Enter Valid ID");
            }
        });

        pay.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                Student s = manager.searchStudent(id);

                if (s == null) {
                    JOptionPane.showMessageDialog(null, "Student Not Found");
                    return;
                }

                String amount = JOptionPane.showInputDialog("Enter Payment Amount:");
                if (amount == null) return;

                double tk = Double.parseDouble(amount);
                s.paidFee += tk;
                manager.saveFile();

                area.setText(
                        "=================================\n" +
                        "        PAYMENT UPDATED\n" +
                        "=================================\n\n" +
                        "ID         : " + s.id       + "\n" +
                        "Name       : " + s.name     + "\n\n" +
                        "Total Fee  : " + s.totalFee + "\n" +
                        "Paid Fee   : " + s.paidFee  + "\n" +
                        "Due Fee    : " + s.dueFee() + "\n"
                );

                JOptionPane.showMessageDialog(null, "✅ Payment Added Successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Invalid Input");
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