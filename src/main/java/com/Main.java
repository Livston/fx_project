package com;

import com.dao.Robot;

public class Main {

    public static void main(String[] args) {


        long startTime = System.currentTimeMillis();

        Robot robot = new Robot();
        //robot.startTraid();
        robot.GetAnalize();

        //DBWorker dbWorker = new DBWorker();
        //dbWorker.insertDataM5();

        long finishTime = System.currentTimeMillis();
        System.out.println(finishTime - startTime + ": ms");




    }


}





