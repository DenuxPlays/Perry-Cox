package com.denux.perry.utils.database.connections;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class Postgres {
    /**
     * The connection String saved here to get a quicker access time.
     */
    public static volatile String connectionString = null;

    /**
     * Establishes a connection to the PostgreSQL Database.
     * Returns null if the given Connection String is wrong or empty.
     * @return Returns a java SQL Connection.
     */
    public Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(connectionString);
            log.info("Successfully connected to the PostgreSQL Database.");
        } catch (SQLException exception) {
            log.error("PostgreSQL Connection String is wrong!\n" +
                    "Error: " + exception);
        }
        return con;
    }
}
