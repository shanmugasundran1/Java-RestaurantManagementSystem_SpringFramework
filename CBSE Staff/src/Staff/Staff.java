package Staff;
import java.util.*;
import java.text.*;

public class Staff {
    public int ID;
    public String lastName;
    public String firstName;
    public String password;
    public byte state;

    //public byte  workState;  //0:not active  1:active (on wark)  2:finish work
    public Date startWorkTime;

    //------------------------------------------------------------
    // constructor
    //------------------------------------------------------------

    public Staff(int newID, String newLastName, String newFirstName, String newPassword) {
        setID(newID);
        setLastName(newLastName);
        setFirstName(newFirstName);
        setPassword(newPassword);
        startWorkTime = null;
        state = 0;
    }

    //------------------------------------------------------------
    // setter
    //------------------------------------------------------------
    public void setID(int newID) {
        this.ID = newID;
    }
    public void setLastName(String newLastName) {
        this.lastName = newLastName;
    }
    public void setFirstName(String newFirstName) {
        this.firstName = newFirstName;
    }
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
    public void setWorkState(byte newState) {
        this.state = newState;
    }

    //------------------------------------------------------------
    // getter
    //------------------------------------------------------------
    public int getID() {
        return this.ID;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getFullName() {
        String fullName = this.firstName + " " + this.lastName;
        return fullName;
    }
    public String getPassword() {
        return this.password;
    }

    public final static byte WORKSTATE_NON_ACTIVE = 0;
    public final static byte WORKSTATE_ACTIVE = 1;
    public final static byte WORKSTATE_FINISH = 2;

    public byte getWorkState() {
        return this.state;
    }

    public String getStartTime() {
        if (startWorkTime == null)
            return "getStartTime Error";
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(startWorkTime);
    }

    //------------------------------------------------------------
    // other methods
    //------------------------------------------------------------
    public void clockIn() {
        startWorkTime = new Date(System.currentTimeMillis());
        state = WORKSTATE_ACTIVE;
    }

    public boolean changeStartTime(Date newStartTime) {
        if (newStartTime.after(new Date(System.currentTimeMillis()))) {
            return false;
        }

        startWorkTime = newStartTime;
        return true;
    }


}