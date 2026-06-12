import javax.swing.*;
import java.awt.*;
import java.io.*;

public class FinancePanel extends JFrame {
    JTextArea area = new JTextArea(20,60);

    FinancePanel(){
        setTitle("Income & Expense");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("💰 INCOME & EXPENSE MANAGEMENT", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(0,100,60));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        area.setEditable(false);
        area.setFont(new Font("Consolas",Font.PLAIN,14));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        JButton addIncome = btn("➕ Add Income", new Color(0,180,100));
        JButton addExpense = btn("➖ Add Expense", new Color(220,60,60));
        JButton viewBtn = btn("📊 View Report", new Color(0,140,220));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(addIncome);
        bottom.add(addExpense);
        bottom.add(viewBtn);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        addIncome.addActionListener(e -> addEntry("INCOME","income.txt"));
        addExpense.addActionListener(e -> addEntry("EXPENSE","expense.txt"));
        viewBtn.addActionListener(e -> viewReport());

        setVisible(true);
    }

    void addEntry(String type, String file){
        String title2 = JOptionPane.showInputDialog("Enter "+type+" Title:");
        if(title2==null) return;
        String amount = JOptionPane.showInputDialog("Enter Amount:");
        if(amount==null) return;

        try{
            double amt = Double.parseDouble(amount);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(title2+","+amt+","+java.time.LocalDate.now());
            bw.newLine();
            bw.close();
            JOptionPane.showMessageDialog(null,"✅ "+type+" Added");
        }catch(Exception ex){ JOptionPane.showMessageDialog(null,"❌ Invalid Amount"); }
    }

    void viewReport(){
        StringBuilder sb = new StringBuilder();
        double totalIncome, totalExpense;

        sb.append("===== INCOME =====\n");
        totalIncome = sumFile("income.txt", sb);

        sb.append("\n===== EXPENSE =====\n");
        totalExpense = sumFile("expense.txt", sb);

        sb.append("\n=====================\n");
        sb.append(String.format("Total Income  : %.2f\n", totalIncome));
        sb.append(String.format("Total Expense : %.2f\n", totalExpense));
        sb.append(String.format("Net Balance   : %.2f\n", totalIncome-totalExpense));

        area.setText(sb.toString());
    }

    double sumFile(String filename, StringBuilder sb){
        double total=0;
        try{
            File f = new File(filename);
            if(!f.exists()){ sb.append("No records.\n"); return 0; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                if(d.length>=3){
                    sb.append(String.format("%-25s %-10s %-12s\n",d[0],d[1],d[2]));
                    total += Double.parseDouble(d[1]);
                }
            }
            br.close();
        }catch(Exception e){ sb.append("Error reading file\n"); }
        return total;
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