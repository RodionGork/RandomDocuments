package none.rg.rnddocs;

import java.io.*;
import java.util.*;

public class Main {
    
    private File directory;
    private String path;
    
    private Generator gen;
    
    Main(String dirPath, Properties props) {
        directory = new File(dirPath);
        gen = new Generator(props);
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
        List<String> argList = new LinkedList<>(Arrays.asList(args));
        Properties props = propsFromArgs(argList);
        if (props.getProperty("help") != null) {
            printHelp();
            return;
        }
        new Main(argList.isEmpty() ? "." : argList.get(0), props).run();
    }
    
    static Properties propsFromArgs(List<String> args) {
        Properties props = new Properties();
        while (!args.isEmpty() && args.get(0).startsWith("--")) {
            String[] parts = args.remove(0).substring(2).split("=", 2);
            if (parts.length > 1) {
                props.setProperty(parts[0], parts[1]);
            } else {
                props.setProperty(parts[0], "");
            }
        }
        return props;
    }
    
    static void printHelp() {
        System.out.println("USAGE: java -jar RndDocs.jar [settings] directory");
        System.out.println("\tSettings:");
        System.out.println("\t\t--files=N - number of files");
        System.out.println("\t\t--words=N - number of different words");
        System.out.println("\t\t--cores=N - number of threads");
        System.out.println("\t\t--maxSize=N - max file size");
        System.out.println("\t\t--minSize=N - min file size");
        System.out.println("\t\t--lineLength=N - length of lines");
    }
}
