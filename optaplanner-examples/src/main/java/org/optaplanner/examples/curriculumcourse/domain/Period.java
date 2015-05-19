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

@Entity(name = "Period")
@Table(name = "Period")
@NamedQueries({
    @NamedQuery(name = "Period.findByIndex",
            query = "SELECT p FROM Period p WHERE p.day.dayIndex = :dayIndex "
                    + "and p.timeslot.timeslotIndex = :timeslotIndex")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XStreamAlias("Period")
public class Period extends AbstractPersistable {
    
    @JoinColumn(name = "day")
    private Day day;
    @JoinColumn (name = "timeslot")
    private Timeslot timeslot;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public String getLabel() {
        return day.getLabel() + "-" + timeslot.getLabel();
    }

    @Override
    public String toString() {
        return day + "-" + timeslot;
    }
 

}
