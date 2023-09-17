package it.zS0bye.eLuckyBlock.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.files.enums.Config;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Getter
public class SQLConnection {

    private ELuckyBlock plugin;
    private HikariConfig config;
    private HikariDataSource source;
    protected Connection connection;
    private File file;

    @SneakyThrows
    public SQLConnection() {
        this.config = new HikariConfig();
        this.config.setJdbcUrl("jdbc:mysql://" + Config.DB_HOST.getStringNoColor() + ":" + Config.DB_PORT.getStringNoColor() + "/" + Config.DB_NAME.getStringNoColor() + Config.DB_CUSTOMURI.getStringNoColor());
        this.config.setUsername(Config.DB_USER.getStringNoColor());
        this.config.setPassword(Config.DB_PASSWORD.getStringNoColor());
        this.config.setMaximumPoolSize(Config.DB_MAXIMUM_POOL_SIZE.getInt());
        this.config.setMinimumIdle(Config.DB_MINIMUM_IDLE.getInt());
        this.config.setMaxLifetime(Config.DB_MAXIMUM_LIFETIME.getInt());
        this.config.setKeepaliveTime(Config.DB_KEEPALIVE_TIME.getInt());
        this.config.setConnectionTimeout(Config.DB_CONNECTION_TIMEOUT.getInt());
        this.config.addDataSourceProperty("cachePrepStmts", "true");
        this.config.addDataSourceProperty("prepStmtCacheSize", "250");
        this.config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.source = new HikariDataSource(config);
        this.connection = this.source.getConnection();
    }

    @SneakyThrows
    public SQLConnection(final ELuckyBlock plugin) {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), "database.db");

        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:"+file.getAbsolutePath());
    }

    @SneakyThrows
    public void closeConnection() {
        if(this.connection.isClosed()) return;
        this.connection.close();
    }

    @SneakyThrows
    public boolean hasClosedConnection() {
        return this.connection == null || this.connection.isClosed();
    }

    public void saves(final String sql, final Object... values) {
        CompletableFuture.supplyAsync(() -> {
            try (final PreparedStatement pst = this.connection.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
//                    System.out.println("index - " + (i + 1) + " oggetto: " + values[i]);
                    pst.setObject(i + 1, values[i]);
                }
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                this.closeConnection();
            }
            return Executors.newFixedThreadPool(1);
        });
    }

}
