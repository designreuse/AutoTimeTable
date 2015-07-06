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
package org.optaplanner.curriculumcourse.edit_view.courses;

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
import org.optaplanner.curriculumcourse.dao.CurriculumDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.service.CourseService;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CoursesViewServlet")
public class CoursesViewServlet extends CoursesServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        courseService = new CourseService(req, resp);
        courseService.allCourseList();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    
}
