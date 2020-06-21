# Linux 通用回显

## 说明
* case1.jsp 中的代码逻辑较为简单，遍历当前进程 ```fd``` 目录下的所有和 ```socket``` 相关的 ```fd``` 文件，并输出结果，效果如下 <br/><br/>
![case1.jsp效果](https://github.com/feihong-cs/deserizationEcho/blob/master/Linux/imgs/20200621-001.png?raw=true) <br/><br/>
但是这种方法存在二个缺陷：
    * 会影响同一时间点所有访问网站的用户（也会看到自定义回显的结果）
    * 导致应用崩溃
        * 使用本地虚拟机 ```Kali Linux``` 搭建的 ```Tomcat 9.0.36``` 测试，Tomcat 进程不会崩溃
        * 使用 ```腾讯云VPS``` 搭建的 ```Tomcat 8.5.56``` 测试，连续访问此文件 ```8```次左右，应用崩溃（Tomcat 进程还在，但是不会再监听 ```8080``` 端口），且有时候重启 Tomcat 也没用，Tomcat 依然会报 ```java.io.IOException: Bad file descriptor``` 错误，需要重启 VPS
        ![应用崩溃](https://github.com/feihong-cs/deserizationEcho/blob/master/Linux/imgs/20200621-002.png?raw=true)
        ![进程还在，但是不在监听端口](https://github.com/feihong-cs/deserizationEcho/blob/master/Linux/imgs/20200621-003.png?raw=true) <br/><br/>
* case2.jsp 中的代码通过延迟等方法来确定唯一正确的 ```fd``` 文件，不会影响访问网站的其他用户，也不会导致应用崩溃 <br/><br/>
![case2.jsp效果](https://github.com/feihong-cs/deserizationEcho/blob/master/Linux/imgs/20200621-004.png?raw=true)

## 参考
* [linux下java反序列化通杀回显方法的低配版实现](https://xz.aliyun.com/t/7307)
* [通杀漏洞利用回显方法-linux平台](https://www.00theway.org/2020/01/17/java-god-s-eye/)

