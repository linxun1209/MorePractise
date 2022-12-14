package com.xingchen.config;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xingchen.pojo.JwtUser;
import com.xingchen.service.impl.JwtUserDetailsService;
import com.xingchen.utils.JwtTokenUtil;
import com.xingchen.utils.RedisUtil;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * @author Li
 * @Date 2022/7/23 1:08
 */


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("token");
        if(requestTokenHeader!=null&&redisUtil.hasKey("user@Limit:Token")&&redisUtil.sHasKey("user@Limit:Token","token@Limit"+requestTokenHeader)){
            JSONObject res = new JSONObject();
            res.put( "flag", "false");
            res.put( "msg", "Unauthorized");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(res.toJSONString());
            return;
        }
        String username = null;
        String jwtToken = null;
        // JWT????????????????????????"Bearer token". ??????"Bearer ",????????????token
        // ????????????
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }catch (MalformedJwtException e){
                System.out.println("JWT Token is error");
            }catch (SignatureException e){
                System.out.println("Token is ??????");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        // ????????????????????????????????????
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            DecodedJWT decode = JWT.decode(jwtToken);
            Claim id = decode.getClaim("id");
            Claim account = decode.getClaim("account");
            Claim password = decode.getClaim("password");
            Claim auth=decode.getClaim("authorities");
            UserDetails userDetails=new JwtUser(id.asInt(),account.asString(),password.asString(),auth.asMap().get("authority").toString(),true);
//            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
            // ???????????????????????????Spring Security?????????????????????
            // ??????
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // ???????????????????????????????????????????????????
                // ??????????????????????????????????????????????????????
                // Spring Security ???????????????
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
