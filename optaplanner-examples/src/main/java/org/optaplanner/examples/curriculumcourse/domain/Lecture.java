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
import java.util.HashMap;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.curriculumcourse.domain.solver.LectureDifficultyWeightFactory;
import org.optaplanner.examples.curriculumcourse.domain.solver.MovableLectureSelectionFilter;
import org.optaplanner.examples.curriculumcourse.domain.solver.PeriodStrengthWeightFactory;
import org.optaplanner.examples.curriculumcourse.domain.solver.RoomStrengthWeightFactory;
@PlanningEntity(difficultyWeightFactoryClass = LectureDifficultyWeightFactory.class,
        movableEntitySelectionFilter = MovableLectureSelectionFilter.class)
@XStreamAlias("Lecture")
public class Lecture extends AbstractPersistable {
    public static  HashMap<String, ArrayList<String>> ROOM_DEP ;
    public static String contextPath;
    private Course course;
    private int lectureIndexInCourse;
    private boolean locked;

    // Planning variables: changes during planning, between score calculations.
    private Period period;
    private Room room;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getLectureIndexInCourse() {
        return lectureIndexInCourse;
    }

    public void setLectureIndexInCourse(int lectureIndexInCourse) {
        this.lectureIndexInCourse = lectureIndexInCourse;
    }

    /**
     * @return true if immovable planning entity
     * @see MovableLectureSelectionFilter
     */
    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @PlanningVariable(valueRangeProviderRefs = {"periodRange"},
            strengthWeightFactoryClass = PeriodStrengthWeightFactory.class)
    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @PlanningVariable(valueRangeProviderRefs = {"roomRange"},
            strengthWeightFactoryClass = RoomStrengthWeightFactory.class)
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************
    public Teacher getTeacher() {
        return course.getTeacher();
    }

    public int getStudentSize() {
        return course.getStudentSize();
    }

    public List<Curriculum> getCurriculumList() {
        return course.getCurriculumList();
    }

    public Day getDay() {
        if (period == null) {
            return null;
        }
        return period.getDay();
    }

    public int getTimeslotIndex() {
        if (period == null) {
            return Integer.MIN_VALUE;
        }
        return period.getTimeslot().getTimeslotIndex();
    }

    public String getLabel() {
        return course.getCode() + "-" + lectureIndexInCourse;
    }

    @Override
    public String toString() {
        return course + "-" + lectureIndexInCourse + " @ " + period + " + " + room;
    }

    public boolean isImmediatlyAfter(Period p, Room r) {
        return p.getDay().getDayIndex() == period.getDay().getDayIndex()
                && p.getTimeslot().getTimeslotIndex() == period.getTimeslot().getTimeslotIndex() + 1
                && r.getCode().equals(room.getCode()) ? true : false;
    }
    
    public boolean isCorrectRoom(Room r) {
        try{
        if(ROOM_DEP != null &&ROOM_DEP.isEmpty() == false && this.course.getCode() != null){
            
            if(ROOM_DEP.keySet().contains(this.getCourse().getCode())) {
                String key = this.course.getCode();
                String value = r.getCode();
                return ROOM_DEP.get(key).contains(value);
            }
            return true;
        }
        return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
   
}
