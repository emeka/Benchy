package org.benchy

import com.db4o.Db4o

class BenchyDbUtils {
    static def loadDb() {
        def homedir = System.getProperty("user.home")
        new File(homedir + "/benchy").mkdirs()
        Db4o.configure().activationDepth(10);
        Db4o.openFile("${homedir}/benchy/Benchy")
    }
}