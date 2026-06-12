public class Student {
    int id;
    String name;
    int age;
    String mobile;
    String email;
    String pass;
    String dept;
    String semester;
    String section;
    double cgpa;
    int totalClass;
    int attended;
    double totalFee;
    double paidFee;
    String status;

    public Student(int id, String name, int age, String mobile, String email, String pass,
                   String dept, String semester, String section, double cgpa,
                   int totalClass, int attended, double totalFee, double paidFee) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.mobile = mobile;
        this.email = email;
        this.pass = pass;
        this.dept = dept;
        this.semester = semester;
        this.section = section;
        this.cgpa = cgpa;
        this.totalClass = totalClass;
        this.attended = attended;
        this.totalFee = totalFee;
        this.paidFee = paidFee;

        if (cgpa >= 3.5 && getAttPer() >= 80) {
            status = "Excellent";
        } else if (getAttPer() < 75 || dueFee() > 5000) {
            status = "Warning";
        } else {
            status = "Normal";
        }
    }

    double getAttPer() {
        if (totalClass == 0) return 0;
        return (attended * 100.0) / totalClass;
    }

    double dueFee() {
        return totalFee - paidFee;
    }
}