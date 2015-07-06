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
import org.optaplanner.examples.curriculumcourse.domain.UnavailablePeriodPenalty;

/**
 *
 * @author gurhan
 */
public class UnavailablePeriodPenaltyDao extends GenericDaoImp<UnavailablePeriodPenalty> {

    public UnavailablePeriodPenaltyDao(EntityManager em) {
        super(em, UnavailablePeriodPenalty.class);
    }

    public List<UnavailablePeriodPenalty> findUvpByCourse(Long courseId) {
        try {
            Query query = em.createNamedQuery("UnavailablePeriodPenalty.findByCourse");
            query.setParameter("courseId", courseId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public UnavailablePeriodPenalty findUvpByCourseAndPeriod(Long courseId, int dayIndex, int timeslotIndex) {
        try {
           Query query = em.createNamedQuery("UnavailablePeriodPenalty.findByCourseAndDayAndPeriod");
           query.setParameter("courseId", courseId);
           query.setParameter("dayIndex", dayIndex);
           query.setParameter("timeslotIndex", timeslotIndex);
           return (UnavailablePeriodPenalty) query.getResultList().get(0);
        }catch (Exception e) {
        }
        return null;
    }
    
    public List<UnavailablePeriodPenalty> findAll() {
        try {
            Query query = em.createNamedQuery("UnavailablePeriodPenalty.findAll");
            return query.getResultList();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
