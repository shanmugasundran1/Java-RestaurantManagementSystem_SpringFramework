package Order;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import Base.MenuItem;

public class Order {
	ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
	MenuItem menuItemBean = (MenuItem) context.getBean("menuitembean");
	OrderDetail orderDetailBean = (OrderDetail) context.getBean("orderdetailbean");
	OrderDetail orderDetailBeanNew = (OrderDetail) context.getBean("orderdetailbean");
	
	
    final public static int ORDER_CLOSED = 1;
    final public static int ORDER_CANCELED = 2;

    public int orderID;
    public int staffID;
    public String staffName;
    public String date;
    public int state; //0:arrive 1:closed 2:canceled
    public double total;
    public ArrayList < OrderDetail > orderDetailList = new ArrayList < OrderDetail > ();

    /**
     * Constructor for objects of class Order
     */

    public Order(int newStaffID, String newStaffName) {
        this.orderID = -1;
        this.state = 0;
        this.staffID = newStaffID;
        this.staffName = newStaffName;
        this.total = 0;
    }
    /**
     *Getter
     */
    public int getOrderID() {
        return this.orderID;
    }
    public int getStaffID() {
        return this.staffID;
    }
    public String getStaffName() {
        return this.staffName;
    }
    public int getState() {
        return this.state;
    }
    public double getTotal() {
        return this.total;
    }
    public ArrayList < OrderDetail > getOrderDetail() {
        return this.orderDetailList;
    }

    /**
     * Setter
     */
    public void setOrderID(int newID) {
        this.orderID = newID;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addItem(MenuItem menuItemBean, byte quantity) {
        Iterator < OrderDetail > it = orderDetailList.iterator();

        boolean found = false;

        while (it.hasNext() && !found) {
            orderDetailBean = it.next();
            if (menuItemBean.getID() == orderDetailBean.getItemID()) {
                found = true;
                orderDetailBean.addQuantity(quantity);
            }
        }

        if (!found) {
//          orderDetailBeanNew = new OrderDetail(menuItemBean, quantity);
        	orderDetailBeanNew = orderDetailBean.createNew(menuItemBean, quantity);
            orderDetailList.add(orderDetailBeanNew);
        }

        calculateTotal();
    }

    public boolean deleteItem(int index) {
        try {
            orderDetailList.remove(index);
            calculateTotal();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void calculateTotal() {
        total = 0;
        Iterator < OrderDetail > it = orderDetailList.iterator();
        while (it.hasNext()) {
        	orderDetailBean = it.next();
            total += orderDetailBean.getTotalPrice();
        }
    }
}