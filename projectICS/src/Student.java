import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String universityLevel;

    public Student(int id, String firstName, String lastName, String dateOfBirth, String universityLevel) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.universityLevel = universityLevel;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id); // Hashing based on student ID
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return id == student.id;
    }


    @Override
    public String toString() {
        return id + " - " + firstName + " " + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUniversityLevel() {
        return universityLevel;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setUniversityLevel(String universityLevel) {
        this.universityLevel = universityLevel;
    }

    public void setID(String id) {
        this.id = Integer.parseInt(id);
    }
}


// Generic entry class for the hash table
class Entry<T> {
    private T dataObject;
    private String status;

    public Entry(T dataObject, String status) {
        this.dataObject = dataObject;
        this.status = status;
    }

    public Entry(T dataObject) {
        this(dataObject, "O");
    }

    public T getDataObject() {
        return dataObject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

// Hash table implementation
class HashTable<T> {
    private Entry<T>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable(int size) {
        this.size = size;
        this.table = new Entry[size];
        for (int i = 0; i < size; i++) {
            table[i] = new Entry<>(null, "E");
        }
    }

    public boolean insert(T dataObject) {
        int hashCode = dataObject.hashCode();
        int index = hashCode % size;

        for (int i = 0; i < size; i++) {
            int currentSlot = (index + i) % size;
            if (table[currentSlot].getStatus().equals("E") || table[currentSlot].getStatus().equals("D")) {
                table[currentSlot] = new Entry<>(dataObject);
                table[currentSlot].setStatus("O");
                return true;
            }
        }
        return false;
    }
    public boolean insertStudent(T dataObject) {
        int hashCode = dataObject.hashCode();
        int index = hashCode % size;

        for (int i = 0; i < size; i++) {
            int currentSlot = (index + i) % size;
            if (table[currentSlot].getStatus().equals("E") || table[currentSlot].getStatus().equals("D")) {
                table[currentSlot] = new Entry<>(dataObject);
                table[currentSlot].setStatus("O");
                saveToFile((HashTable<Student>) this,"students-details.csv");
                return true;
            }
        }
        return false;
    }

    public boolean edit(int id, String firstName, String lastName, String dateOfBirth, String universityLevel) {
        int hashCode = Integer.hashCode(id); // Compute hash code using the ID
        int index = hashCode % size;

        for (int i = 0; i < size; i++) {
            int currentSlot = (index + i) % size;
            if (table[currentSlot].getStatus().equals("O")) {
                // Retrieve the existing Student
                Student existingStudent = (Student) table[currentSlot].getDataObject();

                if (existingStudent.getId() == id) { // Match by ID
                    // Update fields if new values are provided
                    if (firstName != null && !firstName.isEmpty()) {
                        existingStudent.setFirstName(firstName);
                    }
                    if (lastName != null && !lastName.isEmpty()) {
                        existingStudent.setLastName(lastName);
                    }
                    if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                        existingStudent.setDateOfBirth(dateOfBirth);
                    }
                    if (universityLevel != null && !universityLevel.isEmpty()) {
                        existingStudent.setUniversityLevel(universityLevel);
                    }
                    saveToFile((HashTable<Student>) this,"students-details.csv");
                    return true;
                }
            }
        }
        return false; // Student not found
    }


    public boolean delete(T dataObject) {
        int hashCode = dataObject.hashCode();
        int slot = hashCode % size;

        for (int i = 0; i < size; i++) {
            int currentSlot = (slot + i) % size;
            if (table[currentSlot].getStatus().equals("O") && table[currentSlot].getDataObject().equals(dataObject)) {
                table[currentSlot].setStatus("D");
                saveToFile((HashTable<Student>) this,"students-details.csv");
                return true;
            }
        }
        return false;
    }

    public int find(T dataObject) {
        int hashCode = dataObject.hashCode();
        int slot = hashCode % size;

        for (int i = 0; i < size; i++) {
            int currentSlot = (slot + i) % size;
            if (table[currentSlot].getStatus().equals("E")) {
                return -1; // Stop if an empty slot is found
            }
            if (table[currentSlot].getStatus().equals("O") && table[currentSlot].getDataObject().equals(dataObject)) {
                return currentSlot; // Return index if found
            }
        }
        return -1; // Not found
    }

    public List<T> searchByLastName(String lastName) {
        List<T> result = new ArrayList<>();
        for (Entry<T> entry : table) {
            if (entry.getStatus().equals("O")) {
                Student student = (Student) entry.getDataObject();
                if (student.getLastName().equalsIgnoreCase(lastName)) {
                    result.add((T) student);
                }
            }
        }
        return result;
    }

    public List<T> searchByFirstName(String firstName) {
        List<T> result = new ArrayList<>();
        for (Entry<T> entry : table) {
            if (entry.getStatus().equals("O")) {
                Student student = (Student) entry.getDataObject();
                if (student.getFirstName().equalsIgnoreCase(firstName)) {
                    result.add((T) student);
                }
            }
        }
        return result;
    }

    public List<T> retrieveByAcademicLevel(String level) {
        List<T> result = new ArrayList<>();
        for (Entry<T> entry : table) {
            if (entry.getStatus().equals("O")) {
                Student student = (Student) entry.getDataObject();
                if (student.getUniversityLevel().equalsIgnoreCase(level)) {
                    result.add((T) student);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(i).append(": ");
            if (table[i].getStatus().equals("O")) {
                result.append(table[i].getDataObject());
            } else {
                result.append(table[i].getStatus());
            }
            result.append("\n");
        }
        return result.toString();
    }

    public void loadFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { // Skip header line
                    isFirstLine = false;
                    continue;
                }
                String[] details = line.split(",");
                int id = Integer.parseInt(details[0].trim());
                String lastName = details[1].trim();
                String firstName = details[2].trim();
                String dateOfBirth = details[3].trim();
                String universityLevel = details[4].trim();

                Student student = new Student(id, firstName, lastName, dateOfBirth, universityLevel);
                this.insert((T) student); // Insert directly into the hash table
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
    public void saveToFile(HashTable<Student> hashTable,String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < hashTable.size; i++) {
                Entry<Student> entry = hashTable.getTable()[i];
                if (entry.getStatus().equals("O")) {
                    Student student = entry.getDataObject();
                    writer.write(student.getId() + "," +
                            student.getLastName() + "," +
                            student.getFirstName() + "," +
                            student.getDateOfBirth() + "," +
                            student.getUniversityLevel());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }


    public Entry<T>[] getTable() {
        return table;
    }

}