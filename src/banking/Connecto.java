package banking;

import java.sql.*;

class Connecto {
    Connection conn;

    public Connection connect(String dtb) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.out.println(e);
        }
        String connect = "jdbc:sqlite:" + dtb;

        try {
            conn = DriverManager.getConnection(connect);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }
        return conn;
    }


    public void createCard() {
        //execute("delete from card");
        String sql = "CREATE TABLE IF NOT EXISTS card( \n"
                + "id INTEGER, \n"
                + "number TEXT, \n"
                + "pin TEXT, \n"
                + "balance INTEGER DEFAULT 0);";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void putCard(long id, long number, int pin, long balance) {
        String sql;
        try {
            sql = String.format("Insert into card(id, number, pin, balance) \n" +
                    "values(%d,%s,%s,%d)", id, number, pin, balance);

            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getAccount(long id) {
        String sql = String.format("SELECT id, number, pin, balance FROM card WHERE id=%d;", id);
        ResultSet rs;
        try {
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public long maxNum() {
        String sql = "Select Max(id) as id from card;";
        ResultSet rs;
        try {
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            rs.next();
            long x = rs.getLong("id");
            statement.close();
            if (x <= 11111l) {
                return 11111l;
            } else {
                if (Long.toString(x).length() >= 6) {
                    return x / 10 + 1;
                } else {

                    return x;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return 11111l;
        }
    }

    public void execute(String sql) {
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (Exception e) {

        }
    }
}

