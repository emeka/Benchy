package org.benchy;


import com.db4o.ObjectSet
import static org.benchy.BenchyDbUtils.loadDb

public class BenchyPrint {
     public static void main(String[] args) {
        println("Benchy > Prints a benchmark")

        def options = getOptions(args)

        def db = loadDb()
        try {
            def query = db.query();
            query.constrain(Benchmark.class)
            def id;
            if (options.a) {
                println("Benchy > Printing all benchmarks")
            } else {
                println("Benchy > Printing single benchmark")
                id = options.arguments().get(0)
                query.descend("id").constrain(id)
            }

            ObjectSet results = query.execute()
            if (results.isEmpty()) {
                if (options.a) println("Benchy > No benchmark found")
                else println("Benchy > No benchmark found with id [${id}]")
            } else {
                def count = results.size()
                for (def result in results) {

                    println("Benchy > Benchmark deleted with id [${result.id}]")
                }
                if (options.a) println("Benchy > $count benchmarks deleted")
            }
        } finally {
            db.close()
        }
    }

    private static OptionAccessor getOptions(String[] args) {
        def cli = new CliBuilder(usage: 'benchy-print [options]')
        cli.h(longOpt: 'help', 'Prints this message')
        cli.n(longOpt: 'name', args: 1, argName: 'name', 'The name of the benchmarks to print')
        def options = cli.parse(args)

        if (options == null) {
            cli.usage
            System.exit(1)
        }

        if (options.h) {
            cli.usage
            System.exit(0)
        }
        return options
    }
}
