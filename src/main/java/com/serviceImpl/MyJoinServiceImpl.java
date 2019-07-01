package com.serviceImpl;

import com.mapper.MyClassMapper;
import com.mapper.MyJoinMapper;
import com.mapper.SignMapper;
import com.mapper.TaskMapper;
import com.pojo.MyClass;
import com.pojo.MyClassTeaInfo;
import com.pojo.MyJoin;
import com.service.MyJoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("joinService")
public class MyJoinServiceImpl implements MyJoinService {


    @Autowired
    private MyJoinMapper myJoinMapper;

    @Autowired
    private MyClassMapper myClassMapper;

    @Autowired
    private MyClassServiceImpl classService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private SignMapper signMapper;

    @Override
    public List<Integer> joinClassId(int userId) {

        return null;
    }


    @Override
    public List<MyClassTeaInfo> joinClassList(long userId) {


        List<Long> classIdList = myJoinMapper.getClassIdByUserId(userId);
        List<MyClassTeaInfo> myClassList = new ArrayList<>();
        if (classIdList == null) {
            return null;
        }

        //遍历classid 查询所有课堂
        for (int i = 0; i < classIdList.size(); i++) {


            Long teacherId = Long.valueOf(0);
            teacherId = classService.getUserId(classIdList.get(i));
            String teacherName = userService.getUserName(teacherId);


            MyClassTeaInfo myClassTeaInfo = myClassMapper.getClassInfo(classIdList.get(i));
            myClassTeaInfo.setTeacherName(teacherName);

            myClassList.add(myClassTeaInfo);
        }

        return myClassList;
    }

    @Override
    public List<Long> getUserId(MyClass myClass) {

        return myJoinMapper.getUserId(myClass);
    }

    @Override
    public int joinClass(MyJoin record) {
        return myJoinMapper.insert(record);
    }

    @Override
    public int exitClass(MyClass myClass) {

        int result = 0;
        //根据classId 获取 所有 签到任务id
        List<Long> tasks = taskMapper.taskIdsByClassId(myClass.getClassId());
        if (tasks != null) {
            //遍历tasks  根据 taskId 和 userId 删除所有签到记录
            for (int i = 0; i < tasks.size(); i++) {
                try {
                    result = signMapper.deleteSignByUserIdAndTaskId(tasks.get(i), myClass.getUserId());
                }catch (Exception e){
                    System.out.println("签到记录删除异常");
                }

            }

        }

        //根据课堂id和用户id删除用户加课信息
        try {
            result = myJoinMapper.deleteJoinInfo(myClass);
        }catch (Exception e){
            System.out.println("加课记录删除异常");
        }


        return result;
    }

    @Override
    public int getJoinRec(MyClass myClass) {
        return myJoinMapper.getJoinRec(myClass);
    }
}
