package com.service;

import com.pojo.Task;

import java.util.List;

public interface TaskService {

    /**
     * 发布签到任务
     *
     * @param task
     * @return
     */
    int releaseTask(Task task);


    /**
     * 获取签到任务的Id
     *
     * @param task
     * @return
     */
    Long getTaskId(Task task);


    /**
     * 根据任务id获取任务信息
     *
     * @param taskId
     * @return
     */
    Task getTaskInfoById(Long taskId);

    /**
     * 根据课堂id获取历史发布的所有签到任务
     *
     * @param classId
     * @return
     */
    List<Task> getTaskByClassId(Long classId);


    /**
     * 根据 任务Id 获取 该任务的时间
     *
     * @param taskId
     * @return
     */
    Task getTaskEndTime(Long taskId);


    /**
     * //返回 最近一次未签到的任务ID 、开始和结束时间、考勤类型
     *
     * @param classId
     * @return
     */
    Task getRecentlyRec(Long classId);


}
