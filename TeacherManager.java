import java.io.*;
import java.util.*;

public class TeacherManager {
    ArrayList<Teacher> list = new ArrayList<>();
    String FILE = "teachers.txt";

    TeacherManager() { loadFile(); }

    void addTeacher(Teacher t) { list.add(t); saveFile(); }

    void deleteTeacher(int id) { list.removeIf(t -> t.id == id); saveFile(); }

    Teacher searchTeacher(int id) {
        for (Teacher t : list) if (t.id == id) return t;
        return null;
    }

    Teacher login(String input, String pass) {
        for (Teacher t : list) {
            if ((t.mobile.equals(input) || t.email.equals(input)) && t.pass.equals(pass)) return t;
        }
        return null;
    }

    int totalTeachers() { return list.size(); }

    int nextId() {
        int max = 0;
        for (Teacher t : list) if (t.id > max) max = t.id;
        return max + 1;
    }

    void saveFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            for (Teacher t : list) {
                bw.write(t.id+","+t.name+","+t.dept+","+t.mobile+","+t.email+","+t.pass+","+t.subject);
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) { System.out.println("Teacher Save Error"); }
    }

    void loadFile() {
        try {
            File f = new File(FILE);
            if (!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(FILE));
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                Teacher t = new Teacher(Integer.parseInt(d[0]), d[1], d[2], d[3], d[4], d[5], d[6]);
                list.add(t);
            }
            br.close();
        } catch (Exception e) { System.out.println("Teacher Load Error"); }
    }
}