module br.com.aula.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // permite injeção de @FXML na sua Principal
    opens br.com.aula.crud to javafx.fxml;

    // permite reflexão para pegar propriedades do Aluno (PropertyValueFactory)
    opens br.com.aula.crud.model to javafx.base, javafx.fxml;

    exports br.com.aula.crud;
}
