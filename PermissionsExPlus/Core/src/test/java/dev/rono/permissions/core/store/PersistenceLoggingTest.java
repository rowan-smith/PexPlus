package dev.rono.permissions.core.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;

class PersistenceLoggingTest {

    @Test
    void suppressesRoutineJulMessagesWithoutHidingWarnings() {
        var hikari = Logger.getLogger("com.zaxxer.hikari");
        var hibernate = Logger.getLogger("org.hibernate");

        var previousHikari = hikari.getLevel();
        var previousHibernate = hibernate.getLevel();

        try {
            hikari.setLevel(Level.INFO);
            hibernate.setLevel(Level.INFO);

            PersistenceLogging.suppressRoutineMessages();

            assertEquals(Level.WARNING, hikari.getLevel());
            assertEquals(Level.WARNING, hibernate.getLevel());
        } finally {
            hikari.setLevel(previousHikari);
            hibernate.setLevel(previousHibernate);
        }
    }
}
