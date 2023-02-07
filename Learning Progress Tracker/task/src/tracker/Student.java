package tracker;

import lombok.Getter;

@Getter
public class Student {

    private final String email;
    private final String firstName;
    private final String lastName;
    private final int[] points;
    private final int[] submissions;

    public Student(String firstName, String lastName, String email) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.points = new int[StudentsRepository.coursesList.size()];
        this.submissions = new int[StudentsRepository.coursesList.size()];
    }

    public void updatePoints(int[] points) {
        for (int i = 0; i < points.length; i++) {
            this.points[i] += points[i];
            if (points[i] != 0) {
                this.submissions[i]++;
            }
        }
        this.createNotifications(this.points);
    }

    private void createNotifications(int[] points) {
        for (int i = 0; i < StudentsRepository.coursesList.size(); i++) {
            Course course = StudentsRepository.coursesList.get(i);
            if (points[i] >= course.getPoints()) {
                StudentsRepository.addNotification(this, String.format("To: %s %n" +
                        "Re: Your Learning Progress %n" +
                        "Hello, %s %s! You have accomplished our %s course!",
                        this.getEmail(), this.getFirstName(), this.getLastName(), course.getName()));
            }
        }
    }
}
