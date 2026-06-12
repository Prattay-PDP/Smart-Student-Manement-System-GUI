import java.io.*;

public class NoticeBoard {
    static String FILE = "notice.txt";
    static String notice = "📢 Welcome To USTC Student Management System";

    static void loadNotice() {
        try {
            File f = new File(FILE);
            if (!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            if (line != null) notice = line;
            br.close();
        } catch (Exception e) { System.out.println("Notice Load Error"); }
    }

    static void saveNotice(String msg) {
        notice = msg;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE));
            bw.write(msg);
            bw.close();
        } catch (Exception e) { System.out.println("Notice Save Error"); }
    }
}