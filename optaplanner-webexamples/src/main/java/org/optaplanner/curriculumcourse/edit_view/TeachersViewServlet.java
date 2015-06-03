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
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
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
@WebServlet("/curriculumcourse/TeachersViewServlet")
public class TeachersViewServlet extends HttpServlet {

    private TeacherDao tDao;
    private TeacherDegreeDao tdDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
            tDao = new TeacherDao(em);
            tdDao = new TeacherDegreeDao(em);
            List<Teacher> allTeachers = tDao.findTeachers(null);
            List<TeacherDegree> allDegree = tdDao.findAll();
            req.setAttribute("teacherList", allTeachers);
            req.setAttribute("degreeList", allDegree);
            RequestDispatcher rd = req.getRequestDispatcher("editTeacher.jsp");
            rd.forward(req, resp);
    }
}
