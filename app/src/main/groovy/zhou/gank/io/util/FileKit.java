package zhou.gank.io.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

/**
 * Created by 州 on 2015/7/4 0004.
 * 文件操作相关工具类
 */
public class FileKit {

    public static DecimalFormat decimalFormat = new DecimalFormat(".00");

    /**
     * 判断文件是否存在
     *
     * @param path 文件的全路径
     * @return 文件是否存在
     */
    @SuppressWarnings("unused")
    public static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    @SuppressWarnings("unused")
    public static boolean isFileExists(File parent, String name) {
        File file = new File(parent, name);
        return file.exists();
    }

    /**
     * 获取文件的后缀名
     *
     * @param path 文件全路径
     * @return 后缀名
     */
    @SuppressWarnings("unused")
    public static String getFileExtension(String path) {
        return path == null ? null : path.substring(path.lastIndexOf(".") + 1);
    }

    /**
     * 去掉路径的后缀名
     *
     * @param path 文件全路径
     * @return 去后缀名后的文件名
     */
    @SuppressWarnings("unused")
    public static String getPathWithoutExtension(String path) {
        return path == null ? null : path.substring(0, path.lastIndexOf("."));
    }

    /**
     * 将对象写入指定路径的文件中
     *
     * @param path 文件路径
     * @param obj  需要被写入的对象
     */
    @SuppressWarnings("unused")
    public static void writeObject(String path, Object obj) {
        File file = new File(path);
        writeObject(file, obj);
    }

    /**
     * 写入对象到文件中
     *
     * @param file 文件对象
     * @param obj  需要写入文件的对象
     */
    @SuppressWarnings("unused")
    public static void writeObject(File file, Object obj) {
        if (null == file || obj == null) {
            return;
        }
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
        } catch (IOException e) {
            Log.d("writeObject", e.getMessage());
        } finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Log.d("writeObject", e.getMessage());
            }
        }
    }

    /**
     * 从指定路径的文件中读取对象
     *
     * @param path 文件路径
     * @return 读取到的对象
     */
    @SuppressWarnings("unused")
    public static Object readObject(String path) {
        File file = new File(path);
        return readObject(file);
    }

    /**
     * 从文件中读取对象
     *
     * @param file 文件对象
     * @return 读取到的对象
     */
    public static Object readObject(File file) {
        Object obj = null;
        if (file != null && file.exists()) {
            FileInputStream fileInputStream = null;
            ObjectInputStream objectInputStream = null;

            try {
                fileInputStream = new FileInputStream(file);
                objectInputStream = new ObjectInputStream(fileInputStream);

                obj = objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Log.d("readObject", e.getMessage());
            } finally {
                try {
                    if (objectInputStream != null) {
                        objectInputStream.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException e) {
                    Log.d("readObject", e.getMessage());
                }

            }
        }
        return obj;
    }

    public static void writeString(File file, String content) {
        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(content);

        } catch (IOException e) {
            Log.d("writeString", "error", e);
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Log.d("writeString", "error final", e);
            }
        }
    }

    public static String readString(File file) {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        String content = null;

        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            sb.deleteCharAt(sb.length()-1);
            content = sb.toString();
        } catch (FileNotFoundException e) {
            Log.d("readString", "error", e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                Log.d("readString", "error final", e);
            }
        }
        return content;
    }

    /**
     * 格式化文件大小以字符串输出
     *
     * @param size 大小
     * @return B、KB、MB类型的字符串
     */
    @SuppressWarnings("unused")
    public static String formatSize(int size) {
        if (size < 1024 * 0.6) {
            return size + "B";
        } else if (size < 1024 * 1024 * 0.6) {
            return decimalFormat.format((float) size / 1024) + "KB";
        } else {
            return decimalFormat.format((float) size / (1024 * 1024)) + "MB";
        }
    }

}
