module com.example.kursovaaaa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.kursovaaaa to javafx.fxml;
    exports com.example.kursovaaaa;
}