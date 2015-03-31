/*
 * Copyright 2014 JBoss by Red Hat.
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
package org.optaplanner.examples.curriculumcourse.domain.solver;

import java.io.Serializable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.examples.curriculumcourse.domain.Course;

/**
 *
 * @author gurhan
 */
public class CourseSequentialRequirement implements Serializable, Comparable<CourseSequentialRequirement> {

    private final Course course;

    public CourseSequentialRequirement(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public int hashCode() {
       return new HashCodeBuilder().append(course).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
       if (this == o) {
           return true;
       } else if (o instanceof CourseSequentialRequirement) {
           CourseSequentialRequirement other = (CourseSequentialRequirement) o;
           return new EqualsBuilder().append(course, other.course).isEquals();
       } else {
           return false;
       }
    }

    @Override
    public String toString() {
        return course.toString();
    }

    @Override
    public int compareTo(CourseSequentialRequirement other) {
        return new CompareToBuilder().append(course, other.course).toComparison();         
    }

}
