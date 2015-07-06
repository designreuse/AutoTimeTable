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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
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
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.dao.TeacherDegreeDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.curriculumcourse.exception.NoSuchCourseException;
import org.optaplanner.curriculumcourse.exception.NoSuchRoomException;
import org.optaplanner.curriculumcourse.exception.NoSuchTeacherException;
import org.optaplanner.curriculumcourse.service.CourseScheduleService;
import org.optaplanner.examples.curriculumcourse.domain.TeacherDegree;
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
    private PeriodDao periodDao;
    private TeacherDegreeDao tdDao;

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
            throws ServletException, IOException, NoSuchCourseException, NoSuchTeacherException, NoSuchRoomException {

        CourseScheduleService csService = new CourseScheduleService(request, response);
        csService.courseScheduleSave();

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
        try {
            processRequest(request, response);
        } catch (NoSuchCourseException ex) {
            Logger.getLogger(CurriculumCourseSaveServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchTeacherException ex) {
            Logger.getLogger(CurriculumCourseSaveServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchRoomException ex) {
            Logger.getLogger(CurriculumCourseSaveServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
