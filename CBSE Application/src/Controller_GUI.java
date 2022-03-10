import java.util.*;
import Base.MenuItem;
import Order.Order;
import Order.OrderDetail;
import Staff.Manager;
import Staff.Staff;
import java.text.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Controller_GUI {
	ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
	MenuItem menuItemBean = (MenuItem) context.getBean("menuitembean");
	OrderDetail orderDetailBean = (OrderDetail) context.getBean("orderdetailbean");
	Order orderBean = (Order) context.getBean("orderbean");
	Staff staffBean = (Staff) context.getBean("staffbean");
    Manager managerBean = (Manager) context.getBean("managerbean");
	
	public static void main(String[] args) {
		Controller_GUI cController = new Controller_GUI();
	}
	
    public UserInterface_GUI cView;
    public Database cDatabase;
    public int currentUserID;
    public String currentUserName;
    public String todaysDate;

    public int todaysOrderCnt; //Today's order count
    public double totalSales; //Today's total sales
    public int todaysCancelCnt; //Today's cancel count
    public double cancelTotal; //Total cost of today's canceled orders
    
    public String errorMessage;

    //define user type
    public int USER_ANONYMOUS = 0;
    public int USER_EMPLOYEE = 1;
    public int USER_MANAGER = 2;

    public Controller_GUI() {
        this.cDatabase = new Database();
        cDatabase.loadFiles();


        cView = new UserInterface_GUI(this);

        Date date = new Date();
        SimpleDateFormat stf = new SimpleDateFormat("yyyy/MM/dd");
        todaysDate = stf.format(date);
        cView.setVisible(true);
        cView.setTodaysDate(todaysDate);
        todaysOrderCnt = 0;
        totalSales = 0;
        todaysCancelCnt = 0;
        cancelTotal = 0;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        String result = this.errorMessage;
        this.errorMessage = "";
        return result;
    }

    public int getTodaysOrderCnt() {
        return this.todaysOrderCnt;
    }

    public int getTodaysCancelCnt() {
        return this.todaysCancelCnt;
    }

    public double getTotalSales() {
        return this.totalSales;
    }

    public double getCancelTotal() {
        return this.cancelTotal;
    }

    public double getOrderTotalCharge(int orderID) {
        return cDatabase.getOrderTotalCharge(orderID);
    }

    public int getOrderState(int orderID) {
        return cDatabase.getOrderState(orderID);
    }

    public String getCurrentUserName() {
        return this.currentUserName;
    }

    /***********************************************************
     * Login 
     **********************************************************/
    //----------------------------------------------------------
    // Find user
    //----------------------------------------------------------  
    public boolean loginCheck(int inputID, String inputPassword, boolean isManager) {
        final String searchClassName;

        //---------search user----------
        staffBean = cDatabase.findStaffByID(inputID);

        if (isManager) searchClassName = "Manager";
        else searchClassName = "Employee";

        if (staffBean != null) //User data is found
        {
            //Search only particular target(Manager or Employee)
                if (staffBean.getPassword().equals(inputPassword)) {
                    if (staffBean.getWorkState() == 0) //Not clocked in yet
                    {
                        staffBean.clockIn();
                    }
                    if (isManager) {
                        cView.changeMode(cView.MODE_MANAGER);
                    } else {
                        cView.changeMode(cView.MODE_EMPLOYEE);
                    }
                    currentUserID = inputID;
                    currentUserName = staffBean.getFullName();
                    cView.setLoginUserName(currentUserName); //show user name on the view

                    return true; //Login success
                } else {
                    setErrorMessage("Password unmatch.");
                    return false;
                }

        } else {
            setErrorMessage("Not found.");
            return false;
        }

    }

    //----------------------------------------------------------
    // Logout (Set state as Anonymous)
    //----------------------------------------------------------
    public void userLogout() {
        currentUserID = 0;
        cView.setLoginUserName("");
    }



    /***********************************************************
     * Staff management
     **********************************************************/

    public Staff getStaffData(int staffID) {
        return cDatabase.findStaffByID(staffID);
    }


    /***********************************************************
     * Menu management
     **********************************************************/
    public MenuItem getMenuItemData(int menuItemID) {
        return cDatabase.findMenuItemByID(menuItemID);
    }



    /***********************************************************
     * Order management
     **********************************************************/
    public int createOrder() {
        return cDatabase.addOrder(currentUserID, currentUserName);
    }

    public boolean addNewOrderItem(int orderID, int addItemID, byte addItemQuantity) {
        orderBean = cDatabase.findOrderByID(orderID);
        if (currentUserID != orderBean.getStaffID()) {
            setErrorMessage("You are not eligible to edit the order.\nThe order belonges to " + orderBean.getStaffName() + ")");
            return false;
        }

        MenuItem rNewItem = null;

        rNewItem = cDatabase.findMenuItemByID(addItemID);
        if (rNewItem == null) {
            setErrorMessage("MenuID[" + addItemID + "]is not found.");
            addItemID = 0;
            return false;
        }

        //////////ADD!!!(database)/////////////
        cDatabase.addOrderItem(orderID, rNewItem, addItemQuantity);
        return true;
    }

    public boolean deleteOrderItem(int orderID, int deleteNo) {
    	orderBean = cDatabase.findOrderByID(orderID);
        if (currentUserID != orderBean.getStaffID()) {
            setErrorMessage("You are not eligible to delete the order.\nThe order belonges to " + orderBean.getStaffName() + ")");
            return false;
        }

        deleteNo -= 1;
        if (!cDatabase.deleteOrderItem(orderID, deleteNo)) {
            setErrorMessage("Not found.");
            return false;
        }
        return true;
    }

    public boolean closeOrder(int closeOrderID) {
    	orderBean = cDatabase.findOrderByID(closeOrderID);
        if (currentUserID != orderBean.getStaffID()) {
            setErrorMessage("You are not eligible to delete the order.\n(The order belonges to " + orderBean.getStaffName() + ")");
            return false;
        }

        if (orderBean.getState() != 0) {
            setErrorMessage("The order is already closed or canceled.");
            return false;
        }
        cDatabase.closeOrder(closeOrderID);
        todaysOrderCnt++;
        totalSales += orderBean.getTotal();
        return true;
    }

    public boolean cancelOrder(int cancelOrderID) {
        orderBean = cDatabase.findOrderByID(cancelOrderID);
        if (currentUserID != orderBean.getStaffID()) {
            setErrorMessage("You are not eligible to delete the order.\n(The order belonges to " + orderBean.getStaffName() + ")");
            return false;
        }

        if (orderBean.getState() != 0) {
            setErrorMessage("The order is already closed or canceled.");
            return false;
        }

        cDatabase.cancelOrder(cancelOrderID);
        todaysCancelCnt++;
        cancelTotal += orderBean.getTotal();
        return true;
    }




    /***********************************************************
     * Create string lists
     **********************************************************/
    public ArrayList < String > createStaffList() {
        Iterator < Staff > it = cDatabase.getStaffList().iterator();
        ArrayList < String > initData = new ArrayList < String > ();

        while (it.hasNext()) {
            staffBean = (Staff) it.next();
            String fullName = staffBean.getFullName();
            String output = String.format("Staff ID:%4d  Name:%-25s",
                staffBean.getID(), fullName);
            switch (staffBean.getWorkState()) {
                case Staff.WORKSTATE_ACTIVE:
                    output += "[From:" + staffBean.getStartTime() + "]";
                    break;
                case Staff.WORKSTATE_FINISH:
                    output += "[From:" + staffBean.getStartTime() + "]";
                    break;
                default:
                    output += "[Not on work]";
                    break;
            }

            if (staffBean instanceof Manager) {
                output += " * Manager *";
            }
            initData.add(output);
        }

        return initData;
    }



    public ArrayList < String > createOrderList() {
        Iterator < Order > it = cDatabase.getOrderList().iterator();
        String state;
        ArrayList < String > initData = new ArrayList < String > ();
        String output;

        while (it.hasNext()) {
            orderBean = it.next();
            switch (orderBean.getState()) {
                case Order.ORDER_CLOSED:
                    state = "Closed";
                    break;
                case Order.ORDER_CANCELED:
                    state = "Canceled";
                    break;
                default:
                    state = "-";
                    break;
            }

            output = String.format("Order ID:%4d  StaffName:%-20s  Total:$%5.2f State:%-8s\n",
                orderBean.getOrderID(), orderBean.getStaffName(), orderBean.getTotal(), state);
            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No order.");
        return initData;
    }



    public ArrayList < String > createOrderItemlList(int orderID) {
        orderBean = cDatabase.findOrderByID(orderID);
        ArrayList < String > initData = new ArrayList < String > ();

        if (orderBean == null) {
            initData.add("No order information");
            return initData;
        }

        String output;

        Iterator < OrderDetail > it = orderBean.getOrderDetail().iterator();
        int count = 0;

        while (it.hasNext()) {
        	orderDetailBean = it.next();
            output = String.format("%-4d|%-24s|%5d|%5.2f", ++count, orderDetailBean.getItemName(), orderDetailBean.getQuantity(), orderDetailBean.getTotalPrice());
            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No item");
        return initData;
    }

    public ArrayList < String > createMenuList(int disuplayMenuType) {
        Iterator < MenuItem > it = cDatabase.getMenuList().iterator();
        ArrayList < String > initData = new ArrayList < String > ();

        while (it.hasNext()) {
            MenuItem re = (MenuItem) it.next();
            byte menuType = re.getType();
            if (disuplayMenuType != 0 && disuplayMenuType != menuType)
                continue;
            String strMenuType;
            switch (menuType) {
                case MenuItem.MAIN:
                    strMenuType = "Main";
                    break;
                case MenuItem.DRINK:
                    strMenuType = "Drink";
                    break;
                case MenuItem.ALCOHOL:
                    strMenuType = "Alcohol";
                    break;
                case MenuItem.DESSERT:
                    strMenuType = "Dessert";
                    break;
                default:
                    strMenuType = "Undefined";
                    break;
            }
            String output = String.format("Menu ID:%4d  Name:%-20s  Price:%5.2f Type:%s", re.getID(), re.getName(), re.getPrice(), strMenuType);


            initData.add(output);
        }
        if (initData.isEmpty())
            initData.add("No order.");
        return initData;
    }
	

}