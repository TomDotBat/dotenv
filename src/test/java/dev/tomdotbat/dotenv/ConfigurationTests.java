package dev.tomdotbat.dotenv;

import dev.tomdotbat.dotenv.exception.BooleanFormatException;
import dev.tomdotbat.dotenv.exception.MissingKeyException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ConfigurationTests {
    @Test
    @BeforeAll
    public static void testConfigInstantiation() {
        assertDoesNotThrow(() -> config = new Configuration());
        assertDoesNotThrow(() -> new Configuration(new File(".")));
        assertDoesNotThrow(() -> new Configuration(new File(".").toPath()));
    }

    @Test
    public void testConfigLoad() {
        assertDoesNotThrow(config::load);
    }

    @Test
    public void testConfigSave() {
        assertDoesNotThrow(config::save);
    }

    @Test
    public void testConfigStringSetterGetters() {
        config.set("TEST_STRING", "TEST123");

        assertEquals("TEST123", config.getString("TEST_STRING"));
        assertEquals("TEST123", config.getString("TEST_STRING", "BAD"));

        assertTrue(config.keyEquals("TEST_STRING", "TEST123"));
        assertTrue(config.keyEquals("TEST_STRING", "TEST123", false));

        assertFalse(config.keyEquals("TEST_BAD_STRING", "TEST123"));
        assertTrue(config.keyEquals("TEST_BAD_STRING", "TEST123", true));

        assertNull(config.getString("TEST_BAD_STRING"));
        assertEquals("HELLO", config.getString("TEST_BAD_STRING", "HELLO"));
    }

    @Test
    public void testConfigIntegerSetterGetters() {
        config.set("TEST_INTEGER", 1000);

        assertDoesNotThrow(() -> {
            assertEquals(1000, config.getInteger("TEST_INTEGER"));
            assertEquals(1000, config.getInteger("TEST_INTEGER", 0));
        });

        assertTrue(config.keyEquals("TEST_INTEGER", 1000));
        assertTrue(config.keyEquals("TEST_INTEGER", 1000, false));

        assertFalse(config.keyEquals("TEST_BAD_INTEGER", 1000));
        assertTrue(config.keyEquals("TEST_BAD_INTEGER", 1000, true));

        assertThrows(MissingKeyException.class, () -> config.getInteger("TEST_BAD_INTEGER"));
        config.set("TEST_BAD_INTEGER", "HELLO");
        assertThrows(NumberFormatException.class, () -> config.getInteger("TEST_BAD_INTEGER"));
    }

    @Test
    public void testConfigBooleanSetterGetters() {
        config.set("TEST_BOOLEAN", true);

        assertDoesNotThrow(() -> {
            assertTrue(config.getBoolean("TEST_BOOLEAN"));
            assertTrue(config.getBoolean("TEST_BOOLEAN", false));
        });

        assertTrue(config.keyEquals("TEST_BOOLEAN", true));
        assertTrue(config.keyEquals("TEST_BOOLEAN", true, false));

        assertFalse(config.keyEquals("TEST_BAD_BOOLEAN", true));
        assertTrue(config.keyEquals("TEST_BAD_BOOLEAN", false, true));

        assertThrows(MissingKeyException.class, () -> config.getBoolean("TEST_BAD_BOOLEAN"));
        config.set("TEST_BAD_BOOLEAN", "HELLO");
        assertThrows(BooleanFormatException.class, () -> config.getBoolean("TEST_BAD_BOOLEAN"));
    }

    @Test
    public void testConfigKeysGetter() {
        config.set("TEST_VARIABLE", "HELLO WORLD!");
        assertTrue(config.getKeys().length > 0);
    }

    private static Configuration config;
}
