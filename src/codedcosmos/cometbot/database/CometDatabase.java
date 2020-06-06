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

import java.sql.*;

public class CometDatabase {
	
	private static String DATABASE_NAME = "comet";
	
	public CometDatabase() {
		String url = "jdbc:mysql://localhost/";
		url += "?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
		
		try (Connection conn = DriverManager.getConnection(url, "root", "")) {
			Statement s = conn.createStatement();
			
			// Load connection
			DatabaseMetaData meta = conn.getMetaData();
			
			int Result = s.executeUpdate("CREATE DATABASE IF NOT EXISTS "+DATABASE_NAME);
			System.out.println("Created Database if it doesn't exist [" + Result + "]");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CometDatabase database = new CometDatabase();
	}
}
