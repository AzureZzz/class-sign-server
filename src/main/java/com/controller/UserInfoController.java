package com.controller;


import com.pojo.User;
import com.pojo.UserInfo;
import com.pojo.UserToken;
import com.serviceImpl.UserInfoServiceImpl;
import com.serviceImpl.UserServiceImpl;
import com.utils.PictureLink;
import com.utils.ResponseData;
import com.utils.TokenDecipherting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
public class UserInfoController {

    @Autowired
    private TokenDecipherting tokenDecipherting;

    @Autowired
    private UserInfoServiceImpl userInfoServiceImpl;

    @Autowired
    private UserServiceImpl userService;


    /**
     * 头像上传 : 通过测试
     *
     * @param multipartFile
     * @param
     * @param token
     * @return
     */
    @RequestMapping(value = "/userInfo/upLoadHeadPic", method = RequestMethod.POST)
    public ResponseData uploadHeadPic(@RequestParam("file") MultipartFile multipartFile, String token, HttpServletRequest request) {//入参代表上传的文件

        ResponseData responseData = null;
        System.out.println("文件名： " + multipartFile.getOriginalFilename());
        //System.out.println("用户名 ：" + username);
        String src = null;

        UserInfo userinfo = new UserInfo();
        userinfo.setToken(token);
        //userinfo.setUserId(userId);


        //上传的地址
        String upLoadPath = "C:\\ClassSign\\UserHeadPicSrc\\";
        Date a = new Date();

//        token 解密验证
        if (token == null || token.equals("")) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("content", "token值不合法");
            return responseData;
        }

        User user = tokenDecipherting.tokenDecipherting(token);
        if (user == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，token解析失败！");
            return responseData;
        }
        userinfo.setUserId(user.getUserId());
        UserToken userToken = new UserToken();
        userToken.setUserId(user.getUserId());
        userToken.setToken(user.getToken());


