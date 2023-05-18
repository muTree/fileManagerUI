package com.tree.filesmanagerui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    private final ObservableList<OneFile> data = FXCollections.observableArrayList(
            getFiles("d:\\video") );


    @Override
    public void start(Stage primaryStage) {

        // 创建表格
        final TableView<OneFile> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(800, 300);

        // 创建列
        TableColumn<OneFile, String> nameCol = new TableColumn<>("file");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<OneFile, String> pathCol = new TableColumn<>("path");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("path"));

        TableColumn<OneFile, String> typeCol = new TableColumn<>("type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        // 将列添加到表格中
        table.getColumns().addAll(nameCol, pathCol, typeCol);

        // 创建图片组件并添加到 StackPane 容器中
        Image image = new Image("D:\\BaiduSyncdisk\\Pictures\\v2-c5c2524a2da642ffa14f7d5f02b966c6_b.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        StackPane imageStackPane = new StackPane(imageView);

        // 设置表格数据并添加到 VBox 容器中
        table.setItems(data);
        HBox table_image = new HBox(table, imageStackPane);
        VBox tableVBox = new VBox(table_image);

        // 创建 HBox 容器并添加按钮和勾选框
        Button button1 = new Button("更新");
        Button button2 = new Button("重置");
        Button button3 = new Button("Button 3");
        HBox buttonHBox = new HBox(button1, button2, button3);

        CheckBox checkBox1 = new CheckBox("视频");
        CheckBox checkBox2 = new CheckBox("Option 2");
        CheckBox checkBox3 = new CheckBox("Option 3");
        HBox checkBoxHBox = new HBox(checkBox1, checkBox2, checkBox3);

        // 创建 GridPane 容器，将 HBox 容器和 StackPane 容器添加进去
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(buttonHBox, 0, 0);
        grid.add(checkBoxHBox, 0, 1);
//        grid.add(imageStackPane, 1, 0, 1, 2);

        // 创建一个绑定了 tableVBox 和 grid 的 VBox 容器，并设置 scene
        VBox rootVBox = new VBox();
        rootVBox.getChildren().addAll(grid, tableVBox);
        Scene scene = new Scene(rootVBox, 1366, 768);

        primaryStage.setTitle("TableView Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    OneFile[] getFiles(String dir) {
        ArrayList<OneFile> files = new ArrayList<OneFile>();
        String file_dir = dir;
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

        Connection conn = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("org.sqlite.JDBC");
            // 打开连接
            conn = DriverManager.getConnection("jdbc:sqlite:files.db");
        } catch (ClassNotFoundException e) {
            System.err.println("未找到 SQLite JDBC 驱动");
        } catch (SQLException e) {
            System.err.println("连接 SQLite 数据库失败，错误信息：" + e.getMessage());
        }
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS files " +
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
                System.out.println("Video file " + file + " is " + suffix + " file, size: " + fileSize + " bytes (" + fileSizeInKB + " KB, " + fileSizeInMB + " MB, " + fileSizeInGB + " GB)");
                String sql = String.format("INSERT INTO files (name, path, suffix, size) VALUES ('%s', '%s', '%s', '%s')", file, fullPath, suffix, fileSize);
                System.out.println(sql);
                files.add(new OneFile(file, fullPath, suffix));

                try {
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                    System.err.println(e);
                }
            } else {
                System.out.println("The specified file does not exist or is not a regular file.");
            }
        }
        return files.toArray(new OneFile[files.size()]);
    }
}