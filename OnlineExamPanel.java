import javax.swing.*;
import java.awt.*;
import java.io.*;

public class OnlineExamPanel extends JFrame {
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    String FILE = "exams.txt";

    OnlineExamPanel(){
        setTitle("Online Exam");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📝 ONLINE EXAM MANAGEMENT", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(0,80,160));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        list.setFont(new Font("Consolas",Font.PLAIN,14));
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        JButton createExam = btn("➕ Create Exam", new Color(0,180,100));
        JButton deleteExam = btn("🗑 Delete Exam", new Color(220,60,60));
        JButton submitMarks = btn("📤 Submit Marks", new Color(180,80,255));
        JButton viewResults = btn("📊 View Results", new Color(0,140,220));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(createExam);
        bottom.add(deleteExam);
        bottom.add(submitMarks);
        bottom.add(viewResults);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        loadExams();

        createExam.addActionListener(e -> createExam());
        deleteExam.addActionListener(e -> deleteExam());
        submitMarks.addActionListener(e -> submitMarks());
        viewResults.addActionListener(e -> viewResults());

        setVisible(true);
    }

    void createExam(){
        String name = JOptionPane.showInputDialog("Exam Name:");
        if(name==null) return;
        String date = JOptionPane.showInputDialog("Exam Date (YYYY-MM-DD):");
        if(date==null) return;
        String totalMarks = JOptionPane.showInputDialog("Total Marks:");
        if(totalMarks==null) return;

        String entry = name+" | Date: "+date+" | Total Marks: "+totalMarks;
        model.addElement(entry);
        saveExams();
        JOptionPane.showMessageDialog(this,"✅ Exam Created");
    }

    void deleteExam(){
        int idx = list.getSelectedIndex();
        if(idx==-1){ JOptionPane.showMessageDialog(this,"Select an exam"); return; }
        model.remove(idx);
        saveExams();
    }

    void submitMarks(){
        if(list.getSelectedIndex()==-1){ JOptionPane.showMessageDialog(this,"Select an exam"); return; }
        String exam = model.get(list.getSelectedIndex());

        String idStr = JOptionPane.showInputDialog("Student ID:");
        if(idStr==null) return;
        try{
            int id = Integer.parseInt(idStr.trim());
            Student s = AppController.manager.searchStudent(id);
            if(s==null){ JOptionPane.showMessageDialog(this,"Student Not Found"); return; }

            String marks = JOptionPane.showInputDialog("Marks Obtained:");
            if(marks==null) return;

            BufferedWriter bw = new BufferedWriter(new FileWriter("exam_results.txt", true));
            bw.write(s.id+","+s.name+","+exam+","+marks+","+java.time.LocalDate.now());
            bw.newLine();
            bw.close();
            JOptionPane.showMessageDialog(this,"✅ Marks Submitted");
        }catch(Exception ex){ JOptionPane.showMessageDialog(this,"❌ Invalid Input"); }
    }

    void viewResults(){
        StringBuilder sb = new StringBuilder();
        try{
            File f = new File("exam_results.txt");
            if(!f.exists()){ JOptionPane.showMessageDialog(this,"No results yet"); return; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            sb.append(String.format("%-6s %-20s %-30s %-8s %-12s\n","ID","NAME","EXAM","MARKS","DATE"));
            sb.append("=".repeat(90)).append("\n");
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length>=5)
                    sb.append(String.format("%-6s %-20s %-30s %-8s %-12s\n",d[0],d[1],d[2],d[3],d[4]));
            }
            br.close();
        }catch(Exception e){ sb.append("Error reading results"); }

        JTextArea area = new JTextArea(sb.toString(),20,80);
        area.setEditable(false);
        area.setFont(new Font("Consolas",Font.PLAIN,12));
        JDialog d = new JDialog(this,"Exam Results",true);
        d.setSize(800,450);
        d.setLocationRelativeTo(this);
        d.add(new JScrollPane(area));
        d.setVisible(true);
    }

    void saveExams(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            for(int i=0;i<model.size();i++){ bw.write(model.get(i)); bw.newLine(); }
            bw.close();
        }catch(Exception e){ System.out.println("Exam Save Error"); }
    }

    void loadExams(){
        try{
            File f = new File(FILE);
            if(!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null) model.addElement(line);
            br.close();
        }catch(Exception e){ System.out.println("Exam Load Error"); }
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