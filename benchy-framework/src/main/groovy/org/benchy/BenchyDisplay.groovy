package org.benchy

import com.db4o.ObjectContainer
import static org.benchy.BenchyDbUtils.loadDb

class BenchyDisplay {

    public static void main(String[] args) {
        println("Benchy > Displaying benchmark")

        def options = getOptions(args)

        File scriptFile = getScriptFile(options)
        def db = loadDb()
        try {
            execute(db, scriptFile)
        } finally {
            db.close()
        }
    }

    private static File getScriptFile(OptionAccessor options) {
        def filename = options.arguments().get(0)
        def file = new File(filename)
        if (!file.isFile()) {
            println("Benchy > [${filename}] is not a file");
            System.exit(1)
        }
        return file
    }

    private static def execute(ObjectContainer db, File file) {
        def binding = new Binding()
        def parent = BenchyDisplay.class.getClassLoader()
        def loader = new GroovyClassLoader(parent)
        def shell = new GroovyShell(loader, binding)

        binding.setVariable("db", db)
        def searcher = new BenchmarkSearcher()
        searcher.db = db;
        binding.setVariable("searcher", searcher)
        println("Benchy > Loading show script [${file.getAbsolutePath()}]");
        shell.evaluate(file)
        println("Benchy > Showscript loaded successfully")
    }

    private static OptionAccessor getOptions(String[] args) {
        def cli = new CliBuilder(usage: 'benchy-display [options] [script]')
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

        if (options.arguments().size() != 1) {
            println("Benchy > Expected a single display script to execute")
            cli.usage()
            System.exit(1)
        }
        return options
    }
}
