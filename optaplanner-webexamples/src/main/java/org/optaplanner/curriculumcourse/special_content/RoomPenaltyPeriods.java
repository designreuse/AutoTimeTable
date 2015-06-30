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
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import static org.optaplanner.examples.curriculumcourse.domain.Day.WEEKDAYS;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/RoomPenaltyPeriods")
public class RoomPenaltyPeriods extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("utf-8");
            Long roomId = Long.parseLong(req.getParameter("roomId"));
            EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
            RoomDao rDao = new RoomDao(em);
            PeriodDao pDao = new PeriodDao(em);
            DayDao dDao = new DayDao(em);
            TimeslotDao tsDao = new TimeslotDao(em);
            List<Day> dayList = dDao.findAll();
            List<Timeslot> timeslotList = tsDao.findAll();
            Room room = rDao.find(Room.class, roomId);
            List<Period> roomUnfitPeriods = room.getUnfitPeriods();

            boolean[][] periodIsPenaltyArray = new boolean[dayList.size()][timeslotList.size()];
            for (int i = 0; i < dayList.size(); i++) {
                for (int j = 0; j < timeslotList.size(); j++) {
                    periodIsPenaltyArray[i][j] = false;
                }
            }

            for (Period period : roomUnfitPeriods) {
                int dayIndex = period.getDay().getDayIndex();
                int timeslotIndex = period.getTimeslot().getTimeslotIndex();
                periodIsPenaltyArray[dayIndex][timeslotIndex] = true;
            }
            req.setAttribute("dayNames", WEEKDAYS);
            req.setAttribute("timeslotList", timeslotList);
            req.setAttribute("dayList", dayList);
            req.setAttribute("periodIsPenaltyArray", periodIsPenaltyArray);
            req.setAttribute("room", room);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestDispatcher rd = req.getRequestDispatcher("roomPenaltyPeriodEdit.jsp");
        rd.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
}
