import java.util.List;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        HashTable<Student> hashTable = new HashTable<>(400); // Initialize hash table with size 400

        // Load students from CSV file
        String filePath = "students-details.csv";
        hashTable.loadFromFile(filePath);

        // Menu system
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Search Student");
            System.out.println("2. Add New Student");
            System.out.println("3. Show Students in an Academic Level");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> searchStudent(hashTable, scanner);
                case 2 -> addNewStudent(hashTable, scanner);
                case 3 -> showStudentsByLevel(hashTable, scanner);
                case 4 -> {
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void searchStudent(HashTable<Student> hashTable, Scanner scanner) {
        System.out.println("\n===== Search Student =====");
        System.out.println("1. By Exact Student ID");
        System.out.println("2. By Last Name");
        System.out.println("3. By First Name");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();

        scanner.nextLine(); // Consume newline
        switch (choice) {
            case 1 -> {
                System.out.print("Enter Student ID: ");
                int id = scanner.nextInt();
                Student dummyStudent = new Student(id, null, null, null, null);
                int index = hashTable.find(dummyStudent);
                if (index != -1) {
                    Student foundStudent = (Student) hashTable.getTable()[index].getDataObject();
                    System.out.println("Student Found: " + foundStudent);
                    postSearchMenu(hashTable, scanner, foundStudent);
                } else {
                    System.out.println("Student not found.");
                }
            }
            case 2 -> {
                System.out.print("Enter Last Name: ");
                String lastName = scanner.nextLine();
                List<Student> students = hashTable.searchByLastName(lastName);
                displayStudentList(students);
            }
            case 3 -> {
                System.out.print("Enter First Name: ");
                String firstName = scanner.nextLine();
                List<Student> students = hashTable.searchByFirstName(firstName);
                displayStudentList(students);
            }
            default -> System.out.println("Invalid option. Returning to main menu.");
        }
    }

    private static void addNewStudent(HashTable<Student> hashTable, Scanner scanner) {
        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Date of Birth (dd/mm/yyyy): ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter University Level (FR/SO/JR/SR): ");
        String universityLevel = scanner.nextLine();

        Student newStudent = new Student(id, firstName ,lastName, dateOfBirth, universityLevel);
        if (hashTable.insertStudent(newStudent)) {
            System.out.println("Student added successfully.");
        } else {
            System.out.println("Failed to add student. Hash table may be full.");
        }
    }

    private static void showStudentsByLevel(HashTable<Student> hashTable, Scanner scanner) {
        System.out.print("Enter Academic Level (FR/SO/JR/SR): ");
        scanner.nextLine(); // Consume newline
        String level = scanner.nextLine();
        List<Student> students = hashTable.retrieveByAcademicLevel(level);
        displayStudentList(students);
    }

    private static void postSearchMenu(HashTable<Student> hashTable, Scanner scanner, Student student) {
        while (true) {
            System.out.println("\n1. Edit Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Return to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Student ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    // Check if the student exists
                    Student tempStudent = new Student(id, "", "", "", "");
                    if (hashTable.find(tempStudent) != -1) {
                        System.out.print("Enter New First Name (leave blank to keep unchanged): ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter New Last Name (leave blank to keep unchanged): ");
                        String lastName = scanner.nextLine();
                        System.out.print("Enter New Date of Birth (leave blank to keep unchanged): ");
                        String dateOfBirth = scanner.nextLine();
                        System.out.print("Enter New University Level (leave blank to keep unchanged): ");
                        String universityLevel = scanner.nextLine();

                        // Perform the edit operation
                        boolean success = hashTable.edit(id, firstName, lastName, dateOfBirth, universityLevel);
                        if (success) {
                            System.out.println("Student information updated successfully.");
                        } else {
                            System.out.println("Failed to update student information.");
                        }
                    } else {
                        System.out.println("Student with the given ID not found.");
                    }
                }

                case 2 -> {
                    if (hashTable.delete(student)) {
                        System.out.println("Student deleted successfully.");
                    } else {
                        System.out.println("Failed to delete student.");
                    }
                    return; // Return to main menu after deletion
                }
                case 3 -> {
                    System.out.println("Returning to main menu.");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void displayStudentList(List<Student> students) {
        if (!students.isEmpty()) {
            System.out.println("Students found:");
            for (Student student : students) {
                System.out.println(student);
            }
        } else {
            System.out.println("No students found.");
        }
    }
}
