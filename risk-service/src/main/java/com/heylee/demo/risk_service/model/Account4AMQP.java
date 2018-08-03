package com.heylee.demo.risk_service.model;

import org.springframework.web.bind.annotation.RequestMethod;

public class Account4AMQP {

    private RequestMethod method;
    private Account account;

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
