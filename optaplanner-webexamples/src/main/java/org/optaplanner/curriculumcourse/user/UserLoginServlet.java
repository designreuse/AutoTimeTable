/*
 * Copyright 2015 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.optaplanner.curriculumcourse.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author gurhan
 */
@WebServlet("/UserLoginServlet")
public class UserLoginServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        req.setCharacterEncoding("utf-8");
        
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        
        try {
            currentUser.login(token);
            if (currentUser.hasRole("admin")) {
                resp.sendRedirect("curriculumcourse/index.jsp");
            } else if(currentUser.hasRole("guest")) {
                resp.sendRedirect("guest/index.jsp");
            }
            System.out.println("Giriş Başarıyla Yapıldı");
        }catch (UnknownAccountException e){
            System.out.println("Kullanıcı Adı Bulunamadı");
        }catch ( IncorrectCredentialsException e) {
            System.out.println("Parola Yanlış");
        } catch (AuthenticationException e) {
            System.out.println(e.toString());
        }
    }
    
}
