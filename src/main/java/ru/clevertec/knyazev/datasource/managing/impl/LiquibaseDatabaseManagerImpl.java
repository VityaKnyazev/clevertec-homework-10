package ru.clevertec.knyazev.datasource.managing.impl;


import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import ru.clevertec.knyazev.datasource.managing.DatabaseManager;
import ru.clevertec.knyazev.datasource.managing.exception.DatabaseManagerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseDatabaseManagerImpl implements DatabaseManager {
    private static final String LOAD_DATA_ERROR = "Error when updating database with new tables and data";

    private final String driverClassName;
    private final String jdbcURL;
    private final String userName;
    private final String password;

    private final String changelogFile;

    public LiquibaseDatabaseManagerImpl(String driverClassName,
                                        String jdbcURL,
                                        String userName,
                                        String password,
                                        String changelogFile) {
        this.driverClassName = driverClassName;
        this.jdbcURL = jdbcURL;
        this.userName = userName;
        this.password = password;

        this.changelogFile = changelogFile;
    }

    @Override
    public void loadData() {

        try {
            Class.forName(driverClassName);

            Connection connection = DriverManager.getConnection(jdbcURL,
                    userName,
                    password);
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            try (Liquibase liquibase = new Liquibase(changelogFile,
                    new ClassLoaderResourceAccessor(), database)) {

                liquibase.update();

            }

        } catch (SQLException | LiquibaseException | ClassNotFoundException e) {
            throw new DatabaseManagerException(LOAD_DATA_ERROR, e);
        }
    }
}
