package it.zS0bye.eLuckyBlock.database;

import it.zS0bye.eLuckyBlock.eLuckyBlock;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.sql.*;

@Getter
public class SQLConnection {

    private final eLuckyBlock plugin;
    protected Connection connection;
    private File file;

    public SQLConnection(final eLuckyBlock plugin, final String URI, final String username, final String password) throws Exception {
        this.plugin = plugin;
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection(URI, username, password);
    }

    public SQLConnection(final eLuckyBlock plugin) throws Exception {
        this.plugin = plugin;
        this.file = new File(this.plugin.getDataFolder(), "database.db");
        this.saveFile();
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:"+file.getAbsolutePath());
    }

    public void closeConnection() throws SQLException {
        if(this.connection == null) {
            return;
        }
        this.connection.close();
    }

    public boolean hasConnection() {
        return this.connection != null;
    }

    public void saveFile() {

        if (!this.plugin.getDataFolder().exists()) {
            this.plugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
