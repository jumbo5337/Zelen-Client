module HelloZelen {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires tornadofx;
    requires kotlin.stdlib;
    opens com.example.demo.app;
    opens com.example.demo.view;
}