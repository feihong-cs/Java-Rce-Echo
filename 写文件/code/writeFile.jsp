<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    path = path.substring(0, path.indexOf("WEB-INF"));

    java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(Runtime.getRuntime().exec("echo \"It works!\"").getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line;
    while((line = br.readLine()) != null){
        sb.append(line + "\n");
    }


    java.io.PrintWriter printWriter = new java.io.PrintWriter(path + "echo.js");
    printWriter.println(sb.toString());
    printWriter.close();
    br.close();
%>