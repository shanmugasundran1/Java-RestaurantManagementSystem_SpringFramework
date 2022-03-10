package Base;

public class MenuItem {
    //definition of menu item type
    public final static int MAIN = 1;
    public final static int DRINK = 2;
    public final static int ALCOHOL = 3;
    public final static int DESSERT = 4;

    public int ID;
    public String name;
    public byte type;
    public double price;

    public byte state;
    
    public MenuItem(int newID, String newName, double newPrice, byte newType) {
        this.ID = newID;
        this.name = newName;
        this.price = newPrice;
        this.type = newType;
        this.state = 0;
    }

    //getter
    public int getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public double gerRegularPrice() {
        return this.price;
    }

    public byte getType() {
        return this.type;
    }

    public byte getState() {
        return this.state;
    }
}