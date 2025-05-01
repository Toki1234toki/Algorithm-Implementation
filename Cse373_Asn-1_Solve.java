package cse373;
import java.io.*;
import java.util.*;

class Student {
    String id, name;
    int creditsCompleted, probation;
    double cgpa;

    public Student(String id, String name, int creditsCompleted, double cgpa, int probation) {
        this.id = id;
        this.name = name;
        this.creditsCompleted = creditsCompleted;
        this.cgpa = cgpa;
        this.probation = probation;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + creditsCompleted + " " + cgpa + " " + probation;
    }
}

public class toki {
    public static void mergeSort(List<Student> students, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(students, left, mid);
            mergeSort(students, mid + 1, right);
            merge(students, left, mid, right);
        }
    }

    private static void merge(List<Student> students, int left, int mid, int right) {
        List<Student> leftList = new ArrayList<>(students.subList(left, mid + 1));
        List<Student> rightList = new ArrayList<>(students.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (compareStudents(leftList.get(i), rightList.get(j)) <= 0) {
                students.set(k++, leftList.get(i++));
            } else {
                students.set(k++, rightList.get(j++));
            }
        }
        while (i < leftList.size()) {
            students.set(k++, leftList.get(i++));
        }
        while (j < rightList.size()) {
            students.set(k++, rightList.get(j++));
        }
    }

    private static int compareStudents(Student s1, Student s2) {
        if (s1.creditsCompleted != s2.creditsCompleted) {
            return Integer.compare(s2.creditsCompleted, s1.creditsCompleted);
        }
        if (s1.probation != s2.probation) {
            return Integer.compare(s1.probation, s2.probation);
        }
        return Double.compare(s2.cgpa, s1.cgpa);
    }

    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                String[] parts = line.split("\\s+"); // Split by spaces (handles multiple spaces)

                if (parts.length < 5) { // Minimum required parts
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                // Extracting ID, credits, CGPA, and probation
                String id = parts[0];
                int creditsCompleted = Integer.parseInt(parts[parts.length - 3]);
                double cgpa = Double.parseDouble(parts[parts.length - 2]);
                int probation = Integer.parseInt(parts[parts.length - 1]);

                // Extract name (everything between ID and creditsCompleted)
                String name = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 3));

                students.add(new Student(id, name, creditsCompleted, cgpa, probation));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        mergeSort(students, 0, students.size() - 1);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
            for (Student s : students) {
                bw.write(s.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
