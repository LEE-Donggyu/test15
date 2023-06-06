package com.example.test4;

public class Ticket {

    String date;
    String routename;
    String turn;
    String pickupname;
    String userName;

    public Ticket(String routename, String date, String userName, String pickupname, String turn) {
        this.date = date;
        this.routename = routename;
        this.turn = turn;
        this.pickupname = pickupname;
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getPickupname() {
        return pickupname;
    }

    public void setPickupname(String pickupname) {
        this.pickupname = pickupname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
