package telas;

import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import java.io.*;
import modelo.*;
import utils.*;
import telas.controles.Alerta;

public class JanelaPais
{
    private Stage janela;
    private boolean confirmou;
    private TextField txtSigla;
    private TextField txtNome;
    private Spinner<Integer> txtOuro;
    private Spinner<Integer> txtPrata;
    private Spinner<Integer> txtBronze;
    private ImageView bandeira;
    private byte[] imgBandeira;
    private boolean editavel;

    public JanelaPais(boolean editavel)
    {
        this.editavel = editavel;
        janela = new Stage();
        BorderPane layout = new BorderPane();
        Scene scene = new Scene(layout, 370, 350);

        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setCenter(formulario());
        
        janela.setTitle("Dados do País");
        janela.setResizable(false);
        janela.setScene(scene);
        janela.initModality(Modality.APPLICATION_MODAL);
    }

    public void mostrar()
    {
        janela.showAndWait();
    }

    public Pane formulario()
    {
        GridPane painel = new GridPane();
        painel.getColumnConstraints().add(new ColumnConstraints(100));
        painel.getColumnConstraints().add(new ColumnConstraints(250));
        painel.setVgap(10);

        bandeira = null;
        FlowPane pnlBandeira = new FlowPane();
        pnlBandeira.setAlignment(Pos.CENTER);
        try {
            bandeira = new ImageView(new Image("telas/resources/bandeira-padrao.png"));
            if (editavel) {
                bandeira.setOnMousePressed(e -> {
                    try {
                        imgBandeira = solicitarArquivo();
                        if (imgBandeira != null)
                            bandeira.setImage(new Image(new ByteArrayInputStream(imgBandeira)));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Label lblSigla  = new Label("Sigla");
        Label lblNome   = new Label("Nome");
        Label lblOuro   = new Label("Ouro");
        Label lblPrata  = new Label("Prata");
        Label lblBronze = new Label("Bronze");
        txtSigla  = new TextField();
        txtNome   = new TextField();
        txtOuro   = new Spinner<>(0, 1000, 0);
        txtPrata  = new Spinner<>(0, 1000, 0);
        txtBronze = new Spinner<>(0, 1000, 0);
        Button btOk = new Button("Ok");
        Button btCancelar = new Button("Cancelar");

        btOk.setOnAction(e -> {
            if (imgBandeira != null && txtNome.getText() != null && txtSigla.getText() != null) {
                confirmou = true;
                janela.close();
            } else {
                Alerta.erro("Selecione a Bandeira", "Para cadastrar um país, digite todos os seus dados e selecione sua bandeira.");
            }
        });
        btCancelar.setOnAction(e -> {
            confirmou = false;
            janela.close();
        });

        bandeira.setFitHeight(100);
        bandeira.setPreserveRatio(true);
        pnlBandeira.getChildren().add(bandeira);

        txtSigla.setEditable(editavel);
        txtNome.setEditable(editavel);

        painel.add(pnlBandeira, 0, 0, 2, 1);
        painel.add(lblSigla, 0, 1);
        painel.add(lblNome, 0, 2);
        painel.add(lblOuro, 0, 3);
        painel.add(lblPrata, 0, 4);
        painel.add(lblBronze, 0, 5);

        painel.add(txtSigla, 1, 1);
        painel.add(txtNome, 1, 2);
        painel.add(txtOuro, 1, 3);
        painel.add(txtPrata, 1, 4);
        painel.add(txtBronze, 1, 5);

        FlowPane pnlBotoes = new FlowPane();
        pnlBotoes.setAlignment(Pos.CENTER);
        pnlBotoes.getChildren().add(btOk);
        pnlBotoes.getChildren().add(btCancelar);
        pnlBotoes.setHgap(10);

        painel.add(pnlBotoes, 0, 6, 2, 1);

        return painel;
    }

    public void paisToTela(Pais p)
    {
        txtSigla.setText(p.getSigla());
        txtNome.setText(p.getNome());
        txtOuro.getValueFactory().setValue(p.getOuro());
        txtPrata.getValueFactory().setValue(p.getPrata());
        txtBronze.getValueFactory().setValue(p.getBronze());
        try {
            if (p.getBandeira() != null) {
                imgBandeira = p.getBandeira();
                bandeira.setImage(new Image(new ByteArrayInputStream(imgBandeira)));
            } else {
                bandeira.setImage(new Image("telas/resources/bandeira-padrao.png"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void telaToPais(Pais p)
    {
        p.setSigla(txtSigla.getText());
        p.setNome(txtNome.getText());
        p.setOuro(txtOuro.getValueFactory().getValue());
        p.setPrata(txtPrata.getValueFactory().getValue());
        p.setBronze(txtBronze.getValueFactory().getValue());
        p.setBandeira(imgBandeira);
    }

    public byte[] solicitarArquivo()
    {
        FileChooser escolheArquivo = new FileChooser();
        escolheArquivo.setTitle("Selecione o arquivo");
        escolheArquivo.setInitialDirectory(new File(System.getProperty("user.home")));
        escolheArquivo.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.gif")
        );
        
        File arquivoSelecionado = escolheArquivo.showOpenDialog(janela);

        ImageHandler imgHandler = new ImageHandler();
        return arquivoSelecionado != null ? imgHandler.lerImagemDeArquivo(arquivoSelecionado) : null;
    }

    public static boolean executar(Pais p, boolean editavel)
    {
        JanelaPais instancia = new JanelaPais(editavel);
        
        instancia.paisToTela(p);
        instancia.mostrar();

        if (instancia.confirmou) {
            instancia.telaToPais(p);
        }

        return instancia.confirmou;
    }

    public static boolean executar(Pais p)
    {
        return executar(p, true);
    }
}