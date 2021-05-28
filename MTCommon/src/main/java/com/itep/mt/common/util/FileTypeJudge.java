package com.itep.mt.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 文件类型判断类
 */
public final class FileTypeJudge {


    /**
     * Constructor
     */
    private FileTypeJudge() {
    }


    /**
     * 将文件头转换成16进制字符串
     *
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 得到文件头
     *
     * @param filePath 文件路径
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(String filePath) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            inputStream.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }


    /**
     * 判断文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型
     */
    public static FileType getType(String filePath) throws IOException {
        String fileHead = getFileContent(filePath);

        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }

        fileHead = fileHead.toUpperCase();

        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }

        }
        return null;
    }

    /**
     * 文件类型枚取
     */
    public enum FileType {


        /**
         * JEPG.
         */
        JPEG("FFD8FF"),


        /**
         * PNG.
         */
        PNG("89504E47"),


        /**
         * GIF.
         */
        GIF("47494638"),


        /**
         * TIFF.
         */
        TIFF("49492A00"),


        /**
         * Windows Bitmap.
         */
        BMP("424D"),


        /**
         * CAD.
         */
        DWG("41433130"),


        /**
         * Adobe Photoshop.
         */
        PSD("38425053"),


        /**
         * Rich Text Format.
         */
        RTF("7B5C727466"),


        /**
         * XML.
         */
        XML("3C3F786D6C"),


        /**
         * HTML.
         */
        HTML("68746D6C3E"),


        /**
         * Email [thorough only].
         */
        EML("44656C69766572792D646174653A"),


        /**
         * Outlook Express.
         */
        DBX("CFAD12FEC5FD746F"),


        /**
         * Outlook (pst).
         */
        PST("2142444E"),


        /**
         * MS Word/Excel.
         */
        XLS_DOC("D0CF11E0"),


        /**
         * MS Access.
         */
        MDB("5374616E64617264204A"),


        /**
         * WordPerfect.
         */
        WPD("FF575043"),


        /**
         * Postscript.
         */
        EPS("252150532D41646F6265"),


        /**
         * Adobe Acrobat.
         */
        PDF("255044462D312E"),


        /**
         * Quicken.
         */
        QDF("AC9EBD8F"),


        /**
         * Windows Password.
         */
        PWL("E3828596"),


        /**
         * ZIP Archive.
         */
        ZIP("504B0304"),


        /**
         * RAR Archive.
         */
        RAR("52617221"),


        /**
         * Wave.
         */
        WAV("57415645"),


        /**
         * AVI.
         */
        AVI("41564920"),


        /**
         * Real Audio.
         */
        RAM("2E7261FD"),


        /**
         * Real Media.
         */
        RM("2E524D46"),


        /**
         * MPEG (mpg).
         */
        MPG("000001BA"),


        /**
         * Quicktime.
         */
        MOV("6D6F6F76"),


        /**
         * Windows Media.
         */
        ASF("3026B2758E66CF11"),


        /**
         * MIDI.
         */
        MID("4D546864");


        private String value = "";


        /**
         * Constructor.
         */
        private FileType(String value) {
            this.value = value;
        }


        public String getValue() {
            return value;
        }


        public void setValue(String value) {
            this.value = value;
        }
    }
}