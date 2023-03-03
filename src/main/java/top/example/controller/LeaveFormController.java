package top.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.example.entity.LeaveForm;
import top.example.service.EmployeeService;
import top.example.utils.ResponseUtils;

import java.io.IOException;
import java.util.Date;

@WebServlet({"/api/leave/create"})
public class LeaveFormController extends HttpServlet {
    EmployeeService employeeService = new EmployeeService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String servletPath = request.getServletPath();
        if ("/api/leave/create".equals(servletPath)) {
            create(request, response);
        }
    }

    private void create(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        String formType = request.getParameter("formType");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String reason = request.getParameter("reason");
        String eid = request.getParameter("eid");
        ResponseUtils responseUtils = null;

        try {
            LeaveForm leaveForm = LeaveForm.builder().formType(Integer.valueOf(formType))
                    .startTime(new Date(Long.valueOf(startTime))).endTime(new Date(Long.valueOf(endTime)))
                    .reason(reason).employeeId(Long.valueOf(eid)).createTime(new Date())
                    .state("processing").build();

            boolean leave = employeeService.leave(leaveForm);
            responseUtils = new ResponseUtils();
        } catch (Exception e) {
            responseUtils = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
        }

        try {
            response.getWriter().println(responseUtils.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
