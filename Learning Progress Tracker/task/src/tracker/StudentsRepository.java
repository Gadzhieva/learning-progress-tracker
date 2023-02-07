package tracker;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class StudentsRepository {
    private final Map<Integer, Student> studentsMap;
    public static List<Course> coursesList;
    private int nextId;
    private static Map<Student, List<String>> notifications;

    public StudentsRepository() {
        nextId = 0;
        studentsMap = new HashMap<>();
        coursesList = new ArrayList<>();
        notifications = new HashMap<>();
        coursesList.add(new Course("Java", 600));
        coursesList.add(new Course("DSA", 400));
        coursesList.add(new Course("Databases", 480));
        coursesList.add(new Course("Spring", 550));
    }

    public static void addNotification(Student student, String notification) {
        List<String> messages = notifications.getOrDefault(student, new ArrayList<>());
        messages.add(notification);
        notifications.put(student, messages);
    }

    boolean isCredentialsValid(Student student) {


        if (isEmailInvalid(student.getEmail())) {
            System.out.println("Incorrect email");
            return false;
        }

        if (isEmailExists(student.getEmail())) {
            System.out.println("This email is already taken");
            return false;
        }

        if (isNameInvalid(student.getFirstName())) {
            System.out.println("Incorrect first name");
            return false;
        }

        if (isNameInvalid(student.getLastName())) {
            System.out.println("Incorrect last name");
            return false;
        }

        return true;
    }

    boolean isEmailExists(String email) {
        if (studentsMap.isEmpty()) {
            return false;
        }
        return studentsMap.values().stream()
                .map(Student::getEmail)
                .toList()
                .contains(email);
    }

    static boolean isEmailInvalid(String email) {
        return !(email.matches(".+@.+\\..+") && !email.matches(".*@.*@.*"));
    }

    static boolean isNameInvalid(String name) {
        return !name.matches("[A-Za-z][ A-Za-z'-]*[A-Za-z]") || name.matches(".*[-'][-'].*");
    }

    public void addStudent(Student student) {
        int studentId = nextId;
        studentsMap.put(studentId, student);
        nextId++;
    }

    public Student getStudent(int id) {
        return studentsMap.get(id);
    }

    public void printStudentsList() {
        if (studentsMap.isEmpty()) {
            System.out.println("No students found");
        } else {
            System.out.println("Students:");
            for (int id : studentsMap.keySet()) {
                System.out.println(id);
            }
        }
    }

    public void addPoints(int id, int[] points) {
        Student student = getStudent(id);
        if (student == null) {
            System.out.printf("No student is found for id=%d%n", id);
        } else {
            student.updatePoints(points);
            System.out.println("Points updated.");
        }
    }

    public Map<String, String> getCourseStatistics(List<int[]> courseData) {
        Integer[] accumulation = new Integer[coursesList.size()];
        Arrays.fill(accumulation, 0);
        int max = 0;
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < accumulation.length; i++) {
            for (int[] studentsPoints : courseData) {
                if (studentsPoints[i] != 0) {
                    accumulation[i]++;
                }
            }
            max = Math.max(accumulation[i], max);
            min = Math.min(accumulation[i], min);
        }

        return getMaxMinCourses(accumulation, max, min);
    }

    public Map<String, String> getComplexityStatistics() {
        List<int[]> points = getStudentsMap().values().stream()
                .map(Student::getPoints)
                .toList();
        Double[] averageScore = new Double[coursesList.size()];
        Arrays.fill(averageScore, (double) 0);
        double min = Double.MAX_VALUE;
        double max = 0;
        for (int i = 0; i < averageScore.length; i++) {
            int sum = 0;
            int count = 0;
            for (int[] studentsPoints : points) {
                if (studentsPoints[i] != 0) {
                    sum += studentsPoints[i];
                    count++;
                }
            }

            if (count != 0) {
                averageScore[i] = (double) sum / count;
                min = Math.min(averageScore[i], min);
                max = Math.max(averageScore[i], max);
            }
        }

        return getMaxMinCourses(averageScore, max, min);
    }

    private static <T> Map<String, String> getMaxMinCourses(T[] accumulation, T max, T min) {
        Map<String, String> result = new HashMap<>();
        if (max.equals(0) || max.equals(0.0)) {
            result.put("max", "n/a");
            result.put("min", "n/a");
        } else {
            StringBuilder maxCourses = new StringBuilder();
            StringBuilder minCourses = new StringBuilder();
            for (int i = 0; i < coursesList.size(); i++) {
                if (accumulation[i].equals(max)) {
                    if (maxCourses.length() != 0) {
                        maxCourses.append(", ");
                    }
                    maxCourses.append(coursesList.get(i).getName());
                } else if (!min.equals(max) && accumulation[i].equals(min)) {
                    if (minCourses.length() != 0) {
                        minCourses.append(", ");
                    }
                    minCourses.append(coursesList.get(i).getName());

                }
            }

            result.put("max", maxCourses.toString());

            if (minCourses.length() == 0) {
                result.put("min", "n/a");
            } else {
                result.put("min", minCourses.toString());
            }
        }

        return result;
    }

    public void notifyStudents() {
        int students = 0;
        for (Map.Entry<Student, List<String>> entry : notifications.entrySet()) {
            entry.getValue().forEach(System.out::println);
            students++;
        }
        notifications.clear();
        System.out.printf("Total %d students have been notified.%n", students);
    }
}
