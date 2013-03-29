/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 
package org.apache.jdo.tck.api.persistencemanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Constants;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B> Test GetProperties
 *<BR>
 *<B>Keywords:</B> getProperties getSupportedProperties setProperty
 *<BR>
 *<B>Assertion IDs:</B> 
 *<BR>
 *<B>Assertion Description: </B>
 */

public class GetProperties extends JDO_Test implements Constants {

    /** */
    private static final String ASSERTION_FAILED_12_19_1 = 
        "Assertion 12-9-1 setProperty() Set the property name to the specified value. " +
        "If a vendor-specific property is not recognized, it is silently ignored. ";
    private static final String ASSERTION_FAILED_12_19_2 = 
        "Assertion 12-9-2 setProperty() If the value for the property is not supported by the implementation, " +
        "a JDOUserException is thrown. ";
    private static final String ASSERTION_FAILED_12_19_3 = 
        "Assertion 12-9-3 getProperties() Return a map of String, " +
        "Object with the properties and values currently in effect. ";
    private static final String ASSERTION_FAILED_12_19_4 = 
        "Assertion 12-9-4 Changing the values in the map will not affect the properties in the PersistenceManager. ";
    private static final String ASSERTION_FAILED_12_19_5 = 
        "Assertion 12-9-5 getSupportedProperties() Return the set of properties supported by this PersistenceManager. ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetProperties.class);
    }

    private Collection<String> supportedOptions;
    private Set<String> supportedProperties;
    private static Set<TestProperty> testRequiredProperties = new HashSet<TestProperty>();
    private static Map<String, Set<TestProperty>> testOptionalProperties = new HashMap<String, Set<TestProperty>>();

    private interface TestProperty {
        void test(PersistenceManager pm, Set<String> supportedProperties);
        Object get(PersistenceManager pm);
        void set(PersistenceManager pm, Object value);
    }

    abstract class AbstractTestProperty implements TestProperty {
        String propertyName;
        Object testValue1;
        Object testValue2;
        AbstractTestProperty(String propertyName, Object testValue1, Object testValue2) {
            this.propertyName = propertyName;
            this.testValue1 = testValue1;
            this.testValue2 = testValue2;
        }
    }

    /** For each non-api supported property,
     * getProperty(XXX) should return a value
     * setProperty(XXX, value1) should succeed
     * getProperties() should return value1
     * setProperty(XXX, value2) should succeed
     * getProperties() should return value2
     */
    abstract class AbstractNonAPITestProperty extends AbstractTestProperty {
        AbstractNonAPITestProperty(String propertyName, Object testValue1, Object testValue2) {
            super(propertyName, testValue1, testValue2);
        }
        public Object get(PersistenceManager pm) {throw new RuntimeException("not implemented");};
        public void set(PersistenceManager pm, Object value) {throw new RuntimeException("not implemented");};
        public void test(PersistenceManager pm, Set<String> supportedProperties) {
            Object result0 = pm.getProperties().get(propertyName);
            errorIfEqual("getProperties().get(" + propertyName + ")", null, result0);
            pm.setProperty(propertyName, testValue1);
            Object result1 = pm.getProperties().get(propertyName);
            errorIfNotEqual("after pm.setProperty(" + propertyName + ", " + testValue1 + "), getProperties().get(" + propertyName + ")",
                    testValue1, result1);

            pm.setProperty(propertyName, testValue2);
            Object result2 = pm.getProperties().get(propertyName);
            errorIfNotEqual("after pm.setProperty(" + propertyName + ", " + testValue2 + "), getProperties().get(" + propertyName + ")",
                    testValue2, result2);
        }
    }

    /** For each required property, and for each of two values
     * setProperty(XXX, value1) should succeed
     * getXXX() should return value1
     * setXXX(value2) should succeed
     * getProperties() should return value2
     */
    abstract class AbstractAPITestProperty extends AbstractTestProperty {
        AbstractAPITestProperty(String propertyName, Object testValue1, Object testValue2) {
            super(propertyName, testValue1, testValue2);
        }
        public void test(PersistenceManager pm, Set<String> supportedProperties) {
            pm.setProperty(propertyName, testValue1);
            Object result1 = get(pm);
            errorIfNotEqual("after pm.setProperty(" + propertyName + ", " + testValue1 + "), getXXX for " + propertyName,
                    testValue1, result1);

            set(pm, testValue2);
            Object result2 = pm.getProperties().get(propertyName);
            errorIfNotEqual("after pm.setXXX(), getProperties.get(" + propertyName + ")",
                    testValue2, result2);
        }
    };

    private TestProperty testCopyOnAttach = 
           new AbstractAPITestProperty(PROPERTY_COPY_ON_ATTACH, true, false) {
        public Object get(PersistenceManager pm) {return pm.getCopyOnAttach();}
        public void set(PersistenceManager pm, Object value) {pm.setCopyOnAttach((Boolean) value);}
    };

    private TestProperty testDatastoreReadTimeoutMillis = 
        new AbstractAPITestProperty(PROPERTY_DATASTORE_READ_TIMEOUT_MILLIS, 20, 40) {
        public Object get(PersistenceManager pm) {return pm.getDatastoreReadTimeoutMillis();}
        public void set(PersistenceManager pm, Object value) {pm.setDatastoreReadTimeoutMillis((Integer) value);}
    };

    private TestProperty testDatastoreWriteTimeoutMillis = 
            new AbstractAPITestProperty(PROPERTY_DATASTORE_WRITE_TIMEOUT_MILLIS, 60, 80) {
        public Object get(PersistenceManager pm) {return pm.getDatastoreWriteTimeoutMillis();}
        public void set(PersistenceManager pm, Object value) {pm.setDatastoreWriteTimeoutMillis((Integer) value);}
    };

    private TestProperty testMultithreaded = 
            new AbstractAPITestProperty(PROPERTY_MULTITHREADED, true, false) {
        public Object get(PersistenceManager pm) {return pm.getMultithreaded();}
        public void set(PersistenceManager pm, Object value) {pm.setMultithreaded((Boolean) value);}
    };

    private TestProperty testDetachAllOnCommit =
            new AbstractAPITestProperty(PROPERTY_DETACH_ALL_ON_COMMIT, true, false) {
        public Object get(PersistenceManager pm) {return pm.getDetachAllOnCommit();}
        public void set(PersistenceManager pm, Object value) {pm.setDetachAllOnCommit((Boolean) value);}
    };

    private TestProperty testIgnoreCache =
        new AbstractAPITestProperty(PROPERTY_IGNORE_CACHE, true, false) {
    public Object get(PersistenceManager pm) {return pm.getIgnoreCache();}
    public void set(PersistenceManager pm, Object value) {pm.setIgnoreCache((Boolean) value);}
};

    private TestProperty testOptimistic =
        new AbstractNonAPITestProperty(PROPERTY_OPTIMISTIC, true, false) {
        public Object get(PersistenceManager pm) {return pm.getIgnoreCache();}
        public void set(PersistenceManager pm, Object value) {pm.setIgnoreCache((Boolean) value);}
    };

    private TestProperty testRetainValues =
        new AbstractNonAPITestProperty(PROPERTY_RETAIN_VALUES, true, false) {
    };

    private TestProperty testRestoreValues =
        new AbstractNonAPITestProperty(PROPERTY_RESTORE_VALUES, true, false) {
    };

    private TestProperty testNontransactionalRead =
        new AbstractNonAPITestProperty(PROPERTY_NONTRANSACTIONAL_READ, true, false) {
    };

    private TestProperty testNontransactionalWrite =
        new AbstractNonAPITestProperty(PROPERTY_NONTRANSACTIONAL_WRITE, true, false) {
    };

    private Set<TestProperty> setOf(TestProperty... testPropertys) {
        Set<TestProperty> result = new HashSet<TestProperty>();
        for (TestProperty testProperty: testPropertys) {
         result.add(testProperty);
        }
        return result;
    }

    /**
     * For each option supported by the PMF, test that 
     * the corresponding pm property is supported.
     */
    public void testGetSupportedProperties() {
        testRequiredProperties.add(testCopyOnAttach);
        testRequiredProperties.add(testDetachAllOnCommit);
        testRequiredProperties.add(testIgnoreCache);
        testRequiredProperties.add(testOptimistic);
        testRequiredProperties.add(testRetainValues);
        testRequiredProperties.add(testRestoreValues);
        testRequiredProperties.add(testNontransactionalRead);
        testRequiredProperties.add(testNontransactionalWrite);
        
        testOptionalProperties.put(PROPERTY_MULTITHREADED, setOf(testMultithreaded));
        testOptionalProperties.put(OPTION_DATASTORE_TIMEOUT, 
                setOf(testDatastoreReadTimeoutMillis, testDatastoreWriteTimeoutMillis));
        getPM();
        
        supportedOptions = pmf.supportedOptions();
        for (String supportedOption: supportedOptions) {
            System.out.println("supportedOptions returned: " + supportedOption);
        }

        supportedProperties = pm.getSupportedProperties();
        for (String supportedProperty: supportedProperties) {
            System.out.println("supportedProperties returned: " + supportedProperty);
        }

        // test required properties
        for (TestProperty testProperty: testRequiredProperties) {
            testProperty.test(pm, supportedProperties);
        }
        
        Set<Map.Entry<String, Object>> properties = pm.getProperties().entrySet();
        for (Map.Entry<String, Object> entry: properties) {
            System.out.println("getProperties returned: " + entry.getKey() + ": " + entry.getValue());
        }

        // for each supported option, test the corresponding supported property
        for (String supportedOption: supportedOptions) {
            Set<TestProperty> supportedOptionTestList = testOptionalProperties.get(supportedOption);
            if (supportedOptionTestList != null) {
                for (TestProperty supportedOptionTest: supportedOptionTestList) {
                    System.out.println("testing " + supportedOption);
                    supportedOptionTest.test(pm, supportedProperties);
                }
            }
        }

        failOnError();
    }


}
