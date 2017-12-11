package com.fxdb;

import com.dao.Chart;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.Date;

public class DBWorker {

    //private final String url = "jdbc:sqlite:D:\\DB\\FXDB\\FXDB.db";
    private final String url = "jdbc:sqlite:E:\\Java\\fx\\FXDB.db";

    private final int fx_id = 1 ;

    private Connection connection;

    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public DBWorker() {

        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Connection getConnection() {
        return connection;
    }

    public void insertDataDay () {

        TextWorker textWorker = new TextWorker();
        ArrayList<String[]> arrayArrayList = textWorker.readfile();

        String stime = "";
        double open = 0;
        String stimeYear;
        String stimeMonth;
        String stimeDay;

        try {

            statement = connection.createStatement();

//            resultSet = statement.executeQuery("SELECT * FROM Day");

//            while (resultSet.next()) {
//                int anInt = resultSet.getInt(2);
//                System.out.println(anInt);
//
//            }

            for (String[] strings: arrayArrayList) {

                if (strings[2].equals("000000")){

                    stime = strings[1];
                    stimeYear = stime.substring(0, 4);
                    stimeMonth = stime.substring(4, 6);
                    stimeDay = stime.substring(6, 8);
                    stime = stimeYear  + "-" + stimeMonth  + "-" + stimeDay;
                    open = Double.valueOf(strings[3]);
                    statement.execute("INSERT INTO day(time, fxPair_id, open) VALUES('"+stime+"','"+fx_id+"','"+open+"')");

                }
            }

        } catch (SQLException e) {
            System.err.println("havn't got connection");
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (statement != null) statement.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
            }
        }

    }

    public void insertDataM5 () {

        TextWorker textWorker = new TextWorker();
        ArrayList<String[]> arrayArrayList = textWorker.readfile();



        double open = 0;
        double hight = 0;
        double low = 0;
        double close = 0;
        String stime;
        String stimeYear;
        String stimeMonth;
        String stimeDay;
        String stimeHour;
        String stimeMin;
        String stimeSec;


        try {

            String insertTableSQL = "INSERT INTO M5"
                    + "(time, fx_id, open, hight, low, close) VALUES"
                    + "(?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(insertTableSQL);

            connection.setAutoCommit(false);

            for (String[] strings: arrayArrayList) {
                int iTime = Integer.valueOf(strings[2].substring(2, 4));

                if (iTime % 5 == 0){

                    stimeYear = strings[1].substring(0, 4);
                    stimeMonth = strings[1].substring(4, 6);
                    stimeDay = strings[1].substring(6, 8);
                    stimeHour = strings[2].substring(0, 2);;
                    stimeMin = strings[2].substring(2, 4);;
                    stimeSec = strings[2].substring(4, 6);;
                    stime = stimeYear  + "-" + stimeMonth  + "-" + stimeDay + " " + stimeHour + ":" + stimeMin + ":" + stimeSec + ".000";

                    open = Double.valueOf(strings[3]);
                    hight = Double.valueOf(strings[4]);
                    low = Double.valueOf(strings[5]);
                    close = Double.valueOf(strings[6]);

                    //preparedStatement = connection.prepareStatement("INSERT INTO M5(time, fx_id, open, hight, low, close) VALUES('"+stime+"','"+fx_id+"','"+open+"','"+hight+"','"+low+"','"+close+"')");

                    preparedStatement.setString(1, stime);
                    preparedStatement.setInt(2, fx_id);
                    preparedStatement.setDouble(3, open);
                    preparedStatement.setDouble(4, hight);
                    preparedStatement.setDouble(5, low);
                    preparedStatement.setDouble(6, close);
                    preparedStatement.addBatch();
                }
            }

            preparedStatement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            System.err.println("havn't got connection");
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (statement != null) statement.close();
            } catch (Exception e) {
            }
            ;
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
            }
        }

    }



     public LinkedHashMap graffikMap (){

         LinkedHashMap<java.util.Date, double[]> map = new LinkedHashMap<java.util.Date, double[]>();

         String sql = "SELECT * FROM M5 WHERE TIME >= ? AND TIME < ?";

         try {
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, "2015-01-01 00:00:00");
            preparedStatement.setString(2, "2015-12-12  00:00:00");

            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){

                java.sql.Time dbSqlTime = resultSet.getTime("time");
                java.util.Date dbSqlTimeConverted = new java.util.Date(dbSqlTime.getTime());

                map.put(dbSqlTimeConverted,  new double[] {
                        resultSet.getDouble("Open"),
                        resultSet.getDouble("Hight"),
                        resultSet.getDouble("Low"),
                        resultSet.getDouble("Close")});
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch (Exception e) {
            }
            try {
                if (statement != null) statement.close();
            } catch (Exception e) {
            }
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
            }
        }

        return map;

        
        
     }

     public ArrayList <Chart> listH4(){

         ArrayList <Chart> list = new ArrayList<Chart>();

         String sql = "SELECT * FROM M5 WHERE TIME >= ? AND TIME < ?";

         try {
             preparedStatement = connection.prepareStatement(sql);

             preparedStatement.setString(1, "2001-01-01 00:00:00");
             preparedStatement.setString(2, "2015-12-12  00:00:00");

             resultSet = preparedStatement.executeQuery();


             boolean firstIteration = true;
             while (resultSet.next()){

                 java.sql.Time dbSqlTime = resultSet.getTime("time");
                 String dbSqlTimeString = dbSqlTime.toString();

                 java.util.Date dbSqlTimeConverted = new java.util.Date(dbSqlTime.getTime());




                 if (firstIteration){
                     Chart chart = new Chart(dbSqlTimeConverted, 0.0,0.0, 0.0,0.0);
                     list.add(chart);
                 }



                 if (dbSqlTimeString.equals("00:00:00")
                         || dbSqlTimeString.equals("04:00:00")
                         || dbSqlTimeString.equals("08:00:00")
                         || dbSqlTimeString.equals("12:00:00")
                         || dbSqlTimeString.equals("16:00:00")
                         || dbSqlTimeString.equals("20:00:00")
                         ) {

                     Chart chart = new Chart(dbSqlTimeConverted, 0.0, 0.0, 0.0, 0.0);

                     chart.list.add(new  Chart(dbSqlTimeConverted, resultSet.getDouble("Open"),resultSet.getDouble("Hight"), resultSet.getDouble("Low"),resultSet.getDouble("Close")));

                     firstIteration = false;

                 }else {
                     //chart.list.add(new  Chart(dbSqlTimeConverted, resultSet.getDouble("Open"),resultSet.getDouble("Hight"), resultSet.getDouble("Low"),resultSet.getDouble("Close")));

                     firstIteration = false;

                 }



                 //                 list.add(new Chart(dbSqlTimeConverted. = , )
//
//
//
//                         dbSqlTimeConverted,  new double[] {
//                         resultSet.getDouble("Open"),
//                         resultSet.getDouble("Hight"),
//                         resultSet.getDouble("Low"),
//                         resultSet.getDouble("Close")});
             }


         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             try {
                 if (resultSet != null) resultSet.close();
             } catch (Exception e) {
             }
             try {
                 if (statement != null) statement.close();
             } catch (Exception e) {
             }
             try {
                 if (connection != null) connection.close();
             } catch (Exception e) {
             }
         }

         return list;


     }



}
