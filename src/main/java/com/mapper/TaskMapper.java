package com.mapper;


import com.pojo.Task;

import java.util.List;

public interface TaskMapper {
    int deleteByPrimaryKey(Long taskId);

    int insert(Task record);

    /**
     * 教室发布签到任务
     *
     * @param record
     * @return
     */
    int insertSelective(Task record);


    /**
     * 根据任务id获取任务内容
     *
     * @param taskId
     * @return
     */
    Task selectByPrimaryKey(Long taskId);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);


    /**
     * 获取签到任务的Id
     *
     * @param task
     * @return
     */
    Long getTaskId(Task task);


    /**
     * 根据课堂id号获取所有签到任务id号
     *
     * @param classId
     * @return
     */
    List<Long> getTaskIdByClassId(Long classId);


//    /**
//     * 根据任务id获取签到截至时间
//     * @param taskId
//     * @return
//     */
//    String getEndTimmById(Long taskId);


    /**
     * 根据课堂id获取历史发布的所有签到任务
     *
     * @param classId
     * @return
     */
    List<Task> getTaskByClassId(Long classId);


    /**
     * 根据课堂ID 删除课堂所有签到任务
     *
     * @param classId
     * @return
     */
    int disMissTaskByClassId(Long classId);


    /**
     * 根据课堂 id 获取所有签到任务ID
     *
     * @param classId
     * @return
     */
    List<Long> taskIdsByClassId(Long classId);


    /**
     * 根据课堂ID 查询所有任务ID 下的 签到二维码链接
     *
     * @param classId
     * @return
     */
    List<String> getQrSrcs(Long classId);


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