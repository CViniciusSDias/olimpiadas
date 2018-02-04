package telas;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.geometry.*;

public class JanelaSplash
{
    private Stage janela;
    public JanelaSplash()
    {
        janela = new Stage(StageStyle.TRANSPARENT);

        BorderPane bp = new BorderPane();
        setBackground(bp);
        bp.setTop(getCarregando());

        Scene cena = new Scene(bp, 690, 500);

        janela.setScene(cena);
    }

    private void setBackground(Pane layout)
    {
        Image imagem = new Image("telas/resources/splash.jpg");  
        BackgroundSize tamanho = new BackgroundSize(690, 500, false, false, false, false);      
        Background bg = new Background(
            new BackgroundImage(imagem, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, tamanho
        ));
        layout.setBackground(bg);
    }

    private Pane getCarregando()
    {
        FlowPane painel = new FlowPane();
        painel.setAlignment(Pos.CENTER);

        Label carregando = new Label("Carregando países cadastrados");
        carregando.setFont(new Font(carregando.getFont().getName(), 18.0));
        carregando.setTextFill(Color.WHITE);

        painel.setPadding(new Insets(50, 0, 0, 0));
        painel.getChildren().add(carregando);

        return painel;
    }

    public void mostrar()
    {
        janela.show();        
    }

    public void fechar()
    {
        janela.close();
    }
}