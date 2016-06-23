package com.kai.blocksms;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryModel {
    private int id;
    private String number;
    private String message;
    private String createon;

    public HistoryModel() { }

    public HistoryModel(String fromAddress, String messageBody) {

        this.setNumber(fromAddress);
        this.setMessage(messageBody);
        this.setCreateon(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateon() {
        return createon;
    }

    public void setCreateon(String createon) {
        this.createon = createon;
    }

}
