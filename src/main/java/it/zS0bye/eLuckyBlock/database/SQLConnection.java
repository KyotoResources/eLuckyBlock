package it.zS0bye.eLuckyBlock.database;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.*;

@Getter
public class SQLConnection {

    private final ELuckyBlock plugin;
    protected Connection connection;
    private File file;

    @SneakyThrows
    public SQLConnection(final ELuckyBlock plugin, final String URI, final String username, final String password) {
        this.plugin = plugin;
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection(URI, username, password);
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
        if(this.connection == null) {
            return;
        }
        this.connection.close();
    }

    public boolean hasConnection() {
        return this.connection != null;
    }

    @SneakyThrows
    public void saveFile() {

        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
    }

}
