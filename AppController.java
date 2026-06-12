public class AppController {
    static StudentManager manager = new StudentManager();
    static TeacherManager teacherManager = new TeacherManager();
    static ModernLoginUI loginUI;

    public static void start() {
        loginUI = new ModernLoginUI(manager);
    }

    public static void goToLogin() {
        loginUI = new ModernLoginUI(manager);
    }
}