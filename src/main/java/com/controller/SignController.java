package com.controller;


import com.pojo.Sign;
import com.pojo.Task;
import com.pojo.User;
import com.pojo.UserToken;
import com.serviceImpl.MyJoinServiceImpl;
import com.serviceImpl.SignServiceImpl;
import com.serviceImpl.TaskServiceImpl;
import com.serviceImpl.UserServiceImpl;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class SignController {

    @Autowired
    private TokenDecipherting tokenDecipherting;
    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private SignServiceImpl signService;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MyJoinServiceImpl myJoinService;

    /**
     * 学生签到  : 通过测试
     *
     * @param sign ：  签到任务 Id ，定位状态，考勤码，token
     * @return
     */
    @PostMapping(value = "/sign/stuSign")
    public ResponseData stuSign(@RequestBody Sign sign) {

        ResponseData responseData = null;
        User user = new User();
        int result = 0;
        //对token进行解码，判断是否过期
        if (sign.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(sign.getToken());
            sign.setUserId(user.getUserId());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }
            UserToken userToken = new UserToken();
            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());





            //1 获取当前时间
            Date day = new Date();
            SimpleDateFormat df =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String signTime = df.format(day);

            //根据任务id获取任务信息
            Task task = taskService.getTaskInfoById(sign.getTaskId());

            //获取当前任务发布的终止时间
            String endTime = task.getTaskEnd();
            try {
                long signT = df.parse(signTime).getTime();
                long endT = df.parse(endTime).getTime();

                //在范围内
                //对比签到码
                if (task.getTaskCode() == null || task.getTaskCode().equals("")) {
                    //为扫码签到
                    //解析签到二维码
                    String destPath = PictureLink.signQrAddress + task.getTaskQrSrc();
                    String str = QRCodeUtil.decode(destPath);
                    if (str.equals(sign.getCode())) {
                        //签到码对比成功，写入数据库
                        if (endT - signT > 0 && sign.getLocationType() == 1) {
                            //出勤
                            sign.setSignState((byte) 1);
                            sign.setSignTime(signTime);
                            return returnSignInfo(sign, userToken);
                        } else if (endT - signT > 0 && sign.getLocationType() == 2) {
                            //异常
                            sign.setSignState((byte) 2);
                            sign.setSignTime(signTime);
                            return returnSignInfo(sign, userToken);
                        }

                        responseData = ResponseData.forbidden();
                        responseData.putDataValue("newToken", userToken);
                        responseData.putDataValue("data", "发生未知错误，禁止操作！");
                        return responseData;
                    } else {
                        responseData = ResponseData.badRequest();
                        responseData.putDataValue("newToken", userToken);
                        responseData.putDataValue("data", "签到码错误，签到失败！");
                        return responseData;
                    }
                } else {
                    //为考勤码验证方式

                    if (sign.getCode() != null){
                        String s = sign.getCode();
                        s= s.toUpperCase();
                        sign.setCode(s);
                    }

                    if (sign.getCode().equals(task.getTaskCode())) {
                        if (endT - signT > 0 && sign.getLocationType() == 1) {
                            //出勤
                            sign.setSignState((byte) 1);
                            sign.setSignTime(signTime);
                            return returnSignInfo(sign, userToken);
                        } else if (endT - signT > 0 && sign.getLocationType() == 2) {
                            //异常
                            sign.setSignState((byte) 2);
                            sign.setSignTime(signTime);
                            return returnSignInfo(sign, userToken);
                        }
                        responseData = ResponseData.forbidden();
                        responseData.putDataValue("newToken", userToken);
                        responseData.putDataValue("data", "发生未知错误，禁止操作！");
                        return responseData;
                    } else {
                        responseData = ResponseData.badRequest();
                        responseData.putDataValue("newToken", userToken);
                        responseData.putDataValue("data", "签到码错误，签到失败！");
                        return responseData;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            responseData = ResponseData.forbidden();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "发生未知错误，禁止操作！");
            return responseData;
        }

    }

    //签到记录写入返回
    private ResponseData returnSignInfo(
            @RequestBody Sign sign
            ,UserToken userToken)
    {
        int result;
        ResponseData responseData;
        result = signService.updateSignRec(sign);
        if (result == 1) {
            responseData = ResponseData.ok();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "签到成功！");
            return responseData;
        } else {
            responseData = ResponseData.serverInternalError();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "服务端错误，签到失败！");
            return responseData;
        }
    }


    /**
     * 学生获取某个课堂下的所有签到记录  :  通过测试
     *
     * @param sign
     * @return
     */
    @PostMapping(value = "/sign/signRec")
    public ResponseData signRecByUserId(@RequestBody Sign sign) {

        ResponseData responseData = null;
        User user = new User();
        //对token进行解码，判断是否过期
        if (sign.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(sign.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }
            UserToken userToken = new UserToken();
            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());
            sign.setUserId(user.getUserId());

            List<Sign> signList = signService.getSignRec(sign);
            if (signList != null) {

                Long currTime = new Date().getTime();

                SimpleDateFormat df =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Long start11 = new Date().getTime();
                for (int i = 0; i < signList.size(); i++) {
                    //根据 任务ID 获取 签到截至时间
                    Task task = taskService
                            .getTaskEndTime(signList.get(i).getTaskId());
                    try {
                        Long endTime = df.parse(task.getTaskEnd()).getTime();
                        System.out.println("签到截至时间戳： " + endTime);
                        System.out.println("当前系统时间戳： " + currTime);
                        System.out.println("学生查看签到内容：");
                        if (endTime - currTime < 0) {
                            //如果任务A的结束时间大于当前时间，则为旷课 3
                            System.out.println(
                                    "签到任务结束时间 - 当前系统时间 ："
                                    + (endTime - currTime)
                            );
                            if (signList.get(i).getSignState() == 0) {
                                System.out.println("未签到 设置签到状态为旷课 3：");
                                signList.get(i).setSignState((byte) 3);
                            }

                        }
                        //其他保持默认
                        signList.get(i).setSignType(task.getTaskType());
                        signList.get(i).setTaskName(task.getTaskName());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //遍历signList  根据签到任务ID 获取任务详情
                for (int i = 0; i < signList.size(); i++) {
                    Task task = taskService.getTaskInfoById(signList.get(i).getTaskId());
                    if (task != null) {
                        signList.get(i).setTaskLatitude(task.getTaskLatitude());
                        signList.get(i).setTaskLongitude(task.getTaskLongitude());
                    }
                }


                responseData = ResponseData.ok();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("data", signList);
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
     * 教师根据任务id 获取 记录   : 通过测试
     *
     * @param sign
     * @return
     */
    @PostMapping(value = "/sign/getSignRecByTec")
    public ResponseData signRecByTec(@RequestBody Sign sign) {
        UserToken userToken = new UserToken();
        List<Sign> list = new ArrayList<>();
        ResponseData responseData = null;
        User user = new User();
        //对token进行解码，判断是否过期
        if (sign.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(sign.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());


            //1 获取当前时间
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(day);
            //根据任务id获取任务信息
            Task task = taskService.getTaskInfoById(sign.getTaskId());
            //获取当前任务发布的终止时间
            String endTime = task.getTaskEnd();

            try {
                long start = df.parse(time).getTime();   //当前系统时间
                long end = df.parse(endTime).getTime();   //签到任务结束时间
                System.out.println("教师查看签到内容：");
                System.out.println("签到任务结束时间 - 当前系统时间 ：" + (end - start));
                if (start < end) {   //当前
                    //只查询正常签到和异常签到的记录
                    list = signService.getRecByNorMal(sign.getTaskId());
                    //根据userId 查询用户名

                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            String userName = userService
                                    .getUserName(list.get(i).getUserId());
                            list.get(i).setUserName(userName);
                        }
                    }

                } else {
                    //查询所有记录
                    list = signService.getRecByAll(sign.getTaskId());
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            //未签到则为旷课
                            if (list.get(i).getSignState() == 0) {
                                System.out.println("未签到 设置签到状态为旷课 3：");
                                list.get(i).setSignState((byte) 3);  //设置成旷课
                            }
                            String userName = userService
                                    .getUserName(list.get(i).getUserId());
                            list.get(i).setUserName(userName);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        responseData = ResponseData.ok();
        responseData.putDataValue("newToken", userToken);
        responseData.putDataValue("data", list);
        return responseData;
    }


    /**
     * 修改某个签到任务下的 学生签到状态   通过测试
     *
     * @param sign ： 传入学生ID  任务ID  token  状态
     * @return
     */
    @PostMapping(value = "/sign/reviseSignRec")
    public ResponseData reviseSignRec(@RequestBody Sign sign) {

        UserToken userToken = new UserToken();
        ResponseData responseData = null;
        User user = new User();
        //对token进行解码，判断是否过期
        if (sign.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(sign.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());

            if (sign.getTaskId() == null
                    || sign.getUserId() == null
                    || sign.getSignState() == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "缺少任务ID/用户ID/状态码！");
                return responseData;
            }


            //1 获取当前时间
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = df.format(day);
            sign.setSignTime(time);


            int result = 0;
            result = signService.reviseSignState(sign);

            if (result == 1) {
                responseData = ResponseData.ok();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("data", "签到状态修改成功！");
                return responseData;
            } else {
                responseData = ResponseData.serverInternalError();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("data", "签到状态修改失败！");
                return responseData;
            }


        }


    }


    /**
     * 根据用户id 查询出所参加课堂下的所有待签到信息
     *
     * @param sign
     * @return
     */
    @PostMapping(value = "/sign/signWait")
    public ResponseData getAllSignWait(@RequestBody Sign sign) {

        UserToken userToken = new UserToken();
        ResponseData responseData = null;
        List<Task> tasks = new ArrayList<>();
        User user = new User();
        //对token进行解码，判断是否过期
        if (sign.getToken() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，缺少token！");
            return responseData;
        } else {
            //解码
            user = tokenDecipherting.tokenDecipherting(sign.getToken());
            if (user == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，token解析失败！");
                return responseData;
            }

            userToken.setToken(user.getToken());
            userToken.setUserId(user.getUserId());
            sign.setUserId(user.getUserId());

            List<Task> taskList = signService.getRecentlyTaskNot(sign);

            responseData = ResponseData.ok();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", taskList);
            return responseData;


        }

    }
}





