package top.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.example.entity.Node;
import top.example.service.NodeService;
import top.example.utils.ResponseUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/api/node"})
public class NodeController extends HttpServlet {

    static NodeService nodeService = null;

    @Override
    public void init() throws ServletException {
        nodeService = new NodeService();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        if ("/api/node".equals(servletPath)){
            getNode(request,response);
        }
    }

    private void getNode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        Long uid = Long.valueOf(request.getParameter("uid"));
        List<Node> nodes = nodeService.selectNodeByUserId(uid);
        List<Map<String,Object>> treeList = new ArrayList<>();
        Map<String,Object> module = null;
        for (Node node : nodes) {
            if (node.getNodeType() == 1){
                module = new LinkedHashMap<>();
                module.put("node",node);
                module.put("children",new ArrayList<>());
                treeList.add(module);
            } else if (node.getNodeType() == 2) {
                assert module != null;
                List<Node> children = (List<Node>)module.get("children");
                children.add(node);
            }
        }
        ResponseUtils resp = new ResponseUtils();
        Map<String, Object> data = resp.getData();
        data.put("nodeList",treeList);
        response.getWriter().println(resp.toJsonString());
    }


//    public void getNode(HttpServletRequest request, HttpServletResponse response){
//        try {
//            response.setContentType("application/json;charset=utf-8");
//            Long uid = Long.valueOf(request.getParameter("uid"));
//            Map<String, Object> nodes = nodeService.getNodes(uid);
//            ResponseUtils resp = new ResponseUtils();
//            resp.setData(nodes);
//            response.getWriter().println(resp.toJsonString());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
