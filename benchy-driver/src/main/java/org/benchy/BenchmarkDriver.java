package org.benchy;

/**
 * The BenchmarkDriver contains the algorithm you want to test.
 *
 * @author Peter Veentjer.
 */
public abstract class BenchmarkDriver {

    protected volatile boolean shutdown = false;

    /**
     * Sets up the driver (essentially the same as what you do with a @Before annotation using junit.
     */
    public void setUp(){}

    /**
     * Executes the actual benchmark.
     *
     * @param result
     */
    public abstract void run(TestCaseResult result);

    /**
     * Processes the actual results. This method is called after the {@link #run(TestCaseResult)} completes
     * successfully and before the {@link #tearDown()} is called. It gives the ability to derive additional
     * statistics that can be stored in the {@link org.benchy.TestCaseResult}.
     *
     * @param result
     */
    public void processResults(TestCaseResult result){}

    /**
     * Tears down the driver (essentially the same as what you do with a @After annotation using junit.
     */
    public void tearDown(){}

    /**
     *
     */
    public final void init(){
        shutdown = false;
    }

    /**
     *
     */
    public final void shutdown(){
        shutdown = true;
    }
}
