package none.rg.rnddocs;

import java.io.*;
import java.util.*;

public class Main {
    
    private File directory;
    private String path;
    
    private int maxFiles = 10000;
    private int minSize = 5000;
    private int maxSize = 1000000;
    private int lineLength = 80;
    
    private static final byte[] VOWS = "aeoiu".getBytes();
    private static final byte[] CONS = "bcdfghjklmnpqrstvwxz".getBytes();
    
    private byte[] word = new byte[256];
    private int wordLen;
    
    private Random rnd = new Random();
    
    Main(String dirPath) {
        directory = new File(dirPath);
    }
    
    void run() {
        long t = System.currentTimeMillis();
        prepareDir();
        for (int i = 0; i < maxFiles; i++) {
            generateFile(String.format("%s/%08d.txt", path, i));
        }
        t = System.currentTimeMillis() - t;
        System.out.println("Generation time: " + (t / 1000) + " sec");
    }
    
    void generateFile(String name) {
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(name))) {
            generateFile(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    void generateFile(OutputStream output) throws IOException {
        int size = rnd(minSize, maxSize);
        int cnt = 0;
        while (size > 0) {
            generateWord();
            output.write(word, 0, wordLen);
            cnt += wordLen + 1;
            if (cnt < lineLength) {
                output.write(' ');
            } else {
                output.write('\n');
                size -= cnt;
                cnt = 0;
            }
        }
    }
    
    void generateWord() {
        wordLen = rnd(3, 8);
        for (int i = 0; i < wordLen; i++) {
            word[i] = i % 2 == 0 ? CONS[rnd(CONS.length)] : VOWS[rnd(VOWS.length)];
        }
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
            path = directory.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String... args) {
        new Main(args.length > 0 ? args[0] : ".").run();
    }
    
}
