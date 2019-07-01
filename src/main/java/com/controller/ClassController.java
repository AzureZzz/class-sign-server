package com.controller;


import com.pojo.*;
import com.serviceImpl.MyClassServiceImpl;
import com.serviceImpl.MyJoinServiceImpl;
import com.serviceImpl.UserInfoServiceImpl;
import com.serviceImpl.UserServiceImpl;
import com.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ClassController {

    @Autowired
    private TokenDecipherting tokenDecipherting;

    @Autowired
    private MyClassServiceImpl classService;

    @Autowired
    private MyJoinServiceImpl joinService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserInfoServiceImpl userInfoService;


    /**
     * 获取课堂列表 ： 通过测试
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/class/list")
    public ResponseData getClassList(@RequestBody User user) {

        ResponseData responseData = null;

        List<MyClassTeaInfo> myClassTeaInfos = null;

        //对token进行解码，判断是否过期
        if (user.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            User user1 = tokenDecipherting.tokenDecipherting(user.getToken());
            if (user1 == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }


            //先获取参与的
            List<MyClassTeaInfo> myClasses = joinService.joinClassList(user1.getUserId());

            //根据id查询用户身份 1 学生  2教师
            int typeResult = 1; //默认为学生
            typeResult = userService.getUserType(user1.getUserId());
            if (typeResult == 2) {
                //再获取老师发布的创建的所有课堂
                myClassTeaInfos = classService.getTeaClassInfo(user1.getUserId());
                //遍历集合，查询老师的id 查询名字
                for (int i = 0; i < myClassTeaInfos.size(); i++) {

                    Long teacherId = Long.valueOf(0);
                    teacherId = classService.getUserId(myClassTeaInfos.get(i).getClassId());
                    String teacherName = userService.getUserName(teacherId);
                    myClassTeaInfos.get(i).setTeacherName(teacherName);
                }

            }

            UserToken userToken = new UserToken();
            userToken.setUserId(user1.getUserId());
            userToken.setToken(user1.getToken());

            responseData = ResponseData.ok();
            responseData.putDataValue("list", myClasses);
            responseData.putDataValue("listTea", myClassTeaInfos);
            responseData.putDataValue("newToken", userToken);
            return responseData;
        }
    }


    /**
     * 获取课堂成员 ： 通过测试
     *
     * @param myClass : 传入课堂id号、 token
     * @return
     */
    @PostMapping(value = "/class/member")
    public ResponseData getClassMember(@RequestBody MyClass myClass) {

        ResponseData responseData = null;
        User user = new User();
        //对token进行解码，判断是否过期
        if (myClass.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(myClass.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }


            List<User> userList = classService.getUserInfoById(myClass);

            //遍历userList , 用userId 去查询 头像链接
            if (userList != null) {
                for (int i = 0; i < userList.size(); i++) {
                    String src = userInfoService.getUserHeadSrc(userList.get(i).getUserId());
                    if (src != null) {
                        userList.get(i).setUserHeadSrc(PictureLink.userHeadPicLink + src);
                    }

                }
            } else {
                userList = new ArrayList<>();
            }


            UserToken userToken = new UserToken();
            userToken.setUserId(user.getUserId());
            userToken.setToken(user.getToken());

            responseData = ResponseData.ok();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", userList);
            return responseData;
        }
    }


    /**
     * 创建课堂 : 通过测试
     *
     * @param myClass : 传入课堂名称，课号、 token，创建者Id
     * @return
     */
    @PostMapping(value = "/class/establish")
    public ResponseData establishClass(@RequestBody MyClass myClass) {

        ResponseData responseData = null;
        User user = new User();
        String qrSrc = null;
        String joinCode = null;

        //对token进行解码，判断是否过期
        if (myClass.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(myClass.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }
            myClass.setUserId(user.getUserId());
            //随机生成课号：6位大写字母
            joinCode = JoinClassCode.joinCode();
            myClass.setClassCode(joinCode);

            //创建二维码
            //保存在数据库里的链接
            qrSrc = myClass.getClassNo()
                    + "-" + joinCode
                    + "-"
                    + myClass.getClassName()
                    + ".jpg";

            myClass.setClassQrcodeSrc(qrSrc);
            //二维码里包含的内容
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(myClass.getClassNo())
                    .append("-" + joinCode)
                    .append("-" + myClass.getClassName());
            String text = stringBuilder.toString();
            myClass.setClassQrcode(text);
            // 生成的二维码的路径及名称
            String destPath = "C:\\ClassSign\\ClassQrSrc\\" + qrSrc;


            try {
                QRCodeUtil.encode(text, null, destPath, true);
                // 解析二维码
                String str = QRCodeUtil.decode(destPath);
                // 打印出解析出的内容
                System.out.println(str);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        int classId = 0;
        classId = classService.establishClass(myClass);
        UserToken userToken = new UserToken();
        userToken.setUserId(user.getUserId());
        userToken.setToken(user.getToken());

        if (classId == 0) {
            //课堂创建失败
            responseData = ResponseData.serverInternalError();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "创建课堂失败！");

        } else {
            //课堂创建成功
            MyClassPackage myClassPackage = new MyClassPackage();
            myClassPackage.setClassCode(joinCode);
            myClassPackage.setClassId((long) classId);
            myClassPackage.setClassName(myClass.getClassName());
            myClassPackage.setClassQrcodeSrc(PictureLink.JoinClassQrCodePicLink + qrSrc);
            responseData = ResponseData.ok();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", myClassPackage);
        }

        return responseData;
    }


    /**
     * 获取课堂信息  ： 通过检测
     *
     * @param myClass ： token \ classId
     * @return : 老师头像、课堂名称、加课码、加课二维码链接、老师姓名、学生总人数、newToken
     */
    @PostMapping(value = "/class/myClassInfo")
    public ResponseData getClassInfo(@RequestBody MyClass myClass) {

        ResponseData responseData = null;
        User user = null;
        String teacherName = null;
        int num = 0;
        List<User> userList = null;
        String teacherSrc = null;

        if (myClass.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(myClass.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            UserToken userToken = new UserToken();
            userToken.setUserId(user.getUserId());
            userToken.setToken(user.getToken());


            MyClassInfo myClassInfo = new MyClassInfo();

            //查询课堂信息
            MyClass myClass1 = null;
            myClass1 = classService.getClassInfo(myClass.getClassId());

            if (myClass1 == null) {
                //课堂创建失败
                responseData = ResponseData.badRequest();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("data", "该课堂不存在！");

            } else {
                Long teacherId = myClass1.getUserId();
                //查询老师姓名
                teacherName = userService.getUserName(teacherId);
                if (teacherName == null) {
                    responseData = ResponseData.badRequest();
                    responseData.putDataValue("newToken", userToken);
                    responseData.putDataValue("data", "该课堂的老师信息不存在！");
                } else {
                    //获取课堂总人数
                    userList = classService.getUserInfoById(myClass1);
                    if (userList == null) {
                        num = 0;
                    } else {
                        num = userList.size();
                    }
                    //获取老师头像地址
                    teacherSrc = userInfoService.getUserHeadSrc(teacherId);

                    myClassInfo.setClassCode(myClass1.getClassCode());  //加课码赋值
                    myClassInfo.setClassName(myClass1.getClassName());  // 课程名称赋值
                    myClassInfo.setClassQrcodeSrc(PictureLink.JoinClassQrCodePicLink
                            + myClass1.getClassQrcodeSrc());  //加课二维码链接
                    myClassInfo.setStuNumber(num);  //学生人数
                    myClassInfo.setUserName(teacherName); // 老师姓名
                    myClassInfo.setUserHeadSrc(PictureLink.userHeadPicLink + teacherSrc);
                    responseData = ResponseData.ok();
                    responseData.putDataValue("newToken", userToken);
                    responseData.putDataValue("data", myClassInfo);

                }


            }

        }

        return responseData;
    }


    /**
     *
     * 传入TOKEN 和 加课码  : 通过测试
     *
     * @param myClass
     * @return
     */
    @PostMapping(value = "/myClass/join")
    public ResponseData joinClass(@RequestBody MyClass myClass) {


        ResponseData responseData = null;
        User user = null;
        if (myClass.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(myClass.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            UserToken userToken = new UserToken();
            userToken.setUserId(user.getUserId());
            userToken.setToken(user.getToken());

             String s = myClass.getClassCode();
             s = s.toUpperCase();
             myClass.setClassCode(s);


            //根据classId 获取 课堂所有信息
            MyClass myClass1 = classService.getClassInfoByCode(myClass.getClassCode());


            if (myClass1 == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("data", "加课码不正确，加入课堂失败");
            } else {
//                String joinCode =null;

                //根据userId 和 classId 查询 是否已经有加课记录
                MyClass myClass2 = new MyClass();
                myClass2.setUserId(user.getUserId());
                myClass2.setClassId(myClass1.getClassId());

                int result1 = 0 ;
                result1 = joinService.getJoinRec(myClass2);
                System.out.println("加入课堂次数： " +result1);
                if (result1 >0){
                    System.out.println("重复加入课堂次数： " +result1);
                    responseData = ResponseData.forbidden();
                    responseData.putDataValue("newToken", userToken);
                    responseData.putDataValue("data", "已加入课堂！");
                    return responseData;
                }



//
                MyJoin myJoin = new MyJoin();
                myJoin.setClassId(myClass1.getClassId());
                myJoin.setUserId(user.getUserId());
                int result = 0;
                result = joinService.joinClass(myJoin);
                if (result == 1) {
                    responseData = ResponseData.ok();
                    responseData.putDataValue("newToken", userToken);
                    responseData.putDataValue("data", "加入成功！");
                } else {
                    responseData = ResponseData.badRequest();
                    responseData.putDataValue("newToken", userToken);
                    responseData.putDataValue("data", "加入失败！");
                }

            }


        }

        return responseData;


    }


    /**
     * 教师解散课堂 : 通过测试
     *
     * @param myClass ： 传入课堂ID  教师TOken
     * @return
     */
    @PostMapping(value = "/class/disMiss")
    public ResponseData DisMissClass(@RequestBody MyClass myClass) {
        ResponseData responseData = null;
        User user = null;
        if (myClass.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(myClass.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            UserToken userToken = new UserToken();
            userToken.setUserId(user.getUserId());
            userToken.setToken(user.getToken());


            int disMissResult = 0;
            disMissResult = classService.disMissClass(myClass.getClassId());

            if (disMissResult != 0) {
                //课堂解散成功
                responseData = ResponseData.ok();
                responseData.putDataValue("data", "课堂解散成功！");
                responseData.putDataValue("newToken", userToken);
                return responseData;

            } else {
                responseData = ResponseData.serverInternalError();
                responseData.putDataValue("data", "课堂解散失败！");
                responseData.putDataValue("newToken", userToken);
                return responseData;
            }
        }
    }


    /**
     * 学生退出课堂
     *
     * @param myClass
     * @return
     */
    @PostMapping(value = "/class/exit")
    public ResponseData exitClass(@RequestBody MyClass myClass) {


        ResponseData responseData = null;
        User user = null;
        if (myClass.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(myClass.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            UserToken userToken = new UserToken();
            userToken.setUserId(user.getUserId());
            userToken.setToken(user.getToken());

            myClass.setUserId(user.getUserId());

            //删除加入课堂记录
            int result = joinService.exitClass(myClass);

            if (result == 1) {
                responseData = ResponseData.ok();
                responseData.putDataValue("data", "退出课堂成功！");
                responseData.putDataValue("newToken", userToken);
                return responseData;
            } else {

                responseData = ResponseData.serverInternalError();
                responseData.putDataValue("data", "退出课堂失败！");
                responseData.putDataValue("newToken", userToken);
                return responseData;
            }
        }


    }


}














