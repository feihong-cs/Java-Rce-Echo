<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    path = path.substring(0, path.indexOf("WEB-INF"));
    String res = new java.util.Scanner(Runtime.getRuntime().exec("echo \"It works!\"").getInputStream()).useDelimiter("\\A").next();
    java.io.PrintWriter printWriter = new java.io.PrintWriter(path + "echo.js");
    printWriter.println(res);
    printWriter.close();
%>