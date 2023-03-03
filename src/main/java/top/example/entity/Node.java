package top.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Node {
    private Long nodeId;
    /**
     * 节点类型 1-模块 2-功能
     */
    private Integer nodeType;

    /**
     * 节点名
     */
    private String nodeName;
    /**
     * 页面URL
     */
    private String url;
    /**
     * 节点编码
     */
    private Integer nodeCode;
    /**
     * 上级编号
     */
    private Long parentId;

    private String icon;

}
