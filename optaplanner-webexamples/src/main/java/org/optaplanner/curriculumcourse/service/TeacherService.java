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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.dao.TeacherDegreeDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.curriculumcourse.dao.UnavailablePeriodPenaltyDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import static org.optaplanner.examples.curriculumcourse.domain.Day.WEEKDAYS;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.TeacherDegree;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;

/**
 *
 * @author gurhan
 */
public class TeacherService extends GenericServiceImpl<Teacher> implements TeacherSpecialContent {

    private final TeacherDegreeDao tdDao;
    private final TeacherDao tDao;
    private final CourseDao cDao;
    private final DayDao dDao;
    private final TimeslotDao tsDao;
    private final UnavailablePeriodPenaltyDao uvpDao;
    private final PeriodDao pDao;

    public TeacherService(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        super(req, resp, Teacher.class);
        tdDao = new TeacherDegreeDao(em);
        tDao = new TeacherDao(em);
        cDao = new CourseDao(em);
        dDao = new DayDao(em);
        tsDao = new TimeslotDao(em);
        uvpDao = new UnavailablePeriodPenaltyDao(em);
        pDao = new PeriodDao(em);
    }

    @Override
    public void editSave() {
        try {
            req.setCharacterEncoding("utf-8");
            Teacher teacher;
            String teacherId = req.getParameter("teacherId");
            TeacherDegree tDegree = tdDao.findByShortName(req.getParameter("teacherDegree"));
            if (teacherId == null || !teacherId.trim().equals("")) {
                teacher = new Teacher();
            } else {
                teacher = tDao.find(teacherId);
            }
            changeTeacherInfo(teacher, tDegree, req);
            tDao.createOrUpdate(teacher);
            message.setResult(true);
            message.setContent("Değişikler başarıyla kaydedildi");

        } catch (Exception ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
            message.setResult(false);
            message.setContent("Bir sorun oluştu");
        }
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("TeachersViewServlet");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void allTeachersList() throws ServletException, IOException {
        List<Teacher> teachers = tDao.findAll();
        List<TeacherDegree> tDegrees = tdDao.findAll();
        req.setAttribute("teacherList", teachers);
        req.setAttribute("degreeList", tDegrees);
        RequestDispatcher rd = req.getRequestDispatcher("editTeacher.jsp");
        rd.forward(req, resp);
    }

    public void teacherInfo() throws ServletException, IOException {
        String id = req.getParameter("teacherId");
        req.setAttribute("teacher", tDao.find(id));
        req.setAttribute("degreeList", tdDao.findAll());
        RequestDispatcher rd = req.getRequestDispatcher("teacherChangeInfo.jsp");
        rd.forward(req, resp);
    }

    private void changeTeacherInfo(Teacher teacher, TeacherDegree tDegree, HttpServletRequest req) {
        teacher.setDegree(tDegree);
        teacher.setCode(req.getParameter("teacherCode"));
        teacher.setName(req.getParameter("teacherName"));
        teacher.setSurname(req.getParameter("teacherSurname"));

    }

    @Override
    public void assignTheLesson() {
        Long teacherId = Long.parseLong(req.getParameter("teacherId"));
        Long courseId = Long.parseLong(req.getParameter("courseId"));
        try {
            Teacher teacher = tDao.find(teacherId);
            Course course = cDao.find(courseId);
            course.setTeacher(teacher);
            cDao.update(course);
            message.setResult(true);
            message.setContent("Değişiklikler başarıyla kaydedildi");
        } catch (Exception e) {
            e.printStackTrace();
            message.setResult(false);
            message.setContent("Bir sorun oluştu.");
        }
        RequestDispatcher rd = req.getRequestDispatcher("TeacherCoursesEdit");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeAssigmentTheLesson() {

    }

    @Override
    public void getTeacherPeriodPenalty() {
        Long teacherId = Long.parseLong(req.getParameter("teacherId"));
        Teacher teacher = tDao.find(teacherId);
        List<Course> teacherCourseList = cDao.findCourseByTeacher(teacherId);
        List<Day> dayList = dDao.findAll();
        List<Timeslot> timeslotList = tsDao.findAll();

        boolean[][] periodIsPenaltyArray = new boolean[dayList.size()][timeslotList.size()];
        for (int i = 0; i < dayList.size(); i++) {
            for (int j = 0; j < timeslotList.size(); j++) {
                periodIsPenaltyArray[i][j] = false;
            }
        }
        for (Course course : teacherCourseList) {
            List<UnavailablePeriodPenalty> courseUvpList = uvpDao.findUvpByCourse(course.getId());
            for (UnavailablePeriodPenalty uvp : courseUvpList) {
                int dayIndex = uvp.getPeriod().getDay().getDayIndex();
                int timeslotIndex = uvp.getPeriod().getTimeslot().getTimeslotIndex();
                periodIsPenaltyArray[dayIndex][timeslotIndex] = true;
            }
        }

        req.setAttribute("dayNames", WEEKDAYS);
        req.setAttribute("timeslotList", timeslotList);
        req.setAttribute("dayList", dayList);
        req.setAttribute("periodIsPenaltyArray", periodIsPenaltyArray);
        req.setAttribute("teacher", teacher);

        RequestDispatcher rd = req.getRequestDispatcher("teacherPenaltyPeriodEdit.jsp");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveTeacherPeriodPenalty() {
        Long teacherId = Long.parseLong(req.getParameter("teacherId"));
        try {

            int timeSlotSize = tsDao.findAll().size();
            int daySize = dDao.findAll().size();
            Teacher teacher = tDao.find(teacherId);
            List<Course> teacherCourseList = cDao.findCourseByTeacher(teacherId);
            for (int dayIndex = 0; dayIndex < daySize; dayIndex++) {
                for (int timeSlotIndex = 0; timeSlotIndex < timeSlotSize; timeSlotIndex++) {
                    String parameter = "isDaysPeriodPenalty-" + dayIndex + "-" + timeSlotIndex;
                    String value = req.getParameter("isDaysPeriodPenalty-" + dayIndex + "-" + timeSlotIndex);
                    /**
                     * Periyot işaretlenmemiş ise hocanın girdği dersleri unv
                     * den kontrol et eğer deger dönerse değeri sil dönmezse bir
                     * şey yapma
                     */
                    if (value == null) {
                        for (Course course : teacherCourseList) {
                            UnavailablePeriodPenalty unvPenalty = uvpDao.findUvpByCourseAndPeriod(course.getId(), dayIndex, timeSlotIndex);
                            if (unvPenalty != null) {
                                uvpDao.delete(unvPenalty.getId());
                            }
                        }
                    } else if (value != null) {
                        for (Course course : teacherCourseList) {
                            UnavailablePeriodPenalty unvPenalty = uvpDao.findUvpByCourseAndPeriod(course.getId(), dayIndex, timeSlotIndex);
                            if (unvPenalty == null) {
                                unvPenalty = new UnavailablePeriodPenalty();
                                unvPenalty.setCourse(course);
                                unvPenalty.setPeriod(pDao.findPeriodByIndexes(dayIndex, timeSlotIndex));
                                uvpDao.save(unvPenalty);
                            }
                        }
                    }
                }
            }
            message.setResult(true);
            message.setContent("Değişikler başarıyla kaydedildi.");
        } catch (Exception e) {
            e.printStackTrace();
            message.setResult(false);
            message.setContent("Bir sorun oluştu");
        }
        req.setAttribute("message", message);

        RequestDispatcher rd = req.getRequestDispatcher("TeacherPenaltyPeriods?teacherId=" + teacherId);
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void teacherCoursesEdit() {
        Long teacherId = Long.parseLong(req.getParameter("teacherId"));

        Teacher teacher = tDao.find(teacherId);
        List<Course> teacherCourseList = cDao.findCourseByTeacher(teacherId);
        List<Course> noTeacherCourseList = cDao.findTeacherIsNull();

        req.setAttribute("teacher", teacher);
        req.setAttribute("teacherCourseList", teacherCourseList);
        req.setAttribute("noteacherCourseList", noTeacherCourseList);

        RequestDispatcher rd = req.getRequestDispatcher("teacherCourse.jsp");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
