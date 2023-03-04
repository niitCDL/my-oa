package top.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/api/leave/create", "/api/leave/list", "/api/leave/audit"})
public class LeaveFormController extends HttpServlet {
    EmployeeService employeeService = new EmployeeService();


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String servletPath = request.getServletPath();
        if ("/api/leave/create".equals(servletPath)) {
            create(request, response);
        } else if ("/api/leave/list".equals(servletPath)) {
            list(request, response);
        } else if ("/api/leave/audit".equals(servletPath)) {
            aduit(request,response);
        }
    }

    //审批请假单
    private void aduit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String formId = request.getParameter("formId");
        String result = request.getParameter("result");
        String reason = request.getParameter("reason");
        String eid = request.getParameter("eid");

        ResponseUtils resp;
        try {
            employeeService.audit(Long.valueOf(formId),Long.valueOf(eid),result,reason);
            resp = new ResponseUtils();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(),e.getMessage());
        }
        response.getWriter().println(resp.toJsonString());

    }

    //展示需要处理的请假单
    private void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String eid = request.getParameter("eid");
        ResponseUtils resp;
        try {
            List<Map<String, Object>> formList = employeeService.getLeaveFormList("process", Long.valueOf(eid));
            resp = new ResponseUtils();
            Map<String, Object> data = resp.getData();
            data.put("list", formList);

        } catch (Exception e) {
            resp = new ResponseUtils(e.getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(e);
        }
        response.getWriter().println(resp.toJsonString());
    }

    //创建请假单(申请)
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
