package com.Jwt;


import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


public class Jwt {

    //    private static final String SECRET = "XX#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";    //加密所用的乱码
    private static final String SECRET = "XX#$%()(#*!()w!KLe<><Mdf>?Nsww<:{LWPWa///7%%%*4%%^12%$$##$";    //加密所用的乱码
    private static final String EXP = "exp";
    private static final String PAYLOAD = "payload";


    //加密，传入一个对象和有效期
    public static <T> String sign(T object, long maxAge) {
        try {
            final JWTSigner signer = new JWTSigner(SECRET);
            final Map<String, Object> claims = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();
            //使用jackon的ObjectMapper的writeValueAsString方法可以把java对象转化成json字符串
            String jsonString = mapper.writeValueAsString(object);
            claims.put(PAYLOAD, jsonString);
//            System.out.println("System.currentTimeMillis() : "+System.currentTimeMillis());
            //获取当前系统毫秒数: System.currentTimeMillis()
            claims.put(EXP, System.currentTimeMillis() + maxAge);
            return signer.sign(claims);
        } catch (Exception e) {
            return null;
        }
    }


    //解密，传入一个加密后的token字符串和解密后的类型
    public static <T> T unsign(String jwt, Class<T> classT) {
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        try {
            final Map<String, Object> claims = verifier.verify(jwt);

            if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {

                long exp = (Long) claims.get(EXP);

                long currentTimeMillis = System.currentTimeMillis();

                if (exp > currentTimeMillis) {

                    String json = (String) claims.get(PAYLOAD);
                    ObjectMapper objectMapper = new ObjectMapper();

                    return objectMapper.readValue(json, classT);

                }
            }

            return null;
        } catch (Exception e) {

            return null;
        }
    }

}
