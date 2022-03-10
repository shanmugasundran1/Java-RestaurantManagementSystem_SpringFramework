import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
import Base.MenuItem;
import Order.Order;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class UserInterface_GUI extends JFrame implements ActionListener {
	ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
	MenuItem menuItemBean = (MenuItem) context.getBean("menuitembean");
	
    public Container con;
    public Controller_GUI rcController;
    public String currentUserName;

    // components for menu
    public JMenuBar menuBar;
    public JMenu mnFile;
    public JMenuItem mntm1, mntm2;


    //-------- Master panel -------------- 
    //Main content panel(CENTER)
    public JPanel mainPanel;

    //Head panel (North)
    public JPanel headPanel;
    public JLabel headTitle;
    public JButton headBtnLogin;
    public JButton headBtnLogout;

    //Main button panel(WEST)
    public JPanel mainBtnsPanel;
    // Main buttons

    public JButton mainBtnShowMenu;
    public JButton mainBtnManageOrder;
    // Main buttons for management
    public JButton mainBtnManageEmployee;
    public JButton mainBtnShowTotalSales;

    //Information panel(SOUTH)
    public JPanel infoPanel;
    public JLabel labelLoginUserName;
    public JTextArea taMessage;

    //-------- Contents panel --------------
    // components for home panel
    public JPanel homePanel;
    public JLabel homeImage;

    public LoginPanel cLoginPanel;
    public MenuListPanel cMenuListPanel;
    public OrderListPanel cOrderListPanel;
    public OrderDetailPanel cOrderDetailPanel;
    public EmployeeListPanel cEmployeeListPanel;
    public TotalSalesPanel cTotalSalesPanel;

    public final static int WINDOW_X = 100;
    public final static int WINDOW_Y = 100;
    public final static int WINDOW_WIDTH = 900;
    public final static int WINDOW_HEIGHT = 600;
    /**
     * Constructor for objects of class UserInterface_GUI
     */
    public UserInterface_GUI(Controller_GUI rController) {
        this.rcController = rController;
        this.con = getContentPane();

        // Set frame
        setTitle("Valentino Restaurant Management System");
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMasterPanelConpornents();
        currentUserName = "";
        setLoginUserName(currentUserName);

        //------- Create main content panels
        //Home panel
        homePanel = new JPanel();
        homeImage = new JLabel();

        //Random generator = new Random();
        int i = new Random().nextInt(4) + 1;
        homeImage.setHorizontalAlignment(SwingConstants.CENTER);
        homeImage.setVerticalAlignment(SwingConstants.CENTER);
        homeImage.setIcon(new ImageIcon("images/home" + i + ".jpg"));
        homePanel.add(homeImage);
        homePanel.setBackground(Color.WHITE);
        mainPanel.add("Home", homePanel);

        cLoginPanel = new LoginPanel();
        mainPanel.add("Login", cLoginPanel);
        cMenuListPanel = new MenuListPanel();
        mainPanel.add("MenuList", cMenuListPanel);
        cOrderListPanel = new OrderListPanel();
        mainPanel.add("OrderList", cOrderListPanel);
        cOrderDetailPanel = new OrderDetailPanel();
        mainPanel.add("OrderDetail", cOrderDetailPanel);
        cEmployeeListPanel = new EmployeeListPanel();
        mainPanel.add("EmployeeList", cEmployeeListPanel);
        cTotalSalesPanel = new TotalSalesPanel();
        mainPanel.add("TotalSalesPanel", cTotalSalesPanel);

        changeMode(MODE_ANONYMOUS);
    }

    public void createMasterPanelConpornents() {
        // Add menu to frame
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu("File");
        menuBar.add(mnFile);

        mntm1 = new JMenuItem("[1] Login");
        mnFile.add(mntm1);
        mntm1.addActionListener(this);

        mntm2 = new JMenuItem("[2] Exit");
        mnFile.add(mntm2);
        mntm2.addActionListener(this);

        //----------- Create main panels ------------
        con.setLayout(new BorderLayout());

        //head panel
        headPanel = new JPanel();
        headPanel.setBackground(Color.BLACK);
        headPanel.setLayout(new FlowLayout());
        headTitle = new JLabel("Valentino Restaurant Management System");
        headTitle.setForeground(Color.WHITE);
        headTitle.setPreferredSize(new Dimension(500, 30));
        headTitle.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
        headBtnLogin = new JButton("Login");
        headBtnLogin.addActionListener(this);
        headBtnLogout = new JButton("Logout");
        headBtnLogout.addActionListener(this);
        headPanel.add(headTitle);
        headPanel.add(headBtnLogin);
        headPanel.add(headBtnLogout);
        con.add(headPanel, BorderLayout.NORTH);

        //main content panel
        mainPanel = new JPanel();
        mainPanel.setOpaque(true);
        mainPanel.setLayout(new CardLayout());
        con.add(mainPanel, BorderLayout.CENTER);

        //main operate buttons panel
        mainBtnsPanel = new JPanel();
        mainBtnsPanel.setLayout(new GridLayout(0, 1));

        mainBtnShowMenu = new JButton("Show menu");
        mainBtnShowMenu.addActionListener(this);
        mainBtnsPanel.add(mainBtnShowMenu);

        mainBtnManageOrder = new JButton("Order management");
        mainBtnManageOrder.addActionListener(this);
        mainBtnsPanel.add(mainBtnManageOrder);

        mainBtnManageEmployee = new JButton("Manage employees");
        mainBtnManageEmployee.addActionListener(this);
        mainBtnsPanel.add(mainBtnManageEmployee);


        mainBtnShowTotalSales = new JButton("Show total sales");
        mainBtnShowTotalSales.addActionListener(this);
        mainBtnsPanel.add(mainBtnShowTotalSales);


        con.add(mainBtnsPanel, BorderLayout.WEST);

        //Information panel
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout());
        labelLoginUserName = new JLabel();
        labelLoginUserName.setPreferredSize(new Dimension(150, 50));
        taMessage = new JTextArea(3, 50);
        taMessage.setEditable(false);
        taMessage.setText("Wellcome!!");
        taMessage.setOpaque(true);
        LineBorder border = new LineBorder(Color.BLACK, 3, true);
        taMessage.setBorder(border);
        taMessage.setBackground(Color.WHITE);
        infoPanel.add(labelLoginUserName);
        infoPanel.add(taMessage);
        con.add(infoPanel, BorderLayout.SOUTH);
    }


    public void setLoginUserName(String newName) {
        currentUserName = newName;
        if (newName == "") {
            labelLoginUserName.setText("Please login first.");
        } else {
            labelLoginUserName.setText("<html>Login user<br>" + newName + "</html>");
        }
    }

    public final static byte MODE_ANONYMOUS = 0;
    public final static byte MODE_EMPLOYEE = 1;
    public final static byte MODE_MANAGER = 2;

    public void changeMode(byte state) {
        switch (state) {
            case MODE_ANONYMOUS:
                headBtnLogout.setEnabled(false);
                mainBtnShowMenu.setEnabled(false);
                mainBtnManageOrder.setEnabled(false);
                mainBtnManageEmployee.setEnabled(false);
                mainBtnShowTotalSales.setEnabled(false);
                break;
            case MODE_EMPLOYEE:
                headBtnLogout.setEnabled(true);
                mainBtnShowMenu.setEnabled(true);
                mainBtnManageOrder.setEnabled(true);
                mainBtnManageEmployee.setEnabled(false);
                mainBtnShowTotalSales.setEnabled(false);
                break;
            case MODE_MANAGER:
                headBtnLogout.setEnabled(true);
                mainBtnShowMenu.setEnabled(true);
                mainBtnManageOrder.setEnabled(true);
                mainBtnManageEmployee.setEnabled(true);
                mainBtnShowTotalSales.setEnabled(true);
                break;
        }
    }

    public void setTodaysDate(String today) {
        ////
    }
    //--------------------------------------------------------
    // Display message on an information panel
    //--------------------------------------------------------
    public void displayMessage(String message) {
        taMessage.setForeground(Color.BLACK);
        taMessage.setText(message);
    }

    public void displayErrorMessage(String message) {
        taMessage.setForeground(Color.RED);
        taMessage.setText(message);
    }

    //========================================================
    // Show dialog message
    //========================================================
    final static int DIALOG_YES = JOptionPane.YES_OPTION;
    final static int DIALOG_NO = JOptionPane.NO_OPTION;
    final static int DIALOG_CANCEL = JOptionPane.CANCEL_OPTION;

    public int showYesNoDialog(String title, String message) {
        int option = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        return option;
    }

    public int showYesNoCancelDiaglog(String title, String message) {
        int option = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        return option;
    }

    public void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showConfirmDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
    }


    public int getIDfromString(String stringLine, int length) {
        int index = stringLine.indexOf("ID:"); //Search string of "ID:"
        if (index == -1) {
            showErrorDialog("Error", "String 'ID:' is not found!!");
            return -1;
        }

        try {
            String strID = stringLine.substring(index + 3, index + 3 + length);
            int id = Integer.parseInt(strID.trim());
            return id;
        } catch (Exception e) {
            showErrorDialog("Error", "Parse error");
            return -1;
        }
    }


    //========================================================
    // Master panel action
    //========================================================
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == mntm2) {
            System.exit(0);
        } else if (ae.getSource() == mainBtnShowMenu) {
            //((CardLayout) mainPanel.getLayout()).show( mainPanel, "MenuList");
            changeMainPanel("MenuList");
            cMenuListPanel.init();
        } else if (ae.getSource() == mainBtnManageOrder) {
            //((CardLayout) mainPanel.getLayout()).show( mainPanel, "OrderList");
            changeMainPanel("OrderList");
            cOrderListPanel.init();
        } else if (ae.getSource() == mainBtnManageEmployee) {
            changeMainPanel("EmployeeList");
            cEmployeeListPanel.init();
        } else if (ae.getSource() == mainBtnShowTotalSales) {
            changeMainPanel("TotalSalesPanel");
            cTotalSalesPanel.init();
        } else if (ae.getSource() == headBtnLogin || ae.getSource() == mntm1) {
            changeMainPanel("Login");
            cLoginPanel.init();
            displayMessage("Enter your login ID and password.");
        } else if (ae.getSource() == headBtnLogout) {
            if (showYesNoDialog("Logout", "Are you sure to logout?") == DIALOG_YES) {
                rcController.userLogout();
                changeMainPanel("Home");
                changeMode(MODE_ANONYMOUS);
            }
        }

    }



    /****************************************************************
     * Login panel
     *****************************************************************/
    public class LoginPanel extends JPanel implements ActionListener {
        // components for login panel
        //public JPanel         loginPanel;
        public JLabel lblUserID;
        public JTextField tfUserID;
        public JLabel lblPassword;
        public JPasswordField pwPassword;
        public JCheckBox chbIsManager;
        public JButton btnLoginOK;
        public LoginPanel() {
            //loginPanel = new JPanel();
            GridBagLayout gbLayout = new GridBagLayout();
            this.setLayout(gbLayout);
            GridBagConstraints gbc = new GridBagConstraints();
            lblUserID = new JLabel("UserID:");
            lblUserID.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbLayout.setConstraints(lblUserID, gbc);
            this.add(lblUserID);

            tfUserID = new JTextField(20);
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbLayout.setConstraints(tfUserID, gbc);
            this.add(tfUserID);

            lblPassword = new JLabel("Password:");
            lblPassword.setPreferredSize(new Dimension(100, 30));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbLayout.setConstraints(lblPassword, gbc);
            this.add(lblPassword);

            pwPassword = new JPasswordField(20);
            gbc.gridx = 1;
            gbc.gridy = 1;
            gbLayout.setConstraints(pwPassword, gbc);
            this.add(pwPassword);

            chbIsManager = new JCheckBox("Login as manager");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(chbIsManager, gbc);
            this.add(chbIsManager);

            btnLoginOK = new JButton("Login");
            btnLoginOK.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(btnLoginOK, gbc);
            this.add(btnLoginOK);
        }

        public void setUserID(String id) {
            tfUserID.setText(id);
        }

        public void setPassword(String password) {
            pwPassword.setText(password);
        }

        public void init() {
            setUserID("");
            setPassword("");
            tfUserID.setBackground(UIManager.getColor("TextField.background"));
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnLoginOK) {
                //Check whether current focuced compornent have to verify their value
                if (btnLoginOK.getVerifyInputWhenFocusTarget()) {
                    //Try to get focus
                    btnLoginOK.requestFocusInWindow();
                    if (!btnLoginOK.hasFocus()) { //Can not get focus ?Ë the compornent have not been verified
                        return;
                    }
                }
                //if(!inputVerified)
                //    return;

                char[] password;
                boolean isManager = chbIsManager.isSelected();

                byte state = -1;

                String inputID = tfUserID.getText();

                if (inputID.equals("")) {
                    displayErrorMessage("Enter user ID");
                    return;
                }


                password = pwPassword.getPassword();
                String inputPassword = new String(password);
                if (inputPassword.equals("")) {
                    displayErrorMessage("Enter password");
                    return;
                }

                if (rcController.loginCheck(Integer.parseInt(inputID), inputPassword, isManager)) {
                    showConfirmDialog("Message", "Login success!!");
                    displayMessage("Wellcome, " + currentUserName);
                    tfUserID.setText("");
                    pwPassword.setText("");
                    changeMainPanel("Home");
                } else {
                    displayErrorMessage(rcController.getErrorMessage());
                }
            }
        }
    }

    public void changeMainPanel(String panelName) {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, panelName);
        displayMessage("Main panel change :" + panelName);
    }




    /****************************************************************
     * Menu list panel
     *****************************************************************/
    public class MenuListPanel extends JPanel implements ActionListener {
        public JScrollPane scrollPanel;
        public JTextArea displayArea;
        public JPanel btnPanel;
        public JButton btnAll;
        public JButton btnMain;
        public JButton btnDrink;
        public JButton btnAlcohol;
        public JButton btnDessert;

        public MenuListPanel() {
            this.setLayout(new BorderLayout());
            displayArea = new JTextArea();
            displayArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            displayArea.setEditable(false);
            displayArea.setMargin(new Insets(5, 5, 5, 5));
            scrollPanel = new JScrollPane(displayArea);
            scrollPanel.setPreferredSize(new Dimension(200, 400));
            add(scrollPanel, BorderLayout.CENTER);

            btnPanel = new JPanel();
            btnPanel.setLayout(new FlowLayout());
            btnAll = new JButton("All");
            btnAll.addActionListener(this);
            btnMain = new JButton("Main");
            btnMain.addActionListener(this);
            btnDrink = new JButton("Drink");
            btnDrink.addActionListener(this);
            btnAlcohol = new JButton("Alcohol");
            btnAlcohol.addActionListener(this);
            btnDessert = new JButton("Dessert");
            btnDessert.addActionListener(this);

            btnPanel.add(btnAll);
            btnPanel.add(btnMain);
            btnPanel.add(btnDrink);
            btnPanel.add(btnAlcohol);
            btnPanel.add(btnDessert);

            add(btnPanel, BorderLayout.SOUTH);
        }

        public void init() {
            showMenuList(0);
            //displayArea.setText(str);
            //showAllMenuList(displayArea);
        }

        public void showMenuList(int menuType) {
            displayArea.setText("");
            ArrayList < String > menuList = rcController.createMenuList(menuType);
            for (int i = 0; i < menuList.size(); i++)
                displayArea.append(menuList.get(i) + "\n");
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnAll) {
                showMenuList(0);
                //showAllMenuList(displayArea);
            } else if (ae.getSource() == btnMain) {
                showMenuList(menuItemBean.MAIN);
                //showParticularMenuList(MenuItem.MAIN, displayArea);
            } else if (ae.getSource() == btnDrink) {
                showMenuList(menuItemBean.DRINK);
                //showParticularMenuList(MenuItem.DRINK, displayArea);
            } else if (ae.getSource() == btnAlcohol) {
                showMenuList(menuItemBean.ALCOHOL);
                //showParticularMenuList(MenuItem.ALCOHOL, displayArea);
            } else if (ae.getSource() == btnDessert) {
                showMenuList(menuItemBean.DESSERT);
                //showParticularMenuList(MenuItem.DESSERT, displayArea);
            }
        }
    }



    /****************************************************************
     * Employee list panel
     *****************************************************************/
    public class EmployeeListPanel extends JPanel {
        public JScrollPane scrollPanel;
        public JList displayList;
        //public JPanel          btnPanel;

        public EmployeeListPanel() {
            GridBagLayout gbLayout = new GridBagLayout();
            this.setLayout(gbLayout);
            GridBagConstraints gbc = new GridBagConstraints();

            scrollPanel = new JScrollPane();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.gridwidth = 4;
            gbLayout.setConstraints(scrollPanel, gbc);
            this.add(scrollPanel);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.weighty = 0;
            gbc.weightx = 0.25;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            displayList = new JList();
            displayList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }

        public void init() {
            showStaffList();
        }

        public void showStaffList() {
            displayList.setListData(rcController.createStaffList().toArray());
            scrollPanel.getViewport().setView(displayList);
        }

        public int getSelectedStaffID() {
            String orderLine = (String) displayList.getSelectedValue();
            if (orderLine == null)
                return -1;

            return getIDfromString(orderLine, 4);
        }

    }



    /****************************************************************
     * Order list panel (External)
     *****************************************************************/
    public class OrderListPanel extends JPanel implements ActionListener {
        public JScrollPane scrollPanel;
        //public JTextArea       displayArea;
        public JPanel btnPanel;
        public JButton btnNewOrder;
        public JButton btnEditOrder;
        public JButton btnCloseOrder;
        public JButton btnCancelOrder;
        public JLabel lblTotalSales;
        public JLabel lblTotalCount;
        public JLabel lblCancelTotal;
        public JLabel lblCancelCount;
        public JList displayList;

        public OrderListPanel() {
            GridBagLayout gbLayout = new GridBagLayout();
            this.setLayout(gbLayout);
            GridBagConstraints gbc = new GridBagConstraints();
            /*displayArea = new JTextArea();
            displayArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,16));
            displayArea.setEditable(false);
            displayArea.setMargin(new Insets(5, 5, 5, 5));*/
            scrollPanel = new JScrollPane();
            scrollPanel.setPreferredSize(new Dimension(500, 300));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 4;
            gbLayout.setConstraints(scrollPanel, gbc);
            this.add(scrollPanel);

            lblTotalCount = new JLabel();
            lblTotalCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbLayout.setConstraints(lblTotalCount, gbc);
            this.add(lblTotalCount);

            lblTotalSales = new JLabel();
            lblTotalSales.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 2;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblTotalSales, gbc);
            this.add(lblTotalSales);

            lblCancelCount = new JLabel();
            lblCancelCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblCancelCount, gbc);
            this.add(lblCancelCount);

            lblCancelTotal = new JLabel();
            lblCancelTotal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 2;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblCancelTotal, gbc);
            this.add(lblCancelTotal);

            btnNewOrder = new JButton("New");
            btnNewOrder.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            gbc.weightx = 0.25;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbLayout.setConstraints(btnNewOrder, gbc);
            this.add(btnNewOrder);

            btnEditOrder = new JButton("Edit");
            btnEditOrder.addActionListener(this);
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbLayout.setConstraints(btnEditOrder, gbc);
            this.add(btnEditOrder);

            btnCloseOrder = new JButton("Close");
            btnCloseOrder.addActionListener(this);
            gbc.gridx = 2;
            gbc.gridy = 3;
            gbLayout.setConstraints(btnCloseOrder, gbc);
            this.add(btnCloseOrder);

            btnCancelOrder = new JButton("Cancel");
            btnCancelOrder.addActionListener(this);
            gbc.gridx = 3;
            gbc.gridy = 3;
            gbLayout.setConstraints(btnCancelOrder, gbc);
            this.add(btnCancelOrder);

            displayList = new JList();
        }

        public void setTotalCount(int count) {
            lblTotalCount.setText("Today's order: " + count);
        }

        public void setTotalSales(double sales) {
            lblTotalSales.setText("Total:$ " + sales);
        }

        public void setCancelCount(int count) {
            lblCancelCount.setText("Canceled orders: " + count);
        }

        public void setCancelTotal(double sales) {
            lblCancelTotal.setText("Cancel total:$ " + sales);
        }

        public void showOrderList() {
            displayList.setListData(rcController.createOrderList().toArray());
            scrollPanel.getViewport().setView(displayList);

            setTotalCount(rcController.getTodaysOrderCnt());
            setTotalSales(rcController.getTotalSales());
            setCancelCount(rcController.getTodaysCancelCnt());
            setCancelTotal(rcController.getCancelTotal());

        }

        public void init() {
            showOrderList();
        }

        public int getSelectedOrderID() {
            String orderLine = (String) displayList.getSelectedValue();
            if (orderLine == null)
                return -1;

            return getIDfromString(orderLine, 4);
        }

        public String getSelectedOrderStaffName() {
            String stringLine = (String) displayList.getSelectedValue();
            if (stringLine == null)
                return null;

            int index = stringLine.indexOf("Name:"); //Search string of "ID:"
            if (index == -1) {
                showErrorDialog("Error", "String 'Name:' is not found!!");
                return null;
            }


            String staffName = stringLine.substring(index + 5, index + 5 + 22);
            return staffName.trim();
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnNewOrder) {
                //((CardLayout) mainPanel.getLayout()).show( mainPanel, "OrderDetail");
                changeMainPanel("OrderDetail");
                int orderID = rcController.createOrder();
                String staffName = rcController.getCurrentUserName();
                cOrderDetailPanel.init(orderID, staffName);
                //cOrderListPanel.init();
            } else if (ae.getSource() == btnEditOrder) {
                int orderID = getSelectedOrderID();
                String staffName = getSelectedOrderStaffName();
                if (orderID == -1) return;

                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "OrderDetail");
                //int orderID = cController.createOrder();
                cOrderDetailPanel.init(orderID, staffName);
            } else if (ae.getSource() == btnCloseOrder) {
                int orderID = getSelectedOrderID();
                if (orderID == -1) return;

                if (showYesNoDialog("Close order", "Are you sure to close the order?") == DIALOG_YES) {
                    if (!rcController.closeOrder(orderID))
                        displayErrorMessage(rcController.getErrorMessage());
                    showOrderList();
                }
            } else if (ae.getSource() == btnCancelOrder) {
                int orderID = getSelectedOrderID();
                if (orderID == -1) return;

                if (showYesNoDialog("Close order", "Are you sure to close the order?") == DIALOG_YES) {
                    if (!rcController.cancelOrder(orderID))
                        displayErrorMessage(rcController.getErrorMessage());
                    showOrderList();
                }
            }
        }
    }



    /****************************************************************
     * Order detail panel (Internal)
     *****************************************************************/
    public class OrderDetailPanel extends JPanel implements ActionListener, ListSelectionListener {
        //Right
        public JLabel lblRightTitle;

        public JScrollPane menuScrollPanel;
        public JButton btnAll;
        public JButton btnMain;
        public JButton btnDrink;
        public JButton btnAlcohol;
        public JButton btnDessert;

        //Left
        public JLabel lblLeftTitle;
        public JLabel lblLeftInfo;
        public JScrollPane orderScrollPanel;
        //public JTextArea       displayArea;
        public JPanel btnPanel;
        public JButton btnAddItem;
        public JButton btnDeleteItem;
        public JLabel lblQuantity;
        public JTextField tfQuantity;

        public JLabel lblTotalSales;
        public JLabel lblOrderState;
        public JLabel lblStaffName;
        public JList orderItemList;
        public JList menuList;

        public int currentOrderID;
        public int orderItemCnt;
        public int currentOrderState;

        public JPanel orderDetailPanel;
        public JPanel menuListPanel;

        public OrderDetailPanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            //this.insets = new Insets(5, 5, 5, 5);

            orderDetailPanel = new JPanel();
            //orderDetailPanel.setSize(new Dimension(270, 600));

            GridBagLayout gbLayout = new GridBagLayout();
            orderDetailPanel.setLayout(gbLayout);
            GridBagConstraints gbc = new GridBagConstraints();

            lblLeftTitle = new JLabel("Order detail");

            //lblLeftTitle.setMaximumSize(new Dimension(350, 50));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 4;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbLayout.setConstraints(lblLeftTitle, gbc);
            orderDetailPanel.add(lblLeftTitle);

            lblLeftInfo = new JLabel("No  Item name                 quantity    price");
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 4;
            gbLayout.setConstraints(lblLeftInfo, gbc);
            orderDetailPanel.add(lblLeftInfo);

            orderScrollPanel = new JScrollPane();
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.ipadx = 0;
            gbc.ipady = 0;
            gbc.weighty = 1.0;
            //gbc.fill = GridBagConstraints.VERTICAL;
            gbLayout.setConstraints(orderScrollPanel, gbc);
            orderDetailPanel.add(orderScrollPanel);

            lblTotalSales = new JLabel();
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.weighty = 0;
            gbc.gridwidth = 4;
            //gbc.fill = GridBagConstraints.BOTH;
            gbLayout.setConstraints(lblTotalSales, gbc);
            orderDetailPanel.add(lblTotalSales);

            lblOrderState = new JLabel();
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbLayout.setConstraints(lblOrderState, gbc);
            orderDetailPanel.add(lblOrderState);

            lblStaffName = new JLabel();
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 4;
            gbLayout.setConstraints(lblStaffName, gbc);
            orderDetailPanel.add(lblStaffName);

            lblQuantity = new JLabel("Quantity");
            gbc.ipadx = 20;
            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbLayout.setConstraints(lblQuantity, gbc);
            orderDetailPanel.add(lblQuantity);

            tfQuantity = new JTextField();
            tfQuantity.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 7;
            gbLayout.setConstraints(tfQuantity, gbc);
            orderDetailPanel.add(tfQuantity);

            btnAddItem = new JButton("Add");
            btnAddItem.addActionListener(this);
            gbc.gridx = 2;
            gbc.gridy = 6;
            gbc.gridwidth = 1;
            gbc.gridheight = 2;
            gbLayout.setConstraints(btnAddItem, gbc);
            orderDetailPanel.add(btnAddItem);

            btnDeleteItem = new JButton("Delete");
            btnDeleteItem.addActionListener(this);
            gbc.gridx = 3;
            gbc.gridy = 6;
            gbLayout.setConstraints(btnDeleteItem, gbc);
            orderDetailPanel.add(btnDeleteItem);


            //Right panel            
            menuListPanel = new JPanel();

            menuListPanel.setLayout(gbLayout);

            lblRightTitle = new JLabel("Menu list");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.ipadx = 0;
            gbc.gridwidth = 5;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbLayout.setConstraints(lblRightTitle, gbc);
            menuListPanel.add(lblRightTitle);

            menuScrollPanel = new JScrollPane();
            //menuScrollPanel.setPreferredSize(new Dimension(270, 300));
            //menuScrollPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
            gbc.gridy = 1;
            gbc.weighty = 1.0;

            gbLayout.setConstraints(menuScrollPanel, gbc);
            menuListPanel.add(menuScrollPanel);

            btnAll = new JButton("All");
            btnAll.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbLayout.setConstraints(btnAll, gbc);
            menuListPanel.add(btnAll);

            btnMain = new JButton("Main");
            btnMain.addActionListener(this);
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnMain, gbc);
            menuListPanel.add(btnMain);

            btnDrink = new JButton("Drink");
            btnDrink.addActionListener(this);
            gbc.gridx = 2;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnDrink, gbc);
            menuListPanel.add(btnDrink);

            btnAlcohol = new JButton("Alcohol");
            btnAlcohol.addActionListener(this);
            gbc.gridx = 3;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnAlcohol, gbc);
            menuListPanel.add(btnAlcohol);

            btnDessert = new JButton("Dessert");
            btnDessert.addActionListener(this);
            gbc.gridx = 4;
            gbc.gridy = 2;
            gbLayout.setConstraints(btnDessert, gbc);
            menuListPanel.add(btnDessert);

            LineBorder border = new LineBorder(Color.BLACK, 1, false);
            menuListPanel.setBorder(border);
            orderDetailPanel.setBorder(border);
            this.add(orderDetailPanel);
            this.add(menuListPanel);


            //menuListPanel.setMaximumSize(new Dimension(350, 600));

            orderItemList = new JList();
            orderItemList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            orderItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            menuList = new JList();
            menuList.addListSelectionListener(this);
            menuList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
            menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        }

        public void init(int orderID, String staffName) {
            currentOrderID = orderID;
            currentOrderState = rcController.getOrderState(orderID);
            switch (currentOrderState) {
                case Order.ORDER_CLOSED:
                    setOrderState("Closed");
                    break;
                case Order.ORDER_CANCELED:
                    setOrderState("Canceled");
                    break;
                default:
                    break;
            }

            if (currentOrderState != 0) {
                btnAddItem.setEnabled(false);
                btnDeleteItem.setEnabled(false);
            } else {
                btnAddItem.setEnabled(true);
                btnDeleteItem.setEnabled(true);
            }

            refleshOrderDetailList();
            menuList.setListData(rcController.createMenuList(0).toArray());
            menuScrollPanel.getViewport().setView(menuList);
            tfQuantity.setText("");
            tfQuantity.setBackground(UIManager.getColor("TextField.background"));
            setStaffName(staffName);
        }

        public void setTotal(double total) {
            lblTotalSales.setText("Total charge: $" + total);
        }

        public void setOrderState(String state) {
            lblOrderState.setText("Order state: " + state);
        }

        public void setStaffName(String name) {
            lblStaffName.setText("Staff name: " + name);
        }

        public void refleshOrderDetailList() {
            ArrayList < String > list = rcController.createOrderItemlList(currentOrderID);
            setTotal(rcController.getOrderTotalCharge(currentOrderID));
            orderItemCnt = list.size();
            orderItemList.setListData(list.toArray());
            //createOrderItemlList(currentOrderID, orderItemList);
            orderScrollPanel.getViewport().setView(orderItemList);
        }

        public int getOrderDetailIndexFromString(String orderLine) {
            try {
                String strIndex = orderLine.substring(0, 4);
                int index = Integer.parseInt(strIndex.trim());
                return index;
            } catch (Exception e) {
                //showErrorDialog("Error", "Parse error");
                return -1;
            }
        }

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == btnAddItem) {
                //if(!inputVerified)
                //    return;
                //Check whether current focuced compornent have to verify their value
                if (btnAddItem.getVerifyInputWhenFocusTarget()) {
                    //Try to get focus
                    btnAddItem.requestFocusInWindow();
                    if (!btnAddItem.hasFocus()) { //Can not get focus ?Ë the compornent have not been verified
                        return;
                    }
                }

                String menuLine = (String) menuList.getSelectedValue();
                if (menuLine == null)
                    return;

                int id = getIDfromString(menuLine, 4);
                if (id == -1)
                    return;
                if (tfQuantity.getText().equals("")) {
                    showErrorDialog("Error", "Enter quantity!!");
                    return;
                }
                byte quantity = Byte.parseByte(tfQuantity.getText().trim());
                /*if( quantity <= 0 || 100 <= quantity)
                {
                    displayErrorMessage("Quantity must be between 1 and 100");
                    return;
                }*/
                displayMessage("Menu ID = " + id + " Quantity = " + quantity);
                if (rcController.addNewOrderItem(currentOrderID, id, quantity) == false) {
                    displayErrorMessage("addNewOrderItem Error!!\n" + rcController.getErrorMessage());
                }
                refleshOrderDetailList();
                //auto scroll
                orderItemList.ensureIndexIsVisible(orderItemCnt - 1);

            } else if (ae.getSource() == btnDeleteItem) {
                String orderLine = (String) orderItemList.getSelectedValue();
                if (orderLine == null)
                    return;

                int index = getOrderDetailIndexFromString(orderLine);
                if (index == -1)
                    return;
                if (rcController.deleteOrderItem(currentOrderID, index) == false) {
                    displayErrorMessage("deleteOrderItem Error!!\n" + rcController.getErrorMessage());
                }
                refleshOrderDetailList();
            } else if (ae.getSource() == btnAll) {
                menuList.setListData(rcController.createMenuList(0).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnMain) {
                //createParticularMenuList(MenuItem.MAIN, menuList);
                menuList.setListData(rcController.createMenuList(menuItemBean.MAIN).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnDrink) {
                //createParticularMenuList(MenuItem.DRINK, menuList);
                menuList.setListData(rcController.createMenuList(menuItemBean.DRINK).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnAlcohol) {
                //createParticularMenuList(MenuItem.ALCOHOL, menuList);
                menuList.setListData(rcController.createMenuList(menuItemBean.ALCOHOL).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            } else if (ae.getSource() == btnDessert) {
                //createParticularMenuList(MenuItem.DESSERT, menuList);
                menuList.setListData(rcController.createMenuList(menuItemBean.DESSERT).toArray());
                menuScrollPanel.getViewport().setView(menuList);
            }
        }

        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == true) { //when mouce click happens
                if (e.getSource() == menuList) {
                    tfQuantity.setText("1");
                }
            }
        }
    }


    /****************************************************************
     * Total sales panel
     *****************************************************************/
    public class TotalSalesPanel extends JPanel {
        public JScrollPane scrollPanel;
        public JList displayList;
        public JButton btnPrint;
        public JButton btnCloseAllOrder;
        public JLabel lblTotalSales;
        public JLabel lblTotalCount;
        public JLabel lblCancelTotal;
        public JLabel lblCancelCount;


        public TotalSalesPanel() {
            GridBagLayout gbLayout = new GridBagLayout();
            this.setLayout(gbLayout);
            GridBagConstraints gbc = new GridBagConstraints();

            scrollPanel = new JScrollPane();
            //scrollPanel.setPreferredSize(new Dimension(500, 300));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 4;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.BOTH;
            gbLayout.setConstraints(scrollPanel, gbc);
            this.add(scrollPanel);

            lblTotalCount = new JLabel();
            lblTotalCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weighty = 0;
            gbLayout.setConstraints(lblTotalCount, gbc);
            this.add(lblTotalCount);

            lblTotalSales = new JLabel();
            lblTotalSales.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 2;
            gbc.gridy = 1;
            gbLayout.setConstraints(lblTotalSales, gbc);
            this.add(lblTotalSales);

            lblCancelCount = new JLabel();
            lblCancelCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbLayout.setConstraints(lblCancelCount, gbc);
            this.add(lblCancelCount);

            lblCancelTotal = new JLabel();
            lblCancelTotal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
            gbc.gridx = 2;
            gbc.gridy = 2;
            gbLayout.setConstraints(lblCancelTotal, gbc);
            this.add(lblCancelTotal);



            displayList = new JList();
        }

        public void setTotalCount(int count) {
            lblTotalCount.setText("Today's order: " + count);
        }

        public void setTotalSales(double sales) {
            lblTotalSales.setText("Total:$ " + sales);
        }

        public void setCancelCount(int count) {
            lblCancelCount.setText("Canceled orders: " + count);
        }

        public void setCancelTotal(double sales) {
            lblCancelTotal.setText("Cancel total:$ " + sales);
        }

        public void showOrderList() {
            displayList.setListData(rcController.createOrderList().toArray());
            scrollPanel.getViewport().setView(displayList);

            setTotalCount(rcController.getTodaysOrderCnt());
            setTotalSales(rcController.getTotalSales());
            setCancelCount(rcController.getTodaysCancelCnt());
            setCancelTotal(rcController.getCancelTotal());
        }

        public void init() {
            showOrderList();
        }

    }


}