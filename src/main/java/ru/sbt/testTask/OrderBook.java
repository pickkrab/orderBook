package ru.sbt.testTask;

public class OrderBook implements Comparable<OrderBook> {
    private int volume;
    private double price;

    public OrderBook(int volume, double price) {
        super();
        this.volume = volume;
        this.price = price;
    }

    @Override
    public int compareTo(OrderBook o) {

        if (this.price > o.price) {
            return 1;
        }
        else if (this.price < o.price) {
            return -1;
        }
        else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return volume+"@"+ price;
    }
}
