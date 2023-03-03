package top.example.mapper;

import top.example.entity.Node;

import java.util.List;

public interface NodeMapper {
    List<Node> selectNodeByUserId(Long userId);
    List<Node> selectNodeByParentId(Long parentId);

    List<Node> getRootNode();
}
