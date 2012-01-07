package org.benchy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.UUID.randomUUID;

/**
 * Contains all the settings for a single execution for a driver.
 *
 * Each TestCase can executed more than once, leading to multiple testcase results.
 *
 * A TestCase is not threadsafe.
 *
 * @author Peter Veentjer.
 */
public class TestCase {

    private static final String PROPERTY_TEST_CASE_ITERATION_COUNT = "testCaseIterations";
    private static final String PROPERTY_WARMUP_RUN_ITERATION_COUNT = "warmupRunIterationCount";
    private static final String PROPERTY_TESTCASENAME = "testcasename";
    private static final String PROPERTY_DRIVER = "driver";
    private static final String NAME_DURATION = "durationInSeconds";
    private static final String PROPERTY_ID = "id";

    private final Map<String, Object> settings = new HashMap<String, Object>();
    private final List<TestCaseResult> results = new LinkedList<TestCaseResult>();

    /***
     * Creates a TestCase.
     */
    public TestCase() {
        settings.put(PROPERTY_ID, randomUUID().toString());
        setTestCaseIterationCount(1L);
        setWarmupRunIterationCount(1L);
        setDurationInSeconds(Long.MAX_VALUE);
    }

    /**
     * Returns the list containing the TestCaseResults.
     *
     * @return the list containing the TestCaseResults.
     */
    public List<TestCaseResult> getResults() {
        return results;
    }

    /**
     * Removes all TestCaseResults if there are any.
     */
    public void clearResults() {
        results.clear();
    }

    /**
     * Adds a {@link org.benchy.TestCaseResult}. No checks are executed if the TesCaseResult already is one of the results.
     *
     * @param result the TestCaseResult to add.
     * @throws NullPointerException if result is null.
     */
    public void add(TestCaseResult result) {
        if(result == null)throw new NullPointerException();
        results.add(result);
    }

    /**
     * Puts a key/value in this TestCase.
     *
     * @param property the property of the property
     * @param value the value of the property
     * @throws NullPointerException if property is null.
     */
    public void put(String property, Object value) {
        if(property == null)throw new NullPointerException();
        settings.put(property, value);
    }

    /**
     * Gets  a value for a property.
     *
     * @param property the property of the property.
     * @return the value, or null if not found
     */
    public Object get(String property) {
        if(property == null)throw new NullPointerException();
        return settings.get(property);
    }

    /**
     * Returns all properties stored in this TestCase. No copy is made, so you are able to fuck it up.
     *
     * @return all properties stored in this TestCase.
     */
    public Map<String, Object> getSettings() {
        return settings;
    }

    /**
     * Returns the id that uniquely identifies this TestCase.
     *
     * @return the id
     */
    public String getId() {
        return (String) settings.get(PROPERTY_ID);
    }

    /**
     * Returns the duration (so the time this testcase should be executed) in seconds. Long.MAX_VALUE insidcates that
     * there is no bound on the duration.
     *
     * @return thd duration in seconds.
     */
    public long getDurationInSeconds() {
         return (Long) settings.get(NAME_DURATION);
    }

    /**
     * Sets the duration this testcase should be executed in seconds.
     *
     * @param duration the duration in seconds.
     */
    public void setDurationInSeconds(long duration) {
        this.settings.put(NAME_DURATION, duration);
    }

    /**
     * Sets the driver
     *
     * @param driver the driver to set
     * @throws NullPointerException if driver is null.
     */
    public void setDriver(Class<BenchmarkDriver> driver) {
        if(driver == null)throw new NullPointerException();
        settings.put(PROPERTY_DRIVER, driver);
    }

    public Class<BenchmarkDriver> getDriver() {
        return (Class<BenchmarkDriver>) settings.get(PROPERTY_DRIVER);
    }

    /**
     * Sets the name of this TestCase.
     *
     * @param name the name of the TestCase.
     */
    public void setName(String name) {
        settings.put(PROPERTY_TESTCASENAME, name);
    }

    /**
     * Gets the nae of this TestCase.
     *
     * @return the name of this TestCase.
     */
    public String getName() {
        return (String) settings.get(PROPERTY_TESTCASENAME);
    }

    /**
     * Returns the warmup run iteration count.
     *
     * @return the warmup run iteration count.
     */
    public long getWarmupRunIterationCount() {
        return (Long) settings.get(PROPERTY_WARMUP_RUN_ITERATION_COUNT);
    }

    public void setWarmupRunIterationCount(long iterations) {
        settings.put(PROPERTY_WARMUP_RUN_ITERATION_COUNT, iterations);
    }

    public long getTestCaseIterationCount() {
        return (Long) settings.get(PROPERTY_TEST_CASE_ITERATION_COUNT);
    }

    public void setTestCaseIterationCount(long iterations) {
        settings.put(PROPERTY_TEST_CASE_ITERATION_COUNT, iterations);
    }

    @Override
    public String toString(){
        return "TestCase"+settings.toString();
    }
}
