package com.itep.mt.common.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecTeminalUtil {
    private static ExecTeminalUtil _instance = null;
    private static boolean mIsRoot = false;

    public static ExecTeminalUtil getInstance() {
        if (_instance == null) {
            _instance = new ExecTeminalUtil();
        }
        return _instance;
    }

    public String execute(String cmd) {
        Logger.i("Executing: " + cmd);
        try {
            Process process = Runtime.getRuntime().exec("sh");
            DataInputStream is = new DataInputStream(process.getInputStream());
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            try {
                String fullOutput = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    fullOutput = fullOutput + line + "\n";
                }
                return fullOutput;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String executeForResult(String cmd) {
        Process process = null;
        String result = "";
        DataOutputStream os = null;
        DataInputStream is = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            os = new DataOutputStream(process.getOutputStream());
            is = new DataInputStream(process.getInputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            String line = null;
            while ((line = is.readLine()) != null) {
                result += line;
            }
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    public void executeShell(String cmd) {
        Logger.i("Executing shell: " + cmd);
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
