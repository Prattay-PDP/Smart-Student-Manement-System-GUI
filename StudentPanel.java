import javax.swing.*;
import java.awt.*;

public class StudentPanel extends JFrame {

    Student s;
    StudentManager manager;

    public StudentPanel(Student s, StudentManager manager) {
        this.s = s;
        this.manager = manager;

        setTitle("Student Dashboard");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("🎓  STUDENT DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        top.add(title);

        add(top, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 15));

        area.setText(
                "📢 NOTICE BOARD\n\n" +
                NoticeBoard.notice +
                "\n\n====================================\n\n" +
                "👨‍🎓 STUDENT INFORMATION\n\n" +
                "ID        : " + s.id           + "\n" +
                "Name      : " + s.name         + "\n" +
                "Age       : " + s.age          + "\n" +
                "Department: " + s.dept         + "\n" +
                "Semester  : " + s.semester     + "\n" +
                "Section   : " + s.section      + "\n\n" +
                "CGPA      : " + s.cgpa         + "\n" +
                "Attendance: " + String.format("%.2f", s.getAttPer()) + "%\n" +
                "Due Fee   : " + s.dueFee()     + "\n" +
                "Status    : " + s.status       + "\n"
        );

        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(20, 20, 20));
        bottom.add(DashboardButtons.backButton(this));
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }
}