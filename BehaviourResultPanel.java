import javax.swing.*;
import java.awt.*;
import java.io.*;

public class BehaviourResultPanel extends JFrame {
    StudentManager manager;
    JTextArea area = new JTextArea(20,55);
    JTextField idField = new JTextField(8);

    BehaviourResultPanel(StudentManager manager){
        this.manager = manager;
        setTitle("Behaviour & Result");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📋 BEHAVIOUR & RESULT", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(120,0,160));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        area.setEditable(false);
        area.setFont(new Font("Consolas",Font.PLAIN,14));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setForeground(Color.WHITE);

        JButton addResult = btn("📝 Add Result", new Color(0,180,100));
        JButton addBehaviour = btn("⭐ Add Behaviour Note", new Color(255,160,0));
        JButton viewBtn = btn("👁 View Records", new Color(0,140,220));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(idLabel);
        bottom.add(idField);
        bottom.add(addResult);
        bottom.add(addBehaviour);
        bottom.add(viewBtn);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        addResult.addActionListener(e -> addResult());
        addBehaviour.addActionListener(e -> addBehaviour());
        viewBtn.addActionListener(e -> viewRecords());

        setVisible(true);
    }

    void addResult(){
        try{
            int id = Integer.parseInt(idField.getText().trim());
            Student s = manager.searchStudent(id);
            if(s==null){ JOptionPane.showMessageDialog(null,"Student Not Found"); return; }

            String subject = JOptionPane.showInputDialog("Subject Name:");
            if(subject==null) return;
            String marks = JOptionPane.showInputDialog("Marks Obtained:");
            if(marks==null) return;
            String grade = JOptionPane.showInputDialog("Grade:");
            if(grade==null) return;

            BufferedWriter bw = new BufferedWriter(new FileWriter("results.txt", true));
            bw.write(s.id+","+s.name+","+subject+","+marks+","+grade+","+java.time.LocalDate.now());
            bw.newLine();
            bw.close();

            JOptionPane.showMessageDialog(null,"✅ Result Added");
        }catch(Exception ex){ JOptionPane.showMessageDialog(null,"❌ Invalid Input"); }
    }

    void addBehaviour(){
        try{
            int id = Integer.parseInt(idField.getText().trim());
            Student s = manager.searchStudent(id);
            if(s==null){ JOptionPane.showMessageDialog(null,"Student Not Found"); return; }

            String[] ratings = {"Excellent","Good","Average","Needs Improvement","Poor"};
            String rating = (String) JOptionPane.showInputDialog(null,"Behaviour Rating","Select",
                    JOptionPane.QUESTION_MESSAGE,null,ratings,ratings[0]);
            if(rating==null) return;

            String note = JOptionPane.showInputDialog("Additional Note:");
            if(note==null) note="";

            BufferedWriter bw = new BufferedWriter(new FileWriter("behaviour.txt", true));
            bw.write(s.id+","+s.name+","+rating+","+note+","+java.time.LocalDate.now());
            bw.newLine();
            bw.close();

            JOptionPane.showMessageDialog(null,"✅ Behaviour Note Added");
        }catch(Exception ex){ JOptionPane.showMessageDialog(null,"❌ Invalid Input"); }
    }

    void viewRecords(){
        try{
            int id = Integer.parseInt(idField.getText().trim());
            StringBuilder sb = new StringBuilder();

            sb.append("===== RESULTS =====\n");
            readFile("results.txt", id, sb, new String[]{"ID","Name","Subject","Marks","Grade","Date"});

            sb.append("\n===== BEHAVIOUR =====\n");
            readFile("behaviour.txt", id, sb, new String[]{"ID","Name","Rating","Note","Date"});

            area.setText(sb.toString());
        }catch(Exception ex){ area.setText("❌ Invalid ID"); }
    }

    void readFile(String filename, int id, StringBuilder sb, String[] cols) throws IOException{
        File f = new File(filename);
        if(!f.exists()){ sb.append("No records.\n"); return; }
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line; boolean found=false;
        while((line=br.readLine())!=null){
            String[] d = line.split(",");
            if(d.length>0 && d[0].equals(String.valueOf(id))){
                found=true;
                for(int i=0;i<d.length;i++){
                    sb.append(cols[i]).append(": ").append(d[i]).append("  ");
                }
                sb.append("\n");
            }
        }
        br.close();
        if(!found) sb.append("No records found.\n");
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