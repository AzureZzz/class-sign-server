package com.service;

import com.pojo.Sign;
import com.pojo.Task;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SignService {


    /**
     * 保存签到记录
     *
     * @param sign
     * @return
     */
    int saveSignRec(Sign sign);


    /**
     * 获取当前用户在该课堂下的所有签到记录
     *
     * @param sign
     * @return
     */
    List<Sign> getSignRec(Sign sign);

    /***
     *  根据任务Id 向签到表 写入记录
     * @param userId
     * @param taskId
     * @return
     */
    int saveSignRec(@Param("userId") Long userId, @Param("taskId") Long taskId);


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


    /**
     * 根据任务ID 学生ID  签到时间  签到状态 进行修改 签到记录
     *
     * @param sign
     * @return
     */
    int updateSignRec(Sign sign);


    /**
     * 教师修改 某个签到任务下的学生签到状态
     *
     * @param sign
     * @return
     */
    int reviseSignState(Sign sign);


    /**
     * 获取未签到的所有任务
     *
     * @param sign
     * @return
     */
    List<Task> getRecentlyTaskNot(Sign sign);


    /**
     * 根据用户id号 和 签到任务id号确定一条签到记录
     *
     * @param userId
     * @param aLong
     * @return
     */
    Sign getSignRecByUserIdAndTaskId(Long userId, Long aLong);
}
