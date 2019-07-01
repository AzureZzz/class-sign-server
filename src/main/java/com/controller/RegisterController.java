package com.controller;


import com.PhoneUtils.HttpUtil;
import com.pojo.User;
import com.serviceImpl.RegisterServiceImpl;
import com.utils.ResponseData;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//@RestController
@Controller
public class RegisterController {


    /**
     * codeMap ：用于保存验证码
     */
    private static Map<String, Integer> codeMap = new HashMap<>();
    @Autowired
    private RegisterServiceImpl registerService;

    @RequestMapping(value = "/123")
    public String welcome() {
        return "Crud Spring Boot Project ! ";
    }

    @ResponseBody
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    public ResponseData getVerifyCode(@RequestParam("phone") String phone, HttpServletResponse response, HttpServletRequest request) {

        ResponseData responseData = null;
        System.out.println("获取验证码。。。");
        //1.判断验证码缓存里是否已经有该号码的验证码了
        try {
            if (codeMap.get(phone).toString() != null) {

                //代表缓存区里已经存在先前的验证码
                //移除之前的验证码记录
                codeMap.remove(phone);
            }
        } catch (Exception e) {
            System.out.println("该手机号暂时没有接收过验证码。。。。。");
        }

        getNumber(phone);
        responseData = ResponseData.ok();
        responseData.putDataValue("content", "短信验证码已下发！");

        return responseData;

    }


    /**
     * 向用户发送短信验证码的类方法
     *
     * @param phoneNumber ： 传入使用户的手机号码
     */
    private void getNumber(String phoneNumber) {

        int code = 0;

        //获取短信验证码

        String host = "https://api.chanyoo.net";
        String path = "/qcloud_agent";
        String method = "GET";
//        String appcode = "001d695ecd4f4bc3a73612e46e9a4119";
        //随机生成6位验证码
        code = (int) ((Math.random() * 9 + 1) * 100000);
        codeMap.put(phoneNumber, code); //存入codeMap，用于后续校验

        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("secretid", "AKIDh4973YCnnnQb2ujleaj8vkcTzzzIm6qmw5Qq");
        querys.put("secretkey", "33Cr6WzIWt2T0ulvqWLAL8bQYjjnVhlxLod8100k");
        querys.put("mobile", phoneNumber);
        querys.put("content", "您的手机号：" + phoneNumber + "，验证码：" + code + "，请及时完成验证，如不是本人操作请忽略。【腾讯云市场】");

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户注册 : 通过测试
     *
     * @param
     * @return
     */
//    @PostMapping(value = "/user/register")
    @ResponseBody
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResponseData userRegister(@RequestBody User user) {
//
        System.out.println("访问成功。。。。。。。。。。" + user.getUserPhone());

        ResponseData responseData = null;
        int code = 0;
        //数据检测部分
        Map<String, Object> map = new HashMap<>();


//        codeMap.put("13471818975", 000000);
        try {
            code = codeMap.get(user.getUserPhone());
        } catch (Exception e) {
            responseData = ResponseData.notFound();
            responseData.putDataValue("content", "验证码不正确，请重新获取");
            return responseData;
        }


        if (user.getVerifyCode() != code) {
            responseData = ResponseData.notFound();
            responseData.putDataValue("content", "验证码不正确，请重新获取");
            //从map中去除这条记录
            codeMap.remove(user.getUserPhone());
            return responseData;
        } else {

            //密码加密
            String username = user.getUserPhone();
            String password = user.getUserPassword();
            //采用盐值加密
            ByteSource credentialsSalt = ByteSource.Util.bytes(username);
            SimpleHash passMd5 = new SimpleHash("MD5", password, credentialsSalt, 1024);
            user.setUserPassword(passMd5.toHex());

            int result = registerService.userRegister(user);

            if (result == 999) {
                codeMap.remove(user.getUserPhone());
                responseData = ResponseData.forbidden();
                responseData.putDataValue("data", "此手机号已经被注册过！");
                return responseData;
            } else if (result == 1) {
                //注册成功
                //生成token
                responseData = ResponseData.ok();
                responseData.putDataValue("data", "注册成功");
                return responseData;
            } else {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "注册失败");
                return responseData;
            }

        }


    }


}
