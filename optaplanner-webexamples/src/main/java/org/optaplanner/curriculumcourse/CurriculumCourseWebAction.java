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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.http.HttpSession;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.persistence.CurriculumCourseImporter;

/**
 *
 * @author gurhan
 */
public class CurriculumCourseWebAction {

    private static ExecutorService solvingExecutor = Executors.newFixedThreadPool(4);

    public void setup(HttpSession session, String contentName) throws FileNotFoundException, IOException {
        terminateEarly(session);
        SolverFactory solverFactory = SolverFactory.createFromXmlResource("org/optaplanner/examples/curriculumcourse/solver/curriculumCourseSolverConfig.xml");
        Solver solver = solverFactory.buildSolver();
        session.setAttribute(CurriculumCourseSessionAttributeName.SOLVER, solver);

        CurriculumCourseImporter.CurriculumCourseInputBuilder course = new CurriculumCourseImporter.CurriculumCourseInputBuilder();
        File file = new File(session.getServletContext().getRealPath("/") + File.separator + "import" + File.separator + contentName +".ctt");
        course.setInputFile(file);
        course.setBufferedReader(new BufferedReader(new FileReader(file)));
        CourseSchedule solution = (CourseSchedule) course.readSolution();
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
            session.removeAttribute(CurriculumCourseSessionAttributeName.SOLVER);
        }
    }
}
