package no.bjerke.properties;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesTest {

    @Test
    void null_key_should_throw_expected_exception() {
        var exception = assertThrows(NullPointerException.class, () -> Properties.getProperty(null));
        assertEquals("Key may not be null", exception.getMessage());
    }

    @Test
    void key_with_dot_should_throw_expected_exception() {
        var exception = assertThrows(IllegalArgumentException.class, () -> Properties.getProperty("MY.PROPERTY"));
        assertEquals("Property key may not contain dots", exception.getMessage());
    }

    @Test
    void key_with_digit_as_first_character_should_throw_expected_exception() {
        var exception = assertThrows(IllegalArgumentException.class, () -> Properties.getProperty("1_PROPERTY"));
        assertEquals("Property key may not start with a digit", exception.getMessage());
    }

    @Test
    void key_with_lower_case_character_should_throw_expected_exception() {
        var exception = assertThrows(IllegalArgumentException.class, () -> Properties.getProperty("MY_PROPERTy"));
        assertEquals("Illegal key for environment variable/system property: MY_PROPERTy", exception.getMessage());
    }

    @Test
    void key_with_digits_should_return_expected_result() {
        var result = Properties.getProperty("MY_1_PROPERTY");
        assertNotNull(result);
    }

    @Test
    void key_with_dash_should_throw_expected_exception() {
        var exception = assertThrows(IllegalArgumentException.class, () -> Properties.getProperty("MY-PROPERTY"));
        assertEquals("Illegal key for environment variable/system property: MY-PROPERTY", exception.getMessage());
    }

    @Test
    void key_with_matching_system_property_should_return_expected_value() {
        System.setProperty("MY_PROPERTY", "some-value");
        var result = Properties.getProperty("MY_PROPERTY");
        assertTrue(result.isPresent());
        assertEquals("some-value", result.get());
    }

    @Test
    void key_with_matching_env_variable_should_return_expected_value() {
        var entry = System.getenv().entrySet().stream().findFirst().orElseThrow(
                () -> new IllegalStateException("No environment variables present while running test")
        );
        var result = Properties.getProperty(entry.getKey());
        assertTrue(result.isPresent());
        assertEquals(entry.getValue(), result.get());
    }

    @Test
    void key_with_matching_env_variable_and_system_property_should_return_system_property_value() {
        var entry = System.getenv().entrySet().stream().findFirst().orElseThrow(
                () -> new IllegalStateException("No environment variables present while running test")
        );
        var key = entry.getKey();
        assertNull(System.getProperty(key), "Should set a system property which does not already exist");
        System.setProperty(key, "value-of-property");
        var result = Properties.getProperty(key);
        assertTrue(result.isPresent());
        assertEquals("value-of-property", result.get());
        System.clearProperty(key);
    }

}