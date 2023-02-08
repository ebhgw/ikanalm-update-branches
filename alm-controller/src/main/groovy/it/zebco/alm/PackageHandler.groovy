package it.zebco.alm

import org.codehaus.groovy.GroovyException

class PackageHandler {

    static String getService(String sourceDirPath) {
        File dir = new File(sourceDirPath + '/service')
        def countSrvi = 0
        def countSrvs = 0
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName().toLowerCase();
                if (name.endsWith(".srvi"))
                    countSrvi++
                if (name.endsWith(".srvs"))
                    countSrvs++
                return name.endsWith(".srvi") && pathname.isFile();
            }
        })

        if (countSrvi == 0 && countSrvs == 0) {
            ''
        } else if (countSrvi == 1 && countSrvs == 1) {
            files[0].name.take(4)
        } else {
            throw new GroovyException("Check package. Only one service per package")
        }
    }
}
