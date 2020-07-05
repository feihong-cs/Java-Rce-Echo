# Windows 通用回显

## 说明
  看了 ```lufei``` 师傅的文章 ```《win描述符下成功又失败的回显》```，才知道原来在 ```Windows NIO/BIO``` 中也有类似 ```Linux``` 中 ```FileDescriptor``` 的存在，虽然通过查看 ```JNI``` 的源码知道是一个句柄文件，但是用 ```Java``` 
  代码处理起来都是类似的。
  <br /><br />
  ```Windows NIO/BIO``` 是通过 ```JNI``` 调用 ```winsock2.h``` 的 ```SOCKET WSAAPI accept(SOCKET s,sockaddr *addr,int *addrlen);``` 函数获取 ```socket```，
  随后将其转换成 ```jint``` 返回给 ```Java``` 程序，存储为 ```FileDescriptor```。在向 ```socket``` 返回数据时， ```Java``` 代码再把 ```FileDescriptor``` 通过 ```JNI``` 转换为 ```SOCKET``` 传递给 ```
  int WSAAPI WSASend(SOCKET s,LPWSABUF lpBuffers,DWORD dwBufferCount,LPDWORD lpNumberOfBytesSent,DWORD dwFlags,LPWSAOVERLAPPED lpOverlapped,LPWSAOVERLAPPED_COMPLETION_ROUTINE lpCompletionRoutine);``` 
  函数，从而完成数据的发送。
  <br /><br />
  开始的时候，想着可以通过遍历 ```fd``` 的值，利用反射创建对应的 FileDescriptor，然后通过 ```JNI``` 转换成 ```SOCKET``` 并传递给 ```getpeername``` 函数尝试去获取对端地址，如果能拿到结果，说明 ```fd``` 的值是有效的，
  对应着一个有效的 ```socket```。
  然而实践之后并没有拿到想要的结果。由于对 ```c``` 近乎一窍不通，于是只能放弃这种方式，转而从 ```Java``` 代码中尝试去寻找是否有接受 ```FileDescriptor``` 作为参数并返回一些信息的静态方法。结果果然找到了
   ```sun.nio.ch.Net#remoteAddress``` , 这个方法返回的结果就是我最开始时想通过 ```JNI``` 方式拿到的结果。
  <br /><br />
  于是，一切就很简单了，遍历 ```fd``` 的值，利用反射创建对应的 ```FileDescriptor```，然后调用 ```sun.nio.ch.Net#remoteAddress``` 确认 ```FileDescriptor``` 的有效性，如果有效，往里面写数据，
  从而实现回显。

## 效果
在 ```Tomcat 9.0.33```，```Jetty 9.4.30.v20200611```，```Resin/4.0.64``` 中测试通过
![Tomcat](https://github.com/feihong-cs/deserizationEcho/blob/master/Windows/img/Tomcat.png)
![Jetty](https://github.com/feihong-cs/deserizationEcho/blob/master/Windows/img/Jetty.png)
![Resin](https://github.com/feihong-cs/deserizationEcho/blob/master/Windows/img/Resin.png)

## 参考
* [win描述符下成功又失败的回显](https://xz.aliyun.com/t/7566)  
* [Socket 和 SocketChannel 的 FileDescriptor](https://blog.csdn.net/zxcc1314/article/details/99986252)  
* [https://github.com/JetBrains/jdk8u_jdk/blob/master/src/windows/native/sun/nio/ch/ServerSocketChannelImpl.c](https://github.com/JetBrains/jdk8u_jdk/blob/master/src/windows/native/sun/nio/ch/ServerSocketChannelImpl.c)  
* [https://github.com/JetBrains/jdk8u_jdk/blob/master/src/windows/native/sun/nio/ch/SocketDispatcher.c](https://github.com/JetBrains/jdk8u_jdk/blob/master/src/windows/native/sun/nio/ch/SocketDispatcher.c)  
