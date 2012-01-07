package org.benchy;

import java.util.HashMap;
import java.util.Map;

import static java.util.UUID.randomUUID;

/**
 * Contains the result of a TestCase execution. Essentially it is a key/value map so that it can store any kind of result.
 *
 * TestCaseResult is not threadsafe.
 *
 * @author Peter Veentjer.
 */
public class TestCaseResult {

    private final TestCase testCase;
    private final Map<String, Object> properties = new HashMap<String, Object>();

    /**
     * Creates a new TestCaseResult.
     *
     * @param testCase the TestCase this TestCaseResult belongs to.
     * @throws NullPointerException if testCase is null.
     */
    public TestCaseResult(TestCase testCase) {
        if(testCase == null)throw new NullPointerException();
        this.testCase = testCase;
        this.properties.put("id", randomUUID().toString());
    }

    /**
     * Gets the id of this TestCaseResult.
     *
     * @return the id of this TestCaseResult.
     */
    public String getId() {
        return (String) properties.get("id");
    }

    /**
     * Gets the duration this TestCaseResult took to complete in milliseconds.
     *
     * @return the duration in milliseconds.
     */
    public long getDurationMs() {
        return (Long) properties.get("duration[ms]");
    }

    /**
     * Gets a property with a specific name. Null is returned if nothing is found.
     *
     * @param name the name of the property.
     * @return the value, or null of nothing is found.
     */
    public Object get(String name) {
        return properties.get(name);
    }

    /**
     * Sets a property value. If the property already exists, it is overwritten.
     *
     * @param name the name of the property
     * @param value the value of the property.
     * @throws NullPointerException if name is null.
     */
    public void put(String name, Object value) {
        if(name == null)throw new NullPointerException();
        properties.put(name, value);
    }

    /***
     * Gets all the properties stored in this TestCaseResult.
     *
     * @return the properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Gets the TestCase this TestCaseResult belongs to.
     *
     * @return the TestCase this TestCaseResult belongs to.
     */
    public TestCase getTestCase() {
        return testCase;
    }

    @Override
    public String toString() {
        return "TestCaseResult{" +
                "testCase=" + testCase +
                ", properties=" + properties +
                '}';
    }
}
