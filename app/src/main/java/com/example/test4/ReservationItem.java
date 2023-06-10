package com.example.test4;

public class ReservationItem {
    String start; // 정류장
    String bus_time;
    public ReservationItem(String start, String bus_time) {
        this.start = start;
        this.bus_time = bus_time;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getBus_time() { return bus_time;}
    public void setBus_time(String bus_time) {this.bus_time = bus_time;}

}
