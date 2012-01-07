package org.benchy

import java.lang.reflect.Field
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import static java.util.UUID.randomUUID

class Benchmark {
    List<TestCase> testCases = new LinkedList<TestCase>()
    Date date = new Date()
    String id = randomUUID().toString()
    String name = ""
    Map<String,Object> settings = new HashMap<String,Object>()

    Benchmark(){
        //try{
        //RuntimeMXBean RuntimemxBean = ManagementFactory.getRuntimeMXBean();
        //List<String> arguments = RuntimemxBean.getInputArguments();
        //settings.put("jvmsettings", arguments);
        //}catch()
    }

    def add(TestCase testCase) {
        testCases.add(testCase);
    }

    def propertyMissing(String name, Object value) { settings.put(name, value)}

    def propertyMissing(String name) { settings.get(name) }

    void run() {
        println("Benchy > Starting benchmark [$name] and id [${id}] with a total of ${testCases.size()} testcases")
        testCases.each{it.clearResults()}

        def startMs = System.currentTimeMillis();
        int testCaseIteration = 1;
        testCases.each {
            def testCase = it
            println("Benchy > =======================================================")
            println("Benchy > Starting testcase [$testCaseIteration/${testCases.size()}] '${testCase.name}'")
            println("Benchy > Driver [${testCase.driver.getName()}]")
            def driver = (BenchmarkDriver)testCase.driver.newInstance()
            def settings = testCase.getSettings()
            populateDriver(settings, driver)

            if (testCase.warmupRunIterationCount > 0) {
                println("Benchy > Starting warmup with a total of ${testCase.warmupRunIterationCount} iterations")
                for (def iteration in 1..testCase.warmupRunIterationCount) {
                    run(testCase,driver, iteration, testCase.warmupRunIterationCount, true)
                }
                println("Benchy > Finished warmup")
            } else {
                println("Benchy > Skipping warmup")
            }

            println("Benchy > -------------------------------------------------------")
            println("Benchy > Executing a total of ${testCase.warmupRunIterationCount} benchmark iterations")

            for (def iteration in 1..testCase.testCaseIterationCount) {
                def result = run(testCase,driver, iteration, testCase.testCaseIterationCount, false)
                testCase.add(result)
            }
            testCaseIteration++;
        }
        println("Benchy > =======================================================")
        def durationMs = System.currentTimeMillis() - startMs;
        //this.durationMs = durationMs
        println("Benchy > Benchmark completed in ${durationMs/1000} seconds")
    }

    private TestCaseResult run(TestCase testCase, BenchmarkDriver driver, long iteration, long maxIterations, boolean warmup) {
        println("Benchy > -------------------------------------------------------")
        if (warmup) {
            println("Benchy > Running warmup iteration [$iteration/$maxIterations] for testcase [${testCase.name}]")
        } else {
            println("Benchy > Running iteration [$iteration/$maxIterations] for testcase [${testCase.name}]")
        }
        def result = new GroovyTestCaseResult(testCase)
        println("Benchy > Setting up driver")


        def timed = testCase.durationInSeconds < Long.MAX_VALUE
        def scheduledExecutor = null
        if(timed){
            println("Benchy > Running for ${testCase.durationInSeconds} seconds")
            scheduledExecutor = new ScheduledThreadPoolExecutor(1);
            scheduledExecutor.schedule (new ShutdownRunnable(driver),testCase.durationInSeconds, TimeUnit.SECONDS)
        }

        driver.init()
        driver.setUp()

        def startDate = new Date()
        def startMs = System.currentTimeMillis()

        println("Benchy > Starting benchmark")
        Throwable thrown = null;
        try {
            driver.run(result)
        } catch (Throwable t) {
            println("Benchy > An exception was thrown, continuing to the next testcase")
            t.printStackTrace()
            thrown = t
        }
        def endMs = System.currentTimeMillis()
        def durationMs = endMs - startMs

        println("Benchy > Testcase took ${durationMs/1000} seconds")

        result.testCaseIteration = iteration;
        result.thrown = thrown;
        result.'start[ms]' = startMs
        result.'end[ms]' = endMs
        result.'duration[ms]' = durationMs
        result.'start[date]' = startDate

        if (thrown == null) {
            println("Benchy > Processing results")
            driver.processResults(result)
        }
        println("Benchy > Tearing down driver")
        driver.tearDown()
        if(timed){
            println("Benchy > Waiting for benchmark to complete")
            scheduledExecutor.shutdown()
            scheduledExecutor.awaitTermination(3600, TimeUnit.SECONDS)
        }

        result
    }

    private class ShutdownRunnable implements Runnable{
        private final BenchmarkDriver driver;

        ShutdownRunnable(BenchmarkDriver driver) {
            this.driver = driver
        }

        @Override
        void run() {
            println("Benchy > Shutting down driver")
            driver.shutdown()
        }
    }

    private def populateDriver(Map<String, Object> settings, BenchmarkDriver driver) {
        for (def entry in settings.entrySet()) {
            def property = entry.key
            def value = entry.value
            try {
                Field field = driver.class.getDeclaredField(property)
                field.setAccessible true
                field.set(driver, value)
            } catch (NoSuchFieldException ignore) {
            }
        }
    }
}
