package Data;

import Player.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private static Connection conn = null;
    private static Statement stmt = null;

    static {
        createConnection();
    }

    private DatabaseHandler() {
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    Connection getConnection() {
        return conn;
    }

    private static void createConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Locale.setDefault(Locale.ENGLISH);
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "USER007", "1111");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return result;
    }

    public static void main(String[] args) {
        DatabaseHandler.getInstance();
    }

    public ResultSet singUser(String name, String password) throws SQLException {
        String sql = "SELECT * FROM MANAGER WHERE LAST_NAME = ? AND PASSWORD = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, password);
        return ps.executeQuery();
    }

    public int getId(String team_name, String role_position) throws SQLException {
        String sql = "SELECT player.id FROM player WHERE player.CLUB_ID = (SELECT id FROM club WHERE team_name = ?) AND player.role_position = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, team_name);
        ps.setString(2, role_position);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next())
            return resultSet.getInt("id");
        return -1;
    }

    public boolean updateMember(Player player, int ID) {
        try {
            String update = "UPDATE USER007.PLAYER t\n" +
                    "SET t.\"NAME\"        = ?,\n" +
                    "    t.HEIGHT        = ?,\n" +
                    "    t.WEIGHT        = ?,\n" +
                    "    t.NUMDER        = ?,\n" +
                    "    t.ROLE_POSITION = ?,\n" +
                    "    t.CLUB_ID       = (SELECT id\n" +
                    "                       FROM club\n" +
                    "                       WHERE team_name = ?)\n" +
                    "WHERE t.ID = ?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, player.getName());
            stmt.setInt(2, player.getHeight());
            stmt.setFloat(3, player.getWeight());
            stmt.setInt(4, player.getNumber());
            stmt.setString(5, player.getRole());
            stmt.setString(6, player.getTeam_name());
            stmt.setString(7, String.valueOf(ID));
            int res = stmt.executeUpdate();
            return (res > 0);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteMember(Player player) {
        try {
            String deleteStatement = "DELETE FROM player WHERE NAME = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, player.getName());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}