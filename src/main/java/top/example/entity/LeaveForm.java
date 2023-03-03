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
public class LeaveForm {
    private Long formId;
    private Long employeeId;
    private Integer formType;
    private Date startTime;
    private Date endTime;
    private String reason;
    private Date createTime;
    private String state;

}
