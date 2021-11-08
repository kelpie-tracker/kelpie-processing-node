package br.pucrio.inf.lac.storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.pucrio.inf.lac.model.Fence;

public class Storage {
	Database database;
	
    public Storage() {
    	database = new Database("0.0.0.0", "testedb", "MySql2019!");
    }
    
    public void AddNewPosition(String id, String data, Double latitude, Double longitude) {
        String sql = "INSERT INTO Positions (id, date, longitude, latitude)" +
        			 "VALUES ('" + id + "','"+ data + "'," + latitude + "," + longitude + ")";

    	boolean result = database.ExecuteInsertTransaction(sql);
    }
    
    public ArrayList GetGeoFences() {
    	ArrayList fences = new ArrayList();
        String sql = "SELECT * FROM Fences";

        ResultSet result = database.ExecuteSelectTransaction(sql);
        try {
			while ( result.next() ) {
			    float up = result.getFloat("up");
			    float bottom = result.getFloat("bottom");
			    float right = result.getFloat("right");
			    float left = result.getFloat("left");
			    fences.add(new Fence(up,bottom,right,left));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return fences;
    }
}
