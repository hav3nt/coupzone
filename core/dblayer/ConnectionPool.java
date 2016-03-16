package com.core.dblayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.core.exceptions.DBConnectionException;

/**
 * A singleton class with pool of connections, managing connections to DB
 * 
 * @author Baruch
 *
 */
public class ConnectionPool {
	private final int MAX_CONNECTIONS = 100;
	private int availableCons = 100;
	private Map<Connection, Boolean> map = new HashMap<>();
	private String driver = "org.apache.derby.jdbc.ClientDriver40";
	private String DBName = "couponSys";
	private String url = "jdbc:derby://localhost:1527/" + DBName
			+ "; create=true";
	private static ConnectionPool instance;

	static {
		try {
			instance = new ConnectionPool();
		} catch (ExceptionInInitializerError | Exception e) {
			throw new DBConnectionException("System loading operation failed",
					e);
		}
	}

	/**
	 * A constructor which loads driver and establishes the connections to the
	 * DB The connections are stored in a Map, each connection have value
	 * weather available or not
	 * 
	 * @throws Exception
	 */
	private ConnectionPool() throws Exception {
		Class.forName(driver);
		for (int i = 0; i < MAX_CONNECTIONS; i++) {
			Connection con = DriverManager.getConnection(url);
			map.put(con, true);
		}
	}

	/**
	 * returns an instance of the connection pool
	 * 
	 * @throws Exception
	 * @return an instance for the ConnectionPool singleton
	 * */
	public static ConnectionPool getInstance() throws Exception {
		return instance;
	}

	/**
	 * returns a connection from the connection pool
	 * 
	 * @return Connection from the pool
	 * */
	public synchronized Connection getConnection() {
		while (this.availableCons < 1) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		Connection connection = null;
		for (Connection currCon : map.keySet()) {
			if (map.get(currCon)) {
				map.put(currCon, false);
				this.availableCons -= 1;
				connection = currCon;
			}
		}
		return connection;
	}

	/**
	 * return a connection back to the connection pool
	 * 
	 * @param connection
	 *            - the connection to return back to the pool
	 * @throws IllegalMonitorStateException
	 * */
	public synchronized void returnConnection(Connection connection)
			throws IllegalMonitorStateException {
		if (map.put(connection, true) != null) {
			this.availableCons += 1;
			notifyAll();
		}
	}

	/**
	 * closes the resource of prepared statement
	 * 
	 * @param preparedStatement
	 *            - the statement resource to close
	 * @throws SQLException
	 * */
	public void closeStatement(PreparedStatement preparedStatement)
			throws SQLException {
		if (preparedStatement != null) {
			preparedStatement.close();
		}
	}

	/**
	 * closing all of the connections to the DB
	 * 
	 * @throws SQLException
	 * */
	public void closeAllConnections() throws SQLException {
		for (Connection connection : map.keySet()) {
			connection.close();
		}
	}

}
