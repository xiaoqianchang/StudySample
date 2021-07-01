## AIDLDemo

AIDL exercise lib demo.

## module

- remoteservicecontract Android库，定义通讯接口规范，供client与server使用
- remoteserver aidl远程server进程（由服务提供方实现）
- client aidl客服端client进程（与server的通讯）

> 注意： client 进程在绑定 server 进程时设置的 setPackage 值为 **远程服务所在的应用程序包的名称**
