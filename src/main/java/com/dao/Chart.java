package com.dao;

import java.util.ArrayList;
import java.util.Date;

public class Chart {

    public ArrayList <Chart> list = new ArrayList<Chart>();

    public Date timeOpen;

    public double open;
    public double hight;
    public double low;
    public double close;

    public Chart(Date timeOpen, double open, double hight, double low, double close) {
        this.timeOpen = timeOpen;
        this.open = open;
        this.hight = hight;
        this.low = low;
        this.close = close;
    }
}
