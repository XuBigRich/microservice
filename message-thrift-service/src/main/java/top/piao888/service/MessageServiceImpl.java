package top.piao888.service;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.Data;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import top.piao888.message.thrift.message.MessageService;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
@Service
public class MessageServiceImpl implements MessageService.Iface {
  @Value("${email-server.host}")
  private String host;
    @Value("${email-server.protocol}")
  private  String protocol;
    @Value("${email-server.auth}")
  private String auth;
    @Value("${email-server.user}")
    private String sendUser;
    @Value("${email-server.password}")
    private String password;
    @Override
    public boolean send(String email, String code) {
        Properties prop = new Properties();
        prop.setProperty("mail.host", host); //// 设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol", protocol); // 邮件发送协议
        prop.setProperty("mail.smtp.auth", auth); // 需要验证用户名密码
        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication(sendUser, password);
            }
        });
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);

        //2、通过session得到transport对象
        Transport ts = null;
        try {
            ts = session.getTransport();
        //3、使用邮箱的用户名和授权码连上邮件服务器
        ts.connect(host,sendUser,password);

        //4、创建邮件

        //创建邮件对象
        MimeMessage message = new MimeMessage(session);

        //指明邮件的发件人
        message.setFrom(new InternetAddress(sendUser));

        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));

        //邮件的标题
        message.setSubject("验证码");

        //邮件的文本内容
        message.setContent(code, "text/html;charset=UTF-8");

        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());

        ts.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
            return true;
    }
}
