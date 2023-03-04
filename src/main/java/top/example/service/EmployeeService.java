package top.example.service;

import org.apache.ibatis.session.SqlSession;
import top.example.entity.Employee;
import top.example.entity.LeaveForm;
import top.example.entity.Notice;
import top.example.entity.ProcessFlow;
import top.example.mapper.EmployeeMapper;
import top.example.mapper.LeaveFormMapper;
import top.example.mapper.NoticeMapper;
import top.example.mapper.ProcessFlowMapper;
import top.example.service.exception.LeaveFormException;
import top.example.utils.MyBatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EmployeeService {

    EmployeeMapper employeeMapper = MyBatisUtils.getSqlSession().getMapper(EmployeeMapper.class);
    LeaveFormMapper leaveFormMapper = MyBatisUtils.getSqlSession().getMapper(LeaveFormMapper.class);
    ProcessFlowMapper processFlowMapper = MyBatisUtils.getSqlSession().getMapper(ProcessFlowMapper.class);
    NoticeMapper noticeMapper = MyBatisUtils.getSqlSession().getMapper(NoticeMapper.class);
    SqlSession sqlSession = MyBatisUtils.getSqlSession();


    public Employee getEmp(Long id) {
        Employee employee = employeeMapper.selectById(id);
        employee.setDepartmentName(employeeMapper.getDeptNameById(employee.getDepartmentId()));
        return employee;
    }

    //请假业务
    public boolean leave(LeaveForm form) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = form.getStartTime();
        Date endTime = form.getEndTime();
        long diff = endTime.getTime() - startTime.getTime();
        long hours = diff / (1000 * 60 * 60);
        boolean flag = true;
        //添加请假单子
        try {
            leaveFormMapper.insert(form);
            form.setFormId(leaveFormMapper.getMaxId());
            //流水1单
            ProcessFlow processFlow1 = ProcessFlow.builder().formId(form.getFormId())
                    .operatorId(form.getEmployeeId()).action("apply").createTime(new Date())
                    .orderNo(1).state("complete").isLast(0).reason(form.getReason()).build();
            processFlowMapper.insert(processFlow1);
            Employee employee = employeeMapper.selectById(form.getEmployeeId());
            //找到上一级请假
            Employee manager = employeeMapper.getManagerByEmp(employeeMapper.selectById(form.getEmployeeId()));
            //总经理请假
            if ("总经理".equals(employee.getTitle())) {
                ProcessFlow processFlow2 = ProcessFlow.builder().formId(form.getFormId())
                        .operatorId(employee.getEmployeeId()).action("audit").createTime(new Date())
                        .orderNo(2).state("complete").isLast(1).build();
                //流水二单
                processFlowMapper.insert(processFlow2);



            } else if ("部门经理".equals(employee.getTitle())) {
                //部门经理请假
                ProcessFlow processFlow2 = ProcessFlow.builder().formId(form.getFormId())
                        .operatorId(manager.getEmployeeId()).action("audit").createTime(new Date())
                        .orderNo(2).state("process").isLast(1).build();
                processFlowMapper.insert(processFlow2);
            } else {
                //既不是部门经理也不是总经理请假
                ProcessFlow processFlow2 = ProcessFlow.builder().formId(form.getFormId())
                        .operatorId(manager.getEmployeeId()).action("audit").createTime(new Date())
                        .orderNo(2).state("process").isLast(0).build();
                if (hours <= 72) {
                    processFlow2.setIsLast(1);
                    processFlowMapper.insert(processFlow2);
                } else if (hours > 72) {
                    Employee boss = employeeMapper.getManagerByEmp(employeeMapper.selectById(manager.getEmployeeId()));
                    ProcessFlow processFlow3 = ProcessFlow.builder().formId(form.getFormId())
                            .operatorId(boss.getEmployeeId()).action("audit").createTime(new Date())
                            .orderNo(3).state("ready").isLast(1).build();
                    processFlowMapper.insert(processFlow2);
                    processFlowMapper.insert(processFlow3);
                }
                sqlSession.commit();
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            sqlSession.rollback();
        }
        if (flag)
            sqlSession.commit();
        return flag;
    }

    public List<Map<String, Object>> getLeaveFormList(String state, Long operatorId) {
        return leaveFormMapper.selectByParams(state, operatorId);
    }


    //审批流程
    /*
        result有approved与refused两个状态
        流程对象中的state有refused cancel ready三个状态

        process出现的条件:最近一级的领导需要审批出现的状态为process
        当该领导处理完毕后,如若是refused则后面的领导状态全为cancel
        如若是approved则该领导的上一级领导为process状态

        ready出现的条件:上两级及以上的领导为ready状态
        cancel出现的条件:当多级领导中有一级为refused,则其余领导的state从ready改为cancel
     */
    public void audit(Long formId, Long operatorId, String result, String reason) {
        List<ProcessFlow> flowList = processFlowMapper.selectByFormId(formId);
        if (flowList.size() == 0) {
            throw new LeaveFormException("无效的审批流程");
        }
        //获取当前任务ProcessFlow对象
        List<ProcessFlow> processList = flowList.stream().filter(p ->
                Objects.equals(p.getOperatorId(), operatorId) && "process".equals(p.getState())
        ).toList();

        ProcessFlow process;
        if (processList.size() == 0) {
            throw new LeaveFormException("未找到待处理任务节点");
        } else {
            process = processList.get(0);
            process.setState("complete");
            process.setResult(result);
            process.setReason(reason);
            process.setAuditTime(new Date());
            processFlowMapper.update(process);
        }

        LeaveForm form = leaveFormMapper.selectById(formId);
        Employee employee = employeeMapper.selectById(form.getEmployeeId());
        Employee operator = employeeMapper.selectById(operatorId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH时");


        //找到对应的操作者

        //获取对应的流程


        if (process.getIsLast() == 1) {
            form.setState(result);
            leaveFormMapper.update(form);
            String strResult = null;
            if ("approved".equals(result)) {
                strResult = "批准";
            } else if ("refused".equals(result)) {
                strResult = "驳回";
            }
            //发送给申请人的通知
            String notice1 = String.format("您的请假申请[%s-%s]%s%s已%s,审批意见:%s,审批流程已结束",
                    sdf.format(form.getStartTime()),
                    sdf.format(form.getEndTime()),
                    operator.getTitle(),
                    operator.getName(),
                    strResult,
                    reason);
            noticeMapper.insert(Notice.builder().receiverId(form.getEmployeeId())
                    .content(notice1)
                    .createTime(new Date())
                    .build());
            //发送给审批人的通知
            String notice2 = String.format("%s-%s提起请假申请[%s-%s]您已%s,审批意见:%s,审批流程已结束",
                    employee.getTitle(),
                    employee.getName(),
                    sdf.format(form.getStartTime()),
                    sdf.format(form.getEndTime()),
                    strResult,
                    reason);
            noticeMapper.insert(Notice.builder().receiverId(operator.getEmployeeId())
                    .content(notice2)
                    .createTime(new Date())
                    .build());
        } else {
            List<ProcessFlow> readyList = flowList.stream().filter(p -> "ready".equals(p.getState())).toList();
            if ("approved".equals(result)) {
                ProcessFlow readyProcess = readyList.get(0);
                readyProcess.setState("process");
                processFlowMapper.update(readyProcess);
                //消息1:通知表单提交人,
                String notice1 = String.format("您的请假申请[%s-%s]%s%s已批准,审批意见:%s,请继续等待上级审批",
                        sdf.format(form.getStartTime()),
                        sdf.format(form.getEndTime()),
                        operator.getTitle(),
                        operator.getName(),
                        reason);
                noticeMapper.insert(Notice.builder()
                        .receiverId(form.getEmployeeId())
                        .content(notice1)
                        .createTime(new Date())
                        .build());
                //消息2:通知部门经理(当前经办人),员工的申请你以批准,交由上级继续审批
                String notice2 = String.format("%s-%s提起请假申请[%s-%s]您已批准,审批意见:%s,申请转至上级领导继续审批",
                        employee.getTitle(),
                        employee.getName(),
                        sdf.format(form.getStartTime()),
                        sdf.format(form.getEndTime()),
                        reason);
                noticeMapper.insert(Notice.builder()
                        .receiverId(operator.getEmployeeId())
                        .content(notice2)
                        .createTime(new Date())
                        .build());
                //通知总经理
                String notice3 = String.format("%s-%s提起请假申请[%s-%s],请您尽快审批",
                        employee.getTitle(),
                        employee.getName(),
                        sdf.format(form.getStartTime()),
                        sdf.format(form.getEndTime()),
                        reason);
                noticeMapper.insert(Notice.builder()
                        .receiverId(readyProcess.getOperatorId())
                        .content(notice3)
                        .createTime(new Date())
                        .build());
            } else if ("refused".equals(result)) {
                for (ProcessFlow processFlow : readyList) {
                    processFlow.setState("cancel");
                    processFlowMapper.update(processFlow);
                }
                form.setState("refused");
                leaveFormMapper.update(form);
                //消息1:通知申请人表单已被驳回
                String notice1 = String.format("您的请假申请[%s-%s]%s%s已驳回,审批意见:%s,审批流程已结束",
                        sdf.format(form.getStartTime()),
                        sdf.format(form.getEndTime()),
                        operator.getTitle(),
                        operator.getName(),
                        reason);
                noticeMapper.insert(Notice.builder().receiverId(form.getEmployeeId())
                        .content(notice1)
                        .createTime(new Date())
                        .build());
                //发送给审批人的通知
                String notice2 = String.format("%s-%s提起请假申请[%s-%s]您已驳回,审批意见:%s,审批流程已结束",
                        employee.getTitle(),
                        employee.getName(),
                        sdf.format(form.getStartTime()),
                        sdf.format(form.getEndTime()),
                        reason);
                noticeMapper.insert(Notice.builder().receiverId(operator.getEmployeeId())
                        .content(notice2)
                        .createTime(new Date())
                        .build());
            }
        }

        sqlSession.commit();
    }
}
