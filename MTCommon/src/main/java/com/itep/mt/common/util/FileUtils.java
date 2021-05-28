package com.itep.mt.common.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Base64;

import com.itep.mt.common.constant.version.GenericConstant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;


/**
 * Created by JimGw on 2017/8/8.
 */
public class FileUtils {
    public byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    /**
     * the traditional io way
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * NIO way
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray2(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray3(String filename) throws IOException {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
                    fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                // System.out.println("remain");
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String uri2Path(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 将指定路径文件转化成byte数组
     *
     * @param filePath 文件路径
     * @return 二进制数组
     * @throws IOException 若文件不存在或路径为目录则抛出异常
     */
    public static byte[] file2ByteArray(String filePath) throws IOException {
        return file2ByteArray(new File(filePath));
    }

    public static byte[] file2ByteArray(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("文件为空");
        }
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("指定文件不存在或不是文件：" + file.getPath());
        }
        byte[] value = null;
        FileInputStream inputStream = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int fileLength = inputStream.available();
            value = new byte[fileLength];
            inputStream.read(value);
            out.write(value, 0, fileLength);
            value = out.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.flush();
            out.close();
            inputStream.close();
        }
        return value;
    }

    /**
     * 将数组生成文件
     *
     * @param data     二进制数组数据
     * @param path     文件目录
     * @param fileName 文件名称
     */
    public static void byteArray2File(byte[] data, String path, String fileName) {
        if (data == null || data.length == 0) {
            throw new NullPointerException("数组为空");
        }
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(path);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(path, fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    /**
     * 从Android res 或者 raw 读取文件复制到Sd卡
     *
     * @param context     Context上下文
     * @param resId       资源id
     * @param outPath     输出文件目录
     * @param outFileName 文件名称
     */
    public static void copy2SD(Context context, int resId, String outPath, String outFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File file = new File(outPath);
            if (file.exists() == false) {
                file.mkdir();
            }
            File dbFile = new File(outPath + "/" + outFileName);
            if (!dbFile.exists()) {
                is = context.getResources().openRawResource(resId);
                fos = new FileOutputStream(dbFile);
                byte[] buffer = new byte[8 * 1024];// 8K
                while (is.read(buffer) > 0)// >
                {
                    fos.write(buffer);
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制Assets资源到SD卡
     *
     * @param context     Context上下文
     * @param inPath      Assets资源路径
     * @param outPath     输出文件目录
     * @param outFileName 文件名称
     */
    public static void copyAssets2SD(Context context, String inPath, String outPath, String outFileName) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File file = new File(outPath);
            if (file.exists() == false) {
                file.mkdir();
            }
            File dbFile = new File(outPath + "/" + outFileName);
            if (!dbFile.exists()) {
                is = context.getAssets().open(inPath);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                fos = new FileOutputStream(dbFile);
                fos.write(buffer);
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 按指定路径创建目录
     *
     * @param dirpath 指定路径
     * @return 创建是否成功
     */
    public static boolean createDirs(String dirpath) {
        File f = new File(dirpath);
        if (!f.exists()) {
            try {
                f.mkdirs();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断指定文件是否存在
     *
     * @param filepath 文件路径
     * @return 是否是文件并且存在
     */
    public static boolean isFileExist(String filepath) {
        File f = new File(filepath);
        if (f.exists() && f.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断指定文件夹是否存在
     *
     * @param dirpath 文件夹路径
     * @return 是否是目录并且存在
     */
    public static boolean isDirExist(String dirpath) {
        File f = new File(dirpath);
        if (f.exists() && f.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回root目录路径
     *
     * @return root目录的绝对路径
     */
    public static String getRootPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 内部存储是否可用
     * 一般是指内部存储模拟的sd卡
     *
     * @return 是否可用
     */
    public static boolean isSdCardExist() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 返回SD卡的绝对路径
     *
     * @return SD卡根目录的绝对路径
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();
        try {
            Process p = run.exec(cmd);
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure", "");
                        return result;
                    }
                }
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 判断外部存储卡是否可用
     *
     * @return 是否可用
     */
    public static boolean isSecondExternalStorageMounted() {
        String sd2 = getSecondExternalStorageDirectory();
        if (sd2 == null) {
            return false;
        }
        // 通过一次快速读写操作判断磁盘是否被卸载
        return checkFsWritable(sd2 + File.separator);
    }

    /**
     * 获取外部sd卡路径
     *
     * @return 返回外部存储卡根目录的绝度路径
     */
    public static String getSecondExternalStorageDirectory() {
        String sdcard_path = null;
        String sd_default = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("ntfs") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sdcard_path;
    }

    /**
     * 获取所有存储卡路径
     *
     * @return 所有存储卡路径的根目录的绝对路径, 用\n分割
     */
    public static String getSDCardPathEx() {
        String mount = new String();
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;

                if (line.contains("fat")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount = mount.concat("*" + columns[1] + "\n");
                    }
                } else if (line.contains("fuse")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        mount = mount.concat(columns[1] + "\n");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mount;
    }

    /**
     * 获取当前路径可用空间(字节)
     *
     * @param path 指定路径
     * @return 可用空间的字节大小
     */
    public static long getAvailableSize(String path) {
        try {
            File base = new File(path);
            StatFs stat = new StatFs(base.getPath());
            long nAvailableCount = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
            return nAvailableCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 通过一次快速的读写操作判断磁盘是否可用
     *
     * @param dir 指定路径
     * @return 是否可用
     */
    private static boolean checkFsWritable(String dir) {
        if (dir == null)
            return false;
        File directory = new File(dir);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        File f = new File(directory, ".keysharetestgzc");
        try {
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 复制文件或文件夹到指定路径
     *
     * @param from 源
     * @param to   目的地路径
     * @return 复制完成后的完整绝对路径
     * @throws Exception
     */
    public static String copy(String from, String to) throws Exception {
        return copy(from, to, true, false);
    }

    /**
     * 复制文件或文件夹到指定路径
     *
     * @param from   源
     * @param to     目的地路径
     * @param isFlag 是否创建与源目录同名的目录，仅当源为目录时有效
     * @return 复制完成后的完整绝对路径
     * @throws Exception
     */
    public static String copy(String from, String to, boolean isFlag) throws Exception {
        return copy(from, to, isFlag, false);
    }

    /**
     * 剪切文件或文件夹到指定路径
     *
     * @param from 源
     * @param to   目的地路径
     * @return 剪切完成后的完整绝对路径
     * @throws Exception
     */
    public static String cut(String from, String to) throws Exception {
        return copy(from, to, true, true);
    }

    /**
     * 剪切文件或文件夹到指定路径
     *
     * @param from   源
     * @param to     目的地路径
     * @param isFlag 是否创建与源目录同名的目录，仅当源为目录时有效
     * @return 剪切完成后的完整绝对路径
     * @throws Exception
     */
    public static String cut(String from, String to, boolean isFlag) throws Exception {
        return copy(from, to, isFlag, true);
    }

    /**
     * 复制文件或文件夹到指定路径
     *
     * @param from     源
     * @param to       目标
     * @param isFlag   是否创建与源目录同名的目录，仅当源为目录时有效
     * @param isDelete 是否删除源
     * @return 返回目的地路径
     * @throws Exception
     * @throws Exception
     */
    private static String copy(String from, String to, boolean isFlag, boolean isDelete) throws Exception {
        boolean fromIsFile = !from.endsWith(File.separator);
        boolean toLastIsSeparator = to.endsWith(File.separator);// 目标路径结尾是否为路径分割符
        boolean toIsFile = !to.endsWith(File.separator);
        File fromFile = new File(from);
        if (fromFile.exists()) {
            fromIsFile = fromFile.isFile();
        } else {
            throw new FileNotFoundException("源不存在！");
        }
        File toFile = new File(to);
        if (toFile.exists()) {
            toIsFile = toFile.isFile();
            if (toIsFile && !fromIsFile) {
                throw new Exception("无法将一个文件目录复制到一个非目录文件");
            }
        } else {
            if (fromIsFile) {
                toIsFile = true;
                if (toLastIsSeparator) {
                    toFile.mkdir();
                    toFile = new File(to + fromFile.getName());
                } else {
                    toFile.getParentFile().mkdir();
                }
                toFile.createNewFile();
            } else {
                toIsFile = false;
                toFile.mkdir();
            }
        }

        if (fromIsFile && toIsFile) {// 文件对文件
            copyFile(fromFile, toFile);
        } else if (!fromIsFile && !toIsFile) {// 目录对目录
            if (isFlag) {
                if (toLastIsSeparator) {
                    toFile = new File(to + fromFile.getName());
                } else {
                    toFile = new File(to + File.separator + fromFile.getName());
                }
                toFile.mkdir();
            }
            copyDirectiory(fromFile.getPath(), toFile.getPath());
        } else if (fromIsFile && !toIsFile) {// 文件对目录
            if (isFlag) {
                toFile = new File(to + File.separator + fromFile.getName());
                toFile.createNewFile();
            }
            copyFile(fromFile, toFile);
        }
        if (isDelete) {
            delete(from);
        }

        return toFile.getPath();
    }

    /**
     * 复制文件夹
     *
     * @param sourceDir 源文件夹
     * @param targetDir 目的地文件夹
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 复制文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目的地文件
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);
        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);
        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();
        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param path 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     * @throws FileNotFoundException
     */
    public static boolean delete(String path) {
        File file = new File(path);
        // 判断目录或文件是否存在
        if (file.exists()) { // 不存在返回 false
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(path);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(path);
            }
        } else {
            // throw new FileNotFoundException("源不存在！");
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {// 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {// 删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取缓存路径
     *
     * @param context
     * @return
     */
    public static String getFilesDir(Context context, String dirName) {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalFilesDir(android.text.TextUtils.isEmpty(dirName) ? null : dirName).getPath();
        } else {
            filePath = context.getFilesDir().getPath();
        }
        return filePath;
    }

    public static byte[] File2Bytes(File file) {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1; ) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将图片转换成Base64编码的字符串
     *
     * @param path
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;

    }


    /**
     * 获取文件块
     * @param data 数据
     * @param isBig 是否大块 小块1字节表示，大块2字节表示
     * @return
     */
    public static byte[] getBlock(byte[] data,boolean isBig){
        byte[] blocksBytes=new byte[1];
        int lengthBlock=GenericConstant.GENERIC_SIGH_DATA_LENGTH;
        //计算图片块数
        int remainder = data.length % lengthBlock;//余数
        int blocks = 1;//块数
        if (remainder == 0) {
            blocks = data.length / lengthBlock;
        } else {
            blocks = data.length / lengthBlock + 1;
        }
        if(isBig){
            blocksBytes = new byte[2];
            blocksBytes[0] = (byte) (blocks >> 8);
            blocksBytes[1] = (byte) (blocks & 0xFF);
        }else{
            blocksBytes[0] = (byte) blocks;
        }
        return blocksBytes;
    }

    /**
     * 获取小块
     * @param data
     * @return
     */
    public static byte[] getSmallBlock(byte[] data){
        return getBlock(data,false);
    }

    /**
     * 获取大块
     * @param data
     * @return
     */
    public static byte[] getBigBlock(byte[] data){
        return getBlock(data,true);
    }


    /**
     * 读取文件内容
     * @param file
     * @return
     */
    public static String readFileContent(File file) {
        BufferedReader br=null;
        StringBuilder sb = new StringBuilder();
        try{
            br = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = br.readLine()) != null) {
                sb.append(tempStr);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 写文件
     * @param content
     * @param path
     * @return
     */
    public static void writeFileContent(String content,String path) {
        BufferedWriter bw=null;
        try{
            bw=new BufferedWriter(new FileWriter(path));
            bw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 创建目录
     * @param filePath
     * @return
     */
    public static void mkdirs(String filePath){
        if(filePath!=null){
            File file=new File(filePath);
            if(!file.exists() && !file.isDirectory()){
                file.mkdirs();
            }
        }
    }


}