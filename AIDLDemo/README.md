## AIDLDemo

AIDL exercise lib demo.

AIDL 是 IPC 机制的实现，Service 是 C/S 架构。

## module

- remoteservicecontract Android库，定义通讯接口规范，供client与server使用（直接依赖、aar、jar）
- remoteserver aidl远程server进程（由服务提供方实现）
- client aidl客服端client进程（与server通讯）
- client_server client 和 server 在一个 application 里（service 可以在应用进程也可以在独立进程）

> 注意： client 进程在绑定 server 进程的 service 时，设置的 setPackage 值为 **远程服务所在的应用程序包名称**（即 server 进程的 applicationId）


## module 组织方式

1. client/server 由两个 application 组成，client + remoteserver，remoteservicecontract 提供通讯协议规范；
2. client/server 由一个 application 组成，client_server 两个进程在一个工程里（service 在主进程或者独立进程都可以），remoteservicecontract 提供通讯协议规范；\

## client_server

- RemoteService 为服务端
  - RemoteService 的 onBind 方法返回 AIDL 接口中 Stub Binder ，并实现接口里面的方法，  
  该Binder即为服务端逻辑。
- RemoteController 为服务端业务代理类，分发具体服务端的业务逻辑。
- ClientManager 为客服端，连接服务并调用服务。
