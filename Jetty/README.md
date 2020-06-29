# Jetty Echo
## 说明
直接参考 ```c0ny1``` 文章中的截图，找到 ```httpConnection``` 对象，编写代码实现回显
![img1](https://github.com/feihong-cs/deserizationEcho/blob/master/Jetty/img/001.png?raw=true)
![img2](https://github.com/feihong-cs/deserizationEcho/blob/master/Jetty/img/20200628003.png?raw=true)

## 效果
![img3](https://github.com/feihong-cs/deserizationEcho/blob/master/Jetty/img/20200628002.png)

## 踩坑
当拿到 ```httpConnection``` 对象时，想直接调用其 ```send``` 方法实现回显，发现报错。进一步测试发现，对拿到的 ```httpConnection``` 执行 ```instanceof HttpConnection``` 时返回 ```false```,
经过询问朋友 ```Pine.lin``` 才得知，我拿到的 ```httpConnection``` 对象和 ```import``` 进来的对象竟然使用的是不同的类加载器（很奇怪），从而导致了这个问题，导致我在这里卡了很久，
非常感谢 ```Pine.lin``` 的帮忙。
![img4](https://github.com/feihong-cs/deserizationEcho/blob/master/Jetty/img/20200628001.png?raw=true)

## 参考
* [半自动化挖掘request实现多种中间件回显](https://mp.weixin.qq.com/s/uWyHRexDZWQwp81lWjmqqw)  
* [https://www.eclipse.org/jetty/javadoc/current/org/eclipse/jetty/server/HttpConnection.html](https://www.eclipse.org/jetty/javadoc/current/org/eclipse/jetty/server/HttpConnection.html)
