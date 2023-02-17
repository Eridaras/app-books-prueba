package com.distribuida.config;

import com.zaxxer.hikari.HikariDataSource;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.persistence.Persistence;
import javax.sql.DataSource;

@ApplicationScoped
public class DbConfig {
    @Inject
    @ConfigProperty(name="db.connection.username")
    private String dbUser;

    @Inject
    @ConfigProperty(name="db.connection.password")
    private String dbPassword;

    @Inject
    @ConfigProperty(name="db.connection.url")
    private String dbUrl;

    @Produces
    @ApplicationScoped
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();

        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);

        //ds.setMinimumIdle(1);
        //ds.setMaximumPoolSize(5);

        return ds;
    }
    @Produces
    @ApplicationScoped
    public Persistence persistence() {


        return null;
    }
}
