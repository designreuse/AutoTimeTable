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
package org.optaplanner.curriculumcourse.addinfo;

import java.util.HashMap;

/**
 *
 * @author gurhan
 */
public abstract class AddInfo {
    protected String content;
    protected HashMap<String, String> courseNames;
    protected HashMap<String, String> teacherNames;
    
    public AddInfo(String content) {
       this.content = content;
       courseNames = new HashMap<String, String>();
       teacherNames = new HashMap<String, String>();
    }
    
    public abstract void readyInfo();

    public HashMap<String, String> getCourseNames() {
        return courseNames;
    }

    public HashMap<String, String> getTeacherNames() {
        return teacherNames;
    }
    
    
}
