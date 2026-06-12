import javax.swing.*;
import java.awt.*;
import java.io.*;

public class OnlineCoursePanel extends JFrame {
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> courseList = new JList<>(model);
    String FILE = "courses.txt";
    boolean editable;

    OnlineCoursePanel(boolean editable, StudentManager sm, TeacherManager tm){
        this.editable = editable;

        setTitle("Online Courses" + (editable ? " (Edit Mode)" : " (View Only)"));
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("💻 ONLINE COURSE MANAGEMENT" + (editable ? "" : " - VIEW ONLY"), SwingConstants.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setOpaque(true);
        title.setBackground(new Color(0,100,180));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0,60));
        add(title, BorderLayout.NORTH);

        courseList.setFont(new Font("Consolas",Font.PLAIN,14));
        add(new JScrollPane(courseList), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        bottom.setBackground(new Color(30,30,30));

        if(editable){
            JButton addBtn = btn("➕ Add Course", new Color(0,180,100));
            JButton delBtn = btn("🗑 Delete Course", new Color(220,60,60));
            addBtn.addActionListener(e -> addCourse());
            delBtn.addActionListener(e -> deleteCourse());
            bottom.add(addBtn);
            bottom.add(delBtn);
        }

        // Enroll & View Enrollments available to everyone
        JButton enrollBtn = btn("📝 Enroll Student", new Color(180,80,255));
        JButton viewEnrollBtn = btn("👥 View Enrollments", new Color(0,140,220));
        enrollBtn.addActionListener(e -> enrollStudent());
        viewEnrollBtn.addActionListener(e -> viewEnrollments());
        bottom.add(enrollBtn);
        bottom.add(viewEnrollBtn);

        JButton back;
        if(editable){
            back = DashboardButtons.backToSuperAdmin(this, sm, tm);
        } else {
            back = DashboardButtons.backButton(this);
        }
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        loadCourses();
        setVisible(true);
    }

    void addCourse(){
        JTextField nameField = new JTextField(20);
        JTextField instField = new JTextField(20);
        JTextField feeField = new JTextField(10);
        JPanel p = new JPanel(new GridLayout(3,2,5,5));
        p.add(new JLabel("Course Name:")); p.add(nameField);
        p.add(new JLabel("Instructor:")); p.add(instField);
        p.add(new JLabel("Fee:")); p.add(feeField);

        int res = JOptionPane.showConfirmDialog(this,p,"Add Course",JOptionPane.OK_CANCEL_OPTION);
        if(res!=JOptionPane.OK_OPTION) return;

        String entry = nameField.getText()+" | Instructor: "+instField.getText()+" | Fee: "+feeField.getText();
        model.addElement(entry);
        saveCourses();
    }

    void deleteCourse(){
        int idx = courseList.getSelectedIndex();
        if(idx==-1){ JOptionPane.showMessageDialog(this,"Select a course first"); return; }
        model.remove(idx);
        saveCourses();
    }

    void enrollStudent(){
        if(courseList.getSelectedIndex()==-1){ JOptionPane.showMessageDialog(this,"Select a course first"); return; }
        String course = model.get(courseList.getSelectedIndex());
        String idStr = JOptionPane.showInputDialog("Enter Student ID:");
        if(idStr==null) return;
        try{
            int id = Integer.parseInt(idStr.trim());
            Student s = AppController.manager.searchStudent(id);
            if(s==null){ JOptionPane.showMessageDialog(this,"Student Not Found"); return; }

            BufferedWriter bw = new BufferedWriter(new FileWriter("enrollments.txt", true));
            bw.write(s.id+","+s.name+","+course+","+java.time.LocalDate.now());
            bw.newLine();
            bw.close();
            JOptionPane.showMessageDialog(this,"✅ Enrolled: "+s.name+" -> "+course);
        }catch(Exception ex){ JOptionPane.showMessageDialog(this,"Error: Invalid Input"); }
    }

    void viewEnrollments(){
        StringBuilder sb = new StringBuilder();
        try{
            File f = new File("enrollments.txt");
            if(!f.exists()){ JOptionPane.showMessageDialog(this,"No enrollments yet"); return; }
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null){
                String[] d = line.split(",");
                sb.append("ID: ").append(d[0]).append(" | Name: ").append(d[1])
                  .append(" | Course: ").append(d[2]).append(" | Date: ").append(d.length>3?d[3]:"").append("\n");
            }
            br.close();
        }catch(Exception e){ sb.append("Error reading enrollments"); }

        JTextArea area = new JTextArea(sb.toString(),20,50);
        area.setEditable(false);
        area.setFont(new Font("Consolas",Font.PLAIN,13));
        JDialog d = new JDialog(this,"Enrollments",true);
        d.setSize(700,450);
        d.setLocationRelativeTo(this);
        d.add(new JScrollPane(area));
        d.setVisible(true);
    }

    void saveCourses(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            for(int i=0;i<model.size();i++){
                bw.write(model.get(i));
                bw.newLine();
            }
            bw.close();
        }catch(Exception e){ System.out.println("Course Save Error"); }
    }

    void loadCourses(){
        model.clear();
        try{
            File f = new File(FILE);
            if(!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while((line=br.readLine())!=null) model.addElement(line);
            br.close();
        }catch(Exception e){ System.out.println("Course Load Error"); }
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