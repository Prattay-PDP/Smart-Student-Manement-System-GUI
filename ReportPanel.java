import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ReportPanel extends JFrame {
    StudentManager manager;
    JTextArea area = new JTextArea(25,65);

    ReportPanel(StudentManager manager){
        this.manager = manager;
        setTitle("Reports");
        setSize(850,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📈 SYSTEM REPORTS", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(0,60,120));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        area.setEditable(false);
        area.setFont(new Font("Consolas",Font.PLAIN,13));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        JButton studentReport = btn("👥 Student Report", new Color(0,180,100));
        JButton feeReport = btn("💰 Fee Report", new Color(255,160,0));
        JButton financeReport = btn("📊 Finance Report", new Color(0,140,220));
        JButton attendanceReport = btn("📅 Attendance Report", new Color(180,80,255));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(studentReport);
        bottom.add(feeReport);
        bottom.add(financeReport);
        bottom.add(attendanceReport);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        studentReport.addActionListener(e -> studentReport());
        feeReport.addActionListener(e -> feeReport());
        financeReport.addActionListener(e -> financeReport());
        attendanceReport.addActionListener(e -> attendanceReport());

        setVisible(true);
    }

    void studentReport(){
        StringBuilder sb = new StringBuilder();
        sb.append("================ STUDENT REPORT ================\n\n");
        sb.append("Total Students  : "+manager.totalStudents()+"\n");
        sb.append(String.format("Average CGPA    : %.2f\n",manager.avgCGPA()));
        sb.append("Low Attendance  : "+manager.lowAttendance()+" students\n");
        if(manager.topper()!=null)
            sb.append("Top Student     : "+manager.topper().name+" (CGPA: "+manager.topper().cgpa+")\n");
        sb.append("\n");
        sb.append(String.format("%-6s %-20s %-12s %-8s %-8s %-8s\n","ID","NAME","DEPT","CGPA","ATT%","STATUS"));
        sb.append("=".repeat(70)).append("\n");
        for(Student s: manager.list){
            sb.append(String.format("%-6d %-20s %-12s %-8.2f %-8.1f %-8s\n",
                    s.id,s.name,s.dept,s.cgpa,s.getAttPer(),s.status));
        }
        area.setText(sb.toString());
    }

    void feeReport(){
        StringBuilder sb = new StringBuilder();
        sb.append("================ FEE REPORT ================\n\n");
        sb.append(String.format("Total Due (all students) : %.2f\n\n",manager.totalDue()));
        sb.append(String.format("%-6s %-20s %-10s %-10s %-10s\n","ID","NAME","TOTAL","PAID","DUE"));
        sb.append("=".repeat(60)).append("\n");
        for(Student s: manager.list){
            sb.append(String.format("%-6d %-20s %-10.0f %-10.0f %-10.0f\n",
                    s.id,s.name,s.totalFee,s.paidFee,s.dueFee()));
        }
        area.setText(sb.toString());
    }

    void financeReport(){
        StringBuilder sb = new StringBuilder();
        sb.append("================ FINANCE REPORT ================\n\n");
        double inc = readSum("income.txt", sb, "INCOME");
        double exp = readSum("expense.txt", sb, "EXPENSE");
        sb.append("\n");
        sb.append(String.format("Total Income  : %.2f\n",inc));
        sb.append(String.format("Total Expense : %.2f\n",exp));
        sb.append(String.format("Net Balance   : %.2f\n",inc-exp));
        area.setText(sb.toString());
    }

    double readSum(String filename, StringBuilder sb, String label){
        double total=0;
        sb.append("--- "+label+" ---\n");
        try{
            File f = new File(filename);
            if(!f.exists()){ sb.append("No records.\n"); return 0; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length>=2){
                    sb.append(d[0]+" : "+d[1]+"\n");
                    total += Double.parseDouble(d[1]);
                }
            }
            br.close();
        }catch(Exception e){ sb.append("Error reading file\n"); }
        return total;
    }

    void attendanceReport(){
        StringBuilder sb = new StringBuilder();
        sb.append("================ STAFF ATTENDANCE REPORT ================\n\n");
        try{
            File f = new File("staff_attendance.txt");
            if(!f.exists()){ sb.append("No attendance records found."); area.setText(sb.toString()); return; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            sb.append(String.format("%-5s %-20s %-10s %-12s\n","ID","NAME","STATUS","DATE"));
            sb.append("=".repeat(55)).append("\n");
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length>=4)
                    sb.append(String.format("%-5s %-20s %-10s %-12s\n",d[0],d[1],d[2],d[3]));
            }
            br.close();
        }catch(Exception e){ sb.append("Error reading attendance"); }
        area.setText(sb.toString());
    }

    JButton btn(String text, Color color){
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial",Font.BOLD,12));
        return b;
    }
}