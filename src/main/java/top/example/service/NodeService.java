package top.example.service;

import top.example.entity.Node;
import top.example.mapper.NodeMapper;
import top.example.utils.MyBatisUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NodeService {
    NodeMapper mapper = MyBatisUtils.getSqlSession().getMapper(NodeMapper.class);

    public List<Node> selectNodeByUserId(Long userId){
        return mapper.selectNodeByUserId(userId);
    }

    public Map<String, Object> getNodes(Long userId){
        List<Node> nodes = mapper.selectNodeByUserId(userId);
        List<Node> root = new ArrayList<>();
        for (Node node : nodes) {
            if (node.getParentId() == null) {
                root.add(node);
            }
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("parent", root);
        for (Node parent : root) {
            getChildList(parent.getNodeId(),map);
        }
        return map;
    }

    private void getChildList(Long parentId, Map<String, Object> map){
        List<Node> child = mapper.selectNodeByParentId(parentId);
        if (child.size() == 0){
            return;
        }
        for (Node node : child) {
            getChildList(node.getNodeId(),map);
        }
        map.put(String.valueOf(parentId),child);
    }
}
