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
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@Entity(name = "Day")
@Table(name = "Day")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XStreamAlias("Day")
public class Day extends AbstractPersistable {
    
    @ElementCollection
    private static final String[] WEEKDAYS = {"Mo", "Tu", "We", "Th", "Fr"};

    @JoinColumn(name = "dayIndex")
    private int dayIndex;
    
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "dayPeriodList",
            joinColumns = @JoinColumn(name = "day"),
            inverseJoinColumns = @JoinColumn(name = "period"))
    private List<Period> periodList;

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public List<Period> getPeriodList() {
        return periodList;
    }

    public void setPeriodList(List<Period> periodList) {
        this.periodList = periodList;
    }

    public String getLabel() {
        return dayIndex + " " + WEEKDAYS[dayIndex % WEEKDAYS.length];
    }

    @Override
    public String toString() {
        return Integer.toString(dayIndex);
    }

}
