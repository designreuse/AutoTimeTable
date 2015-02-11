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
package org.optaplanner.webexamples.curriculumcourse;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
import org.optaplanner.examples.curriculumcourse.domain.Day;
import org.optaplanner.examples.curriculumcourse.domain.Room;
import org.optaplanner.examples.curriculumcourse.domain.Timeslot;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;

/**
 *
 * @author gurhan
 */
@WebServlet("/CurriculumCourseLoadedServlet")
public class CurriculumCourseLoadedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        new CurriculumCourseWebAction().setup(session);
        CourseSchedule solution = (CourseSchedule) session.getAttribute(CurriculumCourseSessionAttributeName.SHOWN_SOLUTION);
        solution.getLectureList().get(1).getLectureIndexInCourse();
        List<Course> courses = solution.getCourseList();
        List<Day> days = solution.getDayList();
        List<Room> rooms = solution.getRoomList();
        List<Timeslot> timeSlots = solution.getTimeslotList();
        
        HashMap<String, Color> colorOfCourses = createColors(courses);
        HashMap<String, String> courseNames = new HashMap<String, String>();
        HashMap<String, String> teacherNames = new HashMap<String, String>();
        BufferedReader br ;
        Lecture.ROOM_DEP = new HashMap<String, ArrayList<String>>();
        try {
            String line;
            String active = "";
            br = new BufferedReader(new FileReader(session.getServletContext().getRealPath("/") + File.separator + "import" + File.separator + "tc01.ctt"));
            System.out.println("loadaeddaa:"+session.getServletContext().getRealPath("/"));
            while ((line = br.readLine()) != null) {
                if (line.equals("COURSE_NAMES:")) {
                    active = "course";
                    continue;
                } else if (line.equals("TEACHER_NAMES:")) {
                    active = "teacher";
                    continue;
                } else if (line.equals("ROOM_DEPS:")) {
                    active = "room";
                    continue;
                }
                if (active.equals("room")) {
                    String[] s = line.split(" ");
                    String course = s[0];
                    ArrayList<String> courseRooms = new ArrayList<String>();
                    for (int i = 1; i < s.length; i++) {
                        courseRooms.add(s[i]);
                    }
                    
                    Lecture.ROOM_DEP.put(course, courseRooms);
                } else {
                    String code = line.substring(0, line.indexOf(" "));
                    String name = line.substring(line.indexOf(" ") + 1);
                    if (active.equals("course")) {
                        courseNames.put(code, name);
                    } else if (active.equals("teacher")) {
                        teacherNames.put(code, name);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        session.setAttribute("teacherNames", teacherNames);
        session.setAttribute("courseNames", courseNames);
        session.setAttribute("colorOfCourses", colorOfCourses);
        session.setAttribute("timeSlots", timeSlots);
        session.setAttribute("days", days);
        session.setAttribute("rooms", rooms);
        resp.sendRedirect("curriculumcourse/loaded.jsp");
    }

    private HashMap<String, Color> createColors(List<Course> courses) {
        HashMap<String, Color> colorsOfCourses = new HashMap<String, Color>();
        int count = 0;
        while (count < courses.size()) {
            int R = (int) (Math.random() * 256);
            int G = (int) (Math.random() * 256);
            int B = (int) (Math.random() * 256);
            Color color = new Color(R, G, B); //random color, but can be bright or dull

            //to get rainbow, pastel colors
            Random random = new Random();
            final float hue = random.nextFloat();
            final float saturation = 0.9f;//1.0 for brilliant, 0.0 for dull
            final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
            color = Color.getHSBColor(hue, saturation, luminance);
            if (!colorsOfCourses.values().contains(color)) {
                colorsOfCourses.put(courses.get(count).getCode(), color);
                count++;
            }
        }
        return colorsOfCourses;
    }

}
