package top.piao888.user.thrift;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import top.piao888.user.thrift.user.UserService;

import javax.annotation.PostConstruct;
@Configuration
public class ThriftServer {
    @Value("${server.port}")
    private int port;
    @Autowired
    private UserService.Iface userService;

    @PostConstruct
    public void starThriftServer() throws TTransportException {
        //将服务注册到thrift容器当中
        TProcessor processor=new UserService.Processor<>(userService);
        //创捷socket  nio模式
        TNonblockingServerSocket tNonblockingServerSocket=new TNonblockingServerSocket(port);
        TNonblockingServer.Args args=new TNonblockingServer.Args(tNonblockingServerSocket);
        //将 thrift 丢进socket中
        args.processor(processor);
        //设置传输方式 设置为真传输
        args.transportFactory(new TFastFramedTransport.Factory());
        // 传输格式 有二进制 有json  这个是二进制
        args.protocolFactory(new TBinaryProtocol.Factory());
        TServer server=new TNonblockingServer(args);
        server.serve();

    }
}
