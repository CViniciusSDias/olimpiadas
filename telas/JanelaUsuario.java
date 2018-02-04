package telas;

import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.text.*;
import javafx.collections.*;
import modelo.*;

public class JanelaUsuario
{
    private Stage janela;
    private TextField txtNome;
    private TextField txtLogin;
    private PasswordField txtSenha;
    private ComboBox<String> comboNivel;
    private Label lblNivel;
    private Button btOk;
    private Button btCancelar;
    private boolean confirmou;
    private boolean editaNivel;

    public JanelaUsuario(boolean editaNivel)
    {
        this.editaNivel = editaNivel;
        janela = new Stage();
        BorderPane layout = new BorderPane();
        Scene cena = new Scene(layout, 370, 250);

        layout.setTop(topo());
        layout.setCenter(formulario());
        layout.setBottom(botoes());
        layout.setPadding(new Insets(0, 10, 0, 10));

        janela.setTitle("Criar usuário");
        janela.setResizable(false);
        janela.setScene(cena);
        janela.initModality(Modality.APPLICATION_MODAL);
    }

    public void mostrar()
    {
        janela.showAndWait();
    }

    private Pane topo()
    {
        FlowPane painel = new FlowPane();
        painel.setAlignment(Pos.CENTER);
        painel.setPadding(new Insets(15, 0, 15, 0));

        Label lblTitulo = new Label("Cadastrar Usuário");
		Font novaFonte  = Font.font(lblTitulo.getFont().getName(), FontWeight.BOLD, 16);

        lblTitulo.setFont(novaFonte);
        painel.getChildren().add(lblTitulo);

        return painel;        
    }

    private Pane formulario()
    {
        GridPane painel = new GridPane();
        painel.getColumnConstraints().add(new ColumnConstraints(100));
        painel.getColumnConstraints().add(new ColumnConstraints(250));
        painel.setVgap(10);

        txtNome = new TextField();
        txtLogin = new TextField();
        txtSenha = new PasswordField();
        ObservableList<String> lista = FXCollections.observableArrayList("VISITANTE", "OPERADOR", "ADMINISTRADOR");
        comboNivel = new ComboBox<>(lista);
        comboNivel.setValue("VISITANTE");
        lblNivel = new Label();

        painel.add(new Label("Nome"), 0, 0);
        painel.add(txtNome, 1, 0);
        painel.add(new Label("Login"), 0, 1);
        painel.add(txtLogin, 1, 1);
        painel.add(new Label("Senha"), 0, 2);
        painel.add(txtSenha, 1, 2);
        painel.add(new Label("Nivel"), 0, 3);
        if (editaNivel)
            painel.add(comboNivel, 1, 3);
        else
            painel.add(lblNivel, 1, 3);

        return painel;
    }

    private Pane botoes()
    {
        FlowPane painel = new FlowPane();
        painel.setAlignment(Pos.CENTER);
        painel.setPadding(new Insets(15, 0, 15, 0));
        painel.setHgap(10);

        btOk = new Button("Ok");
        btCancelar = new Button("Cancelar");
        btOk.setOnAction(e -> {
            confirmou = true;
            janela.close();
        });
        btCancelar.setOnAction(e -> {
            confirmou = false;
            janela.close();
        });

        painel.getChildren().addAll(btOk, btCancelar);

        return painel;
    }

    public void telaToUsuario(Usuario usuario)
    {
        usuario.setNome(txtNome.getText()).setLogin(txtLogin.getText())
            .setSenha(txtSenha.getText());
        if (editaNivel)
            usuario.setNivel(NivelDeAcesso.valueOf(comboNivel.getValue()));
    }

    public void usuarioToTela(Usuario usuario)
    {
        txtNome.setText(usuario.getNome());
        txtLogin.setText(usuario.getLogin());
        txtSenha.setText(usuario.getSenha());
        comboNivel.setValue(usuario.getNivel());
        lblNivel.setText(usuario.getNivel());
    }

    public static boolean executar(Usuario usuario, boolean editaNivel)
    {
        JanelaUsuario janela = new JanelaUsuario(editaNivel);
        janela.usuarioToTela(usuario);
        janela.mostrar();

        if (janela.confirmou)
            janela.telaToUsuario(usuario);
        
        return janela.confirmou;
    }

    public static boolean executar(Usuario usuario)
    {
        return executar(usuario, true);
    }
}