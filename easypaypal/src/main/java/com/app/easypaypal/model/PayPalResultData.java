package com.app.easypaypal.model;

/**
 * Created by Pratik Surela on 20/6/17.
 */

public class PayPalResultData {

    public String payPalAmount, payPalDesc, transId, payPalTime, date, actualTime;

    public String getPayPalAmount() {
        return payPalAmount;
    }

    public void setPayPalAmount(String payPalAmount) {
        this.payPalAmount = payPalAmount;
    }

    public String getPayPalDesc() {
        return payPalDesc;
    }

    public void setPayPalDesc(String payPalDesc) {
        this.payPalDesc = payPalDesc;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getPayPalTime() {
        return payPalTime;
    }

    public void setPayPalTime(String payPalTime) {
        this.payPalTime = payPalTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }
}