package com.serviceImpl;

import com.mapper.SignMapper;
import com.mapper.TaskMapper;
import com.pojo.Sign;
import com.pojo.Task;
import com.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("signService")
public class SignServiceImpl implements SignService {

    @Autowired
    private SignMapper signMapper;

    @Autowired
    private TaskMapper taskMapper;


    @Override
    public int saveSignRec(Sign sign) {
        return signMapper.insert(sign);
    }

    @Override
    public List<Sign> getSignRec(Sign sign) {

        List<Sign> signList = new ArrayList<>();
        //获取该课堂下的所有任务id数
        List<Long> taskIdLsit = taskMapper.getTaskIdByClassId(sign.getClassId());
        if (taskIdLsit != null) {
            for (int i = 0; i < taskIdLsit.size(); i++) {
                //根据签到任务id 和 学生id获取所有签到记录
                Sign sign1 = signMapper.getSignRec(sign.getUserId(), taskIdLsit.get(i));
                if (sign1 != null) {
                    signList.add(sign1);
                }

            }
        }
        return signList;
    }

    @Override
    public int saveSignRec(Long userId, Long taskId) {
        return signMapper.saveSignRec(userId, taskId);
    }

    @Override
    public List<Sign> getRecByNorMal(Long taskId) {
        return signMapper.getRecByNorMal(taskId);
    }

    @Override
    public List<Sign> getRecByAll(Long taskId) {
        return signMapper.getRecByAll(taskId);
    }

    @Override
    public int updateSignRec(Sign sign) {
        return signMapper.updateSignRec(sign);
    }

    @Override
    public int reviseSignState(Sign sign) {
        return signMapper.updateByPrimaryKey(sign);
    }

    @Override
    public List<Task> getRecentlyTaskNot(Sign sign) {

        //根据userId从签到表中获取 签到状态为0 的所有记录
        List<Sign> taskList = signMapper.getSignRecByUserId(sign.getUserId());
        if (taskList == null) {
            return null;
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long current = new Date().getTime();
        List<Task> taskList1 = new ArrayList<>();



        //遍历
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println("taskList.get(i).getTaskId() : " +taskList.get(i).getTaskId());
            //根据taskId 查内容
            Task task = taskMapper.selectByPrimaryKey(taskList.get(i).getTaskId());
            if (task != null) {
                try {
                    long temp = df.parse(task.getTaskEnd()).getTime();
                    if (temp > current) {
                        taskList1.add(task);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }


        return taskList1;
    }

    @Override
    public Sign getSignRecByUserIdAndTaskId(Long userId, Long aLong) {

        return signMapper.getSignRec(userId,aLong);
    }
}
