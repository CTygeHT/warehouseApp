package Databases;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created untitled2 by ironh on 15.07.2018.
 */
public class MySqlClass {
    public static Connection conn;
    public static Statement stat;


    public static void Conn() throws SQLException, ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/poli_farb?user=root&password=M@dag75askar&serverTimezone=UTC&encodin=UTF-8&useUnicode=true&characterEncoding=UTF-8");

    }

    public static ArrayList<String> getTmc(int warehouse) throws SQLException, ClassNotFoundException {
        ArrayList<String> tmc = new ArrayList<String>();

            Conn();
            stat = conn.createStatement();

        //Select poli_farb.tmc.name, poli_farb.store.cur_leftover from poli_farb.tmc join poli_farb.store on tmc.id_tmc=store.id_tmc where poli_farb.store.id_s = 2;
        ResultSet result = stat.executeQuery("Select poli_farb.tmc.name_tmc, poli_farb.store.cur_leftover " +
                                                    "from poli_farb.tmc " +
                                                    "join poli_farb.store " +
                                                    "on tmc.id_tmc=store.id_tmc " +
                                                    "where poli_farb.store.id_s = " + warehouse + ";");

        while (result.next()) {

            tmc.add(result.getString("name_tmc"));
            tmc.add(result.getString("cur_leftover"));
        }


        result.close();
        conn.close();
        CloseDB();

        return tmc;
    }

    public static ArrayList<String> getTmcFull() throws SQLException, ClassNotFoundException {
        ArrayList<String> tmc = new ArrayList<String>();

            Conn();
            stat = conn.createStatement();

        ResultSet result = stat.executeQuery("Select poli_farb.tmc.name_tmc, poli_farb.store.cur_leftover " +
                                                    "from poli_farb.store " +
                                                    "join poli_farb.tmc " +
                                                    "on tmc.id_tmc=store.id_tmc " +
                                                    "order by tmc.sortOrder, tmc.factCode, id_s;");
                                                    //"order by tmc.comment,name_tmc, id_s;");

        while (result.next()) {

            tmc.add(result.getString("name_tmc"));
            tmc.add(result.getString("cur_leftover"));
        }

        result.close();
        conn.close();
        CloseDB();

        return tmc;
    }

    public static ArrayList<String> getTransaction() throws SQLException, ClassNotFoundException {
        ArrayList<String> transaction = new ArrayList<String>();

        Conn();
        stat = conn.createStatement();

        ResultSet result = stat.executeQuery(
                "select t.id_doc, s.branch, c.name_cust, sum(t.amount), date_format(date_time, '%d-%m-%Y') as dateDoc " +
                "from poli_farb.transactions t " +
                "join poli_farb.store_names s on t.id_s = s.id_s " +
                "left join poli_farb.customer c on t.id_cust = c.id_cust " +
                "group by t.id_doc, s.branch, c.name_cust, dateDoc;");

        while (result.next()) {
            //id_doc, branch, name_cust, sum(t.amount), date(date_time)
            transaction.add(result.getString("id_doc"));
            transaction.add(result.getString("branch"));
            transaction.add(result.getString("name_cust"));
            transaction.add(result.getString("sum(t.amount)"));
            transaction.add(result.getString("dateDoc"));
        }

        result.close();
        conn.close();
        CloseDB();

        return transaction;
    }

    public static ArrayList<String> getTmcList() throws SQLException, ClassNotFoundException {
        ArrayList<String> tmc = new ArrayList<String>();

        Conn();
        stat = conn.createStatement();

        ResultSet result = stat.executeQuery("Select * from poli_farb.tmc");

        while (result.next()) {

            tmc.add(result.getString("id_tmc"));
            tmc.add(result.getString("name_tmc"));
            tmc.add(result.getString("comment"));

        }

        result.close();
        conn.close();
        CloseDB();

        return tmc;
    }

    public static void addTmc(String tmcName, String comment) throws SQLException, ClassNotFoundException {
        try {
            Conn();
            stat = conn.createStatement();
            PreparedStatement statement = conn.prepareStatement("INSERT INTO tmc (comment, name_tmc) VALUES (?, ?)");
            statement.setString(1, comment);
            statement.setString(2, tmcName);
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            stat.close();
            CloseDB();
        }

    }

    public static void addTransaction(String docNumber, String destWarehouse, String customer, String tmcCode, String tmcAmount) throws SQLException, ClassNotFoundException, IOException {

        try {
            Conn();
            stat = conn.createStatement();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO transactions (id_doc, id_s, id_cust, id_tmc, amount, date_time) VALUES (?, ?, ?, ?, ?, now())");
            statement.setString(1, docNumber);
            statement.setString(2, destWarehouse);
            statement.setString(3, customer);
            statement.setString(4, tmcCode);
            statement.setString(5, tmcAmount);
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            FileWriter writer = new FileWriter("d:/sklad/log/log.txt", true);

            String errr = "docN: \t" + docNumber + "\t : " + docNumber.getClass() + "\n" +
                    "WH: \t" + destWarehouse + "\t : " + destWarehouse.getClass() + "\n" +
                    "Cust: \t" + customer + "\t : " + customer.getClass() + "\n" +
                    "tmc: \t"  + tmcCode + "\t : " + tmcCode.getClass() + "\n" +
                    "Amount:\t" + tmcAmount + "\t : " + tmcAmount.getClass() + "\n";
            writer.append(errr);
            writer.flush();
            System.out.println(e);
        } finally {
            stat.close();
            CloseDB();
        }

    }

    public static int getLastDocN() throws SQLException, ClassNotFoundException {
        int lastDocN = 0;

        Conn();
        stat = conn.createStatement();
        ResultSet result = stat.executeQuery("SELECT Max(id_doc) from poli_farb.transactions;");

        while (result.next()) {

            lastDocN = result.getInt(1);
        }
        result.close();
        conn.close();
        CloseDB();

        return lastDocN;
    }

    public static ArrayList<String> getCust() throws SQLException, ClassNotFoundException {
        ArrayList<String> cust = new ArrayList<String>();

        Conn();
        stat = conn.createStatement();

        ResultSet result = stat.executeQuery("select c.id_cust, c.name_cust, sn.branch " +
                                                    "from poli_farb.customer c, poli_farb.store_names sn " +
                                                    "where c.id_s = sn.id_s;");

        while (result.next()) {

            cust.add(result.getString("id_cust"));
            cust.add(result.getString("name_cust"));
            cust.add(result.getString("branch"));
        }

        System.out.println(cust);
        result.close();
        conn.close();
        CloseDB();

        return cust;
    }

    public static ArrayList<String> getWH() throws SQLException, ClassNotFoundException {
        ArrayList<String> warehouseList = new ArrayList<String>();

        Conn();
        stat = conn.createStatement();                  //id_s, branch, pwd
        ResultSet result = stat.executeQuery("select id_s, branch " +
                                                    "from poli_farb.store_names");

        while (result.next()) {

            warehouseList.add(result.getString("id_s"));
            warehouseList.add(result.getString("branch"));
        }

        result.close();
        conn.close();
        CloseDB();

        return warehouseList;
    }

    public static ArrayList<String> getPantListBlank() throws SQLException, ClassNotFoundException {
        ArrayList<String> tmc = new ArrayList<String>();

        Conn();
        stat = conn.createStatement();
        ResultSet result = stat.executeQuery("select id_tmc, name_tmc from poli_farb.tmc t\n" +
                                                    "left join poli_farb.formula f on t.id_tmc=f.id_tmc_prod\n" +
                                                    "where f.id_tmc_prod is null and t.comment = \"PMS\";");

        while (result.next()) {

            tmc.add(result.getString("id_tmc"));
            tmc.add(result.getString("name_tmc"));
        }

        result.close();
        conn.close();
        CloseDB();

        return tmc;
    }

    public static ArrayList<String> getPantList() throws SQLException, ClassNotFoundException {
        ArrayList<String> tmc = new ArrayList<String>();

        Conn();
        stat = conn.createStatement();
        ResultSet result = stat.executeQuery("select distinct(t.id_tmc), (t.name_tmc) from poli_farb.tmc t\n" +
                                                    "left join poli_farb.formula f on t.id_tmc=f.id_tmc_prod\n" +
                                                    "where f.id_tmc_prod is not null and t.comment = \"PMS\";");

        while (result.next()) {

            tmc.add(result.getString("id_tmc"));
            tmc.add(result.getString("name_tmc"));
        }

        result.close();
        conn.close();
        CloseDB();

        return tmc;
    }

    public static ArrayList<String> getFormula(String pant, String customer, String warehouse) throws SQLException, ClassNotFoundException {
        ArrayList<String> tmc = new ArrayList<String>();

        Conn();
        stat = conn.createStatement();
        ResultSet result = stat.executeQuery("Select f.id_tmc_comp, f.portion, s.cur_leftover " +
                                                    "from poli_farb.formula f " +
                                                    "inner join poli_farb.store s on id_tmc_comp = id_tmc " +
                                                    "where id_tmc_prod = " + pant +
                                                    " and f.id_cust = " + customer +
                                                    " and s.id_s = " +warehouse +" ;");

        while (result.next()) {

            tmc.add(result.getString("id_tmc_comp"));
            tmc.add(result.getString("portion"));
            tmc.add(result.getString("cur_leftover"));
        }

        result.close();
        conn.close();
        CloseDB();

        return tmc;
    }

    public static void addMixingOrder(String pantName, String sum, String warehouse) throws SQLException, ClassNotFoundException {
        try {
            Conn();
            stat = conn.createStatement();
            PreparedStatement statement = conn.prepareStatement("call addMixingOrder(?, ?, ?);");
            statement.setString(1, pantName);
            statement.setString(2, sum);
            statement.setString(3, warehouse);
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            stat.close();
            CloseDB();
        }

    }

    public static float getTmcLeftover(String warwhouse, String tmc) throws SQLException, ClassNotFoundException {
        float tmcLeftover = 0;

        Conn();
        stat = conn.createStatement();
//        PreparedStatement statement = conn.prepareStatement(
//                "Select cur_leftover from poli_farb.store where id_s= ? and id_tmc = ?");
//        statement.setString(1, warwhouse);
//        statement.setString(2, tmc);
//        statement.execute();
//        statement.close();
//
        ResultSet result = stat.executeQuery("Select cur_leftover from poli_farb.store where id_s= " + warwhouse +" and id_tmc = " + tmc);

        while (result.next()) {

            tmcLeftover = result.getFloat(1);
        }
        result.close();
        conn.close();
        CloseDB();

        return tmcLeftover;
    }

    public static void addReceipt(String pantName, String customer, String tmcCode, String tmcAmount) throws SQLException, ClassNotFoundException, IOException {

        try {
            Conn();
            stat = conn.createStatement();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO formula (id_cust, id_tmc_prod, id_tmc_comp, portion) VALUES (?, ?, ?, ?)");
            statement.setString(1, customer);
            statement.setString(2, pantName);
            statement.setString(3, tmcCode);
            statement.setString(4, tmcAmount);
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            stat.close();
            CloseDB();
        }

    }

    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
    }


}
