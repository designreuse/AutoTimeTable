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

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.CourseScheduleDao;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CurriculumCourseEditServlet")
public class CurriculumCourseEditServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String content = request.getParameter("content");
        CourseSchedule courseSchedule;
        if ( content != null &&content.trim().equals("")) {
            // content boş değil düzenleme sayfası
            EntityManager em = (EntityManager) request.getServletContext().getAttribute("entityManager");
            CourseScheduleDao csDao = new CourseScheduleDao(em);
            courseSchedule = csDao.findCourseScheduleByName(content);
        } else {
           // content boş yeni ders programı ekleme sayfası
            courseSchedule = new CourseSchedule();
        }
        request.getSession().setAttribute("edittingCourseSchedule", courseSchedule);
        response.sendRedirect("courseScheduleEdit.jsp");

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
        processRequest(request, response);
    }

}
