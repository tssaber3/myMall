package com.tssaber.mmall.util;

import com.tssaber.mmall.common.CExceptionEnums;
import com.tssaber.mmall.exception.GlobalException;
import com.tssaber.mmall.security.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description:
 * @author: tssaber
 * @time: 2019/11/26 0026 22:19
 */
@Component
public class JwtTokenUtil {

    private final static Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    private static final String JWT_TOKEN_SECRET = "shiyu";

    private static final long TOKEN_EXPIRED_MS = 3600 * 24 * 2 * 1000;


    /**
     * 生成token
     * @param user
     * @return
     */
    public static String createToken(JwtUser user){
        String username = user.getUsername();
        //过期时间
        Date expireTime = new Date(System.currentTimeMillis() + TOKEN_EXPIRED_MS);

        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(expireTime)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512,JWT_TOKEN_SECRET)
                .compact();
        return token;
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static boolean verifyToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(JWT_TOKEN_SECRET)
                    .parseClaimsJws(token);
            return true;
        }catch (Exception ex){
            log.error("error token {}",ex.getMessage());
            throw new GlobalException(CExceptionEnums.TOKEN_ERROR);
        }
    }

    /**
     * 获取token中的信息 并且以Claims的形式返回
     * @param token
     * @return
     */
    public static Claims parseJWT(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        }catch (Exception e){
            log.error("error token:{}",e.getMessage());
            return null;
        }
    }

    public static String getUsername(String token){
        Claims claims = parseJWT(token);
        String username;
        try {
            username = claims.getSubject();
        }catch (Exception e){
            log.error("解析错误:{}",e.getMessage());
            return null;
        }
        return username;
    }
}
