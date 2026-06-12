import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FeesPanel extends JFrame {
    StudentManager manager;
    JTextArea area = new JTextArea(20,55);
    JTextField idField = new JTextField(8);

    FeesPanel(StudentManager manager){
        this.manager = manager;
        setTitle("Fees Collection");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("💵 FEES COLLECTION", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(0,120,80));
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

        JButton search = btn("🔍 Search", new Color(0,140,220));
        JButton collect = btn("💰 Collect Fee", new Color(0,180,100));
        JButton history = btn("📜 View History", new Color(180,80,255));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(idLabel);
        bottom.add(idField);
        bottom.add(search);
        bottom.add(collect);
        bottom.add(history);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        search.addActionListener(e -> searchStudent());
        collect.addActionListener(e -> collectFee());
        history.addActionListener(e -> viewHistory());

        setVisible(true);
    }

    void searchStudent(){
        try{
            int id = Integer.parseInt(idField.getText().trim());
            Student s = manager.searchStudent(id);
            if(s==null){ area.setText("❌ Student Not Found"); return; }
            area.setText(
                "ID         : "+s.id+"\n"+
                "Name       : "+s.name+"\n"+
                "Department : "+s.dept+"\n\n"+
                "Total Fee  : "+s.totalFee+"\n"+
                "Paid Fee   : "+s.paidFee+"\n"+
                "Due Fee    : "+s.dueFee()+"\n"
            );
        }catch(Exception ex){ area.setText("❌ Invalid ID"); }
    }

    void collectFee(){
        try{
            int id = Integer.parseInt(idField.getText().trim());
            Student s = manager.searchStudent(id);
            if(s==null){ JOptionPane.showMessageDialog(null,"Student Not Found"); return; }

            String amt = JOptionPane.showInputDialog("Enter Amount Collected:");
            if(amt==null) return;
            double tk = Double.parseDouble(amt);

            String[] methods = {"Cash","Bank","Mobile Banking","Card"};
            String method = (String) JOptionPane.showInputDialog(null,"Payment Method","Select Method",
                    JOptionPane.QUESTION_MESSAGE,null,methods,methods[0]);
            if(method==null) return;

            s.paidFee += tk;
            manager.saveFile();

            saveFeeRecord(s.id, s.name, tk, method);

            area.setText(
                "✅ FEE COLLECTED\n\n"+
                "ID       : "+s.id+"\n"+
                "Name     : "+s.name+"\n"+
                "Amount   : "+tk+"\n"+
                "Method   : "+method+"\n\n"+
                "Total Fee: "+s.totalFee+"\n"+
                "Paid Fee : "+s.paidFee+"\n"+
                "Due Fee  : "+s.dueFee()
            );
            JOptionPane.showMessageDialog(null,"✅ Fee Collected Successfully");
        }catch(Exception ex){ JOptionPane.showMessageDialog(null,"❌ Invalid Input"); }
    }

    void saveFeeRecord(int id, String name, double amt, String method){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter("fees_history.txt", true));
            String date = java.time.LocalDate.now().toString();
            bw.write(id+","+name+","+amt+","+method+","+date);
            bw.newLine();
            bw.close();
        }catch(Exception e){ System.out.println("Fee Save Error"); }
    }

    void viewHistory(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-20s %-10s %-15s %-12s\n","ID","NAME","AMOUNT","METHOD","DATE"));
        sb.append("=".repeat(70)).append("\n");
        try{
            File f = new File("fees_history.txt");
            if(!f.exists()){ area.setText("No fee history found."); return; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length<5) continue;
                sb.append(String.format("%-6s %-20s %-10s %-15s %-12s\n",d[0],d[1],d[2],d[3],d[4]));
            }
            br.close();
        }catch(Exception e){ sb.append("Error reading history"); }
        area.setText(sb.toString());
    }

    JButton btn(String text, Color color){
        JButton b = new JButton(text);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial",Font.BOLD,13));
        return b;
    }
}