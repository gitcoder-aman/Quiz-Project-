package com.example.coderamankumarguptaquizearn;

import android.widget.EditText;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {
    private String userId;
    private String paytmNo;
    private String requestedBy;
    private long noOfCoins;


    public WithdrawRequest() {
    }

    public WithdrawRequest(String userId, String paytmNo, String requestedBy, long noOfCoins) {
        this.userId = userId;
        this.paytmNo = paytmNo;
        this.requestedBy = requestedBy;
        this.noOfCoins = noOfCoins;
    }


    public long getNoOfCoins() {
        return noOfCoins;
    }

    public void setNoOfCoins(long noOfCoins) {
        this.noOfCoins = noOfCoins;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaytmNo() {
        return paytmNo;
    }

    public void setPaytmNo(String paytmNo) {
        this.paytmNo = paytmNo;
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
