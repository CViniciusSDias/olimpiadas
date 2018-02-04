package telas;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.beans.property.*;
import java.io.*;
import modelo.*;
import dao.*;
import telas.controles.Alerta;

public class JanelaPrincipal
{
    private Stage janela;
    private ObservableList<Pais> paises;
    private Quadro quadro;
    private TableView<Pais> tabela;
    private Usuario usuarioAtual;
    private BorderPane layoutPrincipal;

    public JanelaPrincipal(Stage janela)
    {
        usuarioAtual = new Usuario();
        usuarioAtual.setNome("Visitante");

        quadro = new Quadro();
        layoutPrincipal = new BorderPane();
        layoutPrincipal.setTop(getTopo());
        layoutPrincipal.setCenter(getQuadro());
        layoutPrincipal.setBottom(getRodape());

        Scene scene = new Scene(layoutPrincipal, 600, 400);
        janela.setTitle("Ranking de Medalhas");
        janela.setScene(scene);
        janela.centerOnScreen();
        janela.setResizable(false);

        this.janela = janela;
    }

    public void mostrar(/*JanelaSplash splash*/)
    {
        janela.show();
        // splash.fechar();
    }

    private TableView<Pais> getQuadro()
    {
        paises = FXCollections.observableList(quadro.getPaises());
        tabela = new TableView<>();
        TableColumn<Pais, Integer> ranking = new TableColumn<>("Ranking");
        TableColumn<Pais, String> nome     = new TableColumn<>("País");
        TableColumn<Pais, String> sigla    = new TableColumn<>("Sigla");
        TableColumn<Pais, Integer> ouro    = new TableColumn<>("Ouro");
        TableColumn<Pais, Integer> prata   = new TableColumn<>("Prata");
        TableColumn<Pais, Integer> bronze  = new TableColumn<>("Bronze");
        TableColumn<Pais, ImageView> bandeira = new TableColumn<>("Bandeira");

        ranking.setCellValueFactory(new PropertyValueFactory<Pais, Integer>("posicao"));
        nome.setCellValueFactory(new PropertyValueFactory<Pais, String>("nome"));
        sigla.setCellValueFactory(new PropertyValueFactory<Pais, String>("sigla"));
        ouro.setCellValueFactory(new PropertyValueFactory<Pais, Integer>("ouro"));
        prata.setCellValueFactory(new PropertyValueFactory<Pais, Integer>("prata"));
        bronze.setCellValueFactory(new PropertyValueFactory<Pais, Integer>("bronze"));
        bandeira.setCellValueFactory(new PropertyValueFactory<Pais, ImageView>("imagemBandeira"));
        
        tabela.setItems(paises);
        tabela.setEditable(false);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /** addAll() lança um warning (não encontrei o motivo) */
        tabela.getColumns().add(ranking);
        tabela.getColumns().add(nome);
        tabela.getColumns().add(sigla);
        tabela.getColumns().add(ouro);
        tabela.getColumns().add(prata);
        tabela.getColumns().add(bronze);
        tabela.getColumns().add(bandeira);

        return tabela;
    }

    private Pane getTopo()
    {
        VBox painel = new VBox();
        GridPane grid = new GridPane();
        grid.add(getFiltro(), 0, 1);
        if (usuarioAtual.estaLogado())
            grid.add(getBotoes(), 1, 1);

        painel.getChildren().add(getMenu());
        painel.getChildren().add(grid);

        return painel;
    }

    private Pane getFiltro()
    {
        Pane painel = new HBox(5);
        TextField txtFiltro = new TextField();
        Button btFiltro = new Button();

        txtFiltro.setPadding(new Insets(7, 0, 7, 5));
        txtFiltro.setOnAction((e) -> btFiltro.fire());
        btFiltro.setGraphic(new ImageView(new Image("telas/resources/filtrar.png")));
        btFiltro.setOnAction((e) -> paises.setAll(quadro.filtraPorNome(txtFiltro.getText())));

        painel.getChildren().add(txtFiltro);
        painel.getChildren().add(btFiltro);

        painel.setPadding(new Insets(5, 0, 5, 5));

        return painel;
    }

    private Pane getBotoes()
    {
        FlowPane painel = new FlowPane();
        Button btCadastrarPais = new Button();
        Button btRemoverPais   = new Button();
        Button btAtualizarPais = new Button();

        btCadastrarPais.setGraphic(new ImageView(new Image("telas/resources/add-pais.png")));
        btRemoverPais.setGraphic(new ImageView(new Image("telas/resources/del-pais.png")));
        btAtualizarPais.setGraphic(new ImageView(new Image("telas/resources/update-pais.png")));

        btCadastrarPais.setOnAction(e -> {
            Pais pais = new Pais();
            if (JanelaPais.executar(pais)) {
                quadro.adiciona(pais);
                paises.setAll(quadro.getPaises());
            }
        });

        btAtualizarPais.setOnAction(e -> {
            Pais pais = tabela.getSelectionModel().getSelectedItem();
            if (pais == null) {
                Alerta.erro("Selecione um país", "Para editar um país, primeiro clique no mesmo na tabela.");
            } else {
                String siglaAnterior = pais.getSigla();
                boolean editavel = usuarioAtual.ehAdministrador();
                if (JanelaPais.executar(pais, editavel)) {
                    quadro.atualiza(siglaAnterior, pais);
                    paises.setAll(quadro.getPaises());
                }
            }
        });

        btRemoverPais.setOnAction(e -> {
            Pais pais = tabela.getSelectionModel().getSelectedItem();
            if (pais == null) {
                Alerta.erro("Selecione um país", "Para remover um país, primeiro clique no mesmo na tabela.");
            } else {
                if (Alerta.pergunta("Excluir país", "Deseja realmente excluir " + pais.getNome() + "?")) {
                    quadro.remove(pais.getSigla());
                    paises.setAll(quadro.getPaises());
                }
            }
        });

        if (usuarioAtual.ehAdministrador()) {
            painel.getChildren().addAll(btCadastrarPais);
            painel.getChildren().addAll(btRemoverPais);
        }
        
        if (usuarioAtual.estaLogado())
            painel.getChildren().addAll(btAtualizarPais);

        painel.setAlignment(Pos.BASELINE_RIGHT);
        painel.setPadding(new Insets(5, 5, 5, 0));
        painel.setHgap(5);

        return painel;
    }

