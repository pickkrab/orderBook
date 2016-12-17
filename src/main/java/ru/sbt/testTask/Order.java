package ru.sbt.testTask;

public class Order implements Comparable<Order> {

    private String book;
    private String operation;
    private double price;
    private int volume;
    private long orderId;

    public Order(String book, String operation, double price, int volume, long orderId) {
        super();
        this.book = book;
        this.operation = operation;
        this.price = price;
        this.volume = volume;
        this.orderId = orderId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    public long getOrderId() {
        return orderId;
    }

    public String getBook() { return book; }

    @Override
    public int compareTo(Order o) {

        if (this.price > o.price) {
            return 1;
        } else if (this.price < o.price) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return book + ", " + operation + ", " + price + ", " + volume + ", " + orderId;
    }
}
