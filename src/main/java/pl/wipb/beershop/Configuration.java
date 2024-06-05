package pl.wipb.beershop;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;

@DataSourceDefinition(
        name = "java:global/BeerShopDataSource",
        className = "org.h2.jdbcx.JdbcDataSource",
        url = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1",
        minPoolSize = 1,
        initialPoolSize = 1,
        user = "dbuser",
        password = "dbuser123"
)
@Singleton
public class Configuration {
}
