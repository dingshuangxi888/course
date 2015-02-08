package com.dean.course5.memcache.session;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by dean on 15/1/4.
 */
@Controller
public class NodeBController {

    @RequestMapping(value = "/login/b", method = RequestMethod.POST)
    public String login(HttpServletRequest request, String userName) {
        HttpSession httpSession = request.getSession();
        String value = (String) httpSession.getAttribute("SESSION_" + userName);
        if (value != null) {
            System.out.println("SESSION VALUE: " + value);
        } else {
            httpSession.setAttribute("SESSION_" + userName, userName + " login on server node A!");
        }
        return "success";
    }
}
