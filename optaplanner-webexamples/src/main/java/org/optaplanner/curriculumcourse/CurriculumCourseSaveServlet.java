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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseDao;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseExporter;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CurriculumCourseSaveServlet")
public class CurriculumCourseSaveServlet extends HttpServlet {

    private CourseSchedule solution;

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
        solution = (CourseSchedule) session.getAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION);

        String[] changeList = convertToList(request.getParameter("changeList"));
        if (changeList != null && changeList.length > 0) {
            saveChanges(changeList);
        }
        String content = (String) request.getSession().getAttribute("content");
        String path = request.getServletContext().getRealPath("/")+"export";
        File file = new File (path + File.separator + content + ".xml");
        CurriculumCourseDao dao = new CurriculumCourseDao(path);
        dao.writeSolution(solution, file);
        response.sendRedirect("index.jsp");

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
                    System.out.println("Eşleşme Bulundu");
                    l.setRoom(room);
                    l.setPeriod(period);
                    break;
                }
            }
        }
    }

    private String[] convertToList(String s) {
       
        s = s.substring(1, s.length() - 1);
        System.out.println("fafa:"+s.length());
        if(s.length() == 0) {
            return null;
        }
        s = s.replace("\"", "");
        String[] array = s.split(",");
        System.out.println("sizess:"+array.length);
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
