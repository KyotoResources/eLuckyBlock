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
        this.config.setJdbcUrl("jdbc:mysql://" + Config.DB_HOST.getString() + ":" + Config.DB_PORT.getString() + "/" + Config.DB_NAME.getString() + Config.DB_CUSTOMURI.getString());
        this.config.setUsername(Config.DB_USER.getString());
        this.config.setPassword(Config.DB_PASSWORD.getString());
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
        this.saveFile();

        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:"+file.getAbsolutePath());
    }

    @SneakyThrows
    public void closeConnection() {
        if(this.connection.isClosed())
            return;
        this.connection.close();
    }

    public boolean hasConnection() {
        return this.connection != null;
    }

    @SneakyThrows
    public void saveFile() {

        if (!this.plugin.getDataFolder().exists())
            this.plugin.getDataFolder().mkdirs();

        if (!file.exists())
            file.createNewFile();
    }

}
