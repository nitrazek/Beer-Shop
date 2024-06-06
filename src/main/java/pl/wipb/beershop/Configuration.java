package pl.wipb.beershop;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@DataSourceDefinition(
        name = "java:global/H2DB",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1",
        minPoolSize = 1,
        initialPoolSize = 1,
        user = "sa",
        password = "sa"
)
@Singleton
@Startup
public class Configuration {
}
