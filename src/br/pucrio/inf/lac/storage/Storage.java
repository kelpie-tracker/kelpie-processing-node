package br.pucrio.inf.lac.storage;

import java.sql.ResultSet;

public class Storage {
	Database database;
	
    public Storage() {
    	database = new Database("0.0.0.0", "testedb", "MySql2019!");
    }
    
    public void AddNewPosition(String id, String time, String longitude, String latitude) {
        String sql = "INSERT INTO Positions" +
        			 "VALUES (" + id + ","+ time + "," + longitude + "," + latitude + ")";

    	ResultSet result = database.ExecuteTransaction(sql);
    }
    
    
}
