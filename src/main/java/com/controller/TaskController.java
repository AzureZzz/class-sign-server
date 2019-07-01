package com.controller;

import com.pojo.*;
import com.serviceImpl.MyJoinServiceImpl;
import com.serviceImpl.SignServiceImpl;
import com.serviceImpl.TaskServiceImpl;
import com.utils.PictureLink;
import com.utils.QRCodeUtil;
import com.utils.ResponseData;
import com.utils.TokenDecipherting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class TaskController {
    @Autowired
    private TokenDecipherting tokenDecipherting;
    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private MyJoinServiceImpl myJoinService;

    @Autowired
    private SignServiceImpl signService;



    /**
     * 发布签到任务，通过测试  :
     *
     * @param task
     * @return
     */
    @PostMapping(value = "/task/release")
    public ResponseData releaseTask(@RequestBody Task task) {
        ResponseData responseData = null;
        int code = 0;
        String qrSrc = null;
        User user = new User();
        //对token进行解码，判断是否过期
        if (task.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(task.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }
            SimpleDateFormat df =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date day = new Date();
            String start = df.format(day);
            // Long s = new Date().getTime();
            //System.out.println(s);
            long taskEnd = new Long(task.getTaskEnd());
            String end = df.format(new Date(taskEnd));


            task.setTaskEnd(end);
            task.setTaskStart(start);


            //1 数字 2扫码
            if (task.getTaskType() == 1) {
                //随机生成四位数字

                code = (int) ((Math.random() * 9 + 1) * 1000);

                task.setTaskCode(String.valueOf(code));

            } else if (task.getTaskType() == 2) {
                //随机生成签到二维码
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder
                        .append("-" + task.getClassId())
                        .append("-" + task.getTaskEnd())
                        .append("-" + task.getTaskLongitude())
                        .append("-" + task.getTaskLatitude());


                Long qrTime = new Date().getTime();

                //保存在数据库里的链接
                qrSrc = task.getClassId() + "-" + qrTime + ".jpg";
                task.setTaskQrSrc(qrSrc);
                //二维码里包含的内容
                String text = stringBuilder.toString();
                // 嵌入二维码的图片路径
                //String imgPath = "C:\\ClassSign\\TeskQrSrc\\";
                // 生成的二维码的路径及名称
                String destPath = "C:\\ClassSign\\TaskQrSrc\\" + qrSrc;
                //生成二维码
//               QRCodeUtil.encode(text, imgPath, destPath, true);
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


            int result = taskService.releaseTask(task);
            //获取 任务id 号
            long taskId = taskService.getTaskId(task);
            UserToken userToken = new UserToken();
            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());
            if (result == 1) {

                //将该课堂下的用户全都加入到签到表里
                //根据classId 获取所有学生id
                MyClass myClass = new MyClass();
                myClass.setClassId(task.getClassId());
                List<Long> stuList = myJoinService.getUserId(myClass);
                if (stuList != null) {
                    //将所有用户 根据taskId  写入签到表
                    for (int i = 0; i < stuList.size(); i++) {
                        signService.saveSignRec(stuList.get(i), taskId);
                    }
                }

                responseData = ResponseData.ok();
                responseData.putDataValue("newToken", userToken);

                TaskPackage taskPackage = new TaskPackage();
                taskPackage.setTaskId(taskId);
                if (code != 0) {
                    taskPackage.setCheckInCode(code);
                }
                if (qrSrc != null) {
                    String finalSrc = PictureLink.qrCodePicLink + qrSrc;
                    taskPackage.setQrCodeSrc(finalSrc);
                }

                responseData.putDataValue("data", taskPackage);
                return responseData;
            } else {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("data", null);
                return responseData;
            }
        }
    }


    /**
     * 获取某个课堂下的历史签到任务  ： 测试通过
     *
     * @param task
     * @return
     */
    @PostMapping(value = "/task/classTasks")
    public ResponseData getClassTask(@RequestBody Task task) {

        ResponseData responseData = null;
        int code = 0;
        String qrSrc = null;
        User user = new User();
        //对token进行解码，判断是否过期
        if (task.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(task.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            UserToken userToken = new UserToken();
            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());

            //根据classId 获取 课堂下的所有签到任务
            List<Task> tasks = taskService.getTaskByClassId(task.getClassId());
            for (int i = 0; i < tasks.size(); i++) {
                tasks.get(i).setTaskQrSrc(
                        PictureLink.qrCodePicLink
                        + tasks.get(i).getTaskQrSrc()
                );
            }

            responseData = ResponseData.ok();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", tasks);
            return responseData;

        }

    }


    /**
     * 传入 token  课堂id
     * 获取最近一次待签任务
     * @param task
     * @return
     */
    @PostMapping(value = "/task/recently")
    public ResponseData recentlyTask(@RequestBody Task task) {

        ResponseData responseData = null;
        User user = new User();
        UserToken userToken = new UserToken();
        //对token进行解码，判断是否过期
        if (task.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(task.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }


            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());
        }

        //返回 最近一次未签到的任务ID 、开始和结束时间、考勤类型 、 经纬度
        Task taskRecently = taskService.getRecentlyRec(task.getClassId());



        if (taskRecently != null) {
            //获取当前系统时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long current = new Date().getTime();


            //根据userId 和 taskId 查询 签到表中是否存在未签到。
            Sign sign = signService.getSignRecByUserIdAndTaskId(userToken.getUserId(),taskRecently.getTaskId());
            System.out.println("查询是否已经签到过了。。。。。！");
//            System.out.println(sign.getTaskId()+" "+sign.getTaskId());
            if (sign != null && sign.getSignTime()!=null ){
                System.out.println("已经签过，不返回数据！");
                responseData = ResponseData.ok();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("data", null);
                return responseData;
            }



            //String current = df.format(day);
            //long taskEnd = new Long(task.getTaskEnd());
            try {
                Date date = df.parse(taskRecently.getTaskEnd());
                long end = date.getTime();
                //如果当前时间大于 签到任务的截至时间，则不返回最近一次待签记录
                if (current > end) {
                    responseData = ResponseData.ok();
                    responseData.putDataValue("newToken", userToken);
                    responseData.putDataValue("data", null);
                    return responseData;
                } else {
                    responseData = ResponseData.ok();
                    responseData.putDataValue("newToken", userToken);
                    responseData.putDataValue("data", taskRecently);
                    return responseData;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            responseData = ResponseData.ok();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", null);
            return responseData;
        }
        responseData = ResponseData.ok();
        responseData.putDataValue("newToken", userToken);
        responseData.putDataValue("data", null);
        return responseData;

    }


}





















