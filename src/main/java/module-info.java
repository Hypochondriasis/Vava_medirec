module org.medirec{
    requires javafx.web;

    // Hansolo modules:
    //requires eu.hansolo.fx.countries;
    //requires eu.hansolo.fx.heatmap;
    //requires eu.hansolo.toolboxfx;
    //requires eu.hansolo.toolbox;
    //requires eu.hansolo.tilesfx;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires static lombok;
    requires MaterialFX;

    opens org.medirec.frontend.controller to javafx.fxml;
    exports org.medirec.frontend;
}