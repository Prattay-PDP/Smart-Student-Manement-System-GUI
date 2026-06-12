import javax.swing.*;
import java.awt.*;

public class ParentPanel extends JFrame {

    Student s;

    public ParentPanel(Student s) {
        this.s = s;

        setTitle("Parent Dashboard");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("👨‍👩‍👧  PARENT DASHBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setOpaque(true);
        title.setBackground(new Color(30, 30, 30));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0, 65));
        add(title, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 15));

        area.setText(
                "📢 NOTICE\n\n" +
                NoticeBoard.notice +
                "\n\n========================\n\n" +
                "👨‍🎓 STUDENT INFORMATION\n\n" +
                "ID         : " + s.id                                         + "\n" +
                "Name       : " + s.name                                       + "\n" +
                "Department : " + s.dept                                       + "\n" +
                "Semester   : " + s.semester                                   + "\n\n" +
                "CGPA       : " + s.cgpa                                       + "\n" +
                "Attendance : " + String.format("%.2f", s.getAttPer()) + "%\n\n" +
                "Total Fee  : " + s.totalFee                                   + "\n" +
                "Paid Fee   : " + s.paidFee                                    + "\n" +
                "Due Fee    : " + s.dueFee()                                   + "\n\n" +
                "Status     : " + s.status
        );

        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(DashboardButtons.backButton(this));
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}