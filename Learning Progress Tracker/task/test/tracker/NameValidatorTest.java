package tracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class NameValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"ga%dzhieva", "'gadzhieva", "gadzhieva-", "gadzhieva'-ff", "@email.com"})
    @DisplayName("incorrect format")
    public void incorrectFormatTest(String input) {
        assertTrue(StudentsRepository.isNameInvalid(input));
    }

    @Test
    @DisplayName("valid name")
    public void validNameTest() {
        String input = "gadzhieva-mc'lena rashitovna";
        assertFalse(StudentsRepository.isNameInvalid(input));
    }
}
