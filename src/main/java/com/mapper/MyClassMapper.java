package com.mapper;


import com.pojo.MyClass;
import com.pojo.MyClassTeaInfo;

import java.util.List;

public interface MyClassMapper {

    int deleteByPrimaryKey(Long classId);

    int insert(MyClass record);

    /**
     * 创建课堂
     *
     * @param record
     * @return
     */
    int insertSelective(MyClass record);

    /**
     * 根据课堂ID查询课堂内容
     *
     * @param classId
     * @return
     */
    MyClass selectByPrimaryKey(Long classId);

    int updateByPrimaryKeySelective(MyClass record);

    int updateByPrimaryKey(MyClass record);


    /**
     * 根据课堂id号 获取 学生或老师参加的课堂信息
     *
     * @param classId
     * @return
     */
    MyClassTeaInfo getClassInfo(Long classId);


    /**
     * 根据课号 获取该课堂下的老师Id号
     */
    List<Long> getUserId(MyClass myClass);


    /**
     * 获取课堂id
     *
     * @param myClass
     * @return
     */
    int getClassId(MyClass myClass);


    /**
     * 根据教师的id 查询 所创建的所有课堂信息
     *
     * @param userId
     * @return
     */
    List<MyClassTeaInfo> getTeaClassInfo(Long userId);


    /**
     * 根据课堂id 获取用户id
     *
     * @param classId
     * @return
     */
    Long getUserIdByClassId(Long classId);


    /**
     * 根据验证码查询出 课堂记录
     *
     * @param code
     * @return
     */
    MyClass getClassInfoByCode(String code);


    /**
     * 根据 课堂Id 删除 课堂记录
     *
     * @param classId
     * @return
     */
    int disMissByClassId(Long classId);


}