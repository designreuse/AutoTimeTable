/*
 * Copyright 2014 JBoss by Red Hat.
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

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.curriculumcourse.dao.CourseScheduleDao;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;

/**
 * Ders programı daha önce çözülmemişse çalışacak kısım. ctt dosyasının yanı
 * sıra gerekli olan diğer bilgileri tc dosyasından çeker tc dosyasındakileri
 * veri tabanına ekler.
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CurriculumCourseLoadedServlet")
public class CurriculumCourseLoadedServlet extends HttpServlet {

    CourseSchedule solution;
    List<Course> courses;
    List<Day> days;
    List<Room> rooms;
    List<Timeslot> timeSlots;
    HardSoftScore score;
    List<Lecture> lectures;
    HashMap<String, Color> colorOfCourses;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        HttpSession session = req.getSession();

        String contentName = req.getParameter("content");
        String type = req.getParameter("type");


        if (type.equals("new")) {
            String path = req.getServletContext().getRealPath("/");
            new CurriculumCourseWebAction().setup(session, contentName);
            session.setAttribute("content", contentName);

            solution = (CourseSchedule) session.getAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION);

            courses = solution.getCourseList();
            days = solution.getDayList();
            rooms = solution.getRoomList();
            timeSlots = solution.getTimeslotList();
            score = solution.getScore();
            lectures = solution.getLectureList();

            colorOfCourses = createColors(courses);

            session.setAttribute("colorOfCourses", colorOfCourses);
            session.setAttribute("timeSlots", timeSlots);
            session.setAttribute("days", days);
            session.setAttribute("rooms", rooms);

            resp.sendRedirect("loaded.jsp");
        } else {
            
            EntityManager em = (EntityManager) session.getServletContext().getAttribute("entityManager");
            CourseScheduleDao csDao = new CourseScheduleDao(em);
            
            solution = csDao.findCourseScheduleByName(contentName);
            System.out.println(solution.getLectureList().get(1).getPeriod()+":"+solution.getLectureList().get(1).getCourse());
            courses = solution.getCourseList();
            days = solution.getDayList();
            rooms = solution.getRoomList();
            timeSlots = solution.getTimeslotList();
            score = solution.getScore();
            lectures = solution.getLectureList();

            colorOfCourses = createColors(courses);

            req.setAttribute("solution", solution);
            req.setAttribute("score", score);
            req.setAttribute("lectures", lectures);
            req.setAttribute("colorOfCourses", colorOfCourses);
            req.setAttribute("timeSlots", timeSlots);
            req.setAttribute("days", days);
            req.setAttribute("rooms", rooms);
            
            RequestDispatcher rd = req.getRequestDispatcher("solutionView.jsp");
            rd.forward(req, resp);
        }

    }

    private HashMap<String, Color> createColors(List<Course> courses) {
        HashMap<String, Color> colorsOfCourses = new HashMap<String, Color>();
        int count = 0;
        while (count < courses.size()) {
            int R = (int) (Math.random() * 256);
            int G = (int) (Math.random() * 256);
            int B = (int) (Math.random() * 256);
            Color color = new Color(R, G, B); //random color, but can be bright or dull

            //to get rainbow, pastel colors
            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
            final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
            color = Color.getHSBColor(hue, saturation, luminance);
            if (!colorsOfCourses.values().contains(color)) {
                colorsOfCourses.put(courses.get(count).getCode(), color);
                count++;
            }
        }
        return colorsOfCourses;
    }

}
