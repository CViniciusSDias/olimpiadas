package telas;

import modelo.Usuario;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import telas.controles.Alerta;

public class JanelaLogin
{
    private Stage janela;
    private boolean confirmou;
    private Usuario usuarioAtual;
    private TextField txtUsuario;
    private PasswordField txtSenha;

    public JanelaLogin(Usuario usuarioAtual)
    {
        this.usuarioAtual = usuarioAtual;
        janela = new Stage();
        BorderPane layoutPrincipal = new BorderPane();
        layoutPrincipal.setTop(getTitulo());
        layoutPrincipal.setCenter(getFormulario());
        layoutPrincipal.setPadding(new Insets(10, 10, 0, 10));
        Scene scene = new Scene(layoutPrincipal, 300, 250);

        janela.setScene(scene);
        janela.initModality(Modality.APPLICATION_MODAL);
        janela.setResizable(false);
        janela.setTitle("Fazer Login");
    }

    public void telaToUsuario()
    {
        usuarioAtual.setLogin(txtUsuario.getText());
        usuarioAtual.setSenha(txtSenha.getText());
    }

    public void fechar()
    {
        txtUsuario.setText("");
        txtSenha.setText("");
        janela.close();
    }

    private Pane getTitulo()
    {
        FlowPane painel = new FlowPane();
        Label lblTitulo = new Label("Fazer Login");
		Font novaFonte  = Font.font(lblTitulo.getFont().getName(), FontWeight.BOLD, 16);

        lblTitulo.setFont(novaFonte);
        painel.getChildren().add(lblTitulo);
        painel.setAlignment(Pos.CENTER);

        return painel;
    }

    private Pane getFormulario()
    {
        GridPane painel = new GridPane();
        painel.setVgap(20);
        painel.getColumnConstraints().add(new ColumnConstraints(100));
        painel.getColumnConstraints().add(new ColumnConstraints(180));
        painel.setPadding(new Insets(30, 0, 0, 0));
        Label lblUsuario = new Label("Usuário");
        Label lblSenha = new Label("Senha");
        txtUsuario = new TextField();
        txtSenha = new PasswordField();
        Button btOk = new Button("Ok");
        Button btCancelar = new Button("Cancelar");
        btOk.setOnAction(e -> {
            telaToUsuario();
            if (!usuarioAtual.logar())
                Alerta.erro("Login Inválido", "Por favor, verifique o usuário e senha e tente novamente");
            else {
                confirmou = true;
                fechar();
            }
        });
        btCancelar.setOnAction(e -> {
            usuarioAtual.deslogar();
            confirmou = false;
            fechar();
        });
        FlowPane botoes = new FlowPane();
        botoes.setAlignment(Pos.CENTER);
        botoes.setHgap(10);
        botoes.getChildren().addAll(btOk, btCancelar);

        painel.add(lblUsuario, 0, 0);
        painel.add(txtUsuario, 1, 0);
        painel.add(lblSenha, 0, 1);
        painel.add(txtSenha, 1, 1);
        painel.add(botoes, 0, 2, 2, 1);

        return painel;
    }

    public void mostrar()
    {
        janela.showAndWait();
    }

    private static JanelaLogin instancia;
    public static boolean executar(Usuario usuarioAtual)
    {
        if (instancia == null)
            instancia = new JanelaLogin(usuarioAtual);
        
        instancia.mostrar();

        return instancia.confirmou;
    }
}