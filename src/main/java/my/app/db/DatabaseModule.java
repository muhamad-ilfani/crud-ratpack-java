package my.app.db;

import javax.sql.DataSource;

import com.google.inject.AbstractModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.google.inject.Provides;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseModule extends AbstractModule{
    @Provides
    public DataSource dataSource() {
        Dotenv dotenv = Dotenv.load();

        String dbUrl = dotenv.get("DB_URL");
        String dbUserName = dotenv.get("DB_USER_NAME");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbDriver = dotenv.get("DB_DRIVER");
        String dbSchema = dotenv.get("DB_SCHEMA");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUserName);
        config.setPassword(dbPassword);
        config.setDriverClassName(dbDriver);
        config.setSchema(dbSchema);
        return new HikariDataSource(config);
    }

    @Override
    protected void configure() {
        // binding
    }
}
