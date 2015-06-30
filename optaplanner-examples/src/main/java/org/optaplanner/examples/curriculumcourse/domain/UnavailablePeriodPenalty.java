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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@Entity(name = "UnavailablePeriodPenalty")
@Table(name = "UnavailablePeriodPenalty")
@NamedQueries({
    @NamedQuery(name = "UnavailablePeriodPenalty.findAll",
            query = "SELECT uvp FROM UnavailablePeriodPenalty uvp"),
    @NamedQuery(name = "UnavailablePeriodPenalty.findByCourse",
            query = "SELECT uvp FROM UnavailablePeriodPenalty uvp WHERE uvp.course.id=:courseId"),
    @NamedQuery(name = "UnavailablePeriodPenalty.findByCourseAndDayAndPeriod",
            query = "SELECT uvp FROM UnavailablePeriodPenalty uvp WHERE UVP.course.id=:courseId AND UVP.period.day.dayIndex=:dayIndex AND UVP.period.timeslot.timeslotIndex=:timeslotIndex")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XStreamAlias("UnavailablePeriodPenalty")
public class UnavailablePeriodPenalty extends AbstractPersistable {

    @JoinColumn(name = "course")
    private Course course;
    @JoinColumn(name = "period")
    private Period period;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return course + "@" + period;
    }

}
