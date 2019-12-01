namespace java top.piao888.message.thrift.message
struct Message{
    1:string email,
    2:string code
}
service MessageService{
    bool send(1:string email,2:string code);
}