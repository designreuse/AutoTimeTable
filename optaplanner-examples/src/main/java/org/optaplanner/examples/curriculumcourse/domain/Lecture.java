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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.curriculumcourse.domain.solver.LectureDifficultyWeightFactory;
import org.optaplanner.examples.curriculumcourse.domain.solver.MovableLectureSelectionFilter;
import org.optaplanner.examples.curriculumcourse.domain.solver.PeriodStrengthWeightFactory;
import org.optaplanner.examples.curriculumcourse.domain.solver.RoomStrengthWeightFactory;

@Entity(name = "Lecture")
@Table(name = "Lecture")
@NamedQueries({
    @NamedQuery(name = "Lecture.findAll",
            query = "SELECT l FROM Lecture l"),
    @NamedQuery(name = "Lecture.findAllPeriodsNull",
            query = "SElECT l FROM Lecture l WHERE l.period IS NULL and l.room IS NULL")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@PlanningEntity(difficultyWeightFactoryClass = LectureDifficultyWeightFactory.class,
        movableEntitySelectionFilter = MovableLectureSelectionFilter.class)
@XStreamAlias("Lecture")
public class Lecture extends AbstractPersistable {

    @JoinColumn(name = "course")
    private Course course;
    @Column(name = "lectureIndexInCourse")
    private int lectureIndexInCourse;
    @Column(name = "locked")
    private boolean locked;

    // Planning variables: changes during planning, between score calculations.
    @JoinColumn(name = "period")
    private Period period;
    @JoinColumn(name = "room")
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

    public boolean isCorrectTimeSlot() {
        if (course.getCurriculumList() != null && course.getCurriculumList().size() != 0) {
            try {
                if (course.getCurriculumList().get(0).isNightClass()) {
                    if (period.getTimeslot().getTimeslotIndex() >= 9) {
                        return true;
                    }
                } else {
                    if (period.getTimeslot().getTimeslotIndex() <= 10) {
                        return true;
                    }
                }
            } catch (Exception e) {
                System.out.println("hataaa");
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isCorrectRoomTimeslot() {
        try {
            return !room.getUnfitPeriods().contains(period);
        } catch (Exception e) {

        }
        return false;
    }

    public boolean isCorrectRoom() {
        try {
            return course.getRoomDeps().contains(room);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void setId(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
