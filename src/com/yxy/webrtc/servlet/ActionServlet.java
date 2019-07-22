package com.yxy.webrtc.servlet;

import com.alibaba.fastjson.JSON;
import com.yxy.base.exception.BizException;
import com.yxy.base.utils.CommonUtils;
import com.yxy.webrtc.bean.ChatMessage;
import com.yxy.webrtc.bean.ChatRoom;
import com.yxy.webrtc.bean.Result;
import com.yxy.webrtc.model.User;
import com.yxy.webrtc.service.IUserService;
import com.yxy.webrtc.service.impl.UserServiceImpl;
import com.yxy.webrtc.utils.ChatRoomUtils;
import com.yxy.webrtc.utils.ChatUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public class ActionServlet extends HttpServlet {
    private Set<String> supportMethod = new HashSet<String>();
    private IUserService userService;

    public ActionServlet(){
        supportMethod.add("register");
        supportMethod.add("login");
        supportMethod.add("createRoom");
        supportMethod.add("joinRoom");
        supportMethod.add("exitRoom");
        supportMethod.add("endChat");
        supportMethod.add("logout");
        userService = new UserServiceImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("error",null);
        request.setCharacterEncoding("UTF-8");
        String method = request.getParameter("method");
        response.setContentType("text/html;charset=utf-8");
        Result result = new Result();
        PrintWriter writer = response.getWriter();
        if(supportMethod.contains(method)){
            try {
                Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
                Object invoke = m.invoke(this, request, response);
                if(invoke!=null){
                    result.setData(invoke);
                    writer.write(JSON.toJSONString(result));
                }
            }catch (Exception e) {
                result.setSuccess(false);
                if(e.getCause() instanceof BizException){
                    result.setData(e.getCause().getMessage());
                    request.setAttribute("error",e.getMessage());
                }else{
                    request.setAttribute("error","请求失败");
                    result.setData("请求失败");
                }
                writer.write(JSON.toJSONString(result));
            }finally {
                writer.close();
            }
        }else{
            response.sendRedirect("/login.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    public void register(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String name = request.getParameter("name");
        String userName = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        String cpwd = request.getParameter("cpwd");
        if(CommonUtils.isEmpty(pwd)) throw new BizException("请输出密码");
        if(!pwd.equals(cpwd)) throw new BizException("两次输入的密码不一致");
        User user = new User();
        user.setPwd(pwd);
        user.setName(name);
        user.setUserName(userName);
        try {
            userService.register(user);
        } catch (Exception e) {
            response.sendRedirect("/register.jsp");
            request.getSession().setAttribute("error",e.getMessage());
            return;
        }
        response.sendRedirect("/login.jsp");
    }


    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String userName = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        try {
            User user = userService.login(userName, pwd);
            request.getSession().setAttribute("user",user);
        } catch (BizException e) {
            response.sendRedirect("/login.jsp");
            request.getSession().setAttribute("error",e.getMessage());
            return;
        }
        response.sendRedirect("/index.jsp");
    }


    public void logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        if(user!=null&&user.getRoom()!=null){
            user.getRoom().remove(user);
        }
        request.getSession().setAttribute("user",null);
        response.sendRedirect("/login.jsp");
    }




    public String createRoom(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = checkLogin(request, response);
        if(user!=null){
            ChatRoom room = user.getRoom();
            if(room!=null){
                if(room.getCreator()==user){
                    throw new BizException("你已经创建会议");
                }else{
                    throw new BizException("如要创建会议，请先退出会议");
                }
            }else{
                String title = request.getParameter("title");
                String pwd = request.getParameter("pwd");
                room = ChatRoomUtils.createChatRoom(user,title,pwd);
                return room.getRoomName();
            }
        }
        return null;
    }

    public String joinRoom(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = checkLogin(request, response);
        String roomName = request.getParameter("roomName");
        if(user!=null){
            ChatRoom chatRoom = ChatRoomUtils.chatRoomMap.get(roomName);
            if(chatRoom==null) throw new BizException("房间不存在");
            //已经加入过会议  或者是会议发起者 直接放行
            ChatRoom userRoom = user.getRoom();
            if(userRoom!=null){
                if(userRoom!=chatRoom){
                    userRoom.remove(user);
                }
            }
            if(chatRoom!=user.getRoom()&&chatRoom.getCreator()!=user){
                String pwd = request.getParameter("pwd");
                chatRoom.join(user,pwd,true);
            }else{
                chatRoom.join(user,null,false);
            }
            return "已进入房间";
        }
        return null;
    }


    public String exitRoom(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = checkLogin(request, response);
        if(user!=null){
            if(user.getRoom()!=null) {
                user.getRoom().remove(user);
            }else{
                throw new BizException("未加入任何会议");
            }
            return "退出成功";
        }
        return null;
    }


    public String endChat(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = checkLogin(request, response);
        if(user!=null){
            ChatMessage chatMessage = new ChatMessage(ChatUtils.END_CHAT);
            chatMessage.setFrom(user);
            ChatRoom userRoom = user.getRoom();
            if(userRoom==null){
                throw new BizException("你没有创建会议");
            }else{
                if(userRoom.getCreator()!=user) throw new BizException("你无权解散该会议");
                userRoom.remove(user);
            }
            return "解散会议成功";
        }
        return null;
    }




    public User checkLogin(HttpServletRequest request, HttpServletResponse response) throws Exception{
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            response.sendRedirect("/index.jsp");
        }
        return user;
    }


}
