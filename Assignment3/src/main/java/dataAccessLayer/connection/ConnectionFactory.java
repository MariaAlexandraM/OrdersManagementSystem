package dataAccessLayer.connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a class for creating and managing database connections.
 * This class provides methods to create, retrieve, and close connections to the database.
 */

public class ConnectionFactory {

	private static String PASS = "root";
	private static String USER = "root";
	private static String DBURL = "jdbc:mysql://localhost:3306/assignment3DataBase";
	private static String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
	private static ConnectionFactory singleInstance = new ConnectionFactory();
	private static Connection connection;

	/**
	 * Creates the connection to the DB
	 *
	 * @return the newly created connection or null
	 */
	protected Connection createConnection() {

		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("Eroare la Driver");
			e.printStackTrace();
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DBURL, USER, PASS);
		} catch (SQLException sqlException) {
			LOGGER.log(Level.WARNING, "An error occurred while trying to create the Connection to the DB");
			sqlException.printStackTrace();
		}
		return connection;
	}

	/**
	 * Gets the connection to the DB after creating it
	 *
	 * @return the connection to the DB
	 */
	public static Connection getConnection() {
		return singleInstance.createConnection();
	}

	/**
	 * Closes the Connection to the DB
	 *
	 * @param connection the Connection to be closed
	 */
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException sqlException) {
				LOGGER.log(Level.WARNING, "An error occurred while trying to close the Connection to the DB");
			}
		}
	}

	/**
	 * Closes a Statement
	 *
	 * @param statement the Statement to be closed
	 */
	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException sqlException) {
				LOGGER.log(Level.WARNING, "An error occurred while trying to close a Statement");
			}
		}
	}

	/**
	 * Closes a ResultSet
	 *
	 * @param resultSet the ResultSet to be closed
	 */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException sqlException) {
				LOGGER.log(Level.WARNING, "An error occurred while trying to close a ResultSet");
			}
		}
	}
}
