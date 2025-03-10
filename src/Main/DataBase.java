package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBase {
    Connection c;               //Variable de conexión
    Statement query;            //Variablde de consulta
    String user, password, databaseName;
    boolean connected = false;  //Estado de la conexión

    public DataBase(String user, String password, String databaseName){
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
    }

    public void connect(){
        try {
            //Class.forName("com.mysql.jbdc.Driver");
            c = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+databaseName, user, password);
            //Esta instrucción puede fallar por: usuario, contraseña o permisos.

            query = c.createStatement(); //Query vacía
            System.out.println("Conexión exitosa con la base de datos "+databaseName);
            connected = true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
