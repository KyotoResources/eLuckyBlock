package it.zS0bye.eLuckyBlock.mysql.tables;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import it.zS0bye.eLuckyBlock.mysql.SQLConnection;
import lombok.Getter;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ScoreTable {

    private final SQLConnection sqlConnection;
    private Connection connection;

    private final static Map<String, Integer> luckyScore = new HashMap<>();

    public ScoreTable(final ELuckyBlock plugin) {
        this.sqlConnection = plugin.getSqlConnection();
        if(this.sqlConnection.hasClosedConnection()) return;
        this.connection = this.sqlConnection.getConnection();
        this.create();
    }

    protected void create() {
        CompletableFuture.runAsync(() -> {
            final String sql = "CREATE TABLE IF NOT EXISTS breaks(id int AUTO_INCREMENT PRIMARY KEY, PlayerName varchar(30) NOT NULL, Numbers int NOT NULL)";
            try (final Statement st = this.connection.createStatement()) {
                st.execute(sql);
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public boolean doesNotExist(final String player) {
        final String sql = "SELECT Numbers FROM breaks WHERE PlayerName = ?";
        try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
            pst.setString(1, player);
            try (final ResultSet rs = pst.executeQuery()) {
                return !rs.next();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public void initScore(final String player) {
        if(this.sqlConnection.hasClosedConnection()) return;
        if(!this.doesNotExist(player)) return;
        final String sql = "INSERT INTO breaks(PlayerName,Numbers) VALUES(?,?)";
        CompletableFuture.runAsync(() -> {
            try (final PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setString(1, player);
                pst.setInt(2, 0);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void addScore(final String player, final int value) {
        if(this.sqlConnection.hasClosedConnection()) return;
        if(this.doesNotExist(player)) return;
        final String sql = "UPDATE breaks SET Numbers = ? WHERE PlayerName = ?";
        CompletableFuture.runAsync(() -> {
            try (final PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setInt(1, value);
                pst.setString(2, player);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public int getScore(final String player) {
        if (this.sqlConnection.hasClosedConnection()) return 0;
        if (this.doesNotExist(player)) return 0;
        final String sql = "SELECT Numbers FROM breaks WHERE PlayerName = ?";
        try (final PreparedStatement pst = this.connection.prepareStatement(sql)) {
            pst.setString(1, player);
            try (final ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) return 0;
                return rs.getInt("Numbers");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public void importScore(final String player) {
        if(luckyScore.containsKey(player)) return;
        luckyScore.put(player, this.getScore(player));
    }

    public void exportScore(final String player) {
        if(!luckyScore.containsKey(player)) return;
        this.addScore(player, this.getScoreMap(player));
    }

    public void addScore(final String player) {
        if (luckyScore.containsKey(player)) {
            luckyScore.put(player, this.getScoreMap(player) + 1);
            return;
        }
        luckyScore.put(player, this.getScore(player) + 1);
    }

    public int getScoreMap(final String player) {
        if(!luckyScore.containsKey(player)) return 0;
        return luckyScore.get(player);
    }

    public Map<String, Integer> getLuckyScore() {
        return luckyScore;
    }
}
