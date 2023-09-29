import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class StudentData implements Serializable {
    private String studentName;
    private int studentRollNumber;
    private String studentGrade;

    public StudentData(String name, int rollNumber, String grade) {
        this.studentName = name;
        this.studentRollNumber = rollNumber;
        this.studentGrade = grade;
    }

    public String getName() {
        return studentName;
    }

    public int getRollNumber() {
        return studentRollNumber;
    }

    public String getGrade() {
        return studentGrade;
    }

    @Override
    public String toString() {
        return "Name: " + studentName + ", Roll Number: " + studentRollNumber + ", Grade: " + studentGrade;
    }
}

public class StudentManagementApp extends JFrame {
    private List<StudentData> studentDataList = new ArrayList<>();

    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel rollNumberLabel;
    private JTextField rollNumberField;
    private JLabel gradeLabel;
    private JTextField gradeField;
    private JButton addButton;
    private JButton displayButton;
    private JButton searchButton;
    private JButton deleteButton;

    private static final String DATA_FILE = "student_data.txt";

    public StudentManagementApp() {
        setTitle("Student Management App");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        rollNumberLabel = new JLabel("Roll Number:");
        rollNumberField = new JTextField(10);
        gradeLabel = new JLabel("Grade:");
        gradeField = new JTextField(5);
        addButton = new JButton("Add Student");
        displayButton = new JButton("Display Students");
        searchButton = new JButton("Search Student");
        deleteButton = new JButton("Delete Student");

        add(nameLabel);
        add(nameField);
        add(rollNumberLabel);
        add(rollNumberField);
        add(gradeLabel);
        add(gradeField);
        add(addButton);
        add(displayButton);
        add(searchButton);
        add(deleteButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentData();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayStudentData();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudentData();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudentData();
            }
        });

        loadStudentData();
    }

    private void addStudentData() {
        String name = nameField.getText().trim();
        String rollNumberText = rollNumberField.getText().trim();
        String grade = gradeField.getText().trim();

        if (name.isEmpty() || rollNumberText.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Fill all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollNumber = Integer.parseInt(rollNumberText);
            StudentData student = new StudentData(name, rollNumber, grade);
            studentDataList.add(student);

            JOptionPane.showMessageDialog(null, "Student data added successfully.");

            nameField.setText("");
            rollNumberField.setText("");
            gradeField.setText("");

            saveStudentData();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid roll number format. Enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayStudentData() {
        if (studentDataList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No student data to display.");
            return;
        }

        StringBuilder studentDataDisplay = new StringBuilder("Student Data:\n");
        for (StudentData studentData : studentDataList) {
            studentDataDisplay.append(studentData).append("\n");
        }

        JOptionPane.showMessageDialog(null, studentDataDisplay.toString());
    }

    private void searchStudentData() {
        int rollNumber;
        try {
            rollNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter Roll Number to search:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid roll number format. Enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean found = false;

        for (StudentData studentData : studentDataList) {
            if (studentData.getRollNumber() == rollNumber) {
                JOptionPane.showMessageDialog(null, "Student data found:\n" + studentData);
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Student data not found.");
        }
    }

    private void deleteStudentData() {
        int rollNumber;
        try {
            rollNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter Roll Number to delete:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid roll number format. Enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Iterator<StudentData> iterator = studentDataList.iterator();
        while (iterator.hasNext()) {
            StudentData studentData = iterator.next();
            if (studentData.getRollNumber() == rollNumber) {
                iterator.remove();
                JOptionPane.showMessageDialog(null, "Student data deleted successfully.");
                saveStudentData();
                return;
            }
        }

        JOptionPane.showMessageDialog(null, "Student data not found.");
    }

    private void saveStudentData() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            outputStream.writeObject(studentDataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentData() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            studentDataList = (ArrayList<StudentData>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            studentDataList = new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentManagementApp().setVisible(true);
            }
        });
    }
}
