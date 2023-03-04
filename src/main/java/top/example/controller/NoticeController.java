package top.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.example.entity.Notice;
import top.example.service.NoticeService;
import top.example.utils.ResponseUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/notice/list")
public class NoticeController extends HttpServlet {

    NoticeService noticeService = new NoticeService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeedId = request.getParameter("eid");
        ResponseUtils resp = null;
        try {
            List<Notice> noticeList = noticeService.getNoticeList(Long.valueOf(employeedId));
            resp = new ResponseUtils();
            Map<String, Object> data = resp.getData();
            data.put("list", noticeList);
        } catch (Exception e) {
            e.printStackTrace();
            resp = new ResponseUtils(e.getClass().getSimpleName(),e.getMessage());
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(resp.toJsonString());
    }
}
