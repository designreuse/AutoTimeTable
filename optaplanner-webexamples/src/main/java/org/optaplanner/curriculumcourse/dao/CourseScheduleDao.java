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
package org.optaplanner.curriculumcourse.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.optaplanner.examples.curriculumcourse.domain.CourseSchedule;

/**
 *
 * @author gurhan
 */
public class CourseScheduleDao extends GenericDaoImp<CourseSchedule>{

    public CourseScheduleDao(EntityManager em) {
        super(em);
    }
    
    public CourseSchedule findCourseScheduleByName(String code) {

        try {
            return findCourseSchedules(code).get(0);
        } catch (Exception e) {
            System.out.println("CourseSchedule dao:" + e.getMessage());
        }
        return null;
    }

    public List<CourseSchedule> findCourseSchedules(String code) {
        Query query;
        if (code != null) {
            query = em.createNamedQuery("CourseSchedule.findByName");
            query.setParameter("name", code);
        } else {
            query = em.createNamedQuery("CourseSchedule.findAll");
        }
        try {
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("CourseSchedule dao:" + e.getMessage());
        }
        return null;
    }
    
    public int nextCourseScheduleNameIndex(String name) {
        Query query = em.createNamedQuery("CourseSchedule.findLikeNames");
        query.setParameter("name", "%"+name+"%");
        return query.getResultList().size()+1;
    }
    
    
}
