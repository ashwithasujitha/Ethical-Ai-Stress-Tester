package DesignPattern.MVCPatternExample;

public class MVCPatternDemo {
    public static void main(String[] args) {
        // Create the model
        Student student = new Student();
        student.setName("Ashwitha");
        student.setId("CSE022");
        student.setGrade("A");

        StudentView view = new StudentView();
        StudentController controller = new StudentController(student, view);
        controller.updateView();

        System.out.println();
        controller.setStudentGrade("A+");
        controller.updateView();
    }
}
