import javafx.application.Application;
import javafx.stage.Stage;
import telas.*;
import modelo.*;

public class Aplicacao extends Application
{
    public void start(Stage janela)
    {
        // JanelaSplash splash = new JanelaSplash();
        // splash.mostrar();

        /** Busca os países no banco de dados e monta a janela */
        JanelaPrincipal janelaPrincipal = new JanelaPrincipal(janela);

        janelaPrincipal.mostrar(/*splash*/);
    }
}