package cc.openhome.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cc.openhome.model.Message;
import cc.openhome.model.UserService;

@Controller
public class MemberController {
    private String MEMBER_PATH = "/WEB-INF/jsp/member.jsp";
    private String REDIRECT_MEMBER_PATH = "/gossip/member";
    
    @RequestMapping("member")
    public void member(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        List<Message> messages = userService.messages(getUsername(request));
        
        request.setAttribute("messages", messages);
        request.getRequestDispatcher(MEMBER_PATH).forward(request, response);
    }
    
    @RequestMapping(value = "new_message", method = RequestMethod.POST)
    protected void newMessage(
            HttpServletRequest request, HttpServletResponse response) 
                            throws ServletException, IOException {
            
        request.setCharacterEncoding("UTF-8");
        String blabla = request.getParameter("blabla");
        
        if(blabla == null || blabla.length() == 0) {
            response.sendRedirect(REDIRECT_MEMBER_PATH);
            return;
        }        
       
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        
        if(blabla.length() <= 140) {
            userService.addMessage(getUsername(request), blabla);
            response.sendRedirect(REDIRECT_MEMBER_PATH);
        }
        else {
            request.setAttribute("messages", userService.messages(getUsername(request)));
            request.getRequestDispatcher(MEMBER_PATH).forward(request, response);
        }
    }  
    
    @RequestMapping(value = "del_message", method = RequestMethod.POST)
    protected void delMessage(
            HttpServletRequest request, HttpServletResponse response) 
                            throws ServletException, IOException {
            
        String millis = request.getParameter("millis");
        
        if(millis != null) {
            UserService userService = (UserService) request.getServletContext().getAttribute("userService");
            userService.deleteMessage(getUsername(request), millis);
        }
        
        response.sendRedirect(REDIRECT_MEMBER_PATH);
    }        
    
    private String getUsername(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("login");
    }
    
    
}
