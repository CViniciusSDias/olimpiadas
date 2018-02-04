package factories;

import java.sql.*;
import java.io.File;

/**
 * Singleton que me retorna uma conexão com o banco de dados
 */
public class ConnectionFactory
{
    private static Connection con;
    public static Connection getConnection()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(
                "jdbc:sqlite:olimpiadas.bd"
            );

            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null; //Compilador burro
        }
    }
}