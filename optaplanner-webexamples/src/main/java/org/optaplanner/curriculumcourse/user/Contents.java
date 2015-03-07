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
package org.optaplanner.curriculumcourse.user;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 *
 * @author gurhan
 */
public class Contents {

    private ArrayList<String> importContents;
    private ArrayList<String> exportContents;
    private String path;

    public Contents() {
        importContents = new ArrayList<String>();
        exportContents = new ArrayList<String>();
    }

    public Contents(String path) {
        importContents = new ArrayList<String>();
        exportContents = new ArrayList<String>();
        this.path = path;

    }
    
    public void prepareContents() {
        getContentsByType("import", importContents);
        getContentsByType("export", exportContents);
    }
    
    private void getContentsByType(String type, ArrayList<String> contents) {
        final String extension = type.equals("import") ? ".ctt" : ".xml";
        File directory = new File(path + type);
        File[] files = directory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(extension);
            }
        });
        if (files != null) {
            for (File f : files) {
                if (!contents.contains(f.getName().substring(0, f.getName().indexOf(extension)))) {
                    contents.add(f.getName().substring(0, f.getName().indexOf(extension)));
                }
            }
        }
    }
    /**
     * Liste talep edildiğinde import dizini altında dosya listesi alınır bir
     * arrayliste atılır ve geri döndürülür.
     *
     * @return
     */
    
    
    
    /**
     * Gönderilen içeriğin export dizini altında çözülmüş hali olup olmadığına
     * göre çözülüp çözülmediğini döndürür.
     * @param contentName
     * @return
     */
    public boolean isSolved(String contentName) {
        return exportContents.contains(contentName);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<String> getImportContents() {
        return importContents;
    }

    public ArrayList<String> getExportContents() {
        return exportContents;
    }
    

}
