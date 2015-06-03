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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.dao.TeacherDegreeDao;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.TeacherDegree;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/TeacherEditSaveServlet")
public class TeacherEditSaveServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
        TeacherDegreeDao tdDao = new TeacherDegreeDao(em);
        TeacherDao tDao = new TeacherDao(em);
        Teacher teacher;
        String id = req.getParameter("teacherId");
        TeacherDegree degre = tdDao.findByShortName(req.getParameter("teacherDegree"));
        if (id == null || id.trim().equals("")) {
            teacher = new Teacher();

        } else {
            teacher = tDao.find(Teacher.class, Long.parseLong(id));
        }

        try {
            teacher.setDegree(degre);
            teacher.setCode(req.getParameter("teacherCode"));
            teacher.setName(req.getParameter("teacherName"));
            teacher.setSurname(req.getParameter("teacherSurname"));
            tDao.createOrUpdate(teacher);
        } catch (Exception e) {
            System.out.println("Eklenemedi");
            e.printStackTrace();
        }
        resp.sendRedirect("TeachersViewServlet");

    }

}
