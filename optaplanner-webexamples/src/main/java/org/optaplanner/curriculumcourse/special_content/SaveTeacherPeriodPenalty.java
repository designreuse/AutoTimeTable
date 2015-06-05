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
import java.util.Enumeration;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.Message;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.curriculumcourse.dao.UnavailablePeriodPenaltyDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/SaveTeacherPeriodPenalty")
public class SaveTeacherPeriodPenalty extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message m = new Message();
        Long teacherId = Long.parseLong(req.getParameter("teacherId"));
        try {
            req.setCharacterEncoding("utf-8");
            EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
            TeacherDao tDao = new TeacherDao(em);
            UnavailablePeriodPenaltyDao uvpDao = new UnavailablePeriodPenaltyDao(em);
            CourseDao cDao = new CourseDao(em);
            DayDao dDao = new DayDao(em);
            TimeslotDao tsDao = new TimeslotDao(em);
            PeriodDao pDao = new PeriodDao(em);
            int timeSlotSize = tsDao.findAll().size();
            int daySize = dDao.findAll().size();
            Teacher teacher = tDao.find(Teacher.class, teacherId);
            List<Course> teacherCourseList = cDao.findCourseByTeacher(teacherId);
            for (int dayIndex = 0; dayIndex < daySize; dayIndex++) {
                for (int timeSlotIndex = 0; timeSlotIndex < timeSlotSize; timeSlotIndex++) {
                    String parameter = "isDaysPeriodPenalty-" + dayIndex + "-" + timeSlotIndex;
                    System.out.println(parameter);
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
                                uvpDao.delete(UnavailablePeriodPenalty.class, unvPenalty.getId());
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
            m.setResult(true);
            m.setContent("Değişikler başarıyla kaydedildi.");
        }catch (Exception e) {
            e.printStackTrace();
            m.setResult(false);
            m.setContent("Bir sorun oluştu");
        }
        req.setAttribute("message", m);
        RequestDispatcher rd = req.getRequestDispatcher("TeacherPenaltyPeriods?teacherId=" + teacherId);
        rd.forward(req, resp);
    }

}
