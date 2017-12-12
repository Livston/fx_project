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

    public void establishLevels(){

        for (Chart chart: list) {

           this.hight = Math.max(this.hight, chart.hight);
           this.low = Math.min(this.low, chart.low);

        }

        this.close = list.get(list.size()-1).close;

    }
}
