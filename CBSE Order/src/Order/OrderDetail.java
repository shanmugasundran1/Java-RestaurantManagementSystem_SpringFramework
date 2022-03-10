package Order;
import Base.MenuItem;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OrderDetail {
    ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
    MenuItem menuItemBean = (MenuItem) context.getBean("menuitembean");
	
    public int itemID;
    public String itemName;
    public double price;
    public byte quantity;
    public double totalPrice;

    
    public OrderDetail(MenuItem menuItemBean, byte newQuantity) {
        this.itemID = menuItemBean.getID();
        this.itemName = menuItemBean.getName();
        this.price = menuItemBean.getPrice();
        this.quantity = newQuantity;
        this.totalPrice = this.price * this.quantity;
    }

    /**************************************************
     * Getter
     *************************************************/
    public int getItemID() {
        return this.itemID;
    }
    public String getItemName() {
        return this.itemName;
    }
    public double getPrice() {
        return this.price;
    }
    public byte getQuantity() {
        return this.quantity;
    }
    public double getTotalPrice() {
        return this.totalPrice;
    }

    public void addQuantity(byte add) {
        quantity += add;
        totalPrice = price * quantity;
    }
    
    public OrderDetail createNew(MenuItem menuItemBean, byte newQuantity) {
    	OrderDetail orderdetailNew = new OrderDetail(menuItemBean, newQuantity);
    	return orderdetailNew;
    }

}