package top.piao888.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.piao888.message.thrift.message.MessageService;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
//@Order
//@Component
//public class Test implements ApplicationRunner {
@Configuration
public class Test {
    @Autowired
   public MessageService.Iface send;
    @PostConstruct
    public void run() throws Exception {
        send.send("847118663@qq.com","hello world");
    }
}
