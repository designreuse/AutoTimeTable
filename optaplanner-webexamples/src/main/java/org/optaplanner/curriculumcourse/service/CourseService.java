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
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Room;
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
    private RoomDao rDao;
    private Message message;

    public CourseService(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        super(req, resp, Course.class);
        tDao = new TeacherDao(em);
        cDao = new CourseDao(em);
        ccDao = new CurriculumDao(em);
        lDao = new LectureDao(em);
        rDao = new RoomDao(em);
        message = new Message();
    }

    public void allCourseList() throws ServletException, IOException {
        req.setAttribute("courseList", cDao.findAll());
        req.setAttribute("teacherList", tDao.findAll());
        req.setAttribute("curriculumList", ccDao.findAll());
        req.setAttribute("roomList", rDao.findAll());
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
        String checkLab = req.getParameter("checkLab");
        Course course;
        if (id == null || id.trim().equals("")) {

            for (Curriculum curriculum : ccDao.findAll()) {
                if (req.getParameter("isSelected-" + curriculum.getCode()) != null) {
                    course = new Course();
                    saveByCourse(course, curriculum, teacherId, id, false);
                }
            }
            if (checkLab != null || !checkLab.trim().equals("")) {
                for (Curriculum curriculum : ccDao.findAll()) {
                    if (req.getParameter("isSelected-" + curriculum.getCode()) != null) {
                        course = new Course();
                        saveByCourse(course, curriculum, teacherId, id, true);
                    }
                }

            }
        } else {
            course = cDao.find(id);
            if (!course.getName().contains("Lab")) {
                saveByCourse(course, ccDao.find(curriculumId), teacherId, id, false);
            } else {
                saveByCourse(course, ccDao.find(curriculumId), teacherId, id, true);
            }

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

    private void saveByCourse(Course course, Curriculum curriculum, String teacherId, String id, boolean isLab) {
        List<Curriculum> addedCurriculum = new ArrayList<Curriculum>();
        addedCurriculum.add(curriculum);
        course.setTeacher(tDao.find(Long.parseLong(teacherId)));
        course.setCurriculumList(addedCurriculum);
        if (isLab == false) {
            changeCourseInfo(course, curriculum, req);
        } else {
            changeLabInfo(course, curriculum, req);
        }

        try {
            course = cDao.createOrUpdate(course);
            if (id == null) {
                createLectures(course);
            }
            String m = message.getContent() + course.getName() + " dersi " + addedCurriculum.get(0).getCode() + " sınıfına eklendi <br />";
            message.setContent(m);
            message.setResult(true);

        } catch (Exception ex) {
            Logger.getLogger(CourseService.class.getName()).log(Level.SEVERE, null, ex);
            String m = message.getContent() + course.getName() + " dersi " + addedCurriculum.get(0).getCode() + " sınıfına eklenemedi <br />";
            message.setContent(m);
            message.setResult(false);
        }
    }

    private void changeCourseInfo(Course course, Curriculum curriculum, HttpServletRequest req) {
        String studentSize = req.getParameter("courseStudentSize").trim();
        String lectureSize = req.getParameter("courseLectureSize").trim();
        course.setCode(req.getParameter("courseCode") + "-" + curriculum.getCode());
        course.setName(req.getParameter("courseName"));
        course.setLectureSize(Integer.parseInt(lectureSize));
        course.setStudentSize(Integer.parseInt(studentSize));
    }

    private void changeLabInfo(Course course, Curriculum curriculum, HttpServletRequest req) {
        String studentSize = req.getParameter("courseStudentSize").trim();
        String lectureSize = req.getParameter("labLectureSize").trim();
        course.setCode(req.getParameter("courseCode") + "-LAB-" + curriculum.getCode());
        course.setName(req.getParameter("courseName") + " Lab");
        course.setLectureSize(Integer.parseInt(lectureSize));
        course.setStudentSize(Integer.parseInt(studentSize));
        List<Room> selectedRooms = null;
        for (Room room : rDao.findAll()) {
            String isRoomSelected = req.getParameter("isSelectedRoom-" + room.getCode());
            if (isRoomSelected != null && !isRoomSelected.trim().equals("")) {
                if(selectedRooms == null) {
                     selectedRooms = new ArrayList<Room>();
                }
                selectedRooms.add(room);
            }
        }
        if (selectedRooms != null && selectedRooms.size() > 0) {
            course.setRoomDeps(selectedRooms);
        }
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
