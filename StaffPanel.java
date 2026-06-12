import javax.swing.*;
import java.awt.*;
import java.io.*;

public class StaffPanel extends JFrame {
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    String FILE = "staff.txt";

    StaffPanel(){
        setTitle("Human Resources - Staff");
        setSize(850,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("🧑‍💼 HUMAN RESOURCES - STAFF MANAGEMENT", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,22));
        title.setOpaque(true);
        title.setBackground(new Color(60,60,140));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        list.setFont(new Font("Consolas",Font.PLAIN,13));
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        JButton addStaff = btn("➕ Add Staff", new Color(0,180,100));
        JButton delStaff = btn("🗑 Delete Staff", new Color(220,60,60));
        JButton markAtt = btn("✅ Mark Attendance", new Color(0,140,220));
        JButton viewAtt = btn("📊 View Attendance", new Color(180,80,255));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(addStaff);
        bottom.add(delStaff);
        bottom.add(markAtt);
        bottom.add(viewAtt);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        loadStaff();

        addStaff.addActionListener(e -> addStaff());
        delStaff.addActionListener(e -> deleteStaff());
        markAtt.addActionListener(e -> markAttendance());
        viewAtt.addActionListener(e -> viewAttendance());

        setVisible(true);
    }

    void addStaff(){
        JTextField nameField = new JTextField(15);
        JTextField roleField = new JTextField(15);
        JTextField mobileField = new JTextField(15);
        JTextField salaryField = new JTextField(15);

        JPanel p = new JPanel(new GridLayout(4,2,5,5));
        p.add(new JLabel("Name:")); p.add(nameField);
        p.add(new JLabel("Role/Position:")); p.add(roleField);
        p.add(new JLabel("Mobile:")); p.add(mobileField);
        p.add(new JLabel("Salary:")); p.add(salaryField);

        int res = JOptionPane.showConfirmDialog(this,p,"Add Staff",JOptionPane.OK_CANCEL_OPTION);
        if(res!=JOptionPane.OK_OPTION) return;

        int id = model.size()+1;
        String entry = id+" | "+nameField.getText()+" | "+roleField.getText()+" | "+mobileField.getText()+" | Salary: "+salaryField.getText();
        model.addElement(entry);
        saveStaff();
    }

    void deleteStaff(){
        int idx = list.getSelectedIndex();
        if(idx==-1){ JOptionPane.showMessageDialog(this,"Select a staff member"); return; }
        model.remove(idx);
        saveStaff();
    }

    void markAttendance(){
        if(list.getSelectedIndex()==-1){ JOptionPane.showMessageDialog(this,"Select a staff member"); return; }
        String staff = model.get(list.getSelectedIndex());

        String[] status = {"Present","Absent","Leave","Late"};
        String st = (String) JOptionPane.showInputDialog(null,"Attendance Status","Select",
                JOptionPane.QUESTION_MESSAGE,null,status,status[0]);
        if(st==null) return;

        try{
            String[] parts = staff.split("\\|");
            BufferedWriter bw = new BufferedWriter(new FileWriter("staff_attendance.txt", true));
            bw.write(parts[0].trim()+","+parts[1].trim()+","+st+","+java.time.LocalDate.now());
            bw.newLine();
            bw.close();
            JOptionPane.showMessageDialog(this,"✅ Attendance Marked: "+st);
        }catch(Exception e){ JOptionPane.showMessageDialog(this,"Error saving attendance"); }
    }

    void viewAttendance(){
        StringBuilder sb = new StringBuilder();
        try{
            File f = new File("staff_attendance.txt");
            if(!f.exists()){ JOptionPane.showMessageDialog(this,"No attendance records"); return; }
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

        JTextArea area = new JTextArea(sb.toString(),20,55);
        area.setEditable(false);
        area.setFont(new Font("Consolas",Font.PLAIN,13));
        JDialog d = new JDialog(this,"Staff Attendance",true);
        d.setSize(600,450);
        d.setLocationRelativeTo(this);
        d.add(new JScrollPane(area));
        d.setVisible(true);
    }

    void saveStaff(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            for(int i=0;i<model.size();i++){ bw.write(model.get(i)); bw.newLine(); }
            bw.close();
        }catch(Exception e){ System.out.println("Staff Save Error"); }
    }

    void loadStaff(){
        try{
            File f = new File(FILE);
            if(!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null) model.addElement(line);
            br.close();
        }catch(Exception e){ System.out.println("Staff Load Error"); }
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