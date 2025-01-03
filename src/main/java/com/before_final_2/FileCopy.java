package com.before_final_2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileCopy {

    // Simulates the `open` function
    public static RandomAccessFile open(String name, String mode) throws IOException {
        return new RandomAccessFile(name, mode);
    }

    // Simulates the `pread` function
    public static int pread(RandomAccessFile file, byte[] buf, long offset) throws IOException {
        file.seek(offset); // Move the file pointer to the specified offset
        return file.read(buf, 0, buf.length); // Read data into the buffer
    }

    // Simulates the `pwrite` function
    public static void pwrite(RandomAccessFile file, byte[] buf, long offset, int length) throws IOException {
        file.seek(offset); // Move the file pointer to the specified offset
        file.write(buf, 0, length); // Write specified bytes from the buffer
    }

    // Simulates the `close` function
    public static void close(RandomAccessFile file) throws IOException {
        file.close();
    }

    // Actual `copy` implementation
    public static void copy(String dst, String src) throws IOException {
        final int BUFFER_SIZE = 4096; // Buffer size (4 KB)
        byte[] buffer = new byte[BUFFER_SIZE];
        long offset = 0;

        // Open the source and destination files
        try (RandomAccessFile srcFile = open(src, "r");
             RandomAccessFile dstFile = open(dst, "rw")) {

            while (true) {
                // Read data from the source file
                int bytesRead = pread(srcFile, buffer, offset);
                if (bytesRead == -1 || bytesRead == 0) { // End of file
                    break;
                }

                // Write data to the destination file
                pwrite(dstFile, buffer, offset, bytesRead);

                offset += bytesRead; // Increment the offset
            }
        }
    }

    // Test the `copy` function
    public static void main(String[] args) {
        String src = "/Users/sudarshan.kundnani/Documents/PersonalProject/project1/src/main/java/com/before_final_2/one.txt";       // Replace with the path to your source file
        String dst = "/Users/sudarshan.kundnani/Documents/PersonalProject/project1/src/main/java/com/before_final_2/two.txt"; // Replace with the path to your destination file

        try {
            copy(dst, src);
            System.out.println("File copied successfully!");



            byte[] bytes = new byte[4096];
            RandomAccessFile file = new RandomAccessFile(src, "r");
            file.seek(0);
            int size = file.read(bytes, 0, bytes.length);
            System.out.println(size);
            System.out.println(new String(bytes));

        } catch (Exception e) {
            System.err.println("File copy failed: " + e.getMessage());
            e.printStackTrace();
        }



    }
}
