package utils;

import java.io.*;

public class ImageHandler
{
    public byte[] lerImagemDeArquivo(File arquivo)
    {
        try {
            FileInputStream is = new FileInputStream(arquivo);
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            is.close();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return new byte[1];
        }

    }
}