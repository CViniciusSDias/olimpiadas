package dao;

import java.sql.*;
import java.util.*;
import factories.ConnectionFactory;
import modelo.*;

public class UsuarioBdDao
{
    public UsuarioBdDao()
    {
        criarBanco();
    }

    private void criarBanco()
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS usuario (")
            .append("id INTEGER PRIMARY KEY AUTOINCREMENT,")
            .append("nome TEXT, login TEXT, senha TEXT,")
            .append("nivel TEXT);");
            
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

    public void inserir(Usuario usuario)
    {
        try {
            String sql = "INSERT INTO usuario (nome, login, senha, nivel) VALUES (?, ?, ?, ?)";
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getLogin());
            stm.setString(3, usuario.getSenha());
            stm.setString(4, usuario.getNivel());
            stm.executeUpdate();

            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public List<Usuario> getUsuarios()
    {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            String sql = "SELECT * FROM usuario ORDER BY nome;";
            Connection con = ConnectionFactory.getConnection();
            Statement stm = con.createStatement();
            ResultSet r = stm.executeQuery(sql);

            while (r.next())
            {
                Usuario usuario = new Usuario(r.getInt(1), NivelDeAcesso.valueOf(r.getString(5)));
                usuario.setNome(r.getString(2));
                usuario.setLogin(r.getString(3));
                usuario.setSenha(r.getString(4));

                usuarios.add(usuario);
            }

            r.close();
            stm.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return usuarios;
    }

    public Optional<Usuario> getUsuario(String login, String senha)
    {
        Optional<Usuario> op = Optional.empty();
        try {
            String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ? LIMIT 1;";
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, login);
            stm.setString(2, senha);
            ResultSet r = stm.executeQuery();

            if (r.next()) {
                Usuario usuario = new Usuario(r.getInt(1), NivelDeAcesso.valueOf(r.getString(5)));
                usuario.setNome(r.getString(2));
                usuario.setLogin(r.getString(3));
                usuario.setSenha(r.getString(4));

                op = Optional.of(usuario);
            }

            r.close();
            stm.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return op;
    }

    public void atualizar(Usuario usuario)
    {
        try {
            String sql = "UPDATE usuario SET nome = ?, login = ?, senha = ?, nivel = ? WHERE id = ?";
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getLogin());
            stm.setString(3, usuario.getSenha());
            stm.setString(4, usuario.getNivel());
            stm.setInt(5, usuario.getId());
            stm.executeUpdate();

            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void deletar(int id)
    {
        try {
            String sql = "DELETE FROM usuario WHERE id = ?";
            Connection con = ConnectionFactory.getConnection();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            stm.executeUpdate();

            stm.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}