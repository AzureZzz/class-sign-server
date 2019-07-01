package com.serviceImpl;

import com.mapper.*;
import com.pojo.MyClass;
import com.pojo.MyClassTeaInfo;
import com.pojo.User;
import com.service.MyClassService;
import com.utils.PictureLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service("classService")
public class MyClassServiceImpl implements MyClassService {

    @Autowired
    private MyClassMapper myClassMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MyJoinMapper myJoinMapper;

    @Autowired
    private SignMapper signMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public MyClass getClassList(User user) {

        return null;
    }


    @Override
    public List<User> getUserInfoById(MyClass myClass) {


//        List<Long> longs = myClassMapper.getUserId(myClass);
        List<Long> longs = myJoinMapper.getUserId(myClass);

        List<User> userList = new ArrayList<>();
        for (int i = 0; i < longs.size(); i++) {
            User user = new User();
            user = userMapper.selectByPrimaryKey(longs.get(i));
            user.setUserPhone(null);
            user.setUserPassword(null);
            user.setUserSchool(null);
            user.setUserPhone(null);
            userList.add(user);
        }

        return userList;
    }

    @Override
    public int establishClass(MyClass myClass) {

        int result = 0;
        int classId = 0;

        //先存入课堂信息
        result = myClassMapper.insertSelective(myClass);
        if (result == 0) {
            return result;
        } else {
            //查询课堂id
            classId = myClassMapper.getClassId(myClass);
            if (classId == 0) {
                return classId;
            } else {
                return classId;
            }

        }
    }


    @Override
    public MyClass getClassInfo(Long classId) {
        return myClassMapper.selectByPrimaryKey(classId);
    }


    @Override
    public List<MyClassTeaInfo> getTeaClassInfo(Long userId) {
        return myClassMapper.getTeaClassInfo(userId);
    }

    @Override
    public Long getUserId(Long classId) {
        return myClassMapper.getUserIdByClassId(classId);
    }

    @Override
    public MyClass getClassInfoByCode(String code) {
        return myClassMapper.getClassInfoByCode(code);
    }


    /***
     *  删除多个表的数据
     * @param classId
     * @return
     */
    @Override
    public int disMissClass(Long classId) {

        int result = 0;

        //0.根据课堂id查询所有签到任务id
        List<Long> taskIds = taskMapper.getTaskIdByClassId(classId);

        //1.删除签到记录
        if (taskIds != null) {
            //遍历删除
            for (int i = 0; i < taskIds.size(); i++) {
                try{
                    result = signMapper.disMissSignByTaskId(taskIds.get(i));
                }catch (Exception e){
                    System.out.println("删除签到记录 异常！");
                }

            }

        }

        //2.删除签到任务
        if (taskIds != null) {
            //获取所有的签到任务的二维码链接
            List<String> qrList = taskMapper.getQrSrcs(classId);
            result = taskMapper.disMissTaskByClassId(classId);
            if (result > 0 && qrList != null) {
                for (int i = 0; i < qrList.size(); i++) {
                    try {
                        File file = new File(PictureLink.signQrAddress + qrList.get(i));
                        file.delete();
                    }catch (Exception e){
                        System.out.println("签到二维码图片删除异常");
                    }

                }
            }
        }
        //3.删除学生加入课堂记录
        try {
            result = myJoinMapper.disMissJoinByClassId(classId);
        }catch (Exception e){
            System.out.println("学生加入课堂信息删除异常");
        }


        //4.删除创建课堂记录
        MyClass myClass = myClassMapper.selectByPrimaryKey(classId);
        if (myClass != null) {
            try {
                File file = new File(PictureLink.joinClassAddress + myClass.getClassQrcodeSrc());
                file.delete();
                result = myClassMapper.disMissByClassId(classId);
            }catch (Exception e){
                System.out.println("签创建课堂记录删除异常");
            }

        }


        return result;
    }
}
