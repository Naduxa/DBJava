package com.company.DAL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private static Connection connection;

    public static void closeConnection() throws SQLException {
        connection.close();
    }
    public static boolean openConection()  {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:baseMi.db");
            return true;
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void dropTables() throws SQLException {
        Statement query = connection.createStatement();
        query.execute("Drop table Clients");
        query.execute("Drop table Charters");
        query.execute("Drop table ProductAndCharterLinks");
        query.execute("Drop table Products");
    }
    public static boolean createTableProducts(){
        try {
            String text = "CREATE TABLE if not exists Products (title text primary key);";
            Statement query = connection.createStatement();
            query.executeUpdate(text);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean createTableClients(){
        try {
            String text = "CREATE TABLE if not exists Clients (id INTEGER PRIMARY KEY , name text);";
            Statement query = connection.createStatement();
            query.executeUpdate(text);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean createTableCharters(){
        try {
            String text = "CREATE TABLE if not exists Charters (id INTEGER PRIMARY KEY, id_client INTEGER NOT NULL," +
                    " FOREIGN KEY(id_client) REFERENCES Clients(id) ON DELETE CASCADE);";
            Statement query = connection.createStatement();
            query.executeUpdate(text);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean createTableProductAndCharterLinks(){
        try {
            String text = "CREATE TABLE if not exists ProductAndCharterLinks (id INTEGER PRIMARY KEY," +
                    "title_product TEXT NOT NULL," +
                    "id_charter INTEGER NOT NULL," +
                    " FOREIGN KEY(title_product) REFERENCES Products(title) ON DELETE CASCADE," +
                    " FOREIGN KEY(id_charter) REFERENCES Charters(id) ON DELETE CASCADE);";
            Statement query = connection.createStatement();
            query.executeUpdate(text);

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean addProduct(String title){
        try{
            String text = "INSERT into Products (title) VALUES ( + '" + title + "');";
            Statement query = connection.createStatement();
            query.executeUpdate(text);

            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static int addClient(String name){
        try{
            String text = "INSERT into Clients (name) VALUES  ('" + name + "');";
            Statement query = connection.createStatement();
            query.executeUpdate(text);
            query = connection.createStatement();
            ResultSet res = query.executeQuery("SELECT id FROM Clients WHERE id=last_insert_rowid()");

            return res.getInt("id");
        } catch (SQLException e) {
            return -1;
        }
    }
    public static int addCharter(int idClient){
        try{
            String text = "INSERT into Charters (id_client) VALUES (" + idClient + ");";
            Statement query = connection.createStatement();
            query.execute(text);
            query = connection.createStatement();
            ResultSet result = query.executeQuery("SELECT id FROM Charters WHERE id=last_insert_rowid();");
            int id = result.getInt("id");
            return id;
        } catch (SQLException e) {
            return -1;
        }
    }
    public static boolean addProductAndCharterLink(String titleProduct, int idCharter){
        try{
            String text = "INSERT into ProductAndCharterLinks (title_product, id_charter) VALUES ( '" + titleProduct + "', " + idCharter + ");";
            Statement query = connection.createStatement();
            query.execute(text);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static List<String> getProducts() throws SQLException {
        List<String> result = new ArrayList<>();
        Statement query = connection.createStatement();
        ResultSet res = query.executeQuery("Select * from Products;");

        while (res.next()){
            result.add(res.getString("title"));
        }
        return result;
    }
    public static List<String> getClients() throws SQLException {
        List<String> result = new ArrayList<>();
        Statement query = connection.createStatement();
        ResultSet res = query.executeQuery("Select * from Clients;");
        while (res.next()){
            System.out.println("id: " + res.getInt("id") +" name: "+ res.getString("name"));
        }

        return result;
    }
    public static List<Integer> getCharters(int clientID) throws SQLException {
        List<Integer> result = new ArrayList<>();
        Statement query = connection.createStatement();
        ResultSet res = query.executeQuery("Select id from Charters where id_client=" + clientID + ";");
        while (res.next()){
            result.add(res.getInt("id"));
        }
        return result;
    }
    public static List<String> getProductsInCharter(int id) throws SQLException {
        List<String> result = new ArrayList<>();
        Statement query = connection.createStatement();
        String text = "Select title_product from ProductAndCharterLinks where id_charter=" + id + ";";
        ResultSet res = query.executeQuery(text);
        while (res.next()){
           result.add(res.getString("title_product"));
        }

        return result;
    }
    public static List<Integer> getCharterOfProduct(String title) throws SQLException {
        List<Integer> result = new ArrayList<>();
        Statement query = connection.createStatement();
        String text = "Select id_charter from ProductAndCharterLinks where title_product= '" + title + "';";
        ResultSet res = query.executeQuery(text);
        while (res.next()){
            result.add(res.getInt("id_charter"));
        }

        return result;
    }

    public static boolean deleteClient(int clientID){
        Statement query = null;
        try {
            query = connection.createStatement();
            if (!deleteChartersOfClient(clientID)) throw new SQLDataException();
            query.executeUpdate("DELETE from Clients where id=" + clientID + ";");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean deleteProduct(String title){
        Statement query = null;
        try {
            query = connection.createStatement();
            if (!deleteProductFromCharters(title)) throw new SQLDataException();
            query.executeUpdate("DELETE from Products where title='" + title + "';");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean deleteCharter(int id){
        Statement query = null;
        try {
            query = connection.createStatement();
            if (!deleteCharterWithProducts(id)) throw new SQLDataException();
            query.executeUpdate("DELETE from Charters where id=" + id + ";");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean deleteChartersOfClient(int idClient){
        Statement query = null;
        try {
            List<Integer> charters = getCharters(idClient);
            for (int charter : charters){
                if (!deleteCharter(charter)) throw new SQLDataException();
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean deleteProductFromCharters(String title){
        Statement query = null;
        try {
            query = connection.createStatement();
            query.executeUpdate("DELETE from ProductAndCharterLinks where title_product='" + title + "';");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean deleteCharterWithProducts(int id){
        Statement query = null;
        try {
            query = connection.createStatement();
            query.executeUpdate("DELETE from ProductAndCharterLinks where id_charter=" + id + ";");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean deleteProductFromCharter(String title, int id_charter){
        Statement query = null;
        try {
            query = connection.createStatement();
            String text = "Select id from ProductAndCharterLinks limit 1";
            ResultSet res = query.executeQuery(text);
            System.out.println(res.getInt("id"));
            text = "DELETE from ProductAndCharterLinks where id_charter=" + id_charter + " and title_product='" + title + "' and id=" + res.getInt("id") + ";";
            query.executeUpdate(text);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static boolean updateClient(String name, int id){
        Statement query = null;
        try{
            query = connection.createStatement();
            String text = "UPDATE Clients set name = '" + name + "' where id=" + id + ";";
            query.execute(text);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
