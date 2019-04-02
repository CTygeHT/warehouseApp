import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;

import Databases.MySqlClass;
import org.json.JSONObject;


public class MainServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuilder jb = new StringBuilder();
        String line = null;

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            JSONObject jsonObject = new JSONObject(jb.toString());

            response.setContentType("text/html;charset=koi8-u");
            PrintWriter out = response.getWriter();

            int command = jsonObject.getInt("command");

            synchronized (this) {
                switch (command) {

                    case 0: //show table

                        int warehouse = jsonObject.getInt("warehouse");
                        //int date = jsonObject.getInt("dateValue");

                        ArrayList<String> tmc = MySqlClass.getTmc(warehouse);

                        JSONObject jsonToReturn0 = new JSONObject();
                        jsonToReturn0.put("answer", "names");
                        jsonToReturn0.put("list", tmc.toString());
                        out.println(jsonToReturn0.toString());

                        break;

                    case 1: //add new name

                        String data1 = jsonObject.getString("name");
                        String data2 = jsonObject.getString("comm");

                        //SQLiteClass.addName(data);
                        MySqlClass.addTmc(data1, data2);

                        JSONObject jsonToReturn1 = new JSONObject();
                        jsonToReturn1.put("answer", "ok");
                        out.println(jsonToReturn1.toString());

                        break;

                    case 2: //getTMCList

                        ArrayList<String> tmc2 = MySqlClass.getTmcList();

                        JSONObject jsonToReturn2 = new JSONObject();
                        jsonToReturn2.put("answer", "tmc_list");
                        jsonToReturn2.put("list", tmc2.toString());
                        out.println(jsonToReturn2.toString());

                        break;

                    case 3: //addPurchase

                        String docNumber = jsonObject.getString("docNumber");
                        String destWarehouse = jsonObject.getString("warehouse");
                        String customer = jsonObject.getString("customer");
                        int count = jsonObject.getInt("count");           //TODO: Check Tmc loading for purchase
                        String[] queryData = jsonObject.getString("query").split(", ");
                        JSONObject jsonToReturn3 = new JSONObject();

                        for (int i = 0; i < count; i++) {
                            String tmcCode = queryData[i * 2];
                            String tmcAmount = queryData[i * 2 + 1];
                            MySqlClass.addTransaction(docNumber, destWarehouse, customer, tmcCode, tmcAmount);

                        }

                        jsonToReturn3.put("answer" , "ok");
                        System.out.println(jsonToReturn3.toString());
                        out.println(jsonToReturn3.toString());

                        break;

                    case 4: //getCustList

                        ArrayList<String> cust = MySqlClass.getCust();

                        JSONObject jsonToReturn4 = new JSONObject();
                        jsonToReturn4.put("answer", "cust_list");
                        jsonToReturn4.put("list", cust.toString());
                        out.println(jsonToReturn4.toString());

                        break;

                    case 5: //getPantList

                        ArrayList<String> tmc5 = MySqlClass.getPantList();

                        JSONObject jsonToReturn5 = new JSONObject();
                        jsonToReturn5.put("answer", "pant_list");
                        jsonToReturn5.put("list", tmc5.toString());
                        out.println(jsonToReturn5.toString());

                        break;

                    case 6: //getFormula

                        String pant6 = jsonObject.getString("pant");
                        String customer6 = jsonObject.getString("customer");
                        String warehouse6 = jsonObject.getString("warehouse");

                        ArrayList<String> formula = MySqlClass.getFormula(pant6, customer6, warehouse6);

                        JSONObject jsonToReturn6 = new JSONObject();
                        jsonToReturn6.put("answer", "formula");
                        jsonToReturn6.put("list", formula.toString());
                        out.println(jsonToReturn6.toString());

                        break;

                    case 7: //addMixingOrder

                        String pant7 = jsonObject.getString("pant");           //TODO: fix DB Trigger to add new tmc
                        String sum7 = jsonObject.getString("sum");            //TODO: fix DB Trigger to check cur_leftover before updating store
                        String warehouse7 = jsonObject.getString("warehouse");

                        //SQLiteClass.addName(data);
                        MySqlClass.addMixingOrder(pant7, sum7, warehouse7);

                        JSONObject jsonToReturn7 = new JSONObject();
                        jsonToReturn7.put("answer", "ok");
                        out.println(jsonToReturn7.toString());

                        break;

                    case 8: //getLastDocN

                        int lastDocN = MySqlClass.getLastDocN();

                        JSONObject jsonToReturn8 = new JSONObject();
                        jsonToReturn8.put("answer", "docNo");
                        jsonToReturn8.put("No", lastDocN);
                        out.println(jsonToReturn8.toString());

                        break;

                    case 9: //getTmcLeftover

                        String warehouse9 = jsonObject.getString("warehouse");
                        String tmc9 = jsonObject.getString("tmc");
                        float tmcLeftover = MySqlClass.getTmcLeftover(warehouse9, tmc9);

                        JSONObject jsonToReturn9 = new JSONObject();
                        jsonToReturn9.put("answer", "tmcLeftover");
                        jsonToReturn9.put("number", tmcLeftover);
                        out.println(jsonToReturn9.toString());

                        break;

                    case 10: //addPurchase

                        String docNumber10 = jsonObject.getString("docNumber");
                        String destWarehouse10 = jsonObject.getString("warehouse");
                        String customer10 = jsonObject.getString("customer");
                        int count10 = jsonObject.getInt("count");           //TODO: Check Tmc loading for purchase
                        String[] queryData10 = jsonObject.getString("query").split(", ");

                        for (int i = 0; i < count10; i++) {
                            String tmcCode = queryData10[i * 2];
                            String tmcAmount = queryData10[i * 2 + 1];
                            MySqlClass.addTransaction(docNumber10, destWarehouse10, customer10, tmcCode, tmcAmount);
                        }

                        JSONObject jsonToReturn10 = new JSONObject();
                        jsonToReturn10.put("answer", "ok");
                        out.println(jsonToReturn10.toString());

                        break;

                    case 11: //getWH

                        ArrayList<String> warehouseList = MySqlClass.getWH();

                        JSONObject jsonToReturn11 = new JSONObject();
                        jsonToReturn11.put("answer", "wh_list");
                        jsonToReturn11.put("list", warehouseList.toString());
                        out.println(jsonToReturn11.toString());

                        break;

                    case 12: //show full table

                        ArrayList<String> tmc12 = MySqlClass.getTmcFull();

                        JSONObject jsonToReturn12 = new JSONObject();
                        jsonToReturn12.put("answer", "tableFull");
                        jsonToReturn12.put("list", tmc12.toString());
                        out.println(jsonToReturn12.toString());

                        break;

                    case 13: //getTmcLeftoverMove

                        String warehouseIn13 = jsonObject.getString("warehouseIn");
                        String warehouseOut13 = jsonObject.getString("warehouseOut");
                        String tmc13 = jsonObject.getString("tmc");
                        float tmcLeftoverIn13 = MySqlClass.getTmcLeftover(warehouseIn13, tmc13);
                        float tmcLeftoverOut13 = MySqlClass.getTmcLeftover(warehouseOut13, tmc13);

                        JSONObject jsonToReturn13 = new JSONObject();
                        jsonToReturn13.put("answer", "tmcLeftoverMove");
                        jsonToReturn13.put("in", tmcLeftoverIn13);
                        jsonToReturn13.put("Out", tmcLeftoverOut13);
                        out.println(jsonToReturn13.toString());

                        break;

                    case 14: //addReceipt

                        String customer14 = jsonObject.getString("customer");
                        String pant14 = jsonObject.getString("pant");
                        int count14 = jsonObject.getInt("count");
                        String[] recData14 = jsonObject.getString("receipt").split(", ");

                        for (int i = 0; i < count14; i++) {
                            String tmcCode = recData14[i * 2];
                            String tmcAmount = recData14[i * 2 + 1];
                            MySqlClass.addReceipt(pant14, customer14, tmcCode, tmcAmount);
                        }

                        JSONObject jsonToReturn14 = new JSONObject();
                        jsonToReturn14.put("answer", "ok");
                        out.println(jsonToReturn14.toString());

                        break;

                    case 15: //getPantListBlank

                        ArrayList<String> tmc15 = MySqlClass.getPantListBlank();

                        JSONObject jsonToReturn15 = new JSONObject();
                        jsonToReturn15.put("answer", "pant_listBlank");
                        jsonToReturn15.put("list", tmc15.toString());
                        out.println(jsonToReturn15.toString());

                        break;

                    case 16: //show transaction

                        ArrayList<String> transaction16 = MySqlClass.getTransaction();

                        JSONObject jsonToReturn16 = new JSONObject();
                        jsonToReturn16.put("answer", "transaction");
                        jsonToReturn16.put("list", transaction16.toString());
                        out.println(jsonToReturn16.toString());

                        break;







                    default:
                        System.out.println("default switch");
                        break;
                }

            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}