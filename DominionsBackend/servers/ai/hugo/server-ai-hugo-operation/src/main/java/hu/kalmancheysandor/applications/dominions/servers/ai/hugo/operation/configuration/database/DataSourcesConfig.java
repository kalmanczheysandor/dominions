package hu.kalmancheysandor.applications.dominions.servers.ai.hugo.operation.configuration.database;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;
//import org.springframework.session.jdbc.config.annotation.SpringSessionDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourcesConfig {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource.ai-hugo")
    public DataSource mainDataSource() {
        HikariDataSource ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
      //  ds.setTransactionIsolation("TRANSACTION_REPEATABLE_READ");
        return ds;
    }

    @Bean
    @SpringSessionDataSource
    @ConfigurationProperties("app.datasource.session")
    public DataSource sessionDataSource() {
        return DataSourceBuilder.create().build();
    }
}
