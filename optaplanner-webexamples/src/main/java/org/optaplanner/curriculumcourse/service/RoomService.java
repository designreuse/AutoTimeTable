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
package org.optaplanner.curriculumcourse.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.Message;
import org.optaplanner.curriculumcourse.dao.CourseDao;
import org.optaplanner.curriculumcourse.dao.DayDao;
import org.optaplanner.curriculumcourse.dao.PeriodDao;
import org.optaplanner.curriculumcourse.dao.RoomDao;
import org.optaplanner.curriculumcourse.dao.TimeslotDao;
import org.optaplanner.curriculumcourse.exception.NoSuchRoomException;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import static org.optaplanner.examples.curriculumcourse.domain.Day.WEEKDAYS;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;

/**
 *
 * @author gurhan
 */
public class RoomService extends GenericServiceImpl<Room> implements RoomSpecialContent {

    private RoomDao rDao;
    private CourseDao cDao;
    private DayDao dDao;
    private TimeslotDao tsDao;
    private PeriodDao pDao;
    

    public RoomService(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        super(req, resp, Room.class);
        rDao = new RoomDao(em);
        cDao = new CourseDao(em);
        dDao = new DayDao(em);
        tsDao = new TimeslotDao(em);
        pDao = new PeriodDao(em);
    }

    public void allRoomList() throws ServletException, IOException, NoSuchRoomException {
        List<Room> roomList = rDao.findAll();
        req.setAttribute("roomList", roomList);
        RequestDispatcher rd = req.getRequestDispatcher("editRoom.jsp");
        rd.forward(req, resp);
    }

    public void roomInfo() throws UnsupportedEncodingException, ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String id = req.getParameter("roomId");
        Room room = rDao.find(id);
        req.setAttribute("room", room);
        RequestDispatcher rd = req.getRequestDispatcher("roomChangeInfo.jsp");
        rd.forward(req, resp);

    }

    @Override
    public void editSave() {
        try {
            Room room = rDao.find(req.getParameter("roomId"));
            String id = req.getParameter("roomId");
            if (id == null || id.trim().equals("")) {
                room = new Room();

            } else {
                room = rDao.find(id);
            }
            rDao.createOrUpdate(room);
            message.setResult(true);
            message.setContent("Değişikler başarıyla kaydedildi.");
        } catch (Exception ex) {
            Logger.getLogger(TeacherService.class.getName()).log(Level.SEVERE, null, ex);
            message.setResult(false);
            message.setContent("Bir sorun oluştu");

        }
        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("RoomsViewServlet");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void changeRoomInfo(Room room, HttpServletRequest req) {
        room.setCode(req.getParameter("roomCode"));
        room.setCapacity(Integer.parseInt(req.getParameter("roomCapacity")));
    }

    @Override
    public void getCourseRoomDeps() {
        List<Room> roomList = rDao.findAll();
        Course course = cDao.find(Long.parseLong(req.getParameter("courseId")));
        List<Room> courseDeps = course.getRoomDeps();

        boolean[] isRoomSelectArray = new boolean[roomList.size()];
        for (int i = 0; i < roomList.size(); i++) {
            for (int j = 0; j < courseDeps.size(); j++) {
                if (roomList.get(i).getCode().equals(courseDeps.get(j).getCode())) {
                    isRoomSelectArray[i] = true;
                    continue;
                }
            }
        }
        req.setAttribute("roomList", roomList);
        req.setAttribute("course", course);
        req.setAttribute("isRoomSelectArray", isRoomSelectArray);
        RequestDispatcher rd = req.getRequestDispatcher("roomDepsEdit.jsp");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveCourseRoomDeps() {
        Long courseId = Long.parseLong(req.getParameter("courseId"));
        RequestDispatcher rd = req.getRequestDispatcher("CourseRoomDep?courseId=" + courseId);
        try {
            List<Room> roomList = rDao.findAll();
            Course course = cDao.find(courseId);
            for (Room room : roomList) {
                String value = req.getParameter("isSelectedRoom-" + room.getCode());
                if (value == null) {
                    if (course.getRoomDeps().contains(room)) {
                        course.getRoomDeps().remove(room);
                    }
                } else {
                    if (!course.getRoomDeps().contains(room)) {
                        course.getRoomDeps().add(room);
                    }
                }
                cDao.update(course);
                message.setResult(false);
                message.setContent("Değişikler başarıyla kaydedildi");
            }
        } catch (Exception e) {
            e.printStackTrace();
            message.setResult(false);
            message.setContent("Bir sorun oluştu");
        }
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void getRoomPeriodPenalty() {
        try {
            Long roomId = Long.parseLong(req.getParameter("roomId"));
    
            List<Day> dayList = dDao.findAll();
            List<Timeslot> timeslotList = tsDao.findAll();
            Room room = rDao.find(roomId);
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
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveRoomPeriodPenalty() {
         Message m = new Message();
        Long roomId = Long.parseLong(req.getParameter("roomId"));
        try {
            Room room = rDao.find(roomId);
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
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RoomService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
