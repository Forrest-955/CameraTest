package com.itep.mt.qrtest;



import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by cy on 2020/04/03.
 */
public class ShellUtil{

    public static void run (String cmd){
        Log.e("Shell:", cmd);
        try{
            Runtime.getRuntime().exec(cmd);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 可进行耗时命令
     * @param command
     * @return
     */
    public static String runWithResult (String command){
        Process process = null;
        try{
            process = Runtime.getRuntime().exec(command);
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while (null != (line = reader.readLine())){
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
            is.close();
            return sb.toString().trim();
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            if (null != process){
                process.destroy();
            }
        }
        return null;
    }

    public static String runForResult (String cmd){
        Log.e("Shell:", cmd);
        BufferedReader reader;
        String content = "";
        try{
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            int read;
            char[] buffer = new char[4096];
            while ((read = reader.read(buffer)) > 0){
                output.append(buffer, 0, read);
            }
            reader.close();
            content = output.toString().trim();
        }catch (IOException e){
            e.printStackTrace();
            Log.e("ShellError:", e.toString());
        }
        Log.e("ShellResult:", content);
        return content;
    }

    public static class Builder{
        String cmd;

        public Builder (String cmd){
            this.cmd = cmd;
        }

        public Builder grep (final String cmd){
            this.cmd += " | grep \"" + cmd + "\"";
            return this;
        }

        public Builder print (final int n){
            cmd += " | busybox awk '{print $" + n + "}'";
            return this;
        }

        public Builder cut (String divide){
            return cut(divide, 0);
        }

        public Builder cut (int... columns){
            return cut(null, columns);
        }

        public Builder cut (String divide, final int... columns){
            cmd += " | cut";
            int[] f = columns;
            if (f.length > 0){
                cmd += " -f";
                for (int value : f){
                    cmd += value + ",";
                }
            }
            if (!TextUtils.isEmpty(divide.trim())){
                cmd += " -d\"" + divide + "\"";
            }
            return this;
        }

        public String build (){
            return cmd;
        }
    }
}
