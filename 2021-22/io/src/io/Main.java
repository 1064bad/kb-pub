package io;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.Key;
import java.security.SecureRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardOpenOption.*;

public class Main {
    public static void main(String[] args) throws Exception {
        base();
        copy();
        pipeline();
        baseChar();
        pipelineChar();
        zip();
    }

    private static void zip() throws Exception {
        var path = Path.of("/home/kb/test/io/jboss-javassist-javassist-rel_3_27_0_ga-11-g0d468da.zip");
        printZipTree(path);
    }

    private static void printZipTree(Path path) throws Exception {
        var fs = FileSystems.newFileSystem(path);
        fs.getRootDirectories().forEach(root -> {
            try {
                Files.walkFileTree(root, new FileVisitor<>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.toString().endsWith(".jar")) {
                            System.out.println("==============================================");
                            System.out.println("jar: " + file);
                            System.out.println("==============================================");
                            try {
                                printZipTree(file);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("jar ends: " + file);
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                        }
                        else {
                            System.out.println("file: " + file);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        System.out.println("ERROR: " + exc.getMessage());
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void pipelineChar() throws Exception {
        writeStringToFile();
        readStringFromFile();
    }

    private static void writeStringToFile() throws Exception {
        var str = "Привет опять";
        var path = Path.of("/home/kb/test/io/string.txt");
        try (var out = new BufferedWriter(
                new OutputStreamWriter(
                        new CipherOutputStream(
                                Files.newOutputStream(path),
                                getCipher(true))))) {
            out.write(str);
        }
    }

    private static Key key = null;

    private static Cipher getCipher(boolean isEncrypt) throws Exception {
        if (key == null) {
            var gen = KeyGenerator.getInstance("AES");
            gen.init(128, new SecureRandom());
            key = gen.generateKey();
        }
        var result = Cipher.getInstance("AES/CBC/PKCS5Padding");
        result.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
                key,
                new IvParameterSpec(new byte[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16}));
        return result;
    }

    private static void readStringFromFile() throws Exception {
        var path = Path.of("/home/kb/test/io/string.txt");
        try (var in = new BufferedReader(
                new InputStreamReader(
                        new CipherInputStream(
                                Files.newInputStream(path),
                                getCipher(false))))) {
            System.out.println(in.readLine());
        }
    }

    private static void baseChar() throws Exception {
        var str = "Привет!!";
        var path = Path.of("/home/kb/test/io/string.txt");
        try (var out = Files.newBufferedWriter(path, Charset.forName("cp1251"), CREATE, WRITE)) {
            out.write(str);
        }

        try (var in = Files.newBufferedReader(path, Charset.forName("cp1251"))) {
            System.out.println(in.readLine());
        }
    }

    private static void pipeline() throws Exception {
        writeDoublesToFile();
        readDoublesFromFile();
    }

    private static void writeDoublesToFile() throws Exception {
        double[] data = {1.0, 2.5, 3.3};
        var path = Path.of("/home/kb/test/io/doubles.zip");
        ZipOutputStream zos;
        try (var dos = new DataOutputStream(
                zos = new ZipOutputStream(
                        new BufferedOutputStream(
                                Files.newOutputStream(path, CREATE, WRITE))))) {
            zos.putNextEntry(new ZipEntry("doubles.bin"));
            for (var d : data)
                dos.writeDouble(d);
            zos.closeEntry();
        }
    }

    private static void readDoublesFromFile() throws Exception {
        double[] data = new double[3];
        var path = Path.of("/home/kb/test/io/doubles.zip");
        ZipInputStream zis;
        try (var dis = new DataInputStream(
                zis = new ZipInputStream(
                        new BufferedInputStream(
                                Files.newInputStream(path, READ))))) {
            zis.getNextEntry();
            for (int i = 0; i < data.length; i++) {
                data[i] = dis.readDouble();
            }
            zis.closeEntry();
        }
        for (var d : data)
            System.out.println(d);
    }

    private static void copy() throws Exception {
        var path = Path.of("/home/kb/test/io/testfile.txt");
        var pathCopy = Path.of("/home/kb/test/io/testfile_copy.txt");
        try (var in = Files.newInputStream(path, READ);
             var out = Files.newOutputStream(pathCopy, CREATE, WRITE)) {
            in.transferTo(out);
        }
    }

    private static void base() throws Exception {
        var str = "Hello!";
        var path = Path.of("/home/kb/test/io/testfile.txt");
        try (var out = Files.newOutputStream(path, CREATE, WRITE)) {
            for (byte b : str.getBytes(StandardCharsets.UTF_8))
                out.write(b);
//            out.write(str.getBytes());
        }

        var array = new byte[(int) Files.size(path)];
        try (var in = Files.newInputStream(path, READ)) {
            int b, cnt = 0;
            while ((b = in.read()) >= 0) {
                array[cnt++] = (byte) b;
            }
//            in.read(array);
        }
        System.out.println(new String(array, StandardCharsets.UTF_8));
    }
}
