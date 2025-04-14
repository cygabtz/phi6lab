package Main;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    Connection c;               //Variable de conexión
    public Statement query;            //Variablde de consulta
    String user, password, databaseName;
    boolean connected = false;  //Estado de la conexión

    public DataBase(String user, String password, String databaseName) {
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
    }

    public void connect() {
        try {
            //Class.forName("com.mysql.jbdc.Driver");
            c = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + databaseName, user, password);
            //Esta instrucción puede fallar por: usuario, contraseña o permisos.

            query = c.createStatement(); //Query vacía
            System.out.println("Conexión exitosa con la base de datos '" + databaseName + "'");
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









    // ==== Funciones prescindibles =====

    public String getInfo(String tableName, String columnName, String keyName, String keyValue) {
        String q = " SELECT " + columnName +
                " FROM " + tableName +
                " WHERE " + keyName + " = '" + keyValue + "' ";
        System.out.println("Query creada: " + q);

        try {
            ResultSet rs = query.executeQuery(q);
            rs.next();
            System.out.println("Query exitosa: " + q);
            return rs.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public String[] getInfoArray(String tableName, String columnName) {
        int n = getRowsNumFrom(tableName);
        String[] info = new String[n];

        String q = " SELECT " + columnName +
                " FROM " + tableName +
                " ORDER BY " + columnName +
                " ASC";

        try {
            ResultSet rs = query.executeQuery(q);
            int f = 0;
            while (rs.next()) {
                info[f] = rs.getString(1);
                f++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public int getRowsNumFrom(String tableName) {
        String q = " SELECT COUNT(*) AS num FROM " + tableName;

        try {
            ResultSet rs = query.executeQuery(q);
            rs.next();
            return rs.getInt("num");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Devuelve un todos los datos de la tabla `tipo`
    public String[][] getInfoArray2DTipo() {

        int nf = getRowsNumFrom("tipo");
        int numCol = 2;
        String[][] info = new String[nf][numCol];
        String s = " SELECT * FROM " + "tipo" +
                " ORDER BY " + "idTIPO" +
                " ASC";

        try {
            ResultSet rs = query.executeQuery(s);
            int f = 0;
            while (rs.next()) {
                info[f][0] = String.valueOf(rs.getInt("idTIPO"));
                info[f][1] = rs.getString("NOMBRE");
                f++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    // Devuelve todas las columnas de los elementos de la tabla `elemento`
    // cuya columna `SENTIDO` es `ABAJO`

    public String[][] getInfoArray2DElementoABAJO() {
        //Instrucción para conocer el número de filas que cumplen con la restricción
        String qRW = "SELECT COUNT(*) AS num FROM `elemento` WHERE SENTIDO = 'ABAJO' ";
        int numRows = getRowsNumMatchQuery(qRW);

        //Inicialización del String.
        int numCol = 5;
        String[][] info = new String[numRows][numCol];

        String q = " SELECT idELEMENTO, RANGO, SENTIDO, VALOR, UBICACION" +
                " FROM elemento" +
                " WHERE SENTIDO = 'ABAJO'";

        try {
            ResultSet rs = query.executeQuery(q);
            int n = 0;
            while (rs.next()) {
                info[n][0] = String.valueOf(rs.getInt("idELEMENTO"));
                info[n][1] = String.valueOf(rs.getFloat("RANGO"));
                info[n][2] = rs.getString("SENTIDO");
                info[n][3] = String.valueOf(rs.getFloat("VALOR"));
                info[n][4] = String.valueOf(rs.getFloat("UBICACION"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public int getRowsNumMatchQuery(String q) {
        try {
            ResultSet rs = query.executeQuery(q);
            rs.next();
            return rs.getInt("num");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Devuelve información de dos tablas relacionadas con una clave externa y otra interna.
    //Crea una arreglo 2D con la clave primaria de un elemento y sus correspondiente TIPO
    public String[][] getTipoOfElement() {

        String qNF = " SELECT COUNT(*) AS num" +
                " FROM elemento e, tipo t" +
                " WHERE e.TIPO_idTIPO = t.idTIPO";
        int numRows = getRowsNumMatchQuery(qNF);

        String q = " SELECT e.idELEMENTO, t.NOMBRE" +
                " FROM elemento e, tipo t" +
                " WHERE e.TIPO_idTIPO = t.idTIPO";

        int numCol = 2;
        String[][] info = new String[numRows][numCol];

        try {
            ResultSet rs = query.executeQuery(q);
            int n = 0;
            while (rs.next()) {
                info[n][0] = String.valueOf(rs.getInt("e.idELEMENTO"));
                info[n][1] = rs.getString("t.NOMBRE");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info;
    }

    //Devuelve el total de las fuerzas puntuales
    public float totalFuerzasPuntuales() {
        String q = " SELECT SUM(e.VALOR) AS total, t.idTIPO" +
                " FROM elemento e, tipo t" +
                " WHERE e.TIPO_idTIPO = t.idTIPO";

        float total = 0;

        try {
            ResultSet rs = query.executeQuery(q);
            rs.next();
            total = rs.getFloat("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public float getFuerzasPuntuales() {
        String q = " SELECT SUM(e.VALOR) AS total, t.idTIPO" +
                " FROM elemento e, tipo t" +
                " WHERE e.TIPO_idTIPO = t.idTIPO";

        float total = 0;

        try {
            ResultSet rs = query.executeQuery(q);
            rs.next();
            total = rs.getFloat("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    public boolean isUserPass(String userName, String password) {
        String q = " SELECT COUNT(*) AS found" +
                " FROM `usuario`" +
                " WHERE NOMBRE='" + userName + "'" +
                " AND PASSWORD='" + password + "'";
        try {
            ResultSet rs = query.executeQuery(q);
            rs.next();
            return rs.getInt("found") == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertUser(String userName, String password) {
        String q = " INSERT INTO `usuario` (`NOMBRE`, `PASSWORD`)" +
                " VALUES ('" + userName + "', '" + password + "') ";

        try {
            query.execute(q);
        } catch (SQLIntegrityConstraintViolationException e1) {
            System.out.println("USUARIO Y/0 CONTRASEÑA DUPLICADOS");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteUser(String userName) {
        String q = " DELETE FROM usuario WHERE NOMBRE = '" + userName + "' ";

        try {
            query.execute(q);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateUser(String originalUser, String newUserName, String newPassword) {
        String q = " UPDATE usuario " +
                " SET NOMBRE = '" + newUserName + "', PASSWORD = '" + newPassword + "' " +
                " WHERE NOMBRE = '" + originalUser + "'";

        try {
            query.execute(q);
            System.out.println("Usuario '" + originalUser + "' modificado a '"
                    + newUserName + "' con contraseña " + newPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[][] getInfoSimuladores(){
        //Pendiente filtrar por usuario

        int nf = getRowsNumFrom("simulador");
        int numCol = 4;
        String[][] info = new String[nf][numCol];
        String s = " SELECT * FROM " + "simulador" +
                " ORDER BY " + "idSIMULADOR" +
                " ASC";

        try {
            ResultSet rs = query.executeQuery(s);
            int n = 0;
            while (rs.next()) {
                info[n][0] = String.valueOf(rs.getInt("idSIMULADOR"));
                info[n][1] = rs.getString("TITULO");
                info[n][2] = rs.getString("CREACION");
                info[n][3] = rs.getString("MODIFICACION");
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public ArrayList<String[]> getInfoSimuladoresArrayList(){
        //Pendiente filtrar por usuario

        ArrayList<String[]> wholeTable = new ArrayList<>();

        String q = " SELECT * FROM " + "simulador" +
                " ORDER BY " + "idSIMULADOR" +
                " ASC";

        try {
            ResultSet rs = query.executeQuery(q);
            while (rs.next()) {
                wholeTable.add(new String[]{
                        String.valueOf(rs.getInt("idSIMULADOR")),
                        rs.getString("TITULO"),
                        rs.getString("CREACION"),
                        rs.getString("MODIFICACION")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wholeTable;
    }


}
