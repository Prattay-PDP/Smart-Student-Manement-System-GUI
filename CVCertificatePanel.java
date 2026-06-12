import javax.swing.*;
import java.awt.*;
import java.io.*;

public class CVCertificatePanel extends JFrame {
    StudentManager manager;
    JTextArea area = new JTextArea(22,60);
    JTextField idField = new JTextField(8);

    CVCertificatePanel(StudentManager manager){
        this.manager = manager;
        setTitle("Student CV & Certificates");
        setSize(800,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📄 STUDENT CV & CERTIFICATES", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(0,90,140));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        area.setEditable(false);
        area.setFont(new Font("Consolas",Font.PLAIN,13));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setForeground(Color.WHITE);

        JButton generateCV = btn("📄 Generate CV", new Color(0,180,100));
        JButton issueCert = btn("🎓 Issue Certificate", new Color(180,80,255));
        JButton viewCerts = btn("📜 View Certificates", new Color(0,140,220));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(idLabel);
        bottom.add(idField);
        bottom.add(generateCV);
        bottom.add(issueCert);
        bottom.add(viewCerts);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        generateCV.addActionListener(e -> generateCV());
        issueCert.addActionListener(e -> issueCertificate());
        viewCerts.addActionListener(e -> viewCertificates());

        setVisible(true);
    }

    void generateCV(){
        try{
            int id = Integer.parseInt(idField.getText().trim());
            Student s = manager.searchStudent(id);
            if(s==null){ area.setText("❌ Student Not Found"); return; }

            area.setText(
                "================================================\n"+
                "                CURRICULUM VITAE\n"+
                "================================================\n\n"+
                "Name        : "+s.name+"\n"+
                "Student ID  : "+s.id+"\n"+
                "Age         : "+s.age+"\n"+
                "Department  : "+s.dept+"\n"+
                "Semester    : "+s.semester+"\n"+
                "Section     : "+s.section+"\n\n"+
                "Contact\n"+
                "-------\n"+
                "Mobile      : "+s.mobile+"\n"+
                "Email       : "+s.email+"\n\n"+
                "Academic Performance\n"+
                "--------------------\n"+
                "CGPA        : "+s.cgpa+"\n"+
                "Attendance  : "+String.format("%.2f",s.getAttPer())+"%\n"+
                "Status      : "+s.status+"\n\n"+
                "Institution : University of Science and Technology Chittagong (USTC)\n"+
                "================================================\n"
            );
        }catch(Exception ex){ area.setText("❌ Invalid ID"); }
    }

    void issueCertificate(){
        try{
            int id = Integer.parseInt(idField.getText().trim());
            Student s = manager.searchStudent(id);
            if(s==null){ JOptionPane.showMessageDialog(null,"Student Not Found"); return; }

            String[] types = {"Course Completion","Merit Certificate","Participation","Achievement","Character Certificate"};
            String type = (String) JOptionPane.showInputDialog(null,"Certificate Type","Select",
                    JOptionPane.QUESTION_MESSAGE,null,types,types[0]);
            if(type==null) return;

            String reason = JOptionPane.showInputDialog("Reason / Description:");
            if(reason==null) reason="";

            BufferedWriter bw = new BufferedWriter(new FileWriter("certificates.txt", true));
            bw.write(s.id+","+s.name+","+type+","+reason+","+java.time.LocalDate.now());
            bw.newLine();
            bw.close();

            area.setText(
                "================================================\n"+
                "                  CERTIFICATE\n"+
                "================================================\n\n"+
                "This is to certify that\n\n"+
                "    "+s.name+" (ID: "+s.id+")\n\n"+
                "Department: "+s.dept+"\n\n"+
                "has been awarded:\n"+
                "    "+type+"\n\n"+
                "Reason: "+reason+"\n\n"+
                "Date: "+java.time.LocalDate.now()+"\n"+
                "Issued by: USTC Administration\n"+
                "================================================\n"
            );

            JOptionPane.showMessageDialog(null,"✅ Certificate Issued");
        }catch(Exception ex){ JOptionPane.showMessageDialog(null,"❌ Invalid Input"); }
    }

    void viewCertificates(){
        StringBuilder sb = new StringBuilder();
        try{
            File f = new File("certificates.txt");
            if(!f.exists()){ area.setText("No certificates issued yet"); return; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            sb.append(String.format("%-6s %-20s %-25s %-20s %-12s\n","ID","NAME","TYPE","REASON","DATE"));
            sb.append("=".repeat(90)).append("\n");
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length>=5)
                    sb.append(String.format("%-6s %-20s %-25s %-20s %-12s\n",d[0],d[1],d[2],d[3],d[4]));
            }
            br.close();
        }catch(Exception e){ sb.append("Error reading certificates"); }
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