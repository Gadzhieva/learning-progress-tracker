package tracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmailValidatorTest {

    static StudentsRepository studentsRepository;

    @BeforeAll
    void createStudentsRepository() {
       studentsRepository = new StudentsRepository();
       studentsRepository.addStudent(new Student("Lena", "Gadzhieva", "gadzhieva@lena.com"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"gadzhieva", "gadzhieva@", "gadzhieva@.", "gadzhieva@.@", "@email.com"})
    @DisplayName("incorrect format")
    public void incorrectFormatTest(String input) {
        assertTrue(StudentsRepository.isEmailInvalid(input));
    }

    @Test
    @DisplayName("valid email")
    public void validEmailTest() {
        String input = "gadzhieva@lena.com";
        assertFalse(StudentsRepository.isEmailInvalid(input));
    }

    @Test
    @DisplayName("email is taken")
    public void emailTakenTest() {
        String input = "gadzhieva@lena.com";
        assertTrue(studentsRepository.isEmailExists(input));
    }

    @Test
    @DisplayName("email is not taken")
    public void emailNotTakenTest() {
        String input = "gadzhiev@farid.com";
        assertFalse(studentsRepository.isEmailExists(input));
    }
}