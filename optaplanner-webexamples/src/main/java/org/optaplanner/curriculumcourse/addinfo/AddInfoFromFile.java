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
package org.optaplanner.curriculumcourse.addinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.optaplanner.curriculumcourse.entity.CourseName;
//import org.optaplanner.curriculumcourse.entity.Curricula;
//import org.optaplanner.curriculumcourse.entity.Department;
//import org.optaplanner.curriculumcourse.entity.Faculty;
//import org.optaplanner.examples.curriculumcourse.domain.Course;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;
//import org.optaplanner.examples.curriculumcourse.domain.Curriculum;
import org.optaplanner.examples.curriculumcourse.domain.Lecture;

/**
 *
 * @author gurhan
 */
public class AddInfoFromFile extends AddInfo {


    private String path;
    private CourseSchedule solution;

    public AddInfoFromFile(String content, String path, CourseSchedule solution) {
        super(content);
        this.path = path;
    
        this.solution = solution;
    }

    @Override
    public void readyInfo() {
        //Faculty faculty = new Faculty(content);
        try {
            Lecture.ROOM_DEP = new HashMap<String, ArrayList<String>>();
            BufferedReader br = new BufferedReader(new FileReader(path
                    + "import" + File.separator + content + ".tc"));
            String line = "", active = "";
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
                } else if (line.equals("CURRICULA_OF_DEPARTMENT")) {
                    active = "curricula";
                }
                if (active.equals("room")) {
                    //Room kısmı okunuyosa roomları çek
                    String[] s = line.split(" ");
                    String course = s[0];
                    ArrayList<String> courseRooms = new ArrayList<String>();
                    for (int i = 1; i < s.length; i++) {
                        courseRooms.add(s[i]);
                    }

                    Lecture.ROOM_DEP.put(course, courseRooms);
                } else if (active.equals("curricula")) {
                    //Hangi sınıfın hangi bölüme ait olduğu kısım
//                    String[] parts = line.split(" ");
//                    String departmentName = parts[0];
//                    String[] curriculas = parts[1].split(",");
//
//                    Department department = new Department(departmentName);
//                    for (String s : curriculas) {
//                        Curricula curricula = new Curricula(s);
//                        curricula.setCourses(getCourses(s));
//                        department.addCurricula(curricula);
//                    }
//
//                    faculty.addDepartment(department);

                } else {
                    //Hoca ve kurs isimleride burada ayarlanıyor (aynı format)
                    String code = line.substring(0, line.indexOf(" "));
                    String name = line.substring(line.indexOf(" ") + 1);
                    if (active.equals("course")) {
                        courseNames.put(code, name);
                    } else if (active.equals("teacher")) {
                        teacherNames.put(code, name);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AddInfoFromFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddInfoFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    private ArrayList<CourseName> getCourses(String s) {
//        ArrayList<CourseName> courses = new ArrayList<CourseName>();
//        Curriculum curriculum = new Curriculum();
//        for (Course course : solution.getCourseList()) {
//
//            curriculum.setCode(s);
//            if (course.getCurriculumList().contains(curriculum)) {
//                String courseCode = course.getCode();
//                if (courseNames.containsKey(courseCode)) {
//                    courses.add(new CourseName(courseCode, courseNames.get(courseCode)));
//                } else {
//                    courses.add(new CourseName(courseCode));
//                }
//            }
//        }
//        return courses;
//    }

}
