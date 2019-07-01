package com.service;

import com.pojo.MyClass;
import com.pojo.MyClassTeaInfo;
import com.pojo.User;

import java.util.List;

public interface MyClassService {


    /**
     * 获取学生或老师参加的课堂列表
     *
     * @param user
     * @return
     */
    MyClass getClassList(User user);


    /**
     * 根据学生Id号获取所有的学生信息
     */
    List<User> getUserInfoById(MyClass myClass);


    /**
     * 创建课堂
     *
     * @param myClass
     * @return
     */
    int establishClass(MyClass myClass);


    /**
     * 根据课堂Id获取课堂信息
     *
     * @param classId
     * @return
     */
    MyClass getClassInfo(Long classId);


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
    Long getUserId(Long classId);


    /**
     * 根据验证码查询出 课堂记录
     *
     * @param code
     * @return
     */
    MyClass getClassInfoByCode(String code);


    //根据课堂id 删除 课堂所有记录
    int disMissClass(Long classId);
}
