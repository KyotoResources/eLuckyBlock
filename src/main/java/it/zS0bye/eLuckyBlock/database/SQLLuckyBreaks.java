package it.zS0bye.eLuckyBlock.database;

import it.zS0bye.eLuckyBlock.ELuckyBlock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;

public class SQLLuckyBreaks {

    private final SQLConnection sql;

    public SQLLuckyBreaks(final ELuckyBlock plugin) {
        this.sql = plugin.getSqlConnection();
        this.create();
    }

    protected void create() {
        CompletableFuture.runAsync(() -> {
            if (!this.sql.hasConnection()) {
                return;
            }
            String sql = "CREATE TABLE IF NOT EXISTS breaks(id int AUTO_INCREMENT PRIMARY KEY, PlayerName varchar(30) NOT NULL, Numbers int NOT NULL)";
            try {
                Statement st = this.sql.getConnection().createStatement();
                st.execute(sql);
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public CompletableFuture<Boolean> hasNotLuckyBreaks(final String name) {
        return CompletableFuture.supplyAsync(() -> {
            PreparedStatement pst = null;
            ResultSet rs = null;
            try {
                pst = this.sql.getConnection().prepareStatement("SELECT Numbers FROM breaks WHERE PlayerName = ?");
                pst.setString(1, name);
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

    public void setLuckyBreaks(final String name) {
        CompletableFuture.runAsync(() -> {
            PreparedStatement pst = null;
            try {
                pst = this.sql.getConnection().prepareStatement("INSERT INTO breaks(PlayerName,Numbers) VALUES(?,?)");
                pst.setString(1, name);
                pst.setInt(2, 0);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                close(pst);
            }
        });
    }

    public void addLuckyBreaks(final String name, final int luckybreaks) {
        CompletableFuture.runAsync(() -> {
            PreparedStatement pst = null;
            try {
                pst = this.sql.getConnection().prepareStatement("UPDATE breaks SET Numbers = ? WHERE PlayerName = ?");
                pst.setInt(1, luckybreaks);
                pst.setString(2, name);
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                close(pst);
            }
        });


    }

    public CompletableFuture<Integer> getLuckyBreaks(final String name) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        hasNotLuckyBreaks(name).thenAccept(check -> {
            if(check) {
                setLuckyBreaks(name);
            }
            CompletableFuture.runAsync(() -> {
                PreparedStatement pst = null;
                ResultSet rs = null;
                try {
                    pst = this.sql.getConnection().prepareStatement("SELECT Numbers FROM breaks WHERE PlayerName = ?");
                    pst.setString(1, name);
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        future.complete(rs.getInt("Numbers"));
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

    private void close(final PreparedStatement pst) {
        try { pst.close(); } catch (Exception e) { e.printStackTrace();}
    }

    private void close(final PreparedStatement pst, ResultSet rs) {
        try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
        try { pst.close(); } catch (Exception e) { e.printStackTrace();}
    }

}
