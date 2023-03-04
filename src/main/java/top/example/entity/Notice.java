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
public class Notice {
    private Long noticeId;
    private Long receiverId;
    private String content;
    private Date createTime;
}
