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
package org.optaplanner.curriculumcourse.special_content;

import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/TeacherCoursesEdit")
public class TeacherCoursesEdit extends HttpServlet{

    /**
     * Parametre olara gönderilen öğretim elemanının derslerini getirir.
     * Ek olarak hocası atanmamış dersleri getirerek bunlara hocanın
     * atanmasını sağlar.
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
        Long teacherId = Long.parseLong(req.getParameter("teacherId"));
        TeacherDao tDao = new TeacherDao(em);
        CourseDao cDao = new CourseDao(em);
        
        Teacher teacher = tDao.find(Teacher.class, teacherId);
        List<Course> teacherCourseList = cDao.findCourseByTeacher(teacherId);
        List<Course> noTeacherCourseList = cDao.findTeacherIsNull();
        
        req.setAttribute("teacher", teacher);
        req.setAttribute("teacherCourseList", teacherCourseList);
        req.setAttribute("noteacherCourseList", noTeacherCourseList);
        
        RequestDispatcher rd = req.getRequestDispatcher("teacherCourse.jsp");
        rd.forward(req, resp);
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    
}
