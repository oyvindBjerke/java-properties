package no.bjerke.properties;

import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class PropertiesTest {

    @Test(expected = NullPointerException.class)
    public void null_key_should_throw_expected_exception() {
        Properties.getProperty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void key_with_dot_should_throw_expected_exception() {
        Properties.getProperty("MY.PROPERTY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void key_with_digit_as_first_character_should_throw_expected_exception() {
        Properties.getProperty("1_PROPERTY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void key_with_lower_case_character_should_throw_expected_exception() {
        Properties.getProperty("MY_PROPERTy");
    }

    @Test
    public void key_with_digits_should_return_expected_result() {
        Optional<String> result = Properties.getProperty("MY_1_PROPERTY");
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void key_with_dash_should_throw_expected_exception() {
        Properties.getProperty("MY-PROPERTY");
    }

    @Test
    public void key_with_matching_system_property_should_return_expected_value() {
        System.setProperty("MY_PROPERTY", "some-value");
        Optional<String> result = Properties.getProperty("MY_PROPERTY");
        assertTrue(result.isPresent());
        assertEquals("some-value", result.get());
    }

    @Test
    public void key_with_matching_env_variable_should_return_expected_value() {
        Map.Entry<String, String> entry = System.getenv().entrySet().stream().findFirst().orElseThrow(
                () -> new IllegalStateException("No environment variables present while running test")
        );
        Optional<String> result = Properties.getProperty(entry.getKey());
        assertTrue(result.isPresent());
        assertEquals(entry.getValue(), result.get());
    }

    @Test
    public void key_with_matching_env_variable_and_system_property_should_return_system_property_value() {
        Map.Entry<String, String> entry = System.getenv().entrySet().stream().findFirst().orElseThrow(
                () -> new IllegalStateException("No environment variables present while running test")
        );
        String key = entry.getKey();
        assertNull("Should set a system property which does not already exist", System.getProperty(key));
        System.setProperty(key, "value-of-property");
        Optional<String> result = Properties.getProperty(key);
        assertTrue(result.isPresent());
        assertEquals("value-of-property", result.get());
        System.clearProperty(key);
    }

}