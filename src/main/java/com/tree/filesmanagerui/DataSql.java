package com.tree.filesmanagerui;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class DataSql {
    private String filePath;
    private Connection conn;
    private ArrayList<OneFile> files;
    private ArrayList<String> resultList;
    Statement stmt;
    ResultSet rs;
    DataSql() {
        filePath = "d:\\video";
        conn = null;
        files = new ArrayList<OneFile>();

        try {
            // 注册 JDBC 驱动
            Class.forName("org.sqlite.JDBC");
            // 打开连接
            conn = DriverManager.getConnection("jdbc:sqlite:files.db");
            // 获取数据库元数据
            DatabaseMetaData meta = conn.getMetaData();

            // 查询表是否存在
            ResultSet tables = meta.getTables(null, null, "files", null);

            if (tables.next()) {
                System.out.println("Table files exists! 无需初始化。");
                readFiles();
                return;
            } else {
                System.out.println("Table files does not exist.");
            }

            // 关闭数据库连接
            conn.close();
        } catch (ClassNotFoundException e) {
            System.err.println("未找到 SQLite JDBC 驱动");
        } catch (SQLException e) {
            System.err.println("连接 SQLite 数据库失败，错误信息：" + e.getMessage());
        }

        updateDatabase();
    }
    void readFiles() {
        resultList = new ArrayList<>();
        String sql = "SELECT name, path, type, suffix FROM files ";

        try {
            // 执行查询
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            // 遍历结果集并将每行数据添加到 ArrayList 中
            while (rs.next()) {
                String name = rs.getString("name");
                String path = rs.getString("path");
                String type = rs.getString("type");
                String suffix = rs.getString("suffix");

                files.add(new OneFile(name, path, type, suffix));
            }

            // 关闭数据库连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateDatabase() {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE  IF NOT EXISTS files " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL, " +
                    "path TEXT, " +
                    "type TEXT, " +
                    "suffix TEXT, " +
                    "size INTEGER, " +
                    "duration INTEGER, " +
                    "tmp INTEGER)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("创建表失败，错误信息：" + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("关闭声明失败，错误信息：" + e.getMessage());
                }
            }
        }
        String file_dir = filePath;
        File directory = new File(file_dir);

        // 判断路径是否存在
        if (!directory.exists()) {
            System.out.println("目录不存在");
            System.exit(0);
        }

        // 获取目录中所有文件和文件夹的名称
        String[] fileList = directory.list();

        if (fileList.length == 0) {
            System.out.println("目录为空");
        } else {
//            System.out.println("目录中的文件和文件夹：");
            for (String file : fileList) {
                System.out.println(file);
            }
        }
        for (String file : fileList) {
            Path path = Paths.get(file_dir, file);
            String fullPath = path.toAbsolutePath().toString();
            System.out.println(fullPath);

            File videoFile = new File(fullPath); // 创建文件对象
            if (videoFile.exists() && videoFile.isFile()) { // 判断文件是否存在且为普通文件
                long fileSize = videoFile.length(); // 获取文件大小，单位为字节
                long fileSizeInKB = fileSize / 1024; // 将文件大小转换为 KB
                long fileSizeInMB = fileSize / (1024 * 1024); // 将文件大小转换为 MB
                long fileSizeInGB = fileSize / (1024 * 1024 * 1024); // 将文件大小转换为 GB
                int dotIndex = file.lastIndexOf('.');
                String suffix = (dotIndex == -1) ? "" : file.substring(dotIndex + 1);
                String type;
                switch (suffix) {
                    case "avi":
                    case "mkv":
                    case "mp4":
                    case "rmvb":
                        type = "video";
                        break;
                    default:
                        type = "other";
                        break;
                }
                System.out.println("Video file " + file + " is " + suffix + " file, size: " + fileSize + " bytes (" + fileSizeInKB + " KB, " + fileSizeInMB + " MB, " + fileSizeInGB + " GB)");
                String sql = String.format("INSERT INTO files (name, path, suffix, size) VALUES ('%s', '%s', '%s', '%s')", file, fullPath, suffix, fileSize);
                System.out.println(sql);
                try {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                    System.err.println(e);
                }
            } else {
                System.out.println("The specified file does not exist or is not a regular file.");
            }
        }
    }

    void resetDatabase() {

    }
    OneFile[] getFiles() {
        return files.toArray(new OneFile[files.size()]);
    }
}
