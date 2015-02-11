/*
 * Copyright 2013 JBoss Inc
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

package org.optaplanner.core.impl.domain.solution.cloner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.solution.cloner.SolutionCloner;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.testdata.domain.TestdataEntity;
import org.optaplanner.core.impl.testdata.domain.TestdataSolution;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;
import org.optaplanner.core.impl.testdata.domain.accessmodifier.TestdataAccessModifierSolution;
import org.optaplanner.core.impl.testdata.domain.chained.TestdataChainedAnchor;
import org.optaplanner.core.impl.testdata.domain.chained.TestdataChainedEntity;
import org.optaplanner.core.impl.testdata.domain.chained.TestdataChainedObject;
import org.optaplanner.core.impl.testdata.domain.chained.TestdataChainedSolution;
import org.optaplanner.core.impl.testdata.domain.collection.TestdataEntityCollectionPropertyEntity;
import org.optaplanner.core.impl.testdata.domain.collection.TestdataEntityCollectionPropertySolution;
import org.optaplanner.core.impl.testdata.domain.collection.TestdataSetBasedEntity;
import org.optaplanner.core.impl.testdata.domain.collection.TestdataSetBasedSolution;
import org.optaplanner.core.impl.testdata.domain.deepcloning.TestdataDeepCloningEntity;
import org.optaplanner.core.impl.testdata.domain.deepcloning.TestdataDeepCloningSolution;
import org.optaplanner.core.impl.testdata.domain.extended.TestdataExtendedEntity;
import org.optaplanner.core.impl.testdata.domain.extended.TestdataExtendedSolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.optaplanner.core.impl.testdata.util.PlannerAssert.*;
import static org.optaplanner.core.impl.testdata.util.PlannerAssert.assertNull;

public abstract class AbstractSolutionClonerTest {

    protected abstract <Sol extends Solution> SolutionCloner<Sol> createSolutionCloner(
            SolutionDescriptor solutionDescriptor);

    @Test
    public void cloneSolution() {
        SolutionDescriptor solutionDescriptor = TestdataSolution.buildSolutionDescriptor();
        SolutionCloner<TestdataSolution> cloner = createSolutionCloner(solutionDescriptor);

        TestdataValue val1 = new TestdataValue("1");
        TestdataValue val2 = new TestdataValue("2");
        TestdataValue val3 = new TestdataValue("3");
        TestdataEntity a = new TestdataEntity("a", val1);
        TestdataEntity b = new TestdataEntity("b", val1);
        TestdataEntity c = new TestdataEntity("c", val3);
        TestdataEntity d = new TestdataEntity("d", val3);

        TestdataSolution original = new TestdataSolution("solution");
        List<TestdataValue> valueList = Arrays.asList(val1, val2, val3);
        original.setValueList(valueList);
        List<TestdataEntity> originalEntityList = Arrays.asList(a, b, c, d);
        original.setEntityList(originalEntityList);

        TestdataSolution clone = cloner.cloneSolution(original);

        assertNotSame(original, clone);
        assertCode("solution", clone);
        assertSame(valueList, clone.getValueList());

        List<TestdataEntity> cloneEntityList = clone.getEntityList();
        assertNotSame(originalEntityList, cloneEntityList);
        assertEquals(4, cloneEntityList.size());
        TestdataEntity cloneA = cloneEntityList.get(0);
        TestdataEntity cloneB = cloneEntityList.get(1);
        TestdataEntity cloneC = cloneEntityList.get(2);
        TestdataEntity cloneD = cloneEntityList.get(3);
        assertEntityClone(a, cloneA, "a", "1");
        assertEntityClone(b, cloneB, "b", "1");
        assertEntityClone(c, cloneC, "c", "3");
        assertEntityClone(d, cloneD, "d", "3");

        b.setValue(val2);
        assertCode("2", b.getValue());
        // Clone remains unchanged
        assertCode("1", cloneB.getValue());
    }

    @Test
    public void cloneAccessModifierSolution() {
        Object staticObject = new Object();
        TestdataAccessModifierSolution.setStaticField(staticObject);

        SolutionDescriptor solutionDescriptor = TestdataAccessModifierSolution.buildSolutionDescriptor();
        SolutionCloner<TestdataAccessModifierSolution> cloner = createSolutionCloner(solutionDescriptor);

        TestdataValue val1 = new TestdataValue("1");
        TestdataValue val2 = new TestdataValue("2");
        TestdataValue val3 = new TestdataValue("3");
        TestdataEntity a = new TestdataEntity("a", val1);
        TestdataEntity b = new TestdataEntity("b", val1);
        TestdataEntity c = new TestdataEntity("c", val3);
        TestdataEntity d = new TestdataEntity("d", val3);

        TestdataAccessModifierSolution original = new TestdataAccessModifierSolution("solution");
        original.setWriteOnlyField("writeHello");
        List<TestdataValue> valueList = Arrays.asList(val1, val2, val3);
        original.setValueList(valueList);
        List<TestdataEntity> originalEntityList = Arrays.asList(a, b, c, d);
        original.setEntityList(originalEntityList);

        TestdataAccessModifierSolution clone = cloner.cloneSolution(original);

        assertSame("staticFinalFieldValue", TestdataAccessModifierSolution.getStaticFinalField());
        assertSame(staticObject, TestdataAccessModifierSolution.getStaticField());

        assertNotSame(original, clone);
        assertCode("solution", clone);
        assertEquals(original.getFinalField(), clone.getFinalField());
        assertEquals("readHello", clone.getReadOnlyField());
        assertSame(valueList, clone.getValueList());

        List<TestdataEntity> cloneEntityList = clone.getEntityList();
        assertNotSame(originalEntityList, cloneEntityList);
        assertEquals(4, cloneEntityList.size());
        TestdataEntity cloneA = cloneEntityList.get(0);
        TestdataEntity cloneB = cloneEntityList.get(1);
        TestdataEntity cloneC = cloneEntityList.get(2);
        TestdataEntity cloneD = cloneEntityList.get(3);
        assertEntityClone(a, cloneA, "a", "1");
        assertEntityClone(b, cloneB, "b", "1");
        assertEntityClone(c, cloneC, "c", "3");
        assertEntityClone(d, cloneD, "d", "3");

        b.setValue(val2);
        assertCode("2", b.getValue());
        // Clone remains unchanged
        assertCode("1", cloneB.getValue());
    }

    @Test
    public void cloneExtendedSolution() {
        SolutionDescriptor solutionDescriptor = TestdataSolution.buildSolutionDescriptor();
        SolutionCloner<TestdataSolution> cloner = createSolutionCloner(solutionDescriptor);

        TestdataValue val1 = new TestdataValue("1");
        TestdataValue val2 = new TestdataValue("2");
        TestdataValue val3 = new TestdataValue("3");
        TestdataExtendedEntity a = new TestdataExtendedEntity("a", val1, null);
        TestdataExtendedEntity b = new TestdataExtendedEntity("b", val1, "extraObjectOnEntity");
        TestdataExtendedEntity c = new TestdataExtendedEntity("c", val3);
        TestdataExtendedEntity d = new TestdataExtendedEntity("d", val3, c);
        c.setExtraObject(d);

        TestdataExtendedSolution original = new TestdataExtendedSolution("solution", "extraObjectOnSolution");
        List<TestdataValue> valueList = Arrays.asList(val1, val2, val3);
        original.setValueList(valueList);
        List<TestdataEntity> originalEntityList = Arrays.<TestdataEntity>asList(a, b, c, d);
        original.setEntityList(originalEntityList);

        TestdataExtendedSolution clone = (TestdataExtendedSolution) cloner.cloneSolution(original);

        assertNotSame(original, clone);
        assertCode("solution", clone);
        assertEquals("extraObjectOnSolution", clone.getExtraObject());
        assertSame(valueList, clone.getValueList());

        List<TestdataEntity> cloneEntityList = clone.getEntityList();
        assertNotSame(originalEntityList, cloneEntityList);
        assertEquals(4, cloneEntityList.size());
        TestdataExtendedEntity cloneA = (TestdataExtendedEntity) cloneEntityList.get(0);
        TestdataExtendedEntity cloneB = (TestdataExtendedEntity) cloneEntityList.get(1);
        TestdataExtendedEntity cloneC = (TestdataExtendedEntity) cloneEntityList.get(2);
        TestdataExtendedEntity cloneD = (TestdataExtendedEntity) cloneEntityList.get(3);
        assertEntityClone(a, cloneA, "a", "1");
        assertEquals(null, cloneA.getExtraObject());
        assertEntityClone(b, cloneB, "b", "1");
        assertEquals("extraObjectOnEntity", cloneB.getExtraObject());
        assertEntityClone(c, cloneC, "c", "3");
        assertEquals(cloneD, cloneC.getExtraObject());
        assertEntityClone(d, cloneD, "d", "3");
        assertEquals(cloneC, cloneD.getExtraObject());

        b.setValue(val2);
        assertCode("2", b.getValue());
        // Clone remains unchanged
        assertCode("1", cloneB.getValue());
    }

    private void assertEntityClone(TestdataEntity originalEntity, TestdataEntity cloneEntity,
            String entityCode, String valueCode) {
        assertNotSame(originalEntity, cloneEntity);
        assertCode(entityCode, originalEntity);
        assertCode(entityCode, cloneEntity);
        assertCode(valueCode, cloneEntity.getValue());
    }

    @Test
    public void cloneChainedSolution() {
        SolutionDescriptor solutionDescriptor = TestdataChainedSolution.buildSolutionDescriptor();
        SolutionCloner<TestdataChainedSolution> cloner = createSolutionCloner(solutionDescriptor);

        TestdataChainedAnchor a0 = new TestdataChainedAnchor("a0");
        TestdataChainedEntity a1 = new TestdataChainedEntity("a1", a0);
        TestdataChainedEntity a2 = new TestdataChainedEntity("a2", a1);
        TestdataChainedEntity a3 = new TestdataChainedEntity("a3", a2);

        TestdataChainedAnchor b0 = new TestdataChainedAnchor("b0");
        TestdataChainedEntity b1 = new TestdataChainedEntity("b1", b0);

        TestdataChainedSolution original = new TestdataChainedSolution("solution");
        List<TestdataChainedAnchor> anchorList = Arrays.asList(a0, b0);
        original.setChainedAnchorList(anchorList);
        List<TestdataChainedEntity> originalEntityList = Arrays.asList(a1, a2, a3, b1);
        original.setChainedEntityList(originalEntityList);

        TestdataChainedSolution clone = cloner.cloneSolution(original);
        assertNotSame(original, clone);
        assertCode("solution", clone);
        assertSame(anchorList, clone.getChainedAnchorList());

        List<TestdataChainedEntity> cloneEntityList = clone.getChainedEntityList();
        assertNotSame(originalEntityList, cloneEntityList);
        assertEquals(4, cloneEntityList.size());
        TestdataChainedEntity cloneA1 = cloneEntityList.get(0);
        TestdataChainedEntity cloneA2 = cloneEntityList.get(1);
        TestdataChainedEntity cloneA3 = cloneEntityList.get(2);
        TestdataChainedEntity cloneB1 = cloneEntityList.get(3);
        assertChainedEntityClone(a1, cloneA1, "a1", a0);
        assertChainedEntityClone(a2, cloneA2, "a2", cloneA1);
        assertChainedEntityClone(a3, cloneA3, "a3", cloneA2);
        assertChainedEntityClone(b1, cloneB1, "b1", b0);

        a3.setChainedObject(b1);
        assertCode("b1", a3.getChainedObject());
        // Clone remains unchanged
        assertCode("a2", cloneA3.getChainedObject());
    }

    private void assertChainedEntityClone(TestdataChainedEntity originalEntity, TestdataChainedEntity cloneEntity,
            String entityCode, TestdataChainedObject value) {
        assertNotSame(originalEntity, cloneEntity);
        assertCode(entityCode, originalEntity);
        assertCode(entityCode, cloneEntity);
        assertSame(value, cloneEntity.getChainedObject());
    }

    @Test
    public void cloneSetBasedSolution() {
        SolutionDescriptor solutionDescriptor = TestdataSetBasedSolution.buildSolutionDescriptor();
        SolutionCloner<TestdataSetBasedSolution> cloner = createSolutionCloner(solutionDescriptor);

        TestdataValue val1 = new TestdataValue("1");
        TestdataValue val2 = new TestdataValue("2");
        TestdataValue val3 = new TestdataValue("3");
        TestdataSetBasedEntity a = new TestdataSetBasedEntity("a", val1);
        TestdataSetBasedEntity b = new TestdataSetBasedEntity("b", val1);
        TestdataSetBasedEntity c = new TestdataSetBasedEntity("c", val3);
        TestdataSetBasedEntity d = new TestdataSetBasedEntity("d", val3);

        TestdataSetBasedSolution original = new TestdataSetBasedSolution("solution");
        Comparator<TestdataValue> valueComparator = new Comparator<TestdataValue>() {
            public int compare(TestdataValue a, TestdataValue b) {
                return b.getCode().compareTo(a.getCode()); // Reverse alphabetic
            }
        };
        Set<TestdataValue> valueSet = new TreeSet<TestdataValue>(valueComparator);
        valueSet.addAll(Arrays.asList(val1, val2, val3));
        original.setValueSet(valueSet);
        Comparator<TestdataSetBasedEntity> entityComparator = new Comparator<TestdataSetBasedEntity>() {
            public int compare(TestdataSetBasedEntity a, TestdataSetBasedEntity b) {
                return b.getCode().compareTo(a.getCode()); // Reverse alphabetic
            }
        };
        Set<TestdataSetBasedEntity> originalEntitySet = new TreeSet<TestdataSetBasedEntity>(entityComparator);
        originalEntitySet.addAll(Arrays.asList(a, b, c, d));
        original.setEntitySet(originalEntitySet);

        TestdataSetBasedSolution clone = cloner.cloneSolution(original);
        assertNotSame(original, clone);
        assertSame(valueSet, clone.getValueSet());

        Set<TestdataSetBasedEntity> cloneEntitySet = clone.getEntitySet();
        assertNotSame(originalEntitySet, cloneEntitySet);
        assertTrue(cloneEntitySet instanceof SortedSet);
        assertSame(entityComparator, ((SortedSet) cloneEntitySet).comparator());
        assertCode("solution", clone);
        assertEquals(4, cloneEntitySet.size());
        Iterator<TestdataSetBasedEntity> it = cloneEntitySet.iterator();
        // Reverse order because they got sorted
        TestdataSetBasedEntity cloneD = it.next();
        TestdataSetBasedEntity cloneC = it.next();
        TestdataSetBasedEntity cloneB = it.next();
        TestdataSetBasedEntity cloneA = it.next();
        assertSetBasedEntityClone(a, cloneA, "a", "1");
        assertSetBasedEntityClone(b, cloneB, "b", "1");
        assertSetBasedEntityClone(c, cloneC, "c", "3");
        assertSetBasedEntityClone(d, cloneD, "d", "3");

        b.setValue(val2);
        assertCode("2", b.getValue());
        // Clone remains unchanged
        assertCode("1", cloneB.getValue());
    }

    private void assertSetBasedEntityClone(TestdataSetBasedEntity originalEntity, TestdataSetBasedEntity cloneEntity,
            String entityCode, String valueCode) {
        assertNotSame(originalEntity, cloneEntity);
        assertCode(entityCode, originalEntity);
        assertCode(entityCode, cloneEntity);
        assertCode(valueCode, cloneEntity.getValue());
    }

    @Test
    public void cloneEntityCollectionPropertySolution() {
        SolutionDescriptor solutionDescriptor = TestdataEntityCollectionPropertySolution.buildSolutionDescriptor();
        SolutionCloner<TestdataEntityCollectionPropertySolution> cloner = createSolutionCloner(solutionDescriptor);

        TestdataValue val1 = new TestdataValue("1");
        TestdataValue val2 = new TestdataValue("2");
        TestdataValue val3 = new TestdataValue("3");
        TestdataEntityCollectionPropertyEntity a = new TestdataEntityCollectionPropertyEntity("a", val1);
        TestdataEntityCollectionPropertyEntity b = new TestdataEntityCollectionPropertyEntity("b", val1);
        TestdataEntityCollectionPropertyEntity c = new TestdataEntityCollectionPropertyEntity("c", val3);
        a.setEntityList(Arrays.asList(b, c));
        a.setEntitySet(new HashSet<TestdataEntityCollectionPropertyEntity>(Arrays.asList(b, c)));
        a.setStringToEntityMap(new HashMap<String, TestdataEntityCollectionPropertyEntity>());
        a.getStringToEntityMap().put("b", b);
        a.getStringToEntityMap().put("c", c);
        a.setEntityToStringMap(new HashMap<TestdataEntityCollectionPropertyEntity, String>());
        a.getEntityToStringMap().put(b, "b");
        a.getEntityToStringMap().put(c, "c");
        a.setStringToEntityListMap(new HashMap<String, List<TestdataEntityCollectionPropertyEntity>>());
        a.getStringToEntityListMap().put("bc", Arrays.asList(b, c));

        b.setEntityList(Collections.<TestdataEntityCollectionPropertyEntity>emptyList());
        b.setEntitySet(new HashSet<TestdataEntityCollectionPropertyEntity>());
        b.setStringToEntityMap(new HashMap<String, TestdataEntityCollectionPropertyEntity>());
        b.setEntityToStringMap(null);
        b.setStringToEntityListMap(null);

        c.setEntityList(Arrays.asList(a, c));
        c.setEntitySet(new HashSet<TestdataEntityCollectionPropertyEntity>(Arrays.asList(a, c)));
        c.setStringToEntityMap(new HashMap<String, TestdataEntityCollectionPropertyEntity>());
        c.getStringToEntityMap().put("a", a);
        c.getStringToEntityMap().put("c", c);
        c.setEntityToStringMap(null);
        c.setStringToEntityListMap(null);

        TestdataEntityCollectionPropertySolution original = new TestdataEntityCollectionPropertySolution("solution");
        List<TestdataValue> valueList = Arrays.asList(val1, val2, val3);
        original.setValueList(valueList);
        List<TestdataEntityCollectionPropertyEntity> originalEntityList = Arrays.asList(a, b, c);
        original.setEntityList(originalEntityList);

        TestdataEntityCollectionPropertySolution clone = cloner.cloneSolution(original);

        assertNotSame(original, clone);
        assertCode("solution", clone);
        assertSame(valueList, clone.getValueList());

        List<TestdataEntityCollectionPropertyEntity> cloneEntityList = clone.getEntityList();
        assertNotSame(originalEntityList, cloneEntityList);
        assertEquals(3, cloneEntityList.size());
        TestdataEntityCollectionPropertyEntity cloneA = cloneEntityList.get(0);
        TestdataEntityCollectionPropertyEntity cloneB = cloneEntityList.get(1);
        TestdataEntityCollectionPropertyEntity cloneC = cloneEntityList.get(2);

        assertEntityCollectionPropertyEntityClone(a, cloneA, "a", "1");
        assertNotSame(a.getEntityList(), cloneA.getEntityList());
        assertEquals(2, cloneA.getEntityList().size());
        assertSame(cloneB, cloneA.getEntityList().get(0));
        assertSame(cloneC, cloneA.getEntityList().get(1));
        assertNotSame(a.getEntitySet(), cloneA.getEntitySet());
        assertEquals(2, cloneA.getEntitySet().size());
        assertNotSame(a.getStringToEntityMap(), cloneA.getStringToEntityMap());
        assertEquals(2, cloneA.getStringToEntityMap().size());
        assertSame(cloneB, cloneA.getStringToEntityMap().get("b"));
        assertSame(cloneC, cloneA.getStringToEntityMap().get("c"));
        assertNotSame(a.getEntityToStringMap(), cloneA.getEntityToStringMap());
        assertEquals(2, cloneA.getEntityToStringMap().size());
        assertEquals("b", cloneA.getEntityToStringMap().get(cloneB));
        assertEquals("c", cloneA.getEntityToStringMap().get(cloneC));

        assertNotSame(a.getStringToEntityListMap(), cloneA.getStringToEntityListMap());
        assertEquals(1, cloneA.getStringToEntityListMap().size());
        List<TestdataEntityCollectionPropertyEntity> entityListOfMap = cloneA.getStringToEntityListMap().get("bc");
        assertEquals(2, entityListOfMap.size());
        assertSame(cloneB, entityListOfMap.get(0));
        assertSame(cloneC, entityListOfMap.get(1));

        assertEntityCollectionPropertyEntityClone(b, cloneB, "b", "1");
        assertEquals(0, cloneB.getEntityList().size());
        assertEquals(0, cloneB.getEntitySet().size());
        assertEquals(0, cloneB.getStringToEntityMap().size());
        assertNull(cloneB.getEntityToStringMap());
        assertNull(cloneB.getStringToEntityListMap());

        assertEntityCollectionPropertyEntityClone(c, cloneC, "c", "3");
        assertEquals(2, cloneC.getEntityList().size());
        assertSame(cloneA, cloneC.getEntityList().get(0));
        assertSame(cloneC, cloneC.getEntityList().get(1));
        assertEquals(2, cloneC.getEntitySet().size());
        assertEquals(2, cloneC.getStringToEntityMap().size());
        assertSame(cloneA, cloneC.getStringToEntityMap().get("a"));
        assertSame(cloneC, cloneC.getStringToEntityMap().get("c"));
        assertNull(cloneC.getEntityToStringMap());
        assertNull(cloneC.getStringToEntityListMap());
    }

    private void assertEntityCollectionPropertyEntityClone(TestdataEntityCollectionPropertyEntity originalEntity,
            TestdataEntityCollectionPropertyEntity cloneEntity, String entityCode, String valueCode) {
        assertNotSame(originalEntity, cloneEntity);
        assertCode(entityCode, originalEntity);
        assertCode(entityCode, cloneEntity);
        assertCode(valueCode, cloneEntity.getValue());
    }

    @Test
    public void deepPlanningClone() {
        SolutionDescriptor solutionDescriptor = TestdataDeepCloningSolution.buildSolutionDescriptor();
        SolutionCloner<TestdataDeepCloningSolution> cloner = createSolutionCloner(solutionDescriptor);

        TestdataValue val1 = new TestdataValue("1");
        TestdataValue val2 = new TestdataValue("2");
        TestdataValue val3 = new TestdataValue("3");
        TestdataDeepCloningEntity a = new TestdataDeepCloningEntity("a", val1);
        List<String> aShadowVariableList = Arrays.asList("shadow a1", "shadow a2");
        a.setShadowVariableList(aShadowVariableList);
        TestdataDeepCloningEntity b = new TestdataDeepCloningEntity("b", val1);
        Map<String, String> bShadowVariableMap = new HashMap<String, String>();
        bShadowVariableMap.put("shadow key b1", "shadow value b1");
        bShadowVariableMap.put("shadow key b2", "shadow value b2");
        b.setShadowVariableMap(bShadowVariableMap);
        TestdataDeepCloningEntity c = new TestdataDeepCloningEntity("c", val3);
        List<String> cShadowVariableList = Arrays.asList("shadow c1", "shadow c2");
        c.setShadowVariableList(cShadowVariableList);
        TestdataDeepCloningEntity d = new TestdataDeepCloningEntity("d", val3);

        TestdataDeepCloningSolution original = new TestdataDeepCloningSolution("solution");
        List<TestdataValue> valueList = Arrays.asList(val1, val2, val3);
        original.setValueList(valueList);
        List<TestdataDeepCloningEntity> originalEntityList = Arrays.asList(a, b, c, d);
        original.setEntityList(originalEntityList);
        List<String> generalShadowVariableList = Arrays.asList("shadow g1", "shadow g2");
        original.setGeneralShadowVariableList(generalShadowVariableList);

        TestdataDeepCloningSolution clone = cloner.cloneSolution(original);

        assertNotSame(original, clone);
        assertCode("solution", clone);
        assertSame(valueList, clone.getValueList());

        List<TestdataDeepCloningEntity> cloneEntityList = clone.getEntityList();
        assertNotSame(originalEntityList, cloneEntityList);
        assertEquals(4, cloneEntityList.size());
        TestdataDeepCloningEntity cloneA = cloneEntityList.get(0);
        assertDeepCloningEntityClone(a, cloneA, "a", "1");
        TestdataDeepCloningEntity cloneB = cloneEntityList.get(1);
        assertDeepCloningEntityClone(b, cloneB, "b", "1");
        TestdataDeepCloningEntity cloneC = cloneEntityList.get(2);
        assertDeepCloningEntityClone(c, cloneC, "c", "3");
        TestdataDeepCloningEntity cloneD = cloneEntityList.get(3);
        assertDeepCloningEntityClone(d, cloneD, "d", "3");

        List<String> cloneGeneralShadowVariableList = clone.getGeneralShadowVariableList();
        assertNotSame(generalShadowVariableList, cloneGeneralShadowVariableList);
        assertEquals(2, cloneGeneralShadowVariableList.size());
        assertSame(generalShadowVariableList.get(0), cloneGeneralShadowVariableList.get(0));
        assertEquals(generalShadowVariableList.get(1), cloneGeneralShadowVariableList.get(1));

        b.setValue(val2);
        assertCode("2", b.getValue());
        // Clone remains unchanged
        assertCode("1", cloneB.getValue());

        b.getShadowVariableMap().put("shadow key b1", "other shadow value b1");
        assertEquals("other shadow value b1", b.getShadowVariableMap().get("shadow key b1"));
        // Clone remains unchanged
        assertEquals("shadow value b1", cloneB.getShadowVariableMap().get("shadow key b1"));
    }

    private void assertDeepCloningEntityClone(TestdataDeepCloningEntity originalEntity, TestdataDeepCloningEntity cloneEntity,
            String entityCode, String valueCode) {
        assertNotSame(originalEntity, cloneEntity);
        assertCode(entityCode, originalEntity);
        assertCode(entityCode, cloneEntity);
        assertSame(originalEntity.getValue(), cloneEntity.getValue());

        List<String> originalShadowVariableList = originalEntity.getShadowVariableList();
        List<String> cloneShadowVariableList = cloneEntity.getShadowVariableList();
        if (originalShadowVariableList == null) {
            assertNull(cloneShadowVariableList);
        } else {
            assertNotSame(originalShadowVariableList, cloneShadowVariableList);
            assertEquals(originalShadowVariableList.size(), cloneShadowVariableList.size());
            for (int i = 0; i < originalShadowVariableList.size(); i++) {
                assertSame(originalShadowVariableList.get(i), cloneShadowVariableList.get(i));
            }
        }

        Map<String, String> originalShadowVariableMap = originalEntity.getShadowVariableMap();
        Map<String, String> cloneShadowVariableMap = cloneEntity.getShadowVariableMap();
        if (originalShadowVariableMap == null) {
            assertNull(cloneShadowVariableMap);
        } else {
            assertNotSame(originalShadowVariableMap, cloneShadowVariableMap);
            assertEquals(originalShadowVariableMap.size(), cloneShadowVariableMap.size());
            for (String key : originalShadowVariableMap.keySet()) {
                assertSame(originalShadowVariableMap.get(key), cloneShadowVariableMap.get(key));
            }
        }
    }

}
