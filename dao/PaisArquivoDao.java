package dao;

import java.io.*;
import java.util.*;
import modelo.*;

public class PaisArquivoDao
{
    public void gravar(Quadro quadro, File caminho)
    {
        try {
            String uriCaminho = caminho.getCanonicalPath();
            String ps = File.separator;
            uriCaminho += ps + "quadro.bin";
            caminho = new File(uriCaminho);
                
            FileOutputStream os = new FileOutputStream(caminho);
            DataOutputStream writer = new DataOutputStream(os);
            Iterable<Pais> paises = quadro.getPaises();

            for (Pais p : paises) {
                byte[] bandeira = p.getBandeira();
                writer.writeUTF(p.getSigla());
                writer.writeUTF(p.getNome());
                writer.writeInt(p.getOuro());
                writer.writeInt(p.getPrata());
                writer.writeInt(p.getBronze());
                writer.writeInt(bandeira.length);
                writer.write(bandeira, 0, bandeira.length);
            }

            writer.flush();
            writer.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ler(Quadro quadro, File arquivo)
    {
        try {
            FileInputStream is = new FileInputStream(arquivo);
            DataInputStream reader = new DataInputStream(is);
            quadro.limpar();

            while (is.available() > 0) {
                Pais p = new Pais();
                byte[] bandeira;
                p.setSigla(reader.readUTF())
                    .setNome(reader.readUTF())
                    .setOuro(reader.readInt()).setPrata(reader.readInt())
                    .setBronze(reader.readInt());
                int tamanhoBandeira = reader.readInt();
                
                bandeira = new byte[tamanhoBandeira];
                reader.read(bandeira, 0, tamanhoBandeira);
                p.setBandeira(bandeira);

                quadro.adiciona(p);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}