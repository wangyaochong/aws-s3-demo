package com.example.awss3demo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
    public static byte[] readStream(InputStream in) throws IOException {
        int size = 0;
        int read = 0;
        List<byte[]> bufferResult = new ArrayList<>();
        do {
            byte[] buffer = new byte[in.available()];
            read = in.read(buffer);
            bufferResult.add(buffer);
            if (read != -1) {
                size += read;
            }

        } while (read != -1);

        byte[] buf = new byte[size];
        int loc=0;
        for (byte[] bytes : bufferResult) {
            for (int i = 0; i < bytes.length && loc < buf.length; i++) {
                buf[loc] = bytes[i];
                loc++;
                if(loc >= buf.length){
                    break;
                }
            }
        }
        in.close();
        return buf;
    }
}
