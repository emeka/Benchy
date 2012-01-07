package org.benchy

import java.text.SimpleDateFormat
import static org.benchy.BenchyDbUtils.loadDb

class BenchyList {

    public static void main(String[] args) {
        println("Benchy > List benchmarks")

        def options = getOptions(args)

        def db = loadDb();
        try {
            def query = db.query()
            query.constrain(Benchmark.class)
            query.descend("date").orderAscending();

            if (options.n) {
                query.descend("name").constrain(options.ns.get(0))
            } else if (options.i) {
                query.descend("id").constrain(options.is.get(0))
            }

            def results = query.execute()
            println("Benchy > Found ${results.size()} result(s)")
            def benchmarkIndex = 1
            for (def benchmark in results) {
                def date = new SimpleDateFormat("yyyy-M-dd HH:mm:ss").format(benchmark.date)
                def number = "${benchmarkIndex}".padLeft(5, ' ')
                def testCaseIndent = "".padLeft(10, ' ')
                def testCaseResultIndent = "".padLeft(15, ' ')
                if (!options.c) {
                    println "$number ${benchmark.id} ${date} ${benchmark.name}"
                } else {
                    println "$number ${benchmark.id} ${date} ${benchmark.name}"
                    int testCaseIndex = 1
                    benchmark.testCases.each {
                        println "$testCaseIndent $testCaseIndex ${it.id} ${date}"
                        for (def entry: it.settings.entrySet()) {
                            if (entry.key != "id")
                                println "$testCaseIndent   ${entry.key}:${entry.value}"
                        }
                        it.results.each {
                            for (def entry: it.properties.entrySet()) {
                                println "$testCaseResultIndent  ${entry.key}:${entry.value}"
                            }
                            println ""
                        }
                        testCaseIndex++
                    }
                }
                benchmarkIndex++
            }
        } finally {
            db.close()
        }
    }

    private static OptionAccessor getOptions(String[] args) {
        def cli = new CliBuilder(usage: 'benchy-list [OPTIONS]')
        cli.n(longOpt: 'name', args: 1, argName: 'name', 'The name of the benchmarks to list')
        cli.i(longOpt: 'id', args: 1, argName: 'id', 'The id of the benchmarks to list')
        cli.h(longOpt: 'help', 'Prints this message')
        cli.c(longOpt: 'content', 'Prints the content')
        def options = cli.parse(args)

        if (options == null) {
            cli.usage()
            System.exit(1)
        }

        if (options.h) {
            cli.usage()
            System.exit(0)
        }

        return options
    }
}
