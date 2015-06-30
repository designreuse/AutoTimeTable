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
import java.util.ArrayList;
import java.util.List;
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

@Entity(name = "Room")
@Table(name = "Room")
@NamedQueries({
    @NamedQuery(name = "Room.findByCode",
           query = "SELECT r FROM Room r WHERE r.code= :code" ),
    @NamedQuery(name = "Room.findAll",
            query = "SELECT r FROM Room r ")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XStreamAlias("Room")
public class Room extends AbstractPersistable {
    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "capacity")
    private int capacity;
    
    /**
     * Uygun olmayan periyotları listede tut ders bunlardan biriyse
     */
    @OneToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(name = "RoomUnfitPeriodList",
            joinColumns = @JoinColumn(name = "room"),
            inverseJoinColumns = @JoinColumn(name = "period"))
    private List<Period> unfitPeriods;
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLabel() {
        return code;
    }

    public List<Period> getUnfitPeriods() {
        if(unfitPeriods == null) {
            unfitPeriods = new ArrayList<Period>();
        }
        return unfitPeriods;
    }

    public void setUnfitPeriods(List<Period> unfitPeriods) {
        this.unfitPeriods = unfitPeriods;
    }

    @Override
    public String toString() {
        return code + " {C" + capacity + "}";
    }

}
