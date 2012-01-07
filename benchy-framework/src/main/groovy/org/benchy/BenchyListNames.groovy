package org.benchy

import static org.benchy.BenchyDbUtils.loadDb

public class BenchyListNames {

    public static void main(String[] args) {
        println("Benchy > List names")

        def options = getOptions(args)

        def db = loadDb();
        try {
            def query = db.query()
            query.constrain(Benchmark.class)
            def results = query.execute()
            def set = new HashSet<String>()
            for (def benchmark in results){
                set.add(benchmark.name)
            }

            println("Benchy > Found ${set.size()} result(s)")
            def nameList = new LinkedList(set)
            Collections.sort(nameList)
            for(def name in nameList){
                println name
            }
        } finally {
            db.close()
        }
    }

    private static def getOptions(String[] args) {
        def cli = new CliBuilder(usage: 'benchy-listnames [OPTIONS]')
        cli.h(longOpt: 'help', 'Prints this message')
        def options = cli.parse(args)

        if (options == null) {
            cli.usage()
            System.exit(1)
        }

        if (options.h) {
            cli.usage()
            System.exit(0)
        }
        options
    }
}
