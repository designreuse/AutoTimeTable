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
package org.optaplanner.curriculumcourse;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.optaplanner.curriculumcourse.dao.CourseScheduleDao;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CurriculumCourseDeleteServlet")
public class CurriculumCourseDeleteServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("utf-8");
        
        Long solutionId = Long.parseLong(req.getParameter("id"));
        EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
        CourseScheduleDao csDao = new CourseScheduleDao(em);
        csDao.delete(CourseSchedule.class, solutionId);
        resp.sendRedirect("index.jsp");
    }
    
}
