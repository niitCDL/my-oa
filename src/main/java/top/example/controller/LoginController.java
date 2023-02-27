package top.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.example.entity.Employee;
import top.example.entity.User;
import top.example.service.EmployeeService;
import top.example.service.UserService;
import top.example.utils.ResponseUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet({"/api/login","/api/getEmp"})
public class LoginController extends HttpServlet {

    private UserService userService;

    private EmployeeService employeeService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        employeeService = new EmployeeService();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        if ("/api/login".equals(path)){
            login(request,response);
        } else if ("/api/getEmp".equals(path)) {
            getEmp(request,response);
        }

    }

    public void getEmp(HttpServletRequest request,HttpServletResponse response){
        try {
            response.setContentType("application/json;charset=utf-8");
            Long eid = Long.valueOf(request.getParameter("eid"));
            ResponseUtils resp = new ResponseUtils();
            Employee emp = employeeService.getEmp(eid);
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("emp",emp);
            resp.setData(map);
            response.getWriter().println(resp.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void login(HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        ResponseUtils resp = new ResponseUtils();
        Map<String,Object> map = new LinkedHashMap<>();
        try {
            User user = userService.login(username, password);
            user.setPassword(null);
            user.setSalt(null);
            map.put("user",user);
            resp.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(),e.getMessage());
        }
        try {
            response.getWriter().println(resp.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
