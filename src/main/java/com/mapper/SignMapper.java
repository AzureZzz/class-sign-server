package com.mapper;


import com.pojo.Sign;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SignMapper {
    int deleteByPrimaryKey(Long userId);

    /**
     * 写入学生签到信息
     *
     * @param record
     * @return
     */
    int insert(Sign record);

    int insertSelective(Sign record);

    Sign selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(Sign record);


    /**
     * 教师修改学生的签到信息（某个任务下）
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(Sign record);


    /**
     * 根据用户id号 和 签到任务id号确定一条签到记录
     *
     * @param userId
     * @param aLong
     * @return
     */
    Sign getSignRec(Long userId, Long aLong);


    /***
     *  根据任务Id 向签到表 写入记录
     * @param userId
     * @param taskId
     * @return
     */
    int saveSignRec(@Param("userId") Long userId, @Param("taskId") Long taskId);


    /**
     * 根据任务ID 学生ID  签到时间  签到状态 进行修改 签到记录
     *
     * @param sign
     * @return
     */
    int updateSignRec(Sign sign);


    /**
     * 获取正常签到的记录 和 异常签到记录
     *
     * @param taskId
     * @return
     */
    List<Sign> getRecByNorMal(Long taskId);


    /**
     * 获取异常签到的记录
     *
     * @param taskId
     * @return
     */
    List<Sign> getRecByAll(Long taskId);


    /***
     *  根据任务ID 删除所有学生签到信息
     * @param taskId
     * @return
     */
    int disMissSignByTaskId(Long taskId);


    /**
     * 根据用户id 和 任务id 删除一条记录
     *
     * @param taskId
     * @param userId
     * @return
     */
    int deleteSignByUserIdAndTaskId(Long taskId, Long userId);


    /**
     * 根据userId 获取所有未签到记录
     *
     * @param userId
     * @return
     */
    List<Sign> getSignRecByUserId(Long userId);


}