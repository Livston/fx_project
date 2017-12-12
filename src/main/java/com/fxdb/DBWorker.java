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

     public ArrayList <Chart> getlistH4(){

         ArrayList <Chart> listH4 = new ArrayList<Chart>();
         ArrayList <Chart> listM5 = new ArrayList<Chart>();

         String sql = "SELECT * FROM M5 WHERE TIME >= ? AND TIME < ?";

         try {
             preparedStatement = connection.prepareStatement(sql);

             preparedStatement.setString(1, "2001-01-01 00:00:00");
             preparedStatement.setString(2, "2015-12-12  00:00:00");

             resultSet = preparedStatement.executeQuery();


             while (resultSet.next()) {

                 java.sql.Time dbSqlTime = resultSet.getTime("time");
                 java.util.Date dbSqlTimeConverted = new java.util.Date(dbSqlTime.getTime());

                 listM5.add(new Chart(dbSqlTimeConverted,
                         resultSet.getDouble("Open"),
                         resultSet.getDouble("Hight"),
                         resultSet.getDouble("Low"),
                         resultSet.getDouble("Close")));
             }



             listH4.add(new Chart(listM5.get(0).timeOpen, listM5.get(0).open, listM5.get(0).hight, listM5.get(0).low,
                     listM5.get(0).close));
             listH4.get(0).list.add(new Chart(listM5.get(0).timeOpen, listM5.get(0).open, listM5.get(0).hight, listM5.get(0).low, listM5.get(0).close));

             Calendar calendar = GregorianCalendar.getInstance();

             int numOfHours;
             int numOfMinutes;
             int numOfSec;
             int numOfDay;

             boolean newH4 = false;

             for (int i = 1; i < listM5.size(); i++) {

                 calendar.setTime(listH4.get(listH4.size() - 1).timeOpen);
                 int previousDay = calendar.get(Calendar.DAY_OF_YEAR);

                 calendar.setTime(listM5.get(i).timeOpen);

                 numOfHours = calendar.get(Calendar.HOUR_OF_DAY);
                 numOfMinutes = calendar.get(Calendar.MINUTE);
                 numOfSec = calendar.get(Calendar.SECOND);
                 numOfDay = calendar.get(Calendar.DAY_OF_YEAR);

                 newH4 = numOfDay != previousDay || (
                         (numOfHours == 0 && numOfMinutes == 0 && numOfSec == 0)
                                 || (numOfHours == 4 && numOfMinutes == 0 && numOfSec == 0)
                                 || (numOfHours == 8 && numOfMinutes == 0 && numOfSec == 0)
                                 || (numOfHours == 12 && numOfMinutes == 0 && numOfSec == 0)
                                 || (numOfHours == 16 && numOfMinutes == 0 && numOfSec == 0)
                                 || (numOfHours == 20 && numOfMinutes == 0 && numOfSec == 0)
                 );

                 if (newH4) {

                     listH4.add(new Chart(listM5.get(i).timeOpen, listM5.get(i).open, listM5.get(i).hight, listM5.get(i).low,
                             listM5.get(i).close));
                     listH4.get(listH4.size() - 1).list.add(new Chart(listM5.get(i).timeOpen, listM5.get(i).open, listM5.get(i).hight, listM5.get(i).low, listM5.get(i).close));

                 } else {

                     listH4.get(listH4.size() - 1).list.add(new Chart(listM5.get(i).timeOpen, listM5.get(i).open, listM5.get(i).hight, listM5.get(i).low, listM5.get(i).close));

                 }

             }

             for (Chart chart: listH4) {

                 chart.establishLevels();

             }

             System.out.println("1");

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

         return listH4;


     }



}
