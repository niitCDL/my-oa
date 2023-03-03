package top.example.service;

import org.apache.ibatis.session.SqlSession;
import top.example.entity.Employee;
import top.example.entity.LeaveForm;
import top.example.entity.ProcessFlow;
import top.example.mapper.EmployeeMapper;
import top.example.mapper.LeaveFormMapper;
import top.example.mapper.ProcessFlowMapper;
import top.example.utils.MyBatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmployeeService {

    EmployeeMapper employeeMapper = MyBatisUtils.getSqlSession().getMapper(EmployeeMapper.class);
    LeaveFormMapper leaveFormMapper = MyBatisUtils.getSqlSession().getMapper(LeaveFormMapper.class);
    ProcessFlowMapper processFlowMapper = MyBatisUtils.getSqlSession().getMapper(ProcessFlowMapper.class);
    SqlSession sqlSession = MyBatisUtils.getSqlSession();


    public Employee getEmp(Long id) {
        Employee employee = employeeMapper.selectById(id);
        employee.setDepartmentName(employeeMapper.getDeptNameById(employee.getDepartmentId()));
        return employee;
    }

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
                    .orderNo(1).state("complete").isLast(0).build();
            processFlowMapper.insert(processFlow1);
            Employee employee = employeeMapper.selectById(form.getEmployeeId());
            //找到上一级请假
            Employee manager = employeeMapper.getManagerByEmp(employeeMapper.selectById(form.getEmployeeId()));
            //总经理请假
            if ("总经理".equals(employee.getTitle())) {
                ProcessFlow processFlow2 = ProcessFlow.builder().formId(form.getFormId())
                        .operatorId(employee.getEmployeeId()).action("audit").createTime(new Date())
                        .orderNo(2).state("complete").isLast(1).reason(form.getReason()).build();
                //流水二单
                processFlowMapper.insert(processFlow2);
            } else if ("部门经理".equals(employee.getTitle())) {
                //部门经理请假
                ProcessFlow processFlow2 = ProcessFlow.builder().formId(form.getFormId())
                        .operatorId(manager.getEmployeeId()).action("audit").createTime(new Date())
                        .orderNo(2).state("processing").isLast(0).reason(form.getReason()).build();
                processFlowMapper.insert(processFlow2);
            } else {
                //既不是部门经理也不是总经理请假
                ProcessFlow processFlow2 = ProcessFlow.builder().formId(form.getFormId())
                        .operatorId(manager.getEmployeeId()).action("audit").createTime(new Date())
                        .orderNo(2).state("process").isLast(0).reason(form.getReason()).build();
                if (hours <= 72) {
                    processFlow2.setIsLast(1);
                    processFlowMapper.insert(processFlow2);
                } else if (hours > 72) {
                    Employee boss = employeeMapper.getManagerByEmp(employeeMapper.selectById(manager.getEmployeeId()));
                    ProcessFlow processFlow3 = ProcessFlow.builder().formId(form.getFormId())
                            .operatorId(boss.getEmployeeId()).action("audit").createTime(new Date())
                            .orderNo(3).state("ready").isLast(1).reason(form.getReason()).build();
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
}
