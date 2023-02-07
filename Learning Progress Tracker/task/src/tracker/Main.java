package tracker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        StudentsRepository studentsRepository = new StudentsRepository();
        System.out.println("Learning Progress Tracker");

        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "" -> System.out.println("No input");
                case "add points" -> inputPoints(studentsRepository);
                case "add students" -> inputStudent(studentsRepository);
                case "back" -> System.out.println("Enter 'exit' to exit the program.");
                case "exit" -> {
                    System.out.println("Bye!");
                    return;
                }
                case "find" -> findStudent(studentsRepository);
                case "list" -> studentsRepository.printStudentsList();
                case "statistics" -> printStatistics(studentsRepository);
                case "notify" -> studentsRepository.notifyStudents();
                default -> System.out.println("Unknown command!");
            }
        }
    }

    public static void printStatistics(StudentsRepository studentsRepository) {
        List<int[]> points = studentsRepository.getStudentsMap().values().stream()
                .map(Student::getPoints)
                .toList();
        Map<String, String> popularityStatistics = studentsRepository.getCourseStatistics(points);
        List<int[]> submissions = studentsRepository.getStudentsMap().values().stream()
                .map(Student::getSubmissions)
                .toList();
        Map<String, String> submissionStatistics = studentsRepository.getCourseStatistics(submissions);
        Map<String, String> complexityStatistics = studentsRepository.getComplexityStatistics();
        System.out.println("Type the name of a course to see details or 'back' to quit");
        System.out.println("Most popular: " + popularityStatistics.getOrDefault("max", "n/a"));
        System.out.println("Least popular: " + popularityStatistics.getOrDefault("min","n/a"));
        System.out.println("Highest activity: " + submissionStatistics.getOrDefault("max", "n/a"));
        System.out.println("Lowest activity: " + submissionStatistics.getOrDefault("min", "n/a"));
        System.out.println("Easiest course: " + complexityStatistics.getOrDefault("max", "n/a"));
        System.out.println("Hardest course: " + complexityStatistics.getOrDefault("min", "n/a"));

        String input = scanner.nextLine();
        while (!"back".equals(input)) {
            for (int i = 0; i <= StudentsRepository.coursesList.size(); i++) {
                if (i == StudentsRepository.coursesList.size()) {
                    System.out.println("Unknown course.");
                } else {
                    Course course = StudentsRepository.coursesList.get(i);
                    if (course.getName().equals(input)) {
                        List<CourseStatistics> courseStatisticsList = new ArrayList<>();
                        for (Map.Entry<Integer, Student> studentEntry : studentsRepository.getStudentsMap().entrySet()) {
                            int coursePoints = studentEntry.getValue().getPoints()[i];
                            if (coursePoints != 0) {
                                courseStatisticsList.add(new CourseStatistics(studentEntry.getKey(), coursePoints, getCompletedPercent(coursePoints, i)));
                            }
                        }
                        courseStatisticsList.sort(Comparator.comparing(CourseStatistics::getPoints).reversed().thenComparing(CourseStatistics::getId));
                        System.out.println(course.getName());
                        System.out.println("id    points    completed");
                        for (CourseStatistics courseStatistics : courseStatisticsList) {
                            System.out.println(String.format("%1$-6s", courseStatistics.getId()) + String.format("%1$-10s", courseStatistics.getPoints()) + courseStatistics.getCompleted() + "%");
                        }
                        break;
                    }
                }
            }
            input = scanner.nextLine();
        }
    }
    private static String getCompletedPercent(int points, int courseId) {
        int expectedPoints = StudentsRepository.coursesList.get(courseId).getPoints();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        df.setMinimumFractionDigits(1);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(((double) points / expectedPoints) * 100);
    }

    private static void findStudent(StudentsRepository studentsRepository) {
        System.out.println("Enter an id or 'back' to return");
        while (true) {
            String input = scanner.nextLine();
            if ("back".equals(input)) {
                return;
            }
            if (!input.matches("\\d+")) {
                System.out.printf("No student is found for id=%s", input);
            } else {
                Student student = studentsRepository.getStudent(Integer.parseInt(input));
                if (student == null) {
                    System.out.printf("No student is found for id=%s", input);
                } else {
                    int[] points = student.getPoints();
                    System.out.print(input + " points: ");
                    for (int i = 0; i < points.length; i++) {
                        System.out.print(StudentsRepository.coursesList.get(i).getName() + "=" + points[i]);
                        if (i != points.length - 1) {
                            System.out.print("; ");
                        } else {
                            System.out.println();
                        }
                    }
                }
            }
        }
    }

    private static void inputPoints(StudentsRepository studentsRepository) {
        System.out.println("Enter an id and points or 'back' to return");
        while (true) {
            String input = scanner.nextLine();
            if ("back".equals(input)) {
                return;
            }
            String id = input.substring(0, input.indexOf(" "));
            String[] inputArray = input.substring(input.indexOf(" ") + 1).split(" ");
            if (inputArray.length != StudentsRepository.coursesList.size() ||
                    !Arrays.stream(inputArray)
                            .allMatch(n -> n.matches("\\d+"))) {
                System.out.println("Incorrect points format");
            } else if (!id.matches("\\d+")) {
                System.out.printf("No student is found for id=%s%n", id);
            } else {
                int[] points = Arrays.stream(inputArray)
                        .mapToInt(Integer::parseInt)
                        .toArray();
                studentsRepository.addPoints(Integer.parseInt(id), points);
            }
        }
    }

    private static void inputStudent(StudentsRepository studentsRepository) {
        int studentCount = 0;
        System.out.println("Enter student credentials or 'back' to return");
        while (true) {
            String input = scanner.nextLine().trim();

            if ("back".equals(input)) {
                System.out.println("Total " + studentCount + " students were added.");
                return;
            } else {
                int firstSpace = input.indexOf(" ");
                int lastSpace = input.lastIndexOf(" ");

                if (firstSpace == -1 || lastSpace == firstSpace) {
                    System.out.println("Incorrect credentials");
                } else {
                    String firstName = input.substring(0, firstSpace);
                    String lastName = input.substring(firstSpace + 1, lastSpace);
                    String email = input.substring(lastSpace + 1);
                    Student student = new Student(firstName, lastName, email);

                    if (studentsRepository.isCredentialsValid(student)) {
                        studentsRepository.addStudent(student);
                        studentCount++;
                        System.out.println("The student has been added.");
                    }
                }

            }
        }
    }
}
