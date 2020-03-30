package yzw.ahaqth.personaldatacollector.operators;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import yzw.ahaqth.personaldatacollector.tools.EncryptAndDecrypt;

public final class FileOperator {
    private static String TAG = "殷宗旺";
    public final static String BACKUP_FILE_ENDNAME = ".bak";
    public final static String ERROR_LOG_SAVE_DIR = Environment.getExternalStorageDirectory() + File.separator + "ErrorLog";
    public static File cacheDir;
    public static File fileDir;
    public static File externalCacheDir;
    public static File externalFileDir;
    public static File imageDir;
    public static File backupDir;
    public static File sdCardDir;
    public static File imageCacheDir;
    public static File compressedImageDir;

    private static final String BACKUP_FILES_1 = "gF724Rq3Ujt";
    private static final String BACKUP_FILES_2 = "8QS9aWv";

    public static void initialAppDir(Context context) {
        cacheDir = context.getCacheDir();
        fileDir = context.getFilesDir();
        externalCacheDir = context.getExternalCacheDir();
        imageCacheDir = new File(externalCacheDir, "image_cache");
        compressedImageDir = new File(externalCacheDir, "image_compressed");
        externalFileDir = context.getExternalFilesDir(null);
        sdCardDir = Environment.getExternalStorageDirectory();
//        imageDir = new File(externalFileDir, "images" +File.separator+ SetupOperator.getPhoneId());
        imageDir = new File(externalFileDir, "images");
        backupDir = new File(Environment.getExternalStorageDirectory() + File.separator + "yzw.ahaqth.personaldatacollector" + File.separator + "Backup");
        createDirs(imageDir, imageCacheDir, compressedImageDir, backupDir);
        clearFiles(cacheDir);
        clearExternalCacheDir();
    }

    public static void createDirs(File... dir) {
        if (dir == null || dir.length == 0)
            return;
        for (File d : dir) {
            if (!d.exists()) {
                d.mkdirs();
            }
        }
    }

    public static void createDirs(String... dir) {
        if (dir == null || dir.length == 0)
            return;
        File[] files = new File[dir.length];
        for (int i = 0; i < dir.length; i++) {
            files[i] = new File(dir[i]);
        }
        createDirs(files);
    }

    public static String[] getFileList(File path, String endName) {
        String[] result = new String[0];
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null && files.length > 0) {
                List<String> fileNameList = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile()) {
                        String name = file.getName();
                        if (name.endsWith(endName)) {
                            fileNameList.add(name);
                        }
                    }
                }
                result = fileNameList.toArray(new String[0]);
            }
        }
        return result;
    }

    public static String[] getFileList(String pathname, String endName) {
        File path = new File(pathname);
        return getFileList(path, endName);
    }

    public static void deleAllFiles(File dir, String endName) {
        if (dir == null || !dir.exists())
            return;
        if (dir.isFile() && dir.getName().endsWith(endName)) {
            dir.delete();
        } else {
            File[] files = dir.listFiles();
            if (files == null)
                return;
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(endName))
                    file.delete(); // 删除所有文件
                else if (file.isDirectory())
                    deleAllFiles(file, endName); // 递规的方式删除文件夹
            }
        }
    }

    public static boolean clearFiles(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        return clearFiles(new File(path));
    }

    public static boolean clearFiles(File dir) {
        boolean b = true;
        if (dir == null || !dir.exists())
            return false;
        if (dir.isFile()) {
            b = dir.delete();
        } else {
            File[] files = dir.listFiles();
            if (files == null)
                b = false;
            else {
                for (File file : files) {
                    if (file.isFile())
                        b = file.delete(); // 删除所有文件
                    else if (file.isDirectory())
                        b = clearFilesAndDir(file); // 递规的方式删除文件夹
                }
            }
        }
        return b;
    }

    public static boolean clearFilesAndDir(File dir) {
        boolean b;
        b = clearFiles(dir);
        if (b)
            b = dir.delete();// 删除目录本身
        return b;
    }

    public static void clearExternalCacheDir() {
        File[] files = externalCacheDir.listFiles();
        if (files == null || files.length == 0)
            return;
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else {
                clearFiles(file);
            }
        }
    }

    private static byte[] int2ByteArray(int value) {
        byte[] result = new byte[4];
        ByteBuffer byteBuffer = ByteBuffer.wrap(result);
        byteBuffer.putInt(0, value);
        return result;
    }

    private static int byteArray2Int(byte[] array) {
        if (array.length != 4)
            throw new ArrayStoreException("Call byteArray2Int(byte[] array) error! the array.length() != 4 .");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4);
        byteBuffer.put(array, 0, array.length);
        byteBuffer.flip();
        return byteBuffer.getInt();
    }

    private static File getImagesZipFile() throws IOException {
        File tmpZipFile = new File(cacheDir, "images.bak");
        File[] files = imageDir.listFiles();
        if (files != null && files.length > 0) {
            InputStream inputStream = null;
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(tmpZipFile));
            for (File file : files) {
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(zipEntry);
                inputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024 * 8];
                int readBytes = 0;
                while ((readBytes = inputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, readBytes);
                }
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
            inputStream.close();
        }
        return tmpZipFile;
    }

    public static void unzipImages(File imagesZipFile){

    }
