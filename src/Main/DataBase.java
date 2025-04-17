package Main;

import java.sql.*;

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
    /** Objeto que representa la conexión activa con la base de datos MySQL. */
    Connection c;

    /** Objeto que permite ejecutar sentencias SQL (consultas y actualizaciones). */
    public Statement query;

    /** Nombre de usuario utilizado para conectarse a la base de datos. */
    String user;

    /** Contraseña asociada al usuario para acceder a la base de datos. */
    String password;

    /** Nombre de la base de datos a la que se desea conectar. */
    String databaseName;

    /** Bandera que indica si la conexión con la base de datos se ha establecido correctamente. */
    boolean connected = false;

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

    /**
     * Verifica si existe un usuario en la base de datos con el nombre y contraseña especificados.
     *
     * @param userName nombre del usuario
     * @param password contraseña correspondiente
     * @return {@code true} si se encuentra exactamente un usuario coincidente, {@code false} en caso contrario
     */
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

    /**
     * Inserta un nuevo usuario en la tabla {@code usuario}.
     *
     * <p>En caso de duplicación del nombre o contraseña, se lanza una excepción
     * de restricción de integridad (por clave única).
     *
     * @param userName nombre del nuevo usuario
     * @param password contraseña del nuevo usuario
     */
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

    /**
     * Elimina un usuario de la base de datos según su nombre.
     *
     * @param userName nombre del usuario a eliminar
     */
    public void deleteUser(String userName) {
        String q = " DELETE FROM usuario WHERE NOMBRE = '" + userName + "' ";

        try {
            query.execute(q);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el nombre y la contraseña de un usuario existente.
     *
     * @param originalUser nombre actual del usuario
     * @param newUserName  nuevo nombre a asignar
     * @param newPassword  nueva contraseña
     */
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

    /**
     * Recupera la información básica de todos los simuladores pertenecientes a un usuario específico.
     *
     * <p>Este método consulta la base de datos y obtiene los campos:
     * <ul>
     *   <li>{@code idSIMULADOR}: identificador único del simulador</li>
     *   <li>{@code TITULO}: título del simulador</li>
     *   <li>{@code CREACION}: fecha de creación</li>
     *   <li>{@code MODIFICACION}: fecha de última modificación</li>
     * </ul>
     *
     * <p>La consulta se filtra por el nombre de usuario indicado en el parámetro {@code usuario},
     * y los resultados se ordenan por el identificador de simulador de forma ascendente.
     *
     * @param usuario nombre del usuario cuyas simulaciones se quieren recuperar
     * @return un arreglo bidimensional de tipo {@code String[][]}, donde cada fila representa
     *         un simulador y contiene los 4 campos mencionados anteriormente. Si no hay simuladores, se devuelve un arreglo vacío.
     *
     * @see java.sql.ResultSet
     * @see java.sql.Statement#executeQuery(String)
     */
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


}
