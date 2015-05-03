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

import java.io.File;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.CourseScheduleDao;
import org.optaplanner.curriculumcourse.dao.CurriculumDao;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.LectureDao;
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseDao;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CurriculumCourseSaveServlet")
public class CurriculumCourseSaveServlet extends HttpServlet {

    private CourseSchedule solution;
    private TeacherDao teacherDao;
    private CourseDao courseDao;
    private CurriculumDao curriculumDao;
    private DayDao dayDao;
    private TimeslotDao tsDao;
    private CourseScheduleDao csDao;
    private RoomDao roomDao;
    private LectureDao lectureDao;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. Sessiondan solution nesnesini ve requestten changeList
     * parametresini alır.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        EntityManager em = (EntityManager) session.getServletContext().getAttribute("entityManager");
        solution = (CourseSchedule) session.getAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION);
        teacherDao = new TeacherDao(em);
        courseDao = new CourseDao(em);
        curriculumDao = new CurriculumDao(em);
        dayDao = new DayDao(em);
        tsDao = new TimeslotDao(em);
        csDao = new CourseScheduleDao(em);
        roomDao = new RoomDao(em);
        lectureDao = new LectureDao(em);

        String[] changeList = convertToList(request.getParameter("changeList"));
        if (changeList != null && changeList.length > 0) {
            saveChanges(changeList);
        }
        String content = (String) request.getSession().getAttribute("content");
       // String path = request.getServletContext().getRealPath("/") + "export";
        //File file = new File(path + File.separator + content + ".xml");
       // CurriculumCourseDao dao = new CurriculumCourseDao(path);
       //dao.writeSolution(solution, file);

        readySubClasses(solution);
        checkScheduleName(solution);
        csDao.createOrUpdate(solution);

        response.sendRedirect("index.jsp");

    }

    /**
     * Alt sınıflardan idleri çeker.
     *
     * @param solution
     */
    private void readySubClasses(CourseSchedule solution) {

        for (Course c : solution.getCourseList()) {
            Course managedCourse = courseDao.findCourseByCode(c.getCode());
            if (managedCourse != null) {
                c.setId(managedCourse.getId());
            } else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                c.setId(null);
            }

        }

        for (Teacher t : solution.getTeacherList()) {
            Teacher managedTeacher = teacherDao.findTeacherByCode(t.getCode());
            if (managedTeacher != null) {
                t.setId(managedTeacher.getId());
            }else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                t.setId(null);
            }
        }

        for (Curriculum c : solution.getCurriculumList()) {
            Curriculum managedCurriculum = curriculumDao.findCurriculumByCode(c.getCode());
            if (managedCurriculum != null) {
                c.setId(managedCurriculum.getId());
            }else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                c.setId(null);
            }
        }

        for (Day d : solution.getDayList()) {
            Day managedDay = dayDao.findDayByIndex(d.getDayIndex());
            if (managedDay != null) {
                d.setId(managedDay.getId());
            }else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                d.setId(null);
            }
        }

        for (Timeslot t : solution.getTimeslotList()) {
            Timeslot managetTimeslot = tsDao.findTimeslotByIndex(t.getTimeslotIndex());
            if (managetTimeslot != null) {
                t.setId(managetTimeslot.getId());
            }else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                t.setId(null);
            }
        }

        for (Room r : solution.getRoomList()) {
            Room managedRoom = roomDao.findRoomByCode(r.getCode());
            if (managedRoom != null) {
                System.out.println(managedRoom.getCode() + "->" + managedRoom.getId());
                r.setId(managedRoom.getId());
            }else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                r.setId(null);
            }
        }
        for (Lecture l : solution.getLectureList()) {
            l.setId(null);
        }
        for(UnavailablePeriodPenalty up : solution.getUnavailablePeriodPenaltyList()) {
            up.setId(null);
        }
    }

    private void checkScheduleName(CourseSchedule solution) {
        String name = solution.getName();
        int index = csDao.nextCourseScheduleNameIndex(name);
        solution.setName(name + " - " + index);
    }

    /**
     * Kendisine gönderilen listedeki değişikleri solution nesnesine kaydeder.
     *
     * @param changeList
     */
    private void saveChanges(String[] changeList) {
        for (String s : changeList) {
            String[] args = s.split(":");
            String course = args[0];
            int lectureIndexInCourse = Integer.parseInt(args[1]);

            String[] timeArgs = args[2].split("x");
            Day day = new Day();
            int dayIndex = Integer.parseInt(timeArgs[0]);
            day.setDayIndex(dayIndex);

            Timeslot timeSlot = new Timeslot();
            int timeSlotIndex = Integer.parseInt(timeArgs[1]);
            timeSlot.setTimeslotIndex(timeSlotIndex);

            Period period = new Period();
            period.setDay(day);
            period.setTimeslot(timeSlot);

            Room room = solution.getRoomList().get(Integer.parseInt(timeArgs[2]));

            for (Lecture l : solution.getLectureList()) {
                if (l.getCourse().getCode().equals(course) && l.getLectureIndexInCourse() == lectureIndexInCourse) {
                    l.setRoom(room);
                    l.setPeriod(period);
                    break;
                }
            }
        }
    }

    private String[] convertToList(String s) {

        s = s.substring(1, s.length() - 1);
        if (s.length() == 0) {
            return null;
        }
        s = s.replace("\"", "");
        String[] array = s.split(",");
        return array;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("get calisti");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("post calıstı");
        processRequest(request, response);
    }

}
