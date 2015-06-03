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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.optaplanner.examples.common.domain.AbstractPersistable;

@Entity(name = "Teacher")
@Table(name = "Teacher")
@NamedQueries({
    @NamedQuery(name = "Teacher.findTeacherByCode",
            query = "SELECT t FROM Teacher t WHERE t.code=:code"),
    @NamedQuery(name = "Teacher.findAll",
            query = "SELECT t FROM Teacher t")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XStreamAlias("Teacher")
public class Teacher extends AbstractPersistable {

    @Column(name = "code", unique = true)
    private String code;
    
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private TeacherDegree degree;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return code;
    }

    public TeacherDegree getDegree() {
        if(this.degree == null) {
            this.degree = new TeacherDegree();
        }
        return degree;
    }

    public void setDegree(TeacherDegree degree) {
        this.degree = degree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return degree.getShortName()+" "+name + " " + surname;
    }

}
