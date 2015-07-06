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
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.optaplanner.curriculumcourse.CurriculumCourseSessionAttributeName;
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
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;
import org.optaplanner.examples.curriculumcourse.domain.Period;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;
import org.optaplanner.examples.curriculumcourse.domain.TeacherDegree;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;

/**
 *
 * @author gurhan
 */
public class CourseScheduleService extends GenericServiceImpl<CourseSchedule> {

    private CourseSchedule solution;
    private TeacherDao tDao;
    private CourseDao cDao;
    private CurriculumDao ccDao;
    private DayDao dDao;
    private TimeslotDao tsDao;
    private CourseScheduleDao csDao;
    private RoomDao rDao;
    private LectureDao lDao;
    private PeriodDao pDao;
    private TeacherDegreeDao tdDao;

    public CourseScheduleService(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        super(req, resp, CourseSchedule.class);
        solution = new CourseSchedule();
        tDao = new TeacherDao(em);
        cDao = new CourseDao(em);
        ccDao = new CurriculumDao(em);
        dDao = new DayDao(em);
        tsDao = new TimeslotDao(em);
        csDao = new CourseScheduleDao(em);
        rDao = new RoomDao(em);
        lDao = new LectureDao(em);
        pDao = new PeriodDao(em);
        tdDao = new TeacherDegreeDao(em);
    }

    public void courseScheduleDelete() throws IOException {
        Long solutionId = Long.parseLong(req.getParameter("id"));
        CourseScheduleDao csDao = new CourseScheduleDao(em);
        csDao.delete(solutionId);
        resp.sendRedirect("index.jsp");
    }
    
    public void courseScheduleSave() throws IOException, NoSuchCourseException, NoSuchTeacherException, NoSuchRoomException {
        
        HttpSession session = req.getSession(false);

        //eğer solutioınview sayfasından gelinmişse csname not null olacak
        String csName = req.getParameter("courseScheduleName");

        if (csName == null || csName.trim().equals("")) {
            csName = "Yazılım Mühendisliği";
            solution = (CourseSchedule) session.getAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION);
            solution.setName(csName);
            System.out.println("yeni id:" + solution.getId());
        } else {

            solution = csDao.findCourseScheduleByName(csName);
        }
        String[] changeList = convertToList(req.getParameter("changeList"));
        System.out.println("dizi:" + Arrays.toString(changeList));
        if (changeList != null && changeList.length > 0) {
            saveChanges(changeList);
        }
        // String path = request.getServletContext().getRealPath("/") + "export";
        //File file = new File(path + File.separator + content + ".xml");
        // CurriculumCourseDao dao = new CurriculumCourseDao(path);
        //dao.writeSolution(solution, file);

        if (csName == null) {

            readySubClasses(solution);
            setNullLectureAndPenalty(solution);

        } else {
            csDao.getEm().clear();
            setNullLectureAndPenalty(solution);

            solution.setId(0L);
        }
        checkScheduleName(solution);
        csDao.createOrUpdate(solution);

        resp.sendRedirect("index.jsp");
    }
     /**
     * Alt sınıflardan idleri çeker.
     *
     * @param solution
     */
    private void readySubClasses(CourseSchedule solution) throws NoSuchCourseException, NoSuchTeacherException, NoSuchRoomException {

        for (Course c : solution.getCourseList()) {
            Course managedCourse = cDao.findCourseByCode(c.getCode());
            if (managedCourse != null) {
                c.setId(managedCourse.getId());
            } else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                c.setId(null);
            }

        }

        for (Teacher t : solution.getTeacherList()) {
            Teacher managedTeacher = tDao.findTeacher(t.getCode());
            if (managedTeacher != null) {
                t.setId(managedTeacher.getId());
            } else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                t.setId(null);
            }
            t.setDegree(tdDao.find(t.getDegree().getId()));
        }

        for (Curriculum c : solution.getCurriculumList()) {
            Curriculum managedCurriculum = ccDao.findCurriculumByCode(c.getCode());
            if (managedCurriculum != null) {
                c.setId(managedCurriculum.getId());
            } else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                c.setId(null);
            }
        }

        for (Day d : solution.getDayList()) {
            Day managedDay = dDao.findDayByIndex(d.getDayIndex());
            if (managedDay != null) {
                d.setId(managedDay.getId());
            } else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                d.setId(null);
            }
        }

        for (Timeslot t : solution.getTimeslotList()) {
            Timeslot managetTimeslot = tsDao.findTimeslotByIndex(t.getTimeslotIndex());
            if (managetTimeslot != null) {
                t.setId(managetTimeslot.getId());
            } else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                t.setId(null);
            }
        }

        for (Room r : solution.getRoomList()) {
            Room managedRoom = rDao.findRoomByCode(r.getCode());
            if (managedRoom != null) {
                System.out.println(managedRoom.getCode() + "->" + managedRoom.getId());
                r.setId(managedRoom.getId());
            } else {
                //optaplanner id atamıssa sil onu bırak eclipselink atsın
                r.setId(null);
            }
        }

    }

    private void setNullLectureAndPenalty(CourseSchedule solution) {
        for (Lecture l : solution.getLectureList()) {
            l.setId(null);
        }
        for (UnavailablePeriodPenalty up : solution.getUnavailablePeriodPenaltyList()) {
            up.setId(null);
        }
    }

    private void checkScheduleName(CourseSchedule solution) {
        String name = solution.getName();
        if (name.contains("-")) {
            name = name.substring(0, name.indexOf("-") - 1);
        }
        int index = csDao.nextCourseScheduleNameIndex(name);
        solution.setName(name + " - " + index);
    }

    /**
     * Kendisine gönderilen listedeki değişikleri solution nesnesine kaydeder.
     *
     * @param changeList
     */
    private void saveChanges(String[] changeList) throws NoSuchRoomException {
        for (String s : changeList) {
            String[] args = s.split(":");
            String course = args[0];
            int lectureIndexInCourse = Integer.parseInt(args[1]);

            String[] timeArgs = args[2].split("x");

            Day day;
            int dayIndex = Integer.parseInt(timeArgs[0]);
            day = dDao.findDayByIndex(dayIndex);

            Timeslot timeSlot;
            int timeSlotIndex = Integer.parseInt(timeArgs[1]);
            timeSlot = tsDao.findTimeslotByIndex(timeSlotIndex);

            Period period;
            period = pDao.findPeriodByIndexes(day.getDayIndex(), timeSlot.getTimeslotIndex());

            System.out.println("bak bura");
            System.out.println(Arrays.toString(solution.getRoomList().toArray()));
            String roomCode = solution.getRoomList().get(Integer.parseInt(timeArgs[2])).getCode();
            Room room;
            room = rDao.findRoomByCode(roomCode);

            for (Lecture l : solution.getLectureList()) {
                if (l.getCourse().getCode().equals(course) && l.getLectureIndexInCourse() == lectureIndexInCourse) {
                    l.setRoom(room);
                    l.setPeriod(period);
                    break;
                }
            }
        }
    }

    private String[] convertToList(String s) {

        s = s.substring(1, s.length() - 1);
        if (s.length() == 0) {
            return null;
        }
        s = s.replace("\"", "");
        String[] array = s.split(",");
        return array;
    }

}
