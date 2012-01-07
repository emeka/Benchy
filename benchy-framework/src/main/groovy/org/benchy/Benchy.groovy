package org.benchy

class BenchmarkSearcher {

    def db;

    def findNewestBenchmark(String name) {
        def query = db.query();
        query.constrain(Benchmark.class);
        query.descend("name").constrain(name)
        query.ascending("date").orderAscending()
        query.execute()
    }

    def findAllBenchmarks() {
        def query = db.query()
        query.constrain(Benchmark.class)
        query.execute()

    }

    def findAllBenchmarks(String name) {
        def query = db.query();
        query.constrain(Benchmark.class)
        query.descend("name").constrain(name)
        query.execute()
    }
}


class GroovyTestCase extends TestCase {

    def propertyMissing(String name, Object value) { put(name, value)}

    def propertyMissing(String name) { get(name) }

    def average(String property) {
        def sum = 0
        results.each {
                if(it."$property"==null){
                    throw new RuntimeException("Property $property is not found on TestCase $name")
                }
                sum += it."$property"
        }
        sum / results.size()
    }
}

public class GroovyTestCaseResult extends TestCaseResult {
    GroovyTestCaseResult(TestCase testCase) {
        super(testCase);
    }

    def propertyMissing(String name, Object value) { put(name, value)}

    def propertyMissing(String name) { get(name) }
}

