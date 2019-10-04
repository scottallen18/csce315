import java.sql.*;
import javax.swing.JOptionPane;
//import java.sql.DriverManager;

public class postgreSQLConnection {
  public static void main(String args[]) {
    //Building the connection
     Connection conn = null;
     try {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://db-315.cse.tamu.edu/brian.lu32_605",
           dbSetup.username, dbSetup.password);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }
     System.out.println("Opened database successfully");

     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
       String sqlStatement = "SELECT primaryName FROM name_basics WHERE primaryName LIKE '%Fred'";
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       System.out.println("First names from the Database.");
       System.out.println("______________________________________");
       while (result.next()) {
         System.out.println(result.getString("primaryName"));
       }
   } catch (Exception e){
     System.out.println("Error accessing Database.");
   }

    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }
  }
}