        //先查询该用户是否已经设置过个人信息
        int rec = userInfoServiceImpl.getUserInfoRec(user);
        if (rec == 0) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "请先设置个人信息，再设置头像");
            responseData.putDataValue("newToken", userToken);
            return responseData;
        }


        //0，判断是否为空
        if (multipartFile != null && !multipartFile.isEmpty()) {
            /**
             * 对文件名进行操作防止文件重名
             */
            //1，获取原始文件名
            String originalFilename = multipartFile.getOriginalFilename();

            System.out.println("originalFilename : " + originalFilename);
            //2,截取源文件的文件名前缀,不带后缀
            String fileNamePrefix = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            System.out.println("fileNamePrefix : " + fileNamePrefix);
            //3,加工处理文件名，原文件加上用户名和时间戳
            String newFileNamePrefix = userinfo.getUserId() + "-" + fileNamePrefix + System.currentTimeMillis();
            System.out.println("newFileNamePrefix : " + newFileNamePrefix);
            //4,得到新文件名
            String newFileName = newFileNamePrefix + originalFilename.substring(originalFilename.lastIndexOf("."));
            System.out.println("newFileName : " + newFileName);

            //5,构建文件对象
            File file = new File(upLoadPath + newFileName);
            //6,执行上传操作
            try {
                multipartFile.transferTo(file);

                src = newFileName; //用于返回
                //将图片地址存入数据库保存
                userinfo.setUserHeadSrc(newFileName);

                int result = 0;
                //result = userInfoControllerService.updateInfoPart(userinfo);
                result = userInfoServiceImpl.updateUserInfo(userinfo);


                if (result == 1) {
                    responseData = ResponseData.ok();
                    responseData.putDataValue("data", PictureLink.userHeadPicLink + src);
                    responseData.putDataValue("newToken", userToken);
                    return responseData;
                } else {
                    responseData = ResponseData.serverInternalError();
                    responseData.putDataValue("data", "服务器错误，文件上传失败！");
                    responseData.putDataValue("newToken", userToken);
                    return responseData;
                }

            } catch (IOException e) {

                e.printStackTrace();
                responseData = ResponseData.serverInternalError();
                responseData.putDataValue("data", "服务器错误，文件上传失败！");
                responseData.putDataValue("newToken", userToken);
                return responseData;
            }
        } else {
            responseData = ResponseData.notFound();
            responseData.putDataValue("data", "没有找到上传的文件");
            responseData.putDataValue("newToken", userToken);
            return responseData;
        }
    }


    /**
     * 根据用户Id 获取头像地址 ：通过测试
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userInfo/getHeadSrc", method = RequestMethod.GET)
    public String getUserHeadLinkAndNickByUserId(@RequestParam(value = "userId", defaultValue = "0") Long userId) {
        if (userId != 0) {


            String headSrc = userInfoServiceImpl.getUserHeadSrc(userId);

            if (headSrc != null) {
                headSrc = PictureLink.userHeadPicLink + headSrc;
                return headSrc;
            }
            return null;

        } else {
            System.out.println("userId 为 0  无法获取用户头像和昵称");
            return null;
        }

    }


    /**
     * 上传个人信息
     * 通过测试
     *
     * @param userInfo
     * @return
     */
    @PostMapping(value = "/userInfo/upload")
    public ResponseData uploadUserInfo(@RequestBody UserInfo userInfo) {

        ResponseData responseData = null;
        //token 解密验证
        if (userInfo.getToken() == null || userInfo.getToken().equals("")) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "token值不合法");
            return responseData;
        }

        User user = tokenDecipherting.tokenDecipherting(userInfo.getToken());
        if (user == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，token解析失败！");
            return responseData;
        }

        UserToken userToken = new UserToken();
        userToken.setUserId(user.getUserId());
        userToken.setToken(user.getToken());

        int result = 0;
        result = userInfoServiceImpl.uploadUserInfo(userInfo);

        if (result == 1) {
            responseData = ResponseData.ok();
            responseData.putDataValue("data", "个人数据上传成功！");
            responseData.putDataValue("newToken", userToken);
            return responseData;

        } else {
            responseData = ResponseData.serverInternalError();
            responseData.putDataValue("data", "个人数据上传失败！");
            responseData.putDataValue("newToken", userToken);
            return responseData;
        }


    }


    /**
     * 更新用户信息
     *
     * @param userInfo
     * @return
     */
    @PostMapping(value = "/userinfo/replace")
    public ResponseData updateUserInfo(@RequestBody UserInfo userInfo) {


        ResponseData responseData = null;
        //token 解密验证
        if (userInfo.getToken() == null || userInfo.getToken().equals("")) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "token值不合法");
            return responseData;
        }

        User user = tokenDecipherting.tokenDecipherting(userInfo.getToken());
        if (user == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("data", "登陆失败，token解析失败！");
            return responseData;
        }

        UserToken userToken = new UserToken();
        userToken.setUserId(user.getUserId());
        userToken.setToken(user.getToken());

        userInfo.setUserId(user.getUserId());
        //判断必须字段 是否有空
        if (userInfo.getUserName() == null || userInfo.getUserSchool() == null) {
            responseData = ResponseData.badRequest();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "姓名和学校不能为NULL");
            return responseData;
        }


        int result = 0;
        User user1 = new User();  // 要修改的数据
        user1.setUserSchool(userInfo.getUserSchool());
        user1.setUserName(userInfo.getUserName());

        if (userInfo.getUserNo() != null && !userInfo.getUserNo().equals("")) {
            user1.setUserNo(userInfo.getUserNo());
        }
        user1.setUserId(user.getUserId());

        result = userService.updateByPrimaryKey(user1);

        if (result == 0) {
            responseData = ResponseData.serverInternalError();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "服务器出现错误，User表数据无法更新");
            return responseData;
        }

        System.out.println(userInfo.getUserGrade());
        result = userInfoServiceImpl.updateSelective(userInfo);

        if (result == 0) {
            responseData = ResponseData.serverInternalError();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "服务器出现错误，UserInfo表数据无法更新");
            return responseData;
        } else {
            responseData = ResponseData.ok();
            responseData.putDataValue("newToken", userToken);
            responseData.putDataValue("data", "数据更新成功");
            return responseData;
        }


    }

}
