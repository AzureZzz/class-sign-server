package com.controller;


import com.pojo.User;
import com.pojo.UserInfoPackage;
import com.pojo.UserToken;
import com.serviceImpl.LoginServiceImpl;
import com.serviceImpl.UserInfoServiceImpl;
import com.serviceImpl.UserServiceImpl;
import com.utils.PictureLink;
import com.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {


    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserInfoServiceImpl userInfoServiceImpl;

    /**
     * 传入 手机号 和密码  : 通过测试
     *
     * @return
     */
    @PostMapping(value = "/user/login")
    public ResponseData userLogin(@RequestBody User user) {

        ResponseData responseData = null;

        UserToken userToken = loginService.userLogin(user);
        if (userToken != null) {
            System.out.println("userPhone : " + user.getUserPhone());
            System.out.println("userPassword : " + user.getUserPassword());

            User user1 = userService.getUserInfo(user);

//            String school =  user1.getUserSchool();
//            String user =  user1.getUserSchool();
//            //获取用户所在学校
//            String school =  userService.getUserScholle(user);
//            //获取用户的学号
//            String userNo = userService.getUserNo(user);


            //System.out.println("school: "+school);
            //获取用户的其他信息
            UserInfoPackage userInfoPackage = userInfoServiceImpl.getUserInfo(user);
            //根据userId 获取头像链接
            String src = userInfoServiceImpl.getUserHeadSrc(user1.getUserId());

            if (userInfoPackage == null) {
                userInfoPackage = new UserInfoPackage();
                userInfoPackage.setUserSchool(user1.getUserSchool());
                userInfoPackage.setUserNo(user1.getUserNo());
                userInfoPackage.setUserName(user1.getUserName());
                userInfoPackage.setUserType(user1.getUserType());


                userInfoPackage.setUserHeadSrc(PictureLink.userHeadPicLink + src);

            } else {
                userInfoPackage.setUserSchool(user1.getUserSchool());
                userInfoPackage.setUserNo(user1.getUserNo());
                userInfoPackage.setUserName(user1.getUserName());
                userInfoPackage.setUserType(user1.getUserType());
                userInfoPackage.setUserHeadSrc(PictureLink.userHeadPicLink + src);

            }


            if (userToken.getToken() != null) {
                responseData = ResponseData.ok();
                responseData.putDataValue("newToken", userToken);
                responseData.putDataValue("userInfo", userInfoPackage);
                return responseData;
            } else {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "登陆失败，请重试！");
                return responseData;
            }
        } else {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "账号或密码错误，登陆失败，请重试！");
            return responseData;
        }

    }

}
