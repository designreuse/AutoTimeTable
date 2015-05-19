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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.optaplanner.examples.curriculumcourse.domain.Period;

/**
 *
 * @author gurhan
 */
public class PeriodDao extends GenericDaoImp<Period>{

    public PeriodDao(EntityManager em) {
        super(em);
    }
    
    public Period findPeriodByIndexes(int dayIndex, int timeslotIndex) {
        Query query = em.createNamedQuery("Period.findByIndex");
        query.setParameter("dayIndex", dayIndex);
        query.setParameter("timeslotIndex", timeslotIndex);
        try {
            return (Period) query.getResultList().get(0);
        } catch (Exception e) {
            System.out.println("Period dao hata:" + e.getMessage());
        }
        return null;
    }
    
}
