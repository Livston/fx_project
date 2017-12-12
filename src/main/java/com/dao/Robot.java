package com.dao;

import com.fxdb.DBWorker;

import java.util.*;

public class Robot {

    private LinkedList<Order> ordersBuy = new LinkedList<Order>();
    private LinkedList<Order> ordersSell = new LinkedList<Order>();
    private ArrayList<Order> closedOrders = new ArrayList<Order>();


    public void startTraid() {

        double target = 0.01;

        DBWorker dbWorker = new DBWorker();

        LinkedHashMap<java.util.Date, double[]> map = dbWorker.graffikMap();

        Set<Date> dates = map.keySet();

        Date lastDate = new Date();
        double lastPrice = 0.0;

        boolean firstIt = true;
        double nextPointUp = 0.0;
        double nextPointDown = 0.0;

        for (java.util.Date time : dates) {

            if (firstIt) {

                Order orderBuy = new Order(true, map.get(time)[0], time, 1, map.get(time)[0] + target, Strategy.TYPEBUY);
                ordersBuy.add(orderBuy);
                Order orderSell = new Order(true, map.get(time)[0], time, 1, map.get(time)[0] - target, Strategy.TYPESELL);
                ordersSell.add(orderSell);

                firstIt = false;

                nextPointUp = map.get(time)[0] + target;
                nextPointDown = map.get(time)[0] - target;

            }

            double currentHight = map.get(time)[1];
            double currentLow = map.get(time)[2];

            if (currentHight >= nextPointUp) {

                //close previous orders(it could be more than one order)

                while (ordersBuy.size() != 0 ) {
                    Order order = ordersBuy.getLast();
                    if (nextPointUp > order.priceOpen){
                        order.close(time, nextPointUp);
                        closedOrders.add(order);
                        ordersBuy.removeLast();

                    }else { break ;}
                }
                ordersBuy.add(new Order(true, nextPointUp, time, 1, nextPointUp + target, Strategy.TYPEBUY));
                ordersSell.add(new Order(true, nextPointUp, time, 1, nextPointUp - target, Strategy.TYPESELL));

                nextPointUp = nextPointUp + target;
                nextPointDown = nextPointDown + target;
                continue;
            }


            if (currentLow <= nextPointDown) {

                //close previous orders(it could be more than one order)

                while (ordersSell.size() != 0 ) {
                    Order order = ordersSell.getLast();
                    if (nextPointDown < order.priceOpen) {
                        order.close(time, nextPointDown);
                        closedOrders.add(order);
                        ordersSell.removeLast();

                    } else {
                        break;
                    }
                }

                ordersBuy.add(new Order(true, nextPointDown, time, 1, nextPointDown + target, Strategy.TYPEBUY));
                ordersSell.add(new Order(true, nextPointDown, time, 1, nextPointDown - target, Strategy.TYPESELL));

                nextPointDown = nextPointDown - target;
                nextPointUp = nextPointUp - target;
                continue;

            }

            lastDate = time;
            lastPrice = map.get(time)[3];
        }

        double closedResult = 0.0;
        for (Order order: closedOrders) {
            closedResult = closedResult + order.result;
        }
        System.out.println("closedOrders: " + closedOrders.size());
        System.out.println("closedResult: " + closedResult);



        double openResult = 0.0;

        for (Order order: ordersSell) {

            order.close(lastDate, lastPrice);

            openResult = openResult + order.result;

        }

        for (Order order: ordersBuy) {

            order.close(lastDate, lastPrice);

            openResult = openResult + order.result;

        }

        System.out.println("openOrders: " + (ordersSell.size() + ordersBuy.size()));
        System.out.println("openOrders: " + openResult);

    }


    public void GetAnalize (){

        DBWorker dbWorker = new DBWorker();
        ArrayList <Chart> list = dbWorker.getlistH4();

    }
}