//
//    private static File getJSONFile() throws JSONException, IOException {
//        File file = new File(cacheDir, "record.bak");
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
//        bufferedWriter.write(JSONOperator.getBackupJsonString());
//        bufferedWriter.close();
//        return file;
//    }
//
//    public static void testByteArray() throws JSONException, IOException {
//        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(fileDir,"test")));
//        byte[] bytes = new byte[inputStream.available()];
//        inputStream.read(bytes);
//        inputStream.close();
//        byte[] bytes2 = new byte[bytes.length - 8];
//        System.arraycopy(bytes,7,bytes2,0,bytes2.length);
//        String s = hexStr2Str(new String(bytes2));
//
////        String s = str2HexStr(JSONOperator.getBackupJsonString());
////        byte[] bytes1 = s.getBytes();
////        byte[] bytes2 = new byte[bytes1.length + 8];
////        for(int i = 0;i<7;i++){
////            bytes2[i] = (byte) (10+i);
////        }
////        bytes2[bytes2.length - 1] = 20;
////        System.arraycopy(bytes1,0,bytes2,7,bytes1.length);
//        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(cacheDir,"hexStr2Str(bytes2).txt")));
//        outputStream.write(s.getBytes());
//        outputStream.close();
//        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(new File(cacheDir,"bytes.txt")));
//        outputStream1.write(bytes);
//        outputStream1.close();
//        String s1 = hexStr2Str(new String(bytes));
//        OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(new File(cacheDir,"hexStr2Str(bytes).txt")));
//        outputStream2.write(s1.getBytes());
//        outputStream2.close();
//        OutputStream outputStream3 = new BufferedOutputStream(new FileOutputStream(new File(cacheDir,"bytes2.txt")));
//        outputStream3.write(bytes2);
//        outputStream3.close();
//    }


    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    private static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = b & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }


    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */
    private static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static void restore(String fileName) throws JSONException,IOException {



    }

    public static List<String> parseBackupFile(String fileName) throws JSONException,IOException{
        List<String> results = new ArrayList<>();
        File file=new File(backupDir,fileName);
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] size = new byte[4];
        inputStream.read(size);
        int size1 = byteArray2Int(size);
        size = new byte[4];
        inputStream.read(size);
        int size2 = byteArray2Int(size);

        byte[] jsonBuffer = new byte[size1];
        byte[] zipBuffer = new byte[size2];

        inputStream.read(jsonBuffer);
        inputStream.read(zipBuffer);
        inputStream.close();

        byte[] realJson = EncryptAndDecrypt.decryptByteArray(jsonBuffer,EncryptAndDecrypt.JSON_ENCRYPT_SEED);
        byte[] realZip = EncryptAndDecrypt.decryptByteArray(zipBuffer,EncryptAndDecrypt.JSON_ENCRYPT_SEED);

        File zipFile = new File(cacheDir,System.currentTimeMillis() + ".tmp");
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(zipFile));
        outputStream.write(realZip);
        outputStream.close();

        results.add(hexStr2Str(new String(EncryptAndDecrypt.decryptByteArray(realJson,EncryptAndDecrypt.JSON_ENCRYPT_SEED))));
        results.add(zipFile.getAbsolutePath());
        FileWriter outputStreamWriter = new FileWriter(new File(cacheDir,"test.txt"));
        outputStreamWriter.write(results.get(0));
        outputStreamWriter.write("\n");
        outputStreamWriter.write(results.get(1));
        outputStreamWriter.close();
        return results;
    }

    public static void backup() throws JSONException, IOException {
        String bakFileName = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(System.currentTimeMillis()) + BACKUP_FILE_ENDNAME;
        File backupFile = new File(backupDir,bakFileName);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(backupFile));

        byte[] jsonBytes_origin = str2HexStr(JSONOperator.getBackupJsonString()).getBytes();
        byte[] json = EncryptAndDecrypt.encryptByteArray(jsonBytes_origin,EncryptAndDecrypt.JSON_ENCRYPT_SEED);

        InputStream inputStream2 = new BufferedInputStream(new FileInputStream(getImagesZipFile()));
        byte[] imageOrigin = new byte[inputStream2.available()];
        inputStream2.read(imageOrigin);
        inputStream2.close();
        byte[] image = EncryptAndDecrypt.encryptByteArray(imageOrigin,EncryptAndDecrypt.JSON_ENCRYPT_SEED);

        byte[] size1 = int2ByteArray(json.length);
        byte[] size2 = int2ByteArray(image.length);

        outputStream.write(size1);
        outputStream.write(size2);
        outputStream.write(json);
        outputStream.write(image);
        outputStream.close();
    }
}
