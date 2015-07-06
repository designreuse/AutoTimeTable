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
import javax.servlet.http.HttpServletRequest;
import org.optaplanner.examples.curriculumcourse.domain.Day;

/**
 *
 * @author gurhan
 */
public class DayDao extends GenericDaoImp<Day> {

    public DayDao(EntityManager em) {
        super(em,Day.class);
    }

    public Day findDayByIndex(int dayIndex) {
        Query query = em.createNamedQuery("Day.findByIndex", Day.class);
        query.setParameter("dayIndex", dayIndex);
        try {
            return (Day) query.getResultList().get(0);
        } catch (Exception e) {
            System.out.println("Daydao Hata:"+e.getMessage());
        }
        return null;
    }
    public List<Day> findAll() {
        try {
            Query query = em.createNamedQuery("Day.findAll");
            return query.getResultList();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
