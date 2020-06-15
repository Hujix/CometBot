/*
 * Discord CometBot by codedcosmos
 *
 * CometBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * CometBot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License 3 for more details.
 * You should have received a copy of the GNU General Public License 3
 * along with CometBot.  If not, see <https://www.gnu.org/licenses/>.
 */

package codedcosmos.cometbot.database;

import codedcosmos.hyperdiscord.utils.debug.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class CometDatabase {
	
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/EMP";
	
	//  Database credentials
	private static final String USER = "cometbot";
	private static final String PASS = "password";
	
	private Connection connection;
	
	public CometDatabase() throws Exception {
		// Register Driver
		Class.forName(JDBC_DRIVER);
		
		// Open a connection
		Log.print("Connecting to the database. . .");
		connection = DriverManager.getConnection(DB_URL,USER,PASS);
		
		Log.print("Connected to database successfully");
	}
}
