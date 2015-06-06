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
package org.optaplanner.curriculumcourse.edit_view;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.Message;
import org.optaplanner.curriculumcourse.dao.CurriculumDao;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CurriculumEditSaveServlet")
public class CurriculumEditSaveServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        Message message = new Message();
        EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
        CurriculumDao cDao = new CurriculumDao(em);
        String id = req.getParameter("curriculumId");
        Curriculum curricula; 
        if (id == null || id.trim().equals("")) {
            curricula = new Curriculum();
        } else {
            curricula = cDao.find(Curriculum.class, Long.parseLong(id));
        }
        try {
            curricula.setCode(req.getParameter("curriculumCode"));
            cDao.createOrUpdate(curricula);
            message.setContent("Değişikler başarıyla kaydedildi.");
            message.setResult(true);
        } catch(Exception e) {
            e.printStackTrace();
            message.setContent("Bir sorun oluştu.");
            message.setResult(false);
        }
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("CurriculumsViewServlet");
        rd.forward(req, resp);
    }
    
}
