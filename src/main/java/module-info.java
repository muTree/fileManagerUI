module com.tree.filesmanagerui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.tree.filesmanagerui to javafx.fxml;
    exports com.tree.filesmanagerui;
}