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
package org.optaplanner.curriculumcourse.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.Message;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.CurriculumDao;
import org.optaplanner.curriculumcourse.dao.LectureDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;

/**
 *
 * @author gurhan
 */
public class CourseService extends GenericServiceImpl<Course> {

    private TeacherDao tDao;
    private CourseDao cDao;
    private CurriculumDao ccDao;
    private LectureDao lDao;
    private Message message;

    public CourseService(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        super(req, resp, Course.class);
        tDao = new TeacherDao(em);
        cDao = new CourseDao(em);
        ccDao = new CurriculumDao(em);
        lDao = new LectureDao(em);
        message = new Message();
    }

    public void allCourseList() throws ServletException, IOException {
        req.setAttribute("courseList", cDao.findAll());
        req.setAttribute("teacherList", tDao.findAll());
        req.setAttribute("curriculumList", ccDao.findAll());
        RequestDispatcher rd = req.getRequestDispatcher("editCourse.jsp");
        rd.forward(req, resp);
    }

    public void courseInfo() throws ServletException, IOException {
        String id = req.getParameter("courseId");
        req.setAttribute("course", cDao.find(id));
        req.setAttribute("teacherList", tDao.findAll());
        req.setAttribute("curriculumList", ccDao.findAll());
        RequestDispatcher rd = req.getRequestDispatcher("courseInfoEdit.jsp");
        rd.forward(req, resp);
    }

    @Override
    public void editSave() {
        String id = req.getParameter("courseId");
        String teacherId = req.getParameter("courseTeacher");
        String curriculumId = req.getParameter("courseCurriculum");
        Course course;
        if (id == null || id.trim().equals("")) {
            course = new Course();
        } else {
            course = cDao.find(id);
        }

        List<Curriculum> addedCurriculum = new ArrayList<Curriculum>();
        addedCurriculum.add(ccDao.find(curriculumId));
        course.setTeacher(tDao.find(teacherId));
        course.setCurriculumList(addedCurriculum);
        changeCourseInfo(course, req);
        if (id == null) {
            createLectures(course);
        }
        try {
            course = cDao.createOrUpdate(course);
            message.setContent("Değişiklikler kaydedildi");
            message.setResult(true);
            
        } catch (Exception ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            message.setContent("Bir sorun oluştu");
            message.setResult(false);
        }
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("CoursesViewServlet");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void changeCourseInfo(Course course, HttpServletRequest req) {
        String studentSize = req.getParameter("courseStudentSize").trim();
        String lectureSize = req.getParameter("courseLectureSize").trim();
        course.setCode(req.getParameter("courseCode"));
        course.setName(req.getParameter("courseName"));
        course.setLectureSize(Integer.parseInt(lectureSize));
        course.setStudentSize(Integer.parseInt(studentSize));
    }

    public void createLectures(Course course) {
        for (int i = 0; i < course.getLectureSize(); i++) {
            Lecture lecture = new Lecture();
            lecture.setCourse(course);
            lecture.setLectureIndexInCourse(i);
            lDao.createOrUpdate(lecture);
        }
    }

}
