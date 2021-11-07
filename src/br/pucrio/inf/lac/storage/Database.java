package br.pucrio.inf.lac.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public static String status;
    public static String serverName;
    public static String mydatabase;
    public static String url;
    public static String username;
    public static String password;

    public Database(String name, String database, String userPass) {
        status = "Não conectou...";
        serverName = name;
        mydatabase = database;
        url = "jdbc:mysql://" + serverName + ":3306/" + mydatabase;
        username = "root";
        password = userPass;
    }

    public static java.sql.Connection getConexaoMySQL() {
        Connection connection = null;
        
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                status = ("STATUS--->Conectado com sucesso!");
            } else {
                status = ("STATUS--->Não foi possivel realizar conexão");
            }
            
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("O driver expecificado nao foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao Banco de Dados.");
            return null;
        }
    }

    public static String statusConection() {
        return status;
    }

    public static boolean FecharConexao() {
        try {
        	Database.getConexaoMySQL().close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static java.sql.Connection ReiniciarConexao() {
        FecharConexao();
        return Database.getConexaoMySQL();
    }
    
    public static ResultSet ExecuteSelectTransaction(String query) {
    	Statement stmt;
		try {
			stmt = Database.getConexaoMySQL().createStatement();
	    	return stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static boolean ExecuteInsertTransaction(String query) {
    	Statement stmt;
		try {
			stmt = Database.getConexaoMySQL().createStatement();
			return stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
    }
}

