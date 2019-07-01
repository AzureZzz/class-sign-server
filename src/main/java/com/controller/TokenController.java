package com.controller;


import com.pojo.User;
import com.utils.ResponseData;
import com.utils.TokenDecipherting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenDecipherting tokenDecipherting;

    /**
     * 验证token是否过期 ： 通过验证
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/verify")
    public ResponseData verifyToken(@RequestBody User user) {

        ResponseData responseData = null;
        User user1 = null;

        if (user.getToken() == null) {
            responseData = ResponseData.customerError();
            responseData.putDataValue("data", "错误！缺少token！");
            return responseData;
        } else {
            //解码
            user1 = tokenDecipherting.tokenDecipherting(user.getToken());
            if (user1 == null) {
                responseData = ResponseData.badRequest();
                responseData.putDataValue("data", "token过期，解析失败！");
                return responseData;
            } else {
                responseData = ResponseData.ok();
                responseData.putDataValue("data", "token有效，解析成功！");
                return responseData;
            }

        }

    }
}
