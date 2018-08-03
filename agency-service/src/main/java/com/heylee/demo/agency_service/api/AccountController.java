package com.heylee.demo.agency_service.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heylee.demo.agency_service.model.Account;
import com.heylee.demo.agency_service.model.Account4AMQP;
import com.heylee.demo.agency_service.model.Response.AccountsResponse;
import com.heylee.demo.agency_service.model.Response.OperationResponse;
import com.heylee.demo.agency_service.repo.AccountRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.domain.Example;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@EnableBinding({Sink.class,Source.class})
@RestController
@RequestMapping(value = "/api")
@Api(tags = "Account")
public class AccountController {

    @Autowired
    private AccountRepo dao;

    @Autowired
    private Source source;

    @StreamListener(Sink.INPUT)
    public void processMessage(String message) {
        JSONObject object = JSON.parseObject(message);
        JSONObject account = JSON.parseObject(object.get("account").toString());

        System.out.println("Method: " + object.get("method"));

        if (object.getString("method").equals("PUT")) {
            Account vo = new Account();
            vo.setAccount(account.getString("account"));
            vo.setStatus(account.getString("status"));
            edit(vo);
        }

        System.out.println("This is a message from queue: " + message);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "Heylee!!!";
    }

    @RequestMapping(value = "/testQueue", method = RequestMethod.POST)
    public String publishMessage(@RequestBody Object oo) {
        source.output().send(MessageBuilder.withPayload(oo + "POST").build());
        return "message pushed";
    }

    @SendTo(Source.OUTPUT)
    @ApiOperation(value = "Add new account", response = OperationResponse.class)
    @RequestMapping(value = "/account", method = RequestMethod.POST, produces = "application/json")
    public OperationResponse addNew(@RequestBody Account vo) {
        OperationResponse resp = new OperationResponse();

        Account account = dao.findByAccount(vo.getAccount());
        if (account != null) {
            resp.setOperationStatus(OperationResponse.ResponseStatusEnum.ERROR);
            resp.setOperationMessage("Account Already Exist");
            System.out.println("Account Already Exist");
        } else {
            dao.save(vo);

            // send to amqp
            Account4AMQP account4AMQP = new Account4AMQP();
            account4AMQP.setMethod(RequestMethod.POST);
            account4AMQP.setAccount(vo);
            source.output().send(MessageBuilder.withPayload(account4AMQP).build());
            // send to amqp

            resp.setOperationStatus(OperationResponse.ResponseStatusEnum.SUCCESS);
            resp.setOperationMessage("Account Added");
        }
        return resp;
    }

    @ApiOperation(value = "Get All Accounts", response = ApiOperation.class)
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public AccountsResponse list(Account vo) {
        AccountsResponse resp = new AccountsResponse();
        resp.setItems(dao.findAll(Example.of(vo)));
        return resp;
    }

    @ApiOperation(value = "Update Account", response = ApiOperation.class)
    @RequestMapping(value = "/account", method = RequestMethod.PUT)
    public OperationResponse edit(@RequestBody Account vo) {
        OperationResponse resp = new OperationResponse();
        Account account = dao.findByAccount(vo.getAccount());
        if (account == null) {
            resp.setOperationMessage("Unable To Update Account, Account does not exist");
            resp.setOperationStatus(OperationResponse.ResponseStatusEnum.ERROR);
        } else {
            account.setStatus(vo.getStatus());
            dao.save(account);
            resp.setOperationMessage("Account Updated");
            resp.setOperationStatus(OperationResponse.ResponseStatusEnum.SUCCESS);
        }

        return resp;
    }

    @ApiOperation(value = "Drop DB", response = ApiOperation.class)
    @RequestMapping(value = "/dropDB", method = RequestMethod.GET)
    public AccountsResponse dropDB() {
        AccountsResponse resp = new AccountsResponse();
        dao.deleteAll();
        return resp;
    }


}
