package none.rg.rnddocs;

import java.io.*;
import java.util.*;

public class Generator {

    private int numFiles;
    private int minSize;
    private int maxSize;
    private int lineLength;
    private int numWords;
    
    private Random rnd = new Random();
    
    private static final byte[] VOWS = "aeoiu".getBytes();
    private static final byte[] CONS = "bcdfghjklmnpqrstvwxz".getBytes();
    
    private byte[][] words;
    
    public Generator(Properties props) {
        numFiles = settings(props, "files", 1000);
        minSize = settings(props, "minSize", 5000);
        maxSize = settings(props, "maxSize", 100000);
        lineLength = settings(props, "lineLength", 70);
        numWords = settings(props, "words", 20000);
    }
    
    private int settings(Properties props, String name, int def) {
        String v = props.getProperty(name);
        if (v == null || !v.matches("\\d+")) {
            return def;
        }
        return Integer.parseInt(v);
    }
    
    public void generate(String path) {
        for (int i = 0; i < numFiles; i++) {
            generateFile(String.format("%s/%08d.txt", path, i));
        }
    }
    
    private void generateFile(String name) {
        prepareWords(false);
        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(name))) {
            generateFile(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void generateFile(OutputStream output) throws IOException {
        int size = rnd(minSize, maxSize);
        int cnt = 0;
        int pos = 0;
        while (size > 0) {
            pos = (pos + rnd(5) + 1) % words.length;
            byte[] w = words[pos];
            output.write(w, 0, w.length);
            cnt += w.length + 1;
            if (cnt < lineLength) {
                output.write(' ');
            } else {
                output.write('\n');
                size -= cnt;
                cnt = 0;
            }
        }
    }
    
    private void prepareWords(boolean force) {
        if (words != null && !force) {
            return;
        }
        words = new byte[numWords][];
        for (int i = 0; i < numWords; i++) {
            words[i] = generateWord();
        }
    }
    
    private byte[] generateWord() {
        byte[] word = new byte[rnd(3, 8)];
        for (int i = 0; i < word.length; i++) {
            word[i] = i % 2 == 0 ? CONS[rnd(CONS.length)] : VOWS[rnd(VOWS.length)];
        }
        return word;
    }
    
    public int rnd(int a, int b) {
        return rnd.nextInt(b - a) + a;
    }
    
    public int rnd(int b) {
        return rnd.nextInt(b);
    }
    
}

