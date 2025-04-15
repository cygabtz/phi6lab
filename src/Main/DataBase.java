package Main;

import java.sql.*;
import java.util.ArrayList;

/**
 * Clase que gestiona la conexión con la base de datos y permite ejecutar operaciones SQL.
 *
 * <p>Incluye métodos para:
 * <ul>
 *     <li>Conectarse a la base de datos MySQL</li>
 *     <li>Ejecutar consultas y actualizaciones</li>
 *     <li>Autenticar usuarios</li>
 *     <li>Insertar nuevos usuarios</li>
 *     <li>Obtener y guardar simuladores</li>
 * </ul>
 *
 * Esta clase es utilizada en todas las pantallas que necesitan acceder a los datos,
 * especialmente {@link Screens.LoginScreen}, {@link Screens.HomeScreen} y {@link Screens.SimulatorScreen}.
 *
 * @author [Tu nombre]
 * @see java.sql.Connection
 * @see java.sql.Statement
 * @see java.sql.ResultSet
 */
public class DataBase {
    Connection c;               //Variable de conexión
    public Statement query;            //Variablde de consulta
    String user, password, databaseName;
    boolean connected = false;  //Estado de la conexión

    /**
     * Instancia los atributos necesarios para activar la base de datos
     *
     * @param user nombre de usuario de MySQL
     * @param password contraseña de MySQL
     * @param databaseName nombre de la base de datos
     */
    public DataBase(String user, String password, String databaseName) {
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
    }

    /**
     * Establece una conexión con la base de datos MySQL utilizando los parámetros configurados.
     *
     * <p>Intenta conectarse al servidor local (localhost) en el puerto 3306,
     * a la base de datos especificada por {@code databaseName}, usando el
     * {@code user} y {@code password} definidos previamente.
     *
     * <p>Si la conexión es exitosa:
     * <ul>
     *   <li>Se crea un {@link java.sql.Statement} vacío para ejecutar consultas</li>
     *   <li>Se imprime un mensaje de confirmación en consola</li>
     *   <li>Se actualiza el estado {@code connected = true}</li>
     * </ul>
     *
     * <p>En caso de fallo (por credenciales incorrectas, permisos, base inexistente...),
     * el error es impreso en la consola con {@code e.printStackTrace()}.
     *
     * @see java.sql.DriverManager#getConnection(String, String, String)
     * @see java.sql.Statement
     * @see java.sql.Connection
     */
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

    /**
     * Ejecuta una consulta SQL de conteo (por ejemplo, {@code SELECT COUNT(*) AS num ...})
     * y devuelve el número de filas resultantes.
     *
     * <p>La consulta debe estar formulada de forma que devuelva un único valor con el alias {@code num},
     * es decir, debe incluir: {@code SELECT COUNT(*) AS num FROM ...}
     *
     * @param q consulta SQL que devuelve un conteo con alias {@code num}
     * @return número de filas coincidentes; 0 si ocurre un error o no hay resultados
     *
     * @see java.sql.ResultSet
     * @see java.sql.Statement#executeQuery(String)
     */
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

    
    public String[][] getInfoSimuladores(String usuario) {
        String[][] info = new String[0][];
        try {
            // Contar filas
            String countQuery = "SELECT COUNT(*) AS total FROM simulador WHERE USUARIO_NOMBRE = '" + usuario + "'";
            ResultSet countRs = query.executeQuery(countQuery);
            int nf = 0;
            if (countRs.next()) {
                nf = countRs.getInt("total");
            }

            // Inicializar array
            info = new String[nf][4];

            // Consulta real
            String q = "SELECT idSIMULADOR, TITULO, CREACION, MODIFICACION " +
                    "FROM simulador " +
                    "WHERE USUARIO_NOMBRE = '" + usuario + "' " +
                    "ORDER BY idSIMULADOR ASC";

            ResultSet rs = query.executeQuery(q);
            int i = 0;
            while (rs.next() && i < nf) {
                info[i][0] = String.valueOf(rs.getInt("idSIMULADOR"));
                info[i][1] = rs.getString("TITULO");
                info[i][2] = rs.getString("CREACION");
                info[i][3] = rs.getString("MODIFICACION");
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * Verifica si ya existe un usuario con el nombre especificado en la base de datos.
     *
     * <p>Ejecuta una consulta sobre la tabla {@code usuario} buscando coincidencias
     * exactas en el campo {@code NOMBRE}.
     *
     * <p>Este método es útil para evitar duplicación de usuarios durante el registro.
     *
     * @param nombre nombre del usuario a comprobar
     * @return {@code true} si el usuario existe o si ocurre un error; {@code false} si no existe
     */
    public boolean existsUser(String nombre) {
        try {
            ResultSet rs = query.executeQuery("SELECT * FROM usuario WHERE NOMBRE = '" + nombre + "'");
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return true; // asumimos que sí existe si falla
        }
    }

    /**
     * Inserta un nuevo usuario en la tabla {@code usuario} con el nombre y contraseña especificados.
     *
     * <p>Este método debe usarse después de verificar que el usuario no existe previamente,
     * utilizando {@link #existsUser(String)}.
     *
     * @param nombre nombre del nuevo usuario
     * @param contrasena contraseña del nuevo usuario
     */
    public void insertNewUser(String nombre, String contrasena) {
        try {
            String q = "INSERT INTO usuario (NOMBRE, PASSWORD) VALUES ('" + nombre + "', '" + contrasena + "')";
            query.execute(q);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
