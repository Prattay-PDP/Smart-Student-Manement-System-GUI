import java.io.*;
import java.util.*;

public class StudentManager {
    ArrayList<Student> list = new ArrayList<>();
    String FILE = "students.txt";

    StudentManager() {
        loadFile();
    }

    void addStudent(Student s) {
        list.add(s);
        saveFile();
    }

    void deleteStudent(int id) {
        list.removeIf(s -> s.id == id);
        saveFile();
    }

    Student searchStudent(int id) {
        for (Student s : list) if (s.id == id) return s;
        return null;
    }

    Student login(String input, String pass) {
        for (Student s : list) {
            if ((s.mobile.equals(input) || s.email.equals(input)) && s.pass.equals(pass)) return s;
        }
        return null;
    }

    int totalStudents() { return list.size(); }

    double totalDue() {
        double sum = 0;
        for (Student s : list) sum += s.dueFee();
        return sum;
    }

    double avgCGPA() {
        if (list.isEmpty()) return 0;
        double sum = 0;
        for (Student s : list) sum += s.cgpa;
        return sum / list.size();
    }

    int lowAttendance() {
        int c = 0;
        for (Student s : list) if (s.getAttPer() < 75) c++;
        return c;
    }

    Student topper() {
        if (list.isEmpty()) return null;
        Student top = list.get(0);
        for (Student s : list) if (s.cgpa > top.cgpa) top = s;
        return top;
    }

    void saveFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            for (Student s : list) {
                bw.write(s.id+","+s.name+","+s.age+","+s.mobile+","+s.email+","+s.pass+","+
                         s.dept+","+s.semester+","+s.section+","+s.cgpa+","+
                         s.totalClass+","+s.attended+","+s.totalFee+","+s.paidFee);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) { System.out.println("Save Error"); }
    }

    void loadFile() {
        try {
            File f = new File(FILE);
            if (!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                Student s = new Student(
                        Integer.parseInt(d[0]), d[1], Integer.parseInt(d[2]), d[3], d[4], d[5],
                        d[6], d[7], d[8], Double.parseDouble(d[9]),
                        Integer.parseInt(d[10]), Integer.parseInt(d[11]),
                        Double.parseDouble(d[12]), Double.parseDouble(d[13]));
                list.add(s);
            }
            br.close();
        } catch (Exception e) { System.out.println("Load Error"); }
    }
}