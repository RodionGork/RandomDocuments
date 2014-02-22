package none.rg.rnddocs;

import java.io.*;
import java.util.*;

public class Main {
    
    private File directory;
    private String path;
    
    private Generator gen;
    
    Main(String dirPath) {
        directory = new File(dirPath);
        gen = new Generator();
    }
    
    void run() {
        long t = System.currentTimeMillis();
        prepareDir();
        gen.generate(path);
        t = System.currentTimeMillis() - t;
        System.out.println("Generation time: " + (t / 1000) + " sec");
    }
    
    void prepareDir() {
        try {
            if (!directory.exists()) {
                directory.mkdir();
            } else if (!directory.isDirectory()) {
                throw new RuntimeException("Invalid directory path");
            }
            path = directory.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String... args) {
        new Main(args.length > 0 ? args[0] : ".").run();
    }
    
}
