package com.xingcloud.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: witwolf
 * Date: 4/25/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {
    private static Logger logger = Logger.getLogger(FileUtil.class);


    public enum FST {
        LOCAL, HDFS
    }

    public static String readFileContent(String fileName) {
        logger.info("Read content from file : " + fileName);
        StringBuffer content = new StringBuffer();
        BufferedReader reader = null;
        String line = null;
        try {
            File file = new File(fileName);
            if (!file.exists())
                return null;
            InputStream inputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
        } catch (Exception e) {
            logger.error("Read from file  " + fileName + " failed");
        }
        if (content.length() != 0)
            return content.toString();
        return null;
    }

    public static void writeContentToFile(String fileName, String content, FST fsType) {
        logger.info("Write to file : " + fileName);
        try {
            if (fsType == FST.LOCAL) {
                File file = new File(fileName);
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    logger.info("Dir " + parentDir.getPath() + " not exist , create it ");
                    parentDir.mkdirs();
                }
                if (!file.exists()) {
                    logger.info("File " + fileName + " not exist ,create it ");
                    file.createNewFile();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write(content);
                bufferedWriter.close();
            } else {
                Configuration conf = new Configuration() ;
                FileSystem fs = FileSystem.get(conf);

                Path path = new Path(fileName) ;
                Path parent = path.getParent() ;
                if(!fs.exists(parent)){
                    logger.info("Path " + parent.getName() + " not exist , create it");
                    fs.mkdirs(parent);
                }
                FSDataOutputStream outputStream = fs.create(path,true);
                outputStream.write(content.getBytes("UTF-8"));
                outputStream.close();
            }


        } catch (Exception e) {
            logger.error("Write content to " + fileName + " failed ," + e.getMessage());
        }
    }

    public static void deleteHDFSDir(String path){
        try{
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            fs.delete(new Path(path),true);
        }catch (IOException e){
            logger.error("Delete hdfs dir : " + path + " failed");
        }

    }

    public static List<String> listProjects(String path) {
        List<String> projects = new ArrayList<String>();
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    projects.add(files[i].getName());
                }
            }
        } else {
            logger.warn("Dir : " + path + " not exist");
        }
        return projects;
    }

}
