import javax.swing.*;
import java.awt.*;
import java.io.*;

public class AnnualCalendarPanel extends JFrame {
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    String FILE = "calendar.txt";

    AnnualCalendarPanel(){
        setTitle("Annual Calendar");
        setSize(750,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("📅 ANNUAL CALENDAR", SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(160,80,0));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        list.setFont(new Font("Consolas",Font.PLAIN,14));
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        JButton addEvent = btn("➕ Add Event", new Color(0,180,100));
        JButton delEvent = btn("🗑 Delete Event", new Color(220,60,60));
        JButton back = DashboardButtons.backButton(this);

        bottom.add(addEvent);
        bottom.add(delEvent);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        loadEvents();

        addEvent.addActionListener(e -> addEvent());
        delEvent.addActionListener(e -> deleteEvent());

        setVisible(true);
    }

    void addEvent(){
        String date = JOptionPane.showInputDialog("Event Date (YYYY-MM-DD):");
        if(date==null) return;
        String desc = JOptionPane.showInputDialog("Event Description:");
        if(desc==null) return;

        String[] types = {"Holiday","Exam","Class","Meeting","Other"};
        String type = (String) JOptionPane.showInputDialog(null,"Event Type","Select",
                JOptionPane.QUESTION_MESSAGE,null,types,types[0]);
        if(type==null) return;

        String entry = date+" | "+type+" | "+desc;
        model.addElement(entry);
        saveEvents();
    }

    void deleteEvent(){
        int idx = list.getSelectedIndex();
        if(idx==-1){ JOptionPane.showMessageDialog(this,"Select an event"); return; }
        model.remove(idx);
        saveEvents();
    }

    void saveEvents(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            for(int i=0;i<model.size();i++){ bw.write(model.get(i)); bw.newLine(); }
            bw.close();
        }catch(Exception e){ System.out.println("Calendar Save Error"); }
    }

    void loadEvents(){
        try{
            File f = new File(FILE);
            if(!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null) model.addElement(line);
            br.close();
        }catch(Exception e){ System.out.println("Calendar Load Error"); }
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