package com.fxdb;

import java.sql.*;
import java.util.Date;

public class Order {

    public boolean isOpen;

    public double priceOpen;
    public Date timeOpen;

    public Date timeClose;
    public double priceClose;

    public double capacity;

    public double takeProfit;
    public double stopLoss;

    public String type;

    public double swap;

    public double result;


    public Order(boolean isOpen, double priceOpen, Date timeOpen, double capacity, double takeProfit, String type) {
        this.isOpen = isOpen;
        this.priceOpen = priceOpen;
        this.timeOpen = timeOpen;
        this.capacity = capacity;
        this.takeProfit = takeProfit;
        this.type = type;
    }

    public void open (){


    }

    public void close (Date timeClose, double priceClose){

        this.timeClose = timeClose;
        isOpen = false;
        this.priceClose = priceClose;


        long mil = this.timeClose.getTime() - this.timeOpen.getTime();
        int days = (int)(mil/(24*60*60*1000));

        swap =  days * (-0.5);

        if (type.equals(Strategy.TYPEBUY)) {
            result = (priceClose - priceOpen) * 10000 + swap - 2;
        }
        else result = -(priceClose - priceOpen) * 10000 + swap - 2;


    }

    public boolean checkCurrentValue(Date time, double currentHight, double currentLow){

        if ((type.equals(Strategy.TYPEBUY) && currentHight >= this.takeProfit)
                ||
                (type.equals(Strategy.TYPESELL) && currentLow <= this.takeProfit))
        {
            this.close(time, this.takeProfit);

        }
        return !this.isOpen;



    }

//    public void checkCurrentValue(Date time, double currentHight, double currentLow){
//
//        if ((type.equals(Strategy.TYPEBUY) && currentHight >= this.takeProfit)
//                ||
//                (type.equals(Strategy.TYPESELL) && currentLow <= this.takeProfit))
//        {
//            this.close(time, this.takeProfit);
//
//        }
//
//    }

    @Override
    public String toString() {
        return "Order{" +
                "isOpen=" + isOpen +
                ", priceOpen=" + priceOpen +
                ", timeOpen=" + timeOpen +
                ", timeClose=" + timeClose +
                ", priceClose=" + priceClose +
                ", capacity=" + capacity +
                ", takeProfit=" + takeProfit +
                ", stopLoss=" + stopLoss +
                ", type='" + type + '\'' +
                ", swap=" + swap +
                ", result=" + result +
                '}';
    }
}
