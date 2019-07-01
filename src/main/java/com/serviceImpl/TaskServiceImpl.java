package com.serviceImpl;

import com.mapper.TaskMapper;
import com.pojo.Task;
import com.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;


    @Override
    public int releaseTask(Task task) {

        return taskMapper.insertSelective(task);
    }

    @Override
    public Long getTaskId(Task task) {

        return taskMapper.getTaskId(task);
    }

    @Override
    public Task getTaskInfoById(Long taskId) {
        return taskMapper.selectByPrimaryKey(taskId);
    }

    @Override
    public List<Task> getTaskByClassId(Long classId) {
        return taskMapper.getTaskByClassId(classId);
    }

    @Override
    public Task getTaskEndTime(Long taskId) {
        return taskMapper.getTaskEndTime(taskId);
    }

    @Override
    public Task getRecentlyRec(Long classId) {
        return taskMapper.getRecentlyRec(classId);
    }


}
