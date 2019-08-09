package com.qinxy.utils;

import com.alibaba.fastjson.JSONObject;
import com.qinxy.common.Constants;
import com.qinxy.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.function.Function;

/**
 * Created by qinxy on 2019/7/2.
 */
@Component
public class JWTUtil {

    @Value("${jwt.secret:h#l%j@10}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    public String generateToken(User user) {
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim("roles", "user")
                // 用户名写入标题
                .setSubject(JSONObject.toJSONString(user))
                // 有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return JWT;
    }

    public String getUserInfoFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public User getUser(HttpServletRequest request) {
        String token = request.getHeader(Constants.AUTHORIZATION);
        if(StringUtils.isBlank(token)){
            return null;
        }
        String userInfo = null;
        try {
            userInfo = getUserInfoFromToken(token);
        } catch (ExpiredJwtException jwtException) {
            logger.error("token过期:{}", jwtException.getMessage());
        }
        if (StringUtils.isNotBlank(userInfo)) {
            return JSONObject.parseObject(userInfo, User.class);
        }
        return null;
    }

    public Boolean validateToken(String token) {
        String userInfo;
        try {
            userInfo = getUserInfoFromToken(token);
        } catch (ExpiredJwtException jwtException) {
            logger.error("token过期:{}", jwtException.getMessage());
            return false;
        }

        if (StringUtils.isBlank(userInfo)) {
            return false;
        }
        User user = JSONObject.parseObject(userInfo, User.class);
        if (user != null && StringUtils.isBlank(user.getUsername())) {
            return false;
        }
        return true;
    }
}
