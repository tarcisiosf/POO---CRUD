module br.com.aula.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens br.com.aula.crud to javafx.fxml;

    opens br.com.aula.crud.model to javafx.base, javafx.fxml;

    exports br.com.aula.crud;
}
