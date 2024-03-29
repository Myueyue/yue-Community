package com.yue.community.controller;

import com.yue.community.dto.AccessTokenDTO;
import com.yue.community.dto.GithubUser;
import com.yue.community.mapper.UserMapper;
import com.yue.community.model.User;
import com.yue.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

     @Autowired
    private GithubProvider githubProvider;
//     读取配置文件中的值注入
      @Value("${github.client.id}")
     private String clientId;
      @Value("${github.client.secret}")
     private String clientSecret;
      @Value("${github.redirect.uri}")
     private String redirectUri;

      @Autowired
      private UserMapper userMapper;



    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);

        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null && githubUser.getId()!=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            //登录成功，写入cookie

           response.addCookie(new Cookie("token",token));
           return "redirect:/";

        }else {
            //登录失败，重新登录
            return "redirect:/";
        }

    }

}
