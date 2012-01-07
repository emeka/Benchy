package org.benchy

import com.db4o.ObjectSet
import com.db4o.ObjectContainer
import static org.benchy.BenchyDbUtils.loadDb

class BenchyDelete {

    public static void main(String[] args) {
        println("Benchy > Delete benchmarks")

        OptionAccessor options = getOptions(args)

        def db = loadDb()
        try {
            execute(db, options)
        } finally {
            db.close()
        }
    }

    private static def execute(ObjectContainer db, OptionAccessor options) {
        def query = db.query();
        query.constrain(Benchmark.class)
        def id;
        if (options.a) {
            println("Benchy > Deleting all benchmarks")
        } else {
            id = options.arguments().get(0)
            if (options.n) {
                println("Benchy > Deleting single benchmark by name")
                query.descend("name").constrain(id)
            } else {
                println("Benchy > Deleting single benchmark by id")
                query.descend("id").constrain(id)
            }
        }

        ObjectSet benchmarks = query.execute()

        if (benchmarks.isEmpty()) {
            if (options.a) println("Benchy > No benchmark found to delete")
            else if (options.n) println("Benchy > No benchmark found with name [${id}] to delete")
            else println("Benchy > No benchmark found with id [${id}] to delete")
        } else {
            def count = benchmarks.size()
            for (def benchmark in benchmarks) {
                db.delete(benchmark)
                println("Benchy > Benchmark deleted with id [${benchmark.id}]")
            }
            println("Benchy > $count benchmarks deleted")
        }
    }

    private static OptionAccessor getOptions(String[] args) {
        def cli = new CliBuilder(usage: 'benchy-delete [options] [ID]')
        cli.a(longOpt: 'all', 'Deletes all benchmarks (be careful with this)')
        cli.n(longOpt: 'name', 'Deletes by name instead of ID')
        cli.h(longOpt: 'help', 'Prints this message')
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
