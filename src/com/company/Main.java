package com.company;

import com.company.DAL.DAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
// установить гит создать учетку на гитхабе скидаывать туда. github.com
    static Scanner in = new Scanner(System.in);

    public static void main( String args[] ) throws SQLException {
        OpenDB();
        printMenu();
       while (true){
           boolean end = false;
           System.out.println("Show menu - 0");
           System.out.println("Press a key: ");
            DAO.openConection();
            String c = in.next();
            switch (c){
                case "0":
                    printMenu();
                    break;
                case "1" :
                    addClient();
                    break;
                case "2" :
                    addProduct();
                    break;
                case "3" :
                    addCharter();
                    break;
                case "4" :
                    addProductToCharter();
                    break;
                case "5" :
                    printClientsList();
                    break;
                case "6" :
                    printProductsList();
                    break;
                case "7" :
                    printChartersOfClient();
                    break;
                case "8" :
                    printProductsOfCharter();
                    break;
                case "9" :
                    printChartersOfProduct();
                    break;
                case "10" :
                    deleteClient();
                    break;
                case "11" :
                    deleteProduct();
                    break;
                case "12" :
                    deleteCharter();
                    break;
                case "13" :
                    deleteProductFromCharter();
                    break;
                case "14":
                    updateName();
                    break;
                case "15":
                    end = true;
                    break;
                default:
                    System.out.println("Wrong key!");
            }
           if (end) break;
           DAO.closeConnection();
        }
        DAO.closeConnection();
    }

    private static void printMenu(){
        System.out.println("Add a client - 1");
        System.out.println("Add a product - 2");
        System.out.println("Add a charter to client - 3");
        System.out.println("Add a product to charter - 4");
        System.out.println();
        System.out.println("Get a list of clients - 5");
        System.out.println("Get a list of product - 6");
        System.out.println("Get charters of a client - 7");
        System.out.println("Get products of charter - 8");
        System.out.println("Get charters of product - 9");
        System.out.println();
        System.out.println("Delete a client - 10");
        System.out.println("Delete a product - 11");
        System.out.println("Delete a charter - 12");
        System.out.println("Delete a product from charter - 13");
        System.out.println();
        System.out.println("Update a name of client - 14");
        System.out.println("Exit - 15");
    }
    private static void OpenDB(){
        boolean ok = true;
        ok &= DAO.openConection();
        ok &= DAO.createTableClients();
        ok &= DAO.createTableProducts();
        ok &= DAO.createTableCharters();
        ok &= DAO.createTableProductAndCharterLinks();
        if (!ok){
            System.out.println("DB is FAILED");
            System.exit(0);
        }
    }

    private static void addClient(){
        try {
            System.out.println("Enter a name of user: ");
            String name = in.next();
            int addedClient = DAO.addClient(name);
            if (addedClient == -1) {
                System.out.println("Error!");
            } else {
                System.out.println("Client was added. id is " + addedClient);
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }

    }
    private static void addProduct(){
        try {
            System.out.println("Enter a title of product: ");
            String name = in.next();
            if (!DAO.addProduct(name)) {
                System.out.println("Error!");
            } else {
                System.out.println("Product was added.");
            }
        }
        catch (Exception e) {
            System.out.println("Error!");
        }
    }
    private static void addCharter(){
        try {
            System.out.println("Enter ID of user: ");
            int ID = in.nextInt();
            int addedCharter = DAO.addCharter(ID);
            if (addedCharter == -1) {
                System.out.println("Error!");
            } else {
                System.out.println("Charter of Client " + ID + " was added. id is " + addedCharter);
            }
        }
        catch(Exception e){
            System.out.println("Error!");
        }
    }
    private static void addProductToCharter(){
        try {
            System.out.println("Enter ID of charter: ");
            int ID = in.nextInt();
            System.out.println("Enter name of product: ");
            String title = in.next();
            if (!DAO.addProductAndCharterLink(title, ID)) {
                System.out.println("Error!");
            } else {
                System.out.println("Link was added.");
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }

    private static void printClientsList(){
        try {
            System.out.println("List of clients:");
            List<String> clients = DAO.getClients();
            for (String client : clients){
                System.out.println("-" + client);
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }
    private static void printProductsList(){
        try {
            System.out.println("List of products:");
            List<String> products = DAO.getProducts();
            for (String product : products){
                System.out.println("-" + product);
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }
    private static void printChartersOfClient() {
        try {
            System.out.println("Enter ID of Client to show:");
            int id = in.nextInt();
            System.out.println("List of charters of Client:");
            List<Integer> charters = DAO.getCharters(id);
            for (int charter: charters){
                System.out.println("-" + charter);
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }
    private static void printProductsOfCharter() {
        try {
            System.out.println("Enter ID of charter to show:");
            int id = in.nextInt();
            System.out.println("List of products of charter:");
            List<String> products = DAO.getProductsInCharter(id);
            for (String product: products){
                System.out.println("-" + product);
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }
    private static void printChartersOfProduct(){
        try {
            System.out.println("Enter title of product to show:");
            String title = in.next();
            System.out.println("List of charters of product:");
            List<Integer> charters = DAO.getCharterOfProduct(title);
            for (Integer charter: charters){
                System.out.println("-id:" + charter);
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }

    private static void deleteClient(){
        try {
            System.out.println("Enter a id of client: ");
            int id = in.nextInt();
            if (!DAO.deleteClient(id)) {
                System.out.println("Error!");
            } else {
                System.out.println("Client was deleted.");
            }
        }
        catch (Exception e){
            System.out.print("Error!");
        }
    }
    private static void deleteProduct(){
       try {
           System.out.println("Enter a title of product: ");
           String title = in.next();
           if (!DAO.deleteProduct(title)) {
               System.out.println("Error!");
           } else {
               System.out.println("Product was deleted.");
           }
       }
       catch (Exception e){
           System.out.print("Error!");
       }
    }
    private static void deleteCharter(){
        try {
            System.out.println("Enter id of charter: ");
            int id = in.nextInt();
            if (!DAO.deleteCharter(id)) {
                System.out.println("Error!");
            } else {
                System.out.println("Charter was deleted.");
            }
        }
        catch (Exception e){
            System.out.print("Error!");
        }
    }
    private static void deleteProductFromCharter(){
        try {
            System.out.println("Enter id of charter: ");
            int id = in.nextInt();
            System.out.println("Enter title of product: ");
            String title = in.next();
            if (!DAO.deleteProductFromCharter(title, id)) {
                System.out.println("Error!");
            } else {
                System.out.println("Product was deleted from charter.");
            }
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }

    private static void updateName(){
        try {
            System.out.println("Enter ID of client:");
            int id = in.nextInt();
            System.out.println("Enter new name:");
            String name = in.next();
            DAO.updateClient(name, id);
        }
        catch (Exception e){
            System.out.println("Error!");
        }
    }
}
