/*
 * Copyright 2010 JBoss Inc
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
package org.optaplanner.examples.curriculumcourse.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@Entity(name = "Course")
@Table(name = "Course")
@NamedQueries({
    @NamedQuery(
            name = "Course.findCourseByCode",
            query = "SELECT c FROM Course c WHERE c.code = :code"),
    @NamedQuery(
            name = "Course.findAll",
            query = "SELECT c FROM Course c"),
    @NamedQuery(
            name = "Course.findByTeacher",
            query = "SELECT c FROM Course c WHERE c.teacher.id=:teacherId"
    ),
    @NamedQuery(
            name = "Course.findTeacherIsNull", 
            query = "SELECT c FROM Course c WHERE c.teacher IS NULL"
    )
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XStreamAlias("Course")
public class Course extends AbstractPersistable {

    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "name")
    private String name;

    @JoinColumn(name = "teacher")
    private Teacher teacher;
    @Column(name = "lectureSize")
    private int lectureSize;
    @Column(name = "minWorkingDaySize")
    private int minWorkingDaySize;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "coursesCurriculums",
            joinColumns = @JoinColumn(name = "course"),
            inverseJoinColumns = @JoinColumn(name = "curricula"))
    private List<Curriculum> curriculumList;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "roomDependencies",
            joinColumns = @JoinColumn(name = "course"),
            inverseJoinColumns = @JoinColumn(name = "room"))
    private List<Room> roomDeps;

    @Column(name = "studentSize")
    private int studentSize;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getLectureSize() {
        return lectureSize;
    }

    public void setLectureSize(int lectureSize) {
        this.lectureSize = lectureSize;
    }

    public int getMinWorkingDaySize() {
        return minWorkingDaySize;
    }

    public void setMinWorkingDaySize(int minWorkingDaySize) {
        this.minWorkingDaySize = minWorkingDaySize;
    }

    public List<Curriculum> getCurriculumList() {
        if(curriculumList == null) {
            curriculumList = new ArrayList<Curriculum>();
        }
        return curriculumList;
    }

    public List<Room> getRoomDeps() {
        return roomDeps;
    }

    public void setRoomDeps(List<Room> roomDeps) {
        this.roomDeps = roomDeps;
    }

    public void setCurriculumList(List<Curriculum> curriculumList) {
        this.curriculumList = curriculumList;
    }

    public int getStudentSize() {
        return studentSize;
    }

    public void setStudentSize(int studentSize) {
        this.studentSize = studentSize;
    }

    @Override
    public String toString() {
        return code + "{" + teacher + "}";
    }

}
