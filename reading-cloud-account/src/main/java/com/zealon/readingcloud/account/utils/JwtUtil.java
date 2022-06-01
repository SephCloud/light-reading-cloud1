package com.zealon.readingcloud.account.utils;

import com.zealon.readingcloud.account.vo.UserVO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import static com.zealon.readingcloud.common.constant.JwtConstant.SECRET_KEY;
import java.util.Date;

/**
 * JWT工具
 * @author hasee
 */
public class JwtUtil {

    /**
     * 构建JWT对象
     * @param expire
     * @param user
     * @return
     */
    public static String buildJwt(Date expire, UserVO user) {
        String jwt = Jwts.builder()
                // 使用HS256加密算法
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                // 过期时间
                .setExpiration(expire)
                .claim("loginName",user.getLoginName())
                .claim("nickName",user.getNickName())
                .claim("phoneNumber",user.getPhoneNumber())
                .claim("headImgUrl",user.getHeadImgUrl())
                .claim("uuid",user.getUuid())
                .claim("id",user.getId())
                .compact();
        return jwt;
    }


}
