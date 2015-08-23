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
package org.optaplanner.curriculumcourse.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.curriculumcourse.dao.CurriculumDao;
import org.optaplanner.examples.curriculumcourse.domain.Curriculum;

/**
 *
 * @author gurhan
 */
public class CurriculumService extends GenericServiceImpl<Curriculum> {

    private CurriculumDao cDao;

    public CurriculumService(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        super(req, resp, Curriculum.class);
        cDao = new CurriculumDao(em);
    }

    public void curriculumInfo() throws ServletException, IOException {
        Curriculum curriculum = cDao.find(req.getAttribute("curriculumId"));
        req.setAttribute("curriculum", curriculum);
        RequestDispatcher rd = req.getRequestDispatcher("curriculaInfoEdit.jsp");
        rd.forward(req, resp);
    }

    public void allCurriculumList() throws ServletException, IOException {
        List<Curriculum> curriculumList = cDao.findAll();
        req.setAttribute("curriculumList", curriculumList);
        RequestDispatcher rd = req.getRequestDispatcher("editCurricula.jsp");
        rd.forward(req, resp);
    }

    @Override
    public void editSave() {
        String id = req.getParameter("curriculumId");
        Curriculum curricula;
        try {
            if (id == null || id.trim().equals("")) {
                //curricula = new Curriculum();
                createCurriculas();

            } else {
                curricula = cDao.find(Long.parseLong(id));

                curricula.setCode(req.getParameter("curriculumCode"));
                boolean isNigt;
                if (req.getParameter("educationType").equals("night")) {
                    isNigt = true;
                } else {
                    isNigt = false;
                }
                curricula.setNightClass(isNigt);
                cDao.createOrUpdate(curricula);

            }
        } catch (Exception e) {
            e.printStackTrace();
            message.setContent("Bir sorun oluştu.");
            message.setResult(false);
        }
        
        message.setContent("Değişikler başarıyla kaydedildi.");
        message.setResult(true);

        req.setAttribute("message", message);
        RequestDispatcher rd = req.getRequestDispatcher("CurriculumsViewServlet");
        try {
            rd.forward(req, resp);
        } catch (ServletException ex) {
            Logger.getLogger(CurriculumService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CurriculumService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createCurriculas() {
        char[] branchNames = new char[]{'A', 'B', 'C', 'D'};
        String curriculumLevel = req.getParameter("curriculumLevel");
        int numberOfBranch = Integer.parseInt(req.getParameter("numberOfBranch"));
        boolean isCheckedNight = req.getParameter("isCheckedNight") != null;
        boolean isCheckedTr = req.getParameter("isCheckedTr") != null;
        boolean isCheckedPercent30Eng = req.getParameter("isCheckedPercent30Eng") != null;
        boolean isCheckedEng = req.getParameter("isCheckedEng") != null;

        String code;
        for (int i = 0; i < numberOfBranch; i++) {
            if (isCheckedTr) {
                code = "GUN-" + curriculumLevel + "-" + branchNames[i];
                createCurriculum(code, isCheckedNight);
            }
            if (isCheckedPercent30Eng) {
                code = "%30-GUN" + curriculumLevel + "-" + branchNames[i];
                createCurriculum(code, isCheckedNight);
            }
            if (isCheckedEng) {
                code = "%100-GUN" + curriculumLevel + "-" + branchNames[i];
                createCurriculum(code, isCheckedNight);
            }

        }
    }

    private void createCurriculum(String name, boolean isCheckedNight) {
        Curriculum curriculum = new Curriculum(name, false);
        cDao.createOrUpdate(curriculum);
        if (isCheckedNight) {
            createTheNightCurriculum(name);
        }
    }

    private void createTheNightCurriculum(String name) {
        name = name.replace("GUN", "GECE");
        Curriculum curriculum = new Curriculum(name, true);
        cDao.createOrUpdate(curriculum);
    }

}
