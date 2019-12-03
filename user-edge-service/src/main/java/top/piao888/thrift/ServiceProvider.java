package top.piao888.thrift;


import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.piao888.message.thrift.message.MessageService;
import top.piao888.user.thrift.user.UserService;

@Component
public class ServiceProvider {
    @Value("${thrift.user.ip}")
    private String serverIp;
    @Value("${thrift.user.port}")
    private int serverPort;
    @Value("${thrift.message.ip}")
    private String messageServerIp;
    @Value("${thrift.message.port}")
    private int messageServerPort;
    public UserService.Client getUserService(){
        TSocket socket=new TSocket(serverIp,serverPort,30000);
        //客户端要与 服务端 的配置 保持一致 （传输协议 '帧传输'，传输格式 ‘二进制’）
        TTransport transport= new TFramedTransport(socket);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            System.out.println("用户服务出问题了");
            return null;
        }
        //设置传输协议
        TProtocol protocol=new TBinaryProtocol(transport);
        //构造客户端对象
        UserService.Client client=new UserService.Client(protocol);

        return client;
    }
    public MessageService.Client getMessageService(){
        TSocket socket=new TSocket(messageServerIp,messageServerPort,30000);
        TTransport transport= new TFramedTransport(socket);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            System.out.println("信息服务发送出现了问题");
            return null;
        }
        TProtocol protocol=new TBinaryProtocol(transport);
        //构造客户端对象
        MessageService.Client client=new MessageService.Client(protocol);
        return client;
    }
}
