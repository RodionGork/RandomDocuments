package none.rg.rnddocs;

import java.io.*;

public class Main {
    
    private File directory;
    private String path;
    
    private int maxFiles = 1000;
    private int minSize = 5000;
    private int maxSize = 100000;
    private int lineLength = 80;
    
    private static final byte[] VOWS = "aeoiu".getBytes();
    private static final cons[] CONS = "bcdfghjklmnpqrstvwxz".getBytes();
    
    private Random rnd = new Random();
    
    Main(String dirPath) {
        directory = new File(dirPath);
    }
    
    void run() {
        prepareDir();
        for (int i = 0; i < maxFiles; i++) {
            generateFile(String.format("%s/%8d.txt"));
        }
    }
    
    void generateFile(String name) {
        try (PrintStream output = new PrintStream(new FileOutputStream(name))) {
            generateFile(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    void generateFile(PrintStream output) {
        int size = rnd(minSize, maxSize);
        StringBuilder sb = new StringBuilder();
        while (size > 0) {
            sb.append(generateWord);
            if (sb.length() < lineLength) {
                sb.append(' ');
            } else {
                output.println(sb);
                size -= sb.length() + 1;
                sb.setLength(0);
            }
        }
    }
    
    String generateWord() {
        return "paka";
    }
    
    int rnd(int a, int b) {
        return rnd.nextInt(b - a) + a;
    }
    
    int rnd(int b) {
        return rnd.nextInt(b);
    }
    
    void prepareDir() {
        try {
            if (!directory.exists()) {
                directory.mkdir();
            } else if (!directory.isDirectory()) {
                throw new RuntimeException("Invalid directory path");
            }
            path = dir.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String... args) {
        new Main(args.length > 0 ? args[0] : ".").run();
    }
    
}
