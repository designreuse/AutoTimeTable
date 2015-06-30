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
package org.optaplanner.curriculumcourse.edit_view;

import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/CreatePeriods")
public class CreatePeriods extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
        DayDao dDao = new DayDao(em);
        TimeslotDao tDao = new TimeslotDao(em);
        PeriodDao pDao = new PeriodDao(em);
        List<Day> dayList = dDao.findAll();
        List<Timeslot> timeslotList = tDao.findAll();
        for (Day day : dayList) {
            for (Timeslot timeslot : timeslotList) {
                Period period = new Period();
                period.setDay(day);
                period.setTimeslot(timeslot);
                pDao.save(period);
            }
        }
    }
    
}
