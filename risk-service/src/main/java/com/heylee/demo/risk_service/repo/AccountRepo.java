package com.heylee.demo.risk_service.repo;


import com.heylee.demo.risk_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {

    Account findByAccount(String account);
}
