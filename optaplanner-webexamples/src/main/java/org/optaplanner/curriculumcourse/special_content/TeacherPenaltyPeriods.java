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
import java.util.ArrayList;
import java.util.Arrays;
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
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.curriculumcourse.dao.UnavailablePeriodPenaltyDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;
import static org.optaplanner.examples.curriculumcourse.domain.Day.WEEKDAYS;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/TeacherPenaltyPeriods")
public class TeacherPenaltyPeriods extends HttpServlet {

    /**
     * Herhangi bir öğretmenin penalty periyotlarını getirir. Parametre olarak
     * gönderilen teacherId'yi alır bu teacherId ye göre öğretim görevlisinin
     * girdiği dersleri getirir ve bu derslerin unvaliablepenalty tablo içinden
     * yasak oldukları periyotları bulur.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("utf-8");
            Long teacherId = Long.parseLong(req.getParameter("teacherId"));
            EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
            TeacherDao tDao = new TeacherDao(em);
            UnavailablePeriodPenaltyDao uvpDao = new UnavailablePeriodPenaltyDao(em);
            CourseDao cDao = new CourseDao(em);
            DayDao dDao = new DayDao(em);
            TimeslotDao tsDao = new TimeslotDao(em);
            Teacher teacher = tDao.find(Teacher.class, teacherId);
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestDispatcher rd = req.getRequestDispatcher("teacherPenaltyPeriodEdit.jsp");
        rd.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    

}
