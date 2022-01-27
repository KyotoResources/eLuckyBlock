package it.zS0bye.eLuckyBlock.database;

import it.zS0bye.eLuckyBlock.ELuckyBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public class SQLLuckyBlocks {

    private final SQLConnection sql;

    public SQLLuckyBlocks(final ELuckyBlock plugin) {
        this.sql = plugin.getSqlConnection();
        this.create();
    }

    protected void create() {
        CompletableFuture.runAsync(() -> {
            if (!this.sql.hasConnection()) {
                return;
            }
            String sql = "CREATE TABLE IF NOT EXISTS luckyblocks(id int AUTO_INCREMENT PRIMARY KEY, Location text NOT NULL, LuckyName text NOT NULL)";
            try {
                Statement st = this.sql.getConnection().createStatement();
                st.execute(sql);
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<Boolean> hasNotLocation(final String location) {
        return CompletableFuture.supplyAsync(() -> {
            PreparedStatement pst = null;
            ResultSet rs = null;
            try {
                pst = this.sql.getConnection().prepareStatement("SELECT LuckyName FROM luckyblocks WHERE Location = ?");
                pst.setString(1, location);
                rs = pst.executeQuery();
                return !rs.next();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                close(pst, rs);
            }
            return true;
        });
    }

    public void setLocation(final String location, final String name) {
        CompletableFuture.runAsync(() -> {
            PreparedStatement pst = null;
            try {
                pst = this.sql.getConnection().prepareStatement("INSERT INTO luckyblocks(Location,LuckyName) VALUES(?,?)");
                pst.setString(1, location);
                pst.setString(2, name);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                close(pst);
            }
        });
    }

    public void remLocation(final String location) {
        CompletableFuture.runAsync(() -> {
            PreparedStatement pst = null;
            try {
                pst = this.sql.getConnection().prepareStatement("DELETE FROM luckyblocks WHERE Location = ?");
                pst.setString(1, location);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                close(pst);
            }
        });
    }

    public CompletableFuture<String> getLuckyBlock(final String location) {
        CompletableFuture<String> future = new CompletableFuture<>();
        hasNotLocation(location).thenAccept(check -> {
            if(check) {
                return;
            }
            CompletableFuture.runAsync(() -> {
                PreparedStatement pst = null;
                ResultSet rs = null;
                try {
                    pst = this.sql.getConnection().prepareStatement("SELECT LuckyName FROM luckyblocks WHERE Location = ?");
                    pst.setString(1, location);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        future.complete(rs.getString("LuckyName"));
                        remLocation(location);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } finally {
                    close(pst, rs);
                }
            });
        });
        return  future;
    }

    public void fixLocations() {
        CompletableFuture.runAsync(() -> {
            PreparedStatement pst = null;
            ResultSet rs = null;
            try {
                pst = this.sql.getConnection().prepareStatement("SELECT Location FROM luckyblocks ORDER BY LuckyName");
                rs = pst.executeQuery();
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
            } finally {
                close(pst, rs);
            }
        });
    }

    private void close(final PreparedStatement pst) {
        try { pst.close(); } catch (Exception e) { e.printStackTrace();}
    }

    private void close(final PreparedStatement pst, ResultSet rs) {
        try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
        try { pst.close(); } catch (Exception e) { e.printStackTrace();}
    }

    public String convertLoc(final Location loc) {
        String world = loc.getWorld().getName();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return world + ", " + x + ", " + y + ", " + z;
    }

}
