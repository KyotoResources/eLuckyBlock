/*
 * The LuckyBlock Plugin you needed! - https://github.com/KyotoResources/eLuckyBlock
 * Copyright (c) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
            try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                pst.setString(1, name);
                try (ResultSet rs = pst.executeQuery()) {
                    return !rs.next();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
                try (PreparedStatement pst = this.connection.prepareStatement(sql)) {
                    pst.setString(1, name);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) future.complete(rs.getInt("Numbers"));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        });
        return  future;
    }


}
