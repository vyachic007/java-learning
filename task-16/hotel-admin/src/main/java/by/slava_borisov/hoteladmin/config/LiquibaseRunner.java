package by.slava_borisov.hoteladmin.config;

import by.slava_borisov.hoteladmin.util.Messages;
import jakarta.annotation.PostConstruct;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class LiquibaseRunner {

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @PostConstruct
    public void migrate() {
        try (Connection connection = DriverManager.getConnection(
                dbUrl,
                dbUser,
                dbPassword
        )) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    Messages.LIQUIBASE_CHANGELOG_PATH,
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update();
            System.out.println(Messages.LIQUIBASE_MIGRATION_SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException(Messages.LIQUIBASE_MIGRATION_ERROR, e);
        }
    }
}
