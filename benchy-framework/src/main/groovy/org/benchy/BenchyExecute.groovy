package org.benchy

import com.db4o.ObjectContainer
import static org.benchy.BenchyDbUtils.loadDb

class BenchyExecute {

  public static void main(String[] args) {
    println("Benchy > Executing benchmark")

    def options = getOptions(args)

    def filename = options.arguments().get(0)
    def file = new File(filename)
    if (!file.isFile()) {
      println("Benchy > [${filename}] is not a file");
      System.exit(1)
    }

    println("Benchy > Loading benchmark '${file.getAbsolutePath()}'");

    def db = loadDb()
    try {
      execute(file, db, options)
    } finally {
      db.close()
    }

    println("Benchy > Finished running benchmark")
  }

  private static def execute(File file, ObjectContainer db, OptionAccessor options) {
    def binding = new Binding()
    def loader = new GroovyClassLoader(BenchyExecute.class.getClassLoader())
    if (options.cp) {
      for (def classpath in options.cp.split(':')) {
        loader.addClasspath(classpath)
      }
    }

    int processorCount = Runtime.getRuntime().availableProcessors()
    if (options.p) {
      try{
      processorCount = Integer.parseInt(options.p);
      }catch(NumberFormatException e){
        println("Benchy > ${options.p} is not a valid number of processors")
        System.exit(1)
      }

      if(processorCount<1){
        println("Benchy > Processor count [${options.p}] can't be smaller than 0")
        System.exit(1)
      }
    }

    println("Benchy > Running with [$processorCount] processors")
    binding.setVariable("processorCount", processorCount)

    def shell = new GroovyShell(loader, binding)
    def benchmark = (Benchmark) shell.evaluate(file)
    println("Benchy > Benchmark loaded successfully")
    benchmark.run()

    db.store benchmark
  }

  private static OptionAccessor getOptions(String[] args) {
    def cli = new CliBuilder(usage: 'benchy-execute [options] [script]')
    cli.h(longOpt: 'help', 'Prints this message')
    cli.p(longOpt: 'processorcount', args: 1, argName: 'processor', 'The number of processors available for running')
    cli.cp(longOpt: 'classpath', args: 1, argName: 'classpath', 'Sets the classpath of the non benchy classes')
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
      println("Benchy > Expected a single benchmark script to execute")
      cli.usage()
      System.exit(1)
    }
    return options
  }
}
