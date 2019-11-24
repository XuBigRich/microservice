package top.piao888.thrift;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceProvider {
    @Value("${thrift.user.ip}")
    private String serverIp;
    @Value("${thrift.user.port}")
    private int serverPort;
    public UserService.Client getUserService(){
        TSocket socket=new TSocket(serverIp,serverPort,30000);
        //客户端要与 服务端 的配置 保持一致 （传输协议 '帧传输'，传输格式 ‘二进制’）
        TTransport transport= new TFramedTransport(socket);
        try {
            transport.open();
        } catch (TTransportException e) {
            e.printStackTrace();
            System.out.println("这个地方出问题了");
            return null;
        }
        //设置传输协议
        TProtocol protocol=new TBinaryProtocol(transport);
        //构造客户端对象
        UserService.Client client=new UserService.Client(protocol);

        return client;

    }
}