    private Pane getRodape()
    {
        FlowPane painel = new FlowPane();
        painel.setAlignment(Pos.CENTER);
        painel.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        painel.setPadding(new Insets(10, 0, 10, 0));

        Label lblUsuario = new Label("Olá, " + usuarioAtual.getNome());
        lblUsuario.setTextFill(Color.WHITE);

        painel.getChildren().add(lblUsuario);

        return painel;
    }

    private MenuBar getMenu()
    {
        MenuBar menuBar = new MenuBar();
        Menu arquivo = new Menu("Arquivo");
        Menu usuario = new Menu("Usuários");
        Menu ajuda = new Menu("Ajuda");
        MenuItem importar = new MenuItem("Importar");
        MenuItem exportar = new MenuItem("Exportar");
        MenuItem sair     = new MenuItem("Sair");
        MenuItem criar    = new MenuItem("Criar Usuário");
        MenuItem alterar  = new MenuItem("Alterar seus dados");
        MenuItem logar    = new MenuItem("Fazer login");
        MenuItem deslogar = new MenuItem("Fazer logout");
        MenuItem sobre    = new MenuItem("Sobre");

        sobre.setOnAction(e -> {
            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Ranking Olímpico");
            alerta.setHeaderText("Sistema de Ranking de Medalhas Olímpicas");
            alerta.setContentText(
                "Este sistema foi desenvolvido para o trabalho de PRJ de 2016.1.\n" +
                "A comercialização do mesmo é proibida, mas caso consiga vendê-lo, entre em contato com o desenvolvedor para dividir o lucro." +
                "\n\nVersão: 0.1\nCriadores: Vinicius Dias, Victor Bello e Davi Soares\nE-mail: cdias@faeterj-petropolis.edu.br"
            );

            alerta.getDialogPane().setPrefHeight(300);
            alerta.showAndWait();
        });

        importar.setOnAction(e -> {
            FileChooser escolheArquivo = new FileChooser();
            escolheArquivo.setTitle("Selecione o arquivo");
            escolheArquivo.setInitialDirectory(new File(System.getProperty("user.home")));
            escolheArquivo.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivo de Quadro", "*.bin")
            );
            
            File arquivoSelecionado = escolheArquivo.showOpenDialog(janela);
            if (arquivoSelecionado != null) {
                PaisArquivoDao dao = new PaisArquivoDao();
                dao.ler(quadro, arquivoSelecionado);
                paises.setAll(quadro.getPaises());
                Alerta.informacao("Importado", "Seu quadro de medalhas foi importado com sucesso.");
            }
        });
        exportar.setOnAction(e -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Exportar Quadro");
            dc.setInitialDirectory(new File(System.getProperty("user.home")));
            File caminho = dc.showDialog(janela);
            if (caminho != null) {
                PaisArquivoDao dao = new PaisArquivoDao();
                dao.gravar(quadro, caminho);
                Alerta.informacao("Salvo", "Seu quadro de medalhas foi exportado com sucesso");
            }
        });
        sair.setOnAction(e -> janela.close());

        if (usuarioAtual.estaLogado()) {
            deslogar.setOnAction(e -> {
                usuarioAtual.deslogar();
                atualizarTela();
            });

            if (usuarioAtual.ehAdministrador()) {
                criar.setOnAction(e -> {
                    Usuario novoUsuario = new Usuario();
                    if (JanelaUsuario.executar(novoUsuario)) {
                        UsuarioBdDao dao = new UsuarioBdDao();
                        dao.inserir(novoUsuario);
                    }
                });
                usuario.getItems().add(criar);
            }
            alterar.setOnAction(e -> {
                if (JanelaUsuario.executar(usuarioAtual, false)) {
                    UsuarioBdDao dao = new UsuarioBdDao();
                    dao.atualizar(usuarioAtual);
                    atualizarTela();
                }
            });
            usuario.getItems().addAll(alterar, deslogar);
        } else {
            logar.setOnAction(e -> {
                if (JanelaLogin.executar(usuarioAtual))
                    atualizarTela();
            });
            usuario.getItems().add(logar);
        }

        arquivo.getItems().addAll(importar, exportar, new SeparatorMenuItem(), sair);
        ajuda.getItems().add(sobre);
        menuBar.getMenus().addAll(arquivo, usuario, ajuda);

        return menuBar;
    }

    private void atualizarTela()
    {
        layoutPrincipal.setBottom(getRodape());
        layoutPrincipal.setTop(getTopo());
    }
}