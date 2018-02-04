package dao;

import java.sql.*;
import java.util.*;
import factories.ConnectionFactory;
import modelo.Pais;

public class PaisBdDao
{
    public PaisBdDao()
    {
        criarBanco();
    }

    private void criarBanco()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS pais (")
            .append("sigla TEXT PRIMARY KEY,")
            .append("ranking INTEGER NOT NULL,")
            .append("nome TEXT NOT NULL,")
            .append("ouro INTEGER NOT NULL DEFAULT 0,")
            .append("prata INTEGER NOT NULL DEFAULT 0,")
            .append("bronze INTEGER NOT NULL DEFAULT 0,")
            .append("bandeira BLOB NOT NULL);");
            
            Connection c = ConnectionFactory.getConnection();
            Statement stm = c.createStatement();
            stm.executeUpdate(sb.toString());

            stm.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void limpar()
    {
        try {
            Connection c = ConnectionFactory.getConnection();
            Statement stm = c.createStatement();
            stm.executeUpdate("DELETE FROM pais;");
            /** Limpa espaço utilizado desnecessariamente pelo sqlite */
            stm.executeUpdate("VACUUM;");

            stm.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserir(Pais pais)
    {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement stm = c.prepareStatement("INSERT INTO pais (sigla, ranking, nome, ouro, prata, bronze, bandeira) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stm.setString(1, pais.getSigla());
            stm.setInt(2, pais.getRanking());
            stm.setString(3, pais.getNome());
            stm.setInt(4, pais.getOuro());
            stm.setInt(5, pais.getPrata());
            stm.setInt(6, pais.getBronze());
            stm.setBytes(7, pais.getBandeira());
            if (stm.executeUpdate() != 1)
                throw new Exception("Erro ao inserir pais " + pais.getNome());

            stm.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<Pais> getPaises()
    {
        List<Pais> paises = new ArrayList<>();
        try {
            Connection con = ConnectionFactory.getConnection();
            Statement stm = con.createStatement();
            ResultSet r = stm.executeQuery("SELECT * FROM pais ORDER BY ranking;");

            while (r.next()) {
                Pais pais = new Pais();
                pais.setSigla(r.getString(1)).setRanking(r.getInt(2)).setNome(r.getString(3))
                    .setOuro(r.getInt(4)).setPrata(r.getInt(5)).setBronze(r.getInt(6)).setBandeira(r.getBytes(7));
                
                paises.add(pais);
            }

            r.close();
            stm.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return paises;
    }

    public Optional<Pais> getPais(String sigla)
    {
        Optional<Pais> optional = Optional.empty();
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement stm = c.prepareStatement("SELECT * FROM pais WHERE sigla = ?");
            stm.setString(1, sigla);
            ResultSet r = stm.executeQuery();

            if (r.next()) {
                Pais pais = new Pais();
                pais.setSigla(r.getString(1)).setRanking(r.getInt(2)).setNome(r.getString(3))
                    .setOuro(r.getInt(4)).setPrata(r.getInt(5)).setBronze(r.getInt(6)).setBandeira(r.getBytes(7));
                optional = Optional.of(pais);
            }

            r.close();
            stm.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return optional;
    }

    public void atualizar(String sigla, Pais pais)
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE pais SET sigla = ?, ranking = ?, nome = ?, ")
              .append("ouro = ?, prata = ?, bronze = ?, bandeira = ? WHERE sigla = ?");
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sb.toString());
            stm.setString(1, pais.getSigla());
            stm.setInt(2, pais.getRanking());
            stm.setString(3, pais.getNome());
            stm.setInt(4, pais.getOuro());
            stm.setInt(5, pais.getPrata());
            stm.setInt(6, pais.getBronze());
            stm.setBytes(7, pais.getBandeira());
            stm.setString(8, sigla);

            stm.executeUpdate();

            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void deletar(String sigla)
    {
        try {
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement("DELETE FROM pais WHERE sigla = ?");
            stm.setString(1, sigla);
            stm.executeUpdate();

            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}