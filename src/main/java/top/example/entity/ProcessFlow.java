package top.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessFlow {
    private Long processId;
    private Long formId;
    private Long operatorId;
    private String action;
    private String result;
    private String reason;
    private Date createTime;
    private Date auditTime;
    private Integer orderNo;
    private String state;
    private Integer isLast;

}
