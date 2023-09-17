package it.zS0bye.eLuckyBlock.mysql.tables;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class LuckyTable {

    private Connection connection;

    public LuckyTable(final ELuckyBlock plugin) {
        if(plugin.getSqlConnection().hasClosedConnection()) return;
        this.connection = plugin.getSqlConnection().getConnection();
        this.create();
    }

    protected void create() {
        CompletableFuture.runAsync(() -> {
            String sql = "CREATE TABLE IF NOT EXISTS luckyblocks(id int AUTO_INCREMENT PRIMARY KEY, Location text NOT NULL, LuckyName text NOT NULL)";
            try (Statement st = this.connection.createStatement()) {
                st.execute(sql);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<Boolean> hasNotLocation(final String location) {
        final String sql = "SELECT LuckyName FROM luckyblocks WHERE Location = ?";
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setString(1, location);
                ResultSet rs = pst.executeQuery();
                return !rs.next();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return true;
        });
    }

    public void setLocation(final String location, final String name) {
        final String sql = "INSERT INTO luckyblocks(Location,LuckyName) VALUES(?,?)";
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setString(1, location);
                pst.setString(2, name);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void remLocation(final String location) {
        final String sql = "DELETE FROM luckyblocks WHERE Location = ?";
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setString(1, location);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<String> getLuckyBlock(final String location) {
        final String sql = "SELECT LuckyName FROM luckyblocks WHERE Location = ?";
        CompletableFuture<String> future = new CompletableFuture<>();
        hasNotLocation(location).thenAccept(check -> {
            if(check) {
                return;
            }
            CompletableFuture.runAsync(() -> {
                try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                    pst.setString(1, location);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        future.complete(rs.getString("LuckyName"));
                        remLocation(location);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
        return  future;
    }

    public void fixLocations() {
        final String sql = "SELECT Location FROM luckyblocks ORDER BY LuckyName";
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String location = rs.getString("Location");
                    String world = location.split(", ")[0];
                    double x = Double.parseDouble(location.split(", ")[1]);
                    double y = Double.parseDouble(location.split(", ")[2]);
                    double z = Double.parseDouble(location.split(", ")[3]);
                    Block checkBlock = new Location(Bukkit.getWorld(world), x, y, z).getBlock();
                    if(checkBlock.getType() == Material.AIR)
                        remLocation(location);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }


}
