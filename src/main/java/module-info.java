module org.medirec.medirec {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires java.sql;
    requires static lombok;
    requires MaterialFX;
    requires backend;

    opens org.medirec.medirec.frontend.controller to javafx.fxml;
    exports org.medirec.medirec.frontend;
<<<<<<< Updated upstream
=======
    exports org.medirec.medirec.backend.model;
>>>>>>> Stashed changes
}