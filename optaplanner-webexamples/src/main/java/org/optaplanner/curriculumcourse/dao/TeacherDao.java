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
import org.optaplanner.curriculumcourse.exception.NoSuchTeacherException;
import org.optaplanner.examples.curriculumcourse.domain.Teacher;

/**
 *
 * @author gurhan
 */
public class TeacherDao extends GenericDaoImp<Teacher> {

    public TeacherDao(EntityManager em) {
        super(em, Teacher.class);
    }

    public Teacher findTeacher(String code) throws NoSuchTeacherException {
        Query query = em.createNamedQuery("Teacher.findTeacherByCode");
        query.setParameter("code", code);
        try {
            return (Teacher) query.getResultList().get(0);
        } catch (Exception e) {
            throw new NoSuchTeacherException();
        }
    }
    
    public List<Teacher> findAll() {
        Query query = em.createNamedQuery("Teacher.findAll");
        return query.getResultList();
    }

}
