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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.CurriculumDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CourseEditSaveServlet")
public class CourseEditSaveServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
        String id = req.getParameter("courseId");
        String teacherId = req.getParameter("courseTeacher");
        String curriculumId = req.getParameter("courseCurriculum");
        CourseDao cDao = new CourseDao(em);
        TeacherDao tDao = new TeacherDao(em);
        CurriculumDao ccDao = new CurriculumDao(em);
        Course course;
        if (id == null || id.trim().equals("")) {
            course = new Course();
        } else {
            course = cDao.find(Course.class, Long.parseLong(id));
        }
        List<Curriculum> addedCurriculum = new ArrayList<Curriculum>();
        
        try {
            String studentSize = req.getParameter("courseStudentSize").trim();
            String lectureSize = req.getParameter("courseLectureSize").trim();
            course.setCode(req.getParameter("courseCode"));
            course.setName(req.getParameter("courseName"));
            course.setLectureSize(Integer.parseInt(lectureSize));
            course.setStudentSize(Integer.parseInt(studentSize));
            course.setTeacher(tDao.find(Teacher.class, Long.parseLong(teacherId)));
            addedCurriculum.add(ccDao.find(Curriculum.class, Long.parseLong(curriculumId)));
            course.setCurriculumList(addedCurriculum);
            cDao.createOrUpdate(course);
            
        } catch (Exception e) {
            System.out.println("Course Eklenemedi");
            e.printStackTrace();
        }
        resp.sendRedirect("CoursesViewServlet");
        
    }
    
}
