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
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;

/**
 *
 * @author gurhan
 */
public class CourseScheduleDao extends GenericDaoImp<CourseSchedule> {

    public CourseScheduleDao(EntityManager em) {
        super(em, CourseSchedule.class);
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
        query.setParameter("name", "%" + name + "%");
        try {
            CourseSchedule cs = (CourseSchedule) query.getResultList().get(query.getResultList().size() - 1);
            String csName = cs.getName();
            if (csName.contains("-")) {
                String index = csName.split("-")[1].trim();
                return Integer.parseInt(index) + 1;
            } else {
                return 1;
            }
        }catch(Exception e) {
        }
        return 1;
    }

    public Long nextSaveId() {
        Query query = em.createNamedQuery("CourseSchedule.findAll", CourseSchedule.class);
        try {
            List<CourseSchedule> csList = query.getResultList();
            return csList.get(csList.size() - 1).getId() + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
    }

}
