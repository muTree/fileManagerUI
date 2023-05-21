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


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DataSql dataSql = new DataSql();
        final ObservableList<OneFile> data = FXCollections.observableArrayList(
                dataSql.getFiles() );
        // 创建表格
        final TableView<OneFile> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(800, 300);

        // 创建列
        TableColumn<OneFile, String> nameCol = new TableColumn<>("文件");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<OneFile, String> pathCol = new TableColumn<>("路径");
        pathCol.setCellValueFactory(new PropertyValueFactory<>("path"));

        TableColumn<OneFile, String> typeCol = new TableColumn<>("类型");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<OneFile, String> suffixCol = new TableColumn<>("后缀");
        suffixCol.setCellValueFactory(new PropertyValueFactory<>("suffix"));

        // 将列添加到表格中
        table.getColumns().addAll(nameCol, pathCol, typeCol, suffixCol);

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

}