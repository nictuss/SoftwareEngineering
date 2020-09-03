/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dbsimplemanager;

import java.sql.*;



public class DBManager {

    public static void main(String[] args) throws Exception {

        Class.forName("org.sqlite.JDBC");
        Connection conn
                = DriverManager.getConnection("jdbc:sqlite:"+args[0]);
        Statement stat = conn.createStatement();

        if (args.length > 1 && args[1].equals("create")) {
            stat.executeUpdate("drop table if exists fligth;");
            stat.executeUpdate("drop table if exists destination;");
            stat.executeUpdate("create table fligth (id, name, destinationId);");
            stat.executeUpdate("create table destination (id);");
            PreparedStatement prep = conn.prepareStatement(
                    "insert into fligth values (?, ?, ?);");
            prep.setString(1, "1");
            prep.setString(2, "AZ140");
            prep.setString(3, "1");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            prep.setString(1, "2");
            prep.setString(2, "LH999");
            prep.setString(3, "2");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            prep.setString(1, "3");
            prep.setString(2, "FR123");
            prep.setString(3, "3");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            prep.setString(1, "4");
            prep.setString(2, "US666");
            prep.setString(3, "4");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            prep = conn.prepareStatement(
                    "insert into destination values (?);");
            
            prep.setString(1, "1");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            prep.setString(1, "2");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            prep.setString(1, "3");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            prep.setString(1, "4");
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            
            System.out.println("Created Database\n");
        } else {
            System.out.println("FLIGHTS\n");
            
            ResultSet rs1 = stat.executeQuery("select * from fligth;");
            while (rs1.next()) {
                System.out.print("Fligth = " + rs1.getString("id") + " is : ");
                System.out.println(rs1.getString("name") + " destination: " + rs1.getString("destinationId"));

            }
            rs1.close();
            
            System.out.println("\nDESTINATIONS\n");
            
            ResultSet rs2 = stat.executeQuery("select * from destination;");
            while (rs1.next()) {
                System.out.print("Destination = " + rs2.getString("id") + " is : " + rs2.getString("id") + "\n");
            }
            rs1.close();
        }
        conn.close();
    }
}
