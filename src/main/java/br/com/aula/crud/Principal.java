package br.com.aula.crud;

import br.com.aula.crud.dao.AlunoDAO;
import br.com.aula.crud.dao.CursoDAO;
import br.com.aula.crud.model.Aluno;
import br.com.aula.crud.model.Curso;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class Principal extends Application {

    @FXML private TextField tfNome;
    @FXML private CheckBox cbMaior;
    @FXML private ChoiceBox<Curso> chbCurso;
    @FXML private ChoiceBox<String> chbSexo;

    @FXML private TableView<Aluno> tvAlunos;
    @FXML private TableColumn<Aluno, Long>   colMatricula;
    @FXML private TableColumn<Aluno, String> colNome;
    @FXML private TableColumn<Aluno, Boolean>colMaior;
    @FXML private TableColumn<Aluno, Curso>  colCurso;   // agora Curso
    @FXML private TableColumn<Aluno, String> colSexo;

    @FXML private ChoiceBox<Curso> chbFiltroCurso;
    @FXML private TextField      tfFiltroMatricula;

    private final AlunoDAO alunoDAO   = new AlunoDAO();
    private final CursoDAO cursoDAO   = new CursoDAO();  // para carregar cursos
    private ObservableList<Aluno> listaAlunos;
    private ObservableList<Curso> listaCursos;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/br/com/aula/crud/hello-view.fxml")
        );
        loader.setControllerFactory(t -> this);
        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("CRUD de Alunos");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        List<Curso> cursos = cursoDAO.findAll();
        listaCursos = FXCollections.observableArrayList(cursos);

        chbCurso.setItems(listaCursos);
        chbFiltroCurso.setItems(listaCursos);

        chbSexo.setItems(FXCollections.observableArrayList("masculino", "feminino"));

        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colNome     .setCellValueFactory(new PropertyValueFactory<>("nome"));
        colMaior    .setCellValueFactory(new PropertyValueFactory<>("maioridade"));
        colCurso    .setCellValueFactory(new PropertyValueFactory<>("curso"));
        colSexo     .setCellValueFactory(new PropertyValueFactory<>("sexo"));

        carregarAlunos();

        tvAlunos.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldA, novoA) -> preencherFormulario(novoA));
    }

    private void carregarAlunos() {
        List<Aluno> todos = alunoDAO.findAll();
        if (listaAlunos == null) {
            listaAlunos = FXCollections.observableArrayList(todos);
            tvAlunos.setItems(listaAlunos);
        } else {
            listaAlunos.setAll(todos);
        }
    }

    private void preencherFormulario(Aluno a) {
        if (a == null) return;
        tfNome.setText(a.getNome());
        cbMaior.setSelected(a.isMaioridade());
        chbCurso.setValue(a.getCurso());
        chbSexo.setValue(a.getSexo());
    }



    @FXML
    private void onNovo() {
        tfNome.clear();
        cbMaior.setSelected(false);
        chbCurso.getSelectionModel().clearSelection();
        chbSexo.getSelectionModel().clearSelection();
        tvAlunos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onSalvar() {
        Aluno sel = tvAlunos.getSelectionModel().getSelectedItem();
        boolean isNovo = (sel == null);
        Aluno a = isNovo ? new Aluno() : sel;

        a.setNome(tfNome.getText().trim());
        a.setMaioridade(cbMaior.isSelected());
        a.setCurso(chbCurso.getValue());  // agora Curso
        a.setSexo(chbSexo.getValue());

        if (isNovo) {
            alunoDAO.create(a);
            listaAlunos.add(a);
        } else {
            alunoDAO.update(a);
            tvAlunos.refresh();
        }
        onNovo();
    }

    @FXML
    private void onExcluir() {
        Aluno sel = tvAlunos.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        alunoDAO.delete(sel.getMatricula());
        listaAlunos.remove(sel);
        onNovo();
    }


    @FXML
    private void onFiltrarPorCurso() {
        Curso sel = chbFiltroCurso.getValue();
        if (sel == null) {
            carregarAlunos();
        } else {
            listaAlunos.setAll(alunoDAO.findByCurso(sel));
        }
        onNovo();
    }

    @FXML
    private void onBuscarPorMatricula() {
        String txt = tfFiltroMatricula.getText().trim();
        if (txt.isEmpty()) {
            carregarAlunos();
        } else {
            try {
                Long mat = Long.valueOf(txt);
                Optional<Aluno> opt = alunoDAO.findById(mat);
                listaAlunos.clear();
                opt.ifPresent(listaAlunos::add);
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING,
                        "Matrícula inválida!", ButtonType.OK).showAndWait();
            }
        }
        onNovo();
    }

    @FXML
    private void onLimparFiltros() {
        chbFiltroCurso.getSelectionModel().clearSelection();
        tfFiltroMatricula.clear();
        carregarAlunos();
        onNovo();
    }
}















/*package br.com.aula.crud;

import br.com.aula.crud.dao.AlunoDAO;
import br.com.aula.crud.model.Aluno;
import br.com.aula.crud.model.Cursos;

import java.util.List;
import java.util.Optional;

public class Principal {
    public static void main(String[] args) {
        AlunoDAO alunoDAO = new AlunoDAO();
        Aluno aluno = new Aluno();aluno.setNome("Tarcísio");
        aluno.setMaioridade(true);
        aluno.setCurso(Cursos.ECMP);
        aluno.setSexo("masculino");
        alunoDAO.create(aluno);
        System.out.println("Matricula do novo aluno:" +aluno.getMatricula());

        List<Aluno> lista = alunoDAO.findAll();
        for (Aluno a : lista) {
            System.out.println("Matricula: " + a.getMatricula());
            System.out.println("Nome: " + a.getNome());
            System.out.println(a.isMaioridade()?" - Adulto":" - Adolescente");
            System.out.println("Curso: " + a.getCurso());
            System.out.println("Sexo: " + a.getSexo());
            System.out.println("=====================");
        }

        Optional<Aluno> aluno = alunoDAO.findById(1L);
        aluno.ifPresent(a -> {
            System.err.println("Matricula: " + a.getMatricula());
            System.err.println("Nome: " + a.getNome());
            System.err.println(a.isMaioridade()?" - Adulto":" - Adolescente");
            System.err.println("Curso: " + a.getCurso());
            System.err.println("Sexo: " + a.getSexo());
        });

        System.out.println("\n===== Lista de Alunos do Curso SISTEMAS =====");
        List<Aluno> listaPorCurso = alunoDAO.findByCurso(Cursos.ECMP);
        for (Aluno a : listaPorCurso) {
            System.out.println("Matrícula: " + a.getMatricula());
            System.out.println("Nome: " + a.getNome());
            System.out.println(a.isMaioridade() ? " - Adulto" : " - Adolescente");
            System.out.println("Sexo: " + a.getSexo());
            System.out.println("--------------------------------------------");
        }
    }
}*/
