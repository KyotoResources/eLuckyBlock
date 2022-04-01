package it.zS0bye.eLuckyBlock.mysql.tables;

import it.zS0bye.eLuckyBlock.ELuckyBlock;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class ScoreTable {

    private Connection connection;

    public ScoreTable(final ELuckyBlock plugin) {

        if(!plugin.getSqlConnection().hasConnection())
            return;

        this.connection = plugin.getSqlConnection().getConnection();
        this.create();
    }

    protected void create() {
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS breaks(id int AUTO_INCREMENT PRIMARY KEY, PlayerName varchar(30) NOT NULL, Numbers int NOT NULL)";
            try (Statement st = this.connection.createStatement()) {
                st.execute(sql);
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<Boolean> hasNotScore(final String name) {
        String sql = "SELECT Numbers FROM breaks WHERE PlayerName = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement pst = this.connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
                pst.setString(1, name);
                return !rs.next();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return true;
        });
    }

    public void setScore(final String name) {
        String sql = "INSERT INTO breaks(PlayerName,Numbers) VALUES(?,?)";
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setString(1, name);
                pst.setInt(2, 0);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void addScore(final String name, final int luckybreaks) {
        String sql = "UPDATE breaks SET Numbers = ? WHERE PlayerName = ?";
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setInt(1, luckybreaks);
                pst.setString(2, name);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });


    }

    public CompletableFuture<Integer> getScore(final String name) {
        String sql = "SELECT Numbers FROM breaks WHERE PlayerName = ?";
        CompletableFuture<Integer> future = new CompletableFuture<>();
        hasNotScore(name).thenAccept(check -> {
            if(check) {
                setScore(name);
            }
            CompletableFuture.runAsync(() -> {
                try (PreparedStatement pst = this.connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
                    pst.setString(1, name);
                    if (rs.next()) {
                        future.complete(rs.getInt("Numbers"));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
        return  future;
    }


}
