package ru.sbt.testTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();

        Long t1 = System.currentTimeMillis();

        List<Order> buyOrder = new ArrayList<Order>();
        List<Order> sellOrder = new ArrayList<Order>();
        List<Order> buy1 = new ArrayList<Order>();
        List<Order> sell1 = new ArrayList<Order>();
        List<Order> buy2 = new ArrayList<Order>();
        List<Order> sell2 = new ArrayList<Order>();
        List<Order> buy3 = new ArrayList<Order>();
        List<Order> sell3 = new ArrayList<Order>();
        List<OrderBook> buyBook1 = new ArrayList<OrderBook>();
        List<OrderBook> sellBook1 = new ArrayList<OrderBook>();
        List<OrderBook> buyBook2 = new ArrayList<OrderBook>();
        List<OrderBook> sellBook2 = new ArrayList<OrderBook>();
        List<OrderBook> buyBook3 = new ArrayList<OrderBook>();
        List<OrderBook> sellBook3 = new ArrayList<OrderBook>();

        try {
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("AddOrder");
            NodeList dList = doc.getElementsByTagName("DeleteOrder");

            addOrder(nList, buyOrder, sellOrder);
            deleteOrder(dList, buyOrder, sellOrder);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Collections.sort(buyOrder, Collections.reverseOrder());
        Collections.sort(sellOrder);

        placement("book-1", buyOrder, buy1);
        placement("book-2", buyOrder, buy2);
        placement("book-3", buyOrder, buy3);
        placement("book-1", sellOrder, sell1);
        placement("book-2", sellOrder, sell2);
        placement("book-3", sellOrder, sell3);

        aggregate(sellBook1, sell1);
        aggregate(sellBook2, sell2);
        aggregate(sellBook3, sell3);
        aggregate(buyBook1, buy1);
        aggregate(buyBook2, buy2);
        aggregate(buyBook3, buy3);

        print(buyBook1, sellBook1, "book-1");
        print(buyBook2, sellBook2, "book-2");
        print(buyBook3, sellBook3, "book-3");

        System.out.println(System.currentTimeMillis()-t1);


    }

    private static void aggregate(List<OrderBook> Orderbook, List<Order> order) {
        int z = 0;
        if (order.size() > 0) {
            int vol = order.get(0).getVolume();
            Orderbook.add(new OrderBook(order.get(0).getVolume(), order.get(0).getPrice()));
            for (int i = 1; i < order.size(); i++) {
                if (order.get(i - 1).getPrice() == order.get(i).getPrice()) {
                    vol = vol + order.get(i).getVolume();
                    Orderbook.set(z, new OrderBook(vol, order.get(i).getPrice()));
                } else {
                    vol = order.get(i).getVolume();
                    z++;
                    Orderbook.add(new OrderBook(vol, order.get(i).getPrice()));
                }

            }
        }
    }

    private static void addOrder(NodeList nList, List<Order> buyOrder, List<Order> sellOrder) {
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            Element eElement = (Element) nNode;

            String book = eElement.getAttribute("book");
            String operation = eElement.getAttribute("operation");
            double price = Double.parseDouble(eElement.getAttribute("price"));
            int volume = Integer.parseInt(eElement.getAttribute("volume"));
            long orderId = Long.parseLong(eElement.getAttribute("orderId"));

            if (operation.equals("BUY")) {
                buyOrder.add(new Order(book, operation, price, volume, orderId));
            }
            if (operation.equals("SELL")) {
                sellOrder.add(new Order(book, operation, price, volume, orderId));
            }
        }
    }

    private static void deleteOrder(NodeList dList, List<Order> buyOrder, List<Order> sellOrder) {
        for (int temp = 0; temp < dList.getLength(); temp++) {
            Node dNode = dList.item(temp);
            Element eElement = (Element) dNode;

            long orderId = Long.parseLong(eElement.getAttribute("orderId"));

            for (Order str : buyOrder) {
                if (str.getOrderId() == orderId) {
                    buyOrder.remove(str);
                    break;
                }
            }
            for (Order str : sellOrder) {
                if (str.getOrderId() == orderId) {
                    sellOrder.remove(str);
                    break;
                }
            }
        }
    }

    private static void placement(String book, List<Order> list, List<Order> newList) {
        for (Order str : list) {
            if (book.equals(str.getBook())) {
                newList.add(str);
            }
        }
    }

    private static void print(List<OrderBook> buyBook, List<OrderBook> sellBook, String name) {
        System.out.println("Order Book: " + name + "\n" + "BID" + "\t\t\t" + "ASK");
        for (OrderBook str : buyBook) {
            System.out.println(str);
        }
        for (OrderBook str : sellBook) {
            System.out.println("\t\t" +str);
        }
    }
}
