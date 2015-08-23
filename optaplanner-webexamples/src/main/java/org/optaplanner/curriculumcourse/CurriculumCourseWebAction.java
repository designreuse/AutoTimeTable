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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.CurriculumDao;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.LectureDao;
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.curriculumcourse.dao.TeacherDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.curriculumcourse.dao.UnavailablePeriodPenaltyDao;
import org.optaplanner.curriculumcourse.exception.NoSuchRoomException;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseImporter;

/**
 *
 * @author gurhan
 */
public class CurriculumCourseWebAction {

    private static ExecutorService solvingExecutor = Executors.newFixedThreadPool(4);

    public void setup(HttpSession session, String contentName) throws FileNotFoundException, IOException, NoSuchRoomException {
        terminateEarly(session);
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();
        session.setAttribute(CurriculumCourseSessionAttributeName.SOLVER, solver);
        CourseSchedule solution = buildScheduleFromDb(session);
        System.out.println(solution.getTeacherList().size());
        session.setAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION, solution);
    }

    public void solve(final HttpSession session) {
        final Solver solver = (Solver) session.getAttribute(CurriculumCourseSessionAttributeName.SOLVER);
        final CourseSchedule unsolvedSolution = (CourseSchedule) session.getAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION);
        
        solver.addEventListener(new SolverEventListener<CourseSchedule>() {

            @Override
            public void bestSolutionChanged(BestSolutionChangedEvent<CourseSchedule> event) {
                CourseSchedule bestSolution = event.getNewBestSolution();
                session.setAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION, bestSolution);
            }

        });
        session.setAttribute("solver", solver);
        solvingExecutor.submit(new Runnable() {

            @Override
            public void run() {
                solver.solve(unsolvedSolution);

            }
        });
    }

    public void terminateEarly(HttpSession session) {
        final Solver solver = (Solver) session.getAttribute(CurriculumCourseSessionAttributeName.SOLVER);
        if (solver != null) {
            solver.terminateEarly();
            session.setAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION, solver.getBestSolution());
        }
    }

    private CourseSchedule buildScheduleFromDb(HttpSession session) throws NoSuchRoomException {
        EntityManager em = (EntityManager) session.getServletContext().getAttribute("entityManager");
        CourseDao cDao = new CourseDao(em);
        TeacherDao tDao = new TeacherDao(em);
        UnavailablePeriodPenaltyDao uvpDao = new UnavailablePeriodPenaltyDao(em);
        CurriculumDao ccDAo = new CurriculumDao(em);
        RoomDao rDao = new RoomDao(em);
        PeriodDao pDao = new PeriodDao(em);
        DayDao dDao = new DayDao(em);
        TimeslotDao tsDao = new TimeslotDao(em);
        LectureDao lDao = new LectureDao(em);

        List<Teacher> teacherList = tDao.findAll();
        List<Course> courseList = cDao.findAll();
        List<Room> roomList = rDao.findAll();
        List<Day> dayList = dDao.findAll();
        List<Timeslot> tsList = tsDao.findAll();
        List<Period> periodList = pDao.findAll();
        List<Curriculum> curriculumList = ccDAo.findAll();
        List<UnavailablePeriodPenalty> uvpList = uvpDao.findAll();
        List<Lecture> lectureList = lDao.findAllPeriodsNull();
        
       
        
        CourseSchedule solution = new CourseSchedule();
        solution.setId(0L);
        solution.setCourseList(courseList);
        solution.setTeacherList(teacherList);
        solution.setRoomList(roomList);
        solution.setDayList(dayList);
        solution.setTimeslotList(tsList);
        solution.setCurriculumList(curriculumList);
        solution.setPeriodList(periodList);
        solution.setLectureList(lectureList);
        solution.setUnavailablePeriodPenaltyList(uvpList);
        
        return solution;
    }
}
