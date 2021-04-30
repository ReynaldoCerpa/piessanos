package viewControllers;

import java.sql.*;

public class SQLconnector {
    public SQLconnector(){

    }

    public ResultSet connectSQL(String tableName){
        ResultSet myRs = null;
        String strQuery = "SELECT * from $tableName";
        String query = strQuery.replace("$tableName",tableName);
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/piessanos","root","pass");
            PreparedStatement prStmt = myConn.prepareStatement(strQuery);
            myRs = prStmt.executeQuery(query);
            //myRs.next();

        } catch (Exception e){
            e.printStackTrace();
        }
        return myRs;
    }

    public PreparedStatement updateData(String sql){
        PreparedStatement myStmt = null;
        try{
            Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/piessanos","root","pass");
            myStmt = myConn.prepareStatement(sql);
            /*myStmt = myConn.createStatement();
            myStmt.executeUpdate(sql);*/

        } catch (Exception e){
            e.printStackTrace();
        }
        return myStmt;
    }
}
