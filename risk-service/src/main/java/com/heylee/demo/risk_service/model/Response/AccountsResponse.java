package com.heylee.demo.risk_service.model.Response;

import com.heylee.demo.risk_service.model.Account;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class AccountsResponse extends OperationResponse {

    @Getter
    @Setter
    private List<Account> items;

    public List<Account> getItems() {
        return items;
    }

    public void setItems(List<Account> list) {
        this.items = list;
    }
}
