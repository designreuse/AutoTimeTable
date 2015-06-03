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
package org.optaplanner.examples.curriculumcourse.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.optaplanner.examples.common.domain.AbstractPersistable;

/**
 *
 * @author gurhan
 */
@Entity(name = "TeacherDegree")
@Table(name = "TeacherDegree")
@NamedQueries({
    @NamedQuery(name="TeacherDegree.findByName", 
            query = "SELECT t FROM TeacherDegree t WHERE t.name=:name"),
    @NamedQuery(name = "TeacherDegree.findByShortName",
            query = "SELECT t FROM TeacherDegree t WHERE t.shortName=:shortName"),
    @NamedQuery(name = "TeacherDegree.findAll",
            query = "SELECT t FROM TeacherDegree t")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XStreamAlias("TeacherDegree")
public class TeacherDegree extends AbstractPersistable{
    @Column(name = "name")
    private String name;
    
    @Column(name = "shortName")
    private String shortName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shorName) {
        this.shortName = shorName;
    }

    @Override
    public String toString() {
        return name + "-" + shortName;
    }
    
    
    
}
