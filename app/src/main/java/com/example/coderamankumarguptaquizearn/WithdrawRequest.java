package com.example.coderamankumarguptaquizearn;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {
    private String userId;
    private String paytmNo;
    private String requestedBy;


    public WithdrawRequest() {
    }

    public WithdrawRequest(String userId, String paytmNo, String requestedBy) {
        this.userId = userId;
        this.paytmNo = paytmNo;
        this.requestedBy = requestedBy;
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
