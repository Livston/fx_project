package com.fxdb;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class Main {

    private static final String url = "jdbc:sqlite:D:\\DB\\FXDB\\FXDB.db";

    public static void main(String[] args) {


        long startTime = System.currentTimeMillis();

        Robot robot = new Robot();
        robot.startTraid();

        //DBWorker dbWorker = new DBWorker();
        //dbWorker.insertDataM5();

        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTime + ": ms");




    }


}





