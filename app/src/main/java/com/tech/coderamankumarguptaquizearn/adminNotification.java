package com.tech.coderamankumarguptaquizearn;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class adminNotification {
    private String number;
    private String requestedBy;
    private long noOfCoins;
    private String status;
    private long rupees;
    private String paymentId;
    private String uid;
    private String paymentType;

    public adminNotification() {
    }

    public adminNotification(String paymentId,String number, String requestedBy, long noOfCoins, String status,long rupees,String uid,String paymentType) {
        this.number = number;
        this.requestedBy = requestedBy;
        this.noOfCoins = noOfCoins;
        this.status = status;
        this.rupees = rupees;
        this.paymentId = paymentId;
        this.uid = uid;
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public long getRupees() {
        return rupees;
    }

    public void setRupees(long rupees) {
        this.rupees = rupees;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getNoOfCoins() {
        return noOfCoins;
    }

    public void setNoOfCoins(long noOfCoins) {
        this.noOfCoins = noOfCoins;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }


    @ServerTimestamp //  firebase current time store in firestore
    private Date createAt;

}
