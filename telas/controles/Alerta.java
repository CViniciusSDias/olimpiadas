package telas.controles;

import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import java.util.Optional;

public class Alerta {
    public static void informacao(String titulo, String texto)
    {
        Alert info = new Alert(AlertType.INFORMATION);
        setaTextosETitulos(info, titulo, texto);
        
        info.showAndWait();
    }
    
    public static void erro(String titulo, String texto)
    {
        Alert erro = new Alert(AlertType.ERROR);
        setaTextosETitulos(erro, titulo, texto);
        
        erro.showAndWait();
    }
    
    public static boolean pergunta(String titulo, String texto)
    {
        Alert pergunta = new Alert(AlertType.CONFIRMATION);
        setaTextosETitulos(pergunta, titulo, texto);
        Optional<ButtonType> resposta = pergunta.showAndWait();
        
        if (resposta.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    } 
    
    private static void setaTextosETitulos(Alert alert, String titulo, String texto)
    {
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.setTitle(titulo);
    }
}