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
import org.optaplanner.curriculumcourse.Message;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.curriculumcourse.dao.UnavailablePeriodPenaltyDao;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;

/**
 *
 * @author gurhan
 */
@WebServlet("/curriculumcourse/SaveRoomPeriodPenalty")
public class SaveRoomPeriodPenalty extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Message m = new Message();
        Long roomId = Long.parseLong(req.getParameter("roomId"));
        try {
            req.setCharacterEncoding("utf-8");
            EntityManager em = (EntityManager) req.getServletContext().getAttribute("entityManager");
            RoomDao rDao = new RoomDao(em);
            DayDao dDao = new DayDao(em);
            TimeslotDao tsDao = new TimeslotDao(em);
            PeriodDao pDao = new PeriodDao(em);
            Room room = rDao.find(Room.class, roomId);
            int timeSlotSize = tsDao.findAll().size();
            int daySize = dDao.findAll().size();
            List<Period> roomUnfitPeriodList = room.getUnfitPeriods();
            for (int dayIndex = 0; dayIndex < daySize; dayIndex++) {
                for (int timeSlotIndex = 0; timeSlotIndex < timeSlotSize; timeSlotIndex++) {
                    String parameter = "isDaysPeriodPenalty-" + dayIndex + "-" + timeSlotIndex;
                    String value = req.getParameter("isDaysPeriodPenalty-" + dayIndex + "-" + timeSlotIndex);
                    /**
                     * Periyot işaretlenmemiş ise hocanın girdği dersleri unv
                     * den kontrol et eğer deger dönerse değeri sil dönmezse bir
                     * şey yapma
                     */
                    System.out.println(parameter);
                    if (value == null) {
                        for (Period period : roomUnfitPeriodList) {
                            if(period.getDay().getDayIndex() == dayIndex && 
                                    period.getTimeslot().getTimeslotIndex() == timeSlotIndex) {
                                roomUnfitPeriodList.remove(period);
  
                                break;
                            }
                        }
                    } else {
                        boolean isThere = false;
                        for (Period period : roomUnfitPeriodList) {
                             if(period.getDay().getDayIndex() == dayIndex && 
                                    period.getTimeslot().getTimeslotIndex() == timeSlotIndex) {
                                isThere = true;
                                break;
                            }
                        }
                        if(isThere == false) {
                            Period period = pDao.findPeriodByIndexes(dayIndex, timeSlotIndex);
                            roomUnfitPeriodList.add(period);
                        }
                    }
                    
                }
            }
            rDao.createOrUpdate(room);
            m.setResult(true);
            m.setContent("Değişikler Başarıyla Kaydedildi");
        } catch (Exception e) {
            e.printStackTrace();
            m.setContent("Bir Sorun oluştu");
            m.setResult(false);
        }
        req.setAttribute("message", m);
        
        RequestDispatcher rd = req.getRequestDispatcher("RoomPenaltyPeriods?roomId=" + roomId);
        rd.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    
    

}
