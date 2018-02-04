package modelo;

import java.util.Optional;
import dao.UsuarioBdDao;

public class Usuario
{
    private int id;
    private String nome;
    private String login;
    private String senha;
    private NivelDeAcesso nivel;

    public Usuario()
    {
        nivel = NivelDeAcesso.VISITANTE;
    }
    
    public Usuario(int id, NivelDeAcesso nivel)
    {
        this.id = id;
        this.nivel = nivel;
    }

    public int getId()
    {
        return id;
    }

    public String getNome()
    {
        return nome;
    }

    public Usuario setNome(String nome)
    {
        this.nome = nome;
        return this;
    }

    public Usuario setLogin(String login)
    {
        this.login = login;
        return this;
    }

    public String getLogin()
    {
        return login;
    }

    public Usuario setSenha(String senha)
    {
        this.senha = senha;
        return this;
    }

    public String getSenha()
    {
        return senha;
    }

    public boolean logar()
    {
        UsuarioBdDao dao = new UsuarioBdDao();
        Optional<Usuario> usuario = dao.getUsuario(login, senha);
        usuario.ifPresent(u -> {
            id    = u.id;
            nome  = u.nome;
            login = u.login;
            senha = u.senha;
            nivel = u.nivel;
        });

        return usuario.isPresent();
    }

    public Usuario setNivel(NivelDeAcesso nivel)
    {
        this.nivel = nivel;
        return this;
    }

    public String getNivel()
    {
        return nivel.name();
    }

    public void deslogar()
    {
        if (estaLogado()) {
            nome = "Visitante";
            nivel = NivelDeAcesso.VISITANTE;
            senha = "";
            login = "";
        }
    }

    public String toString()
    {
        return nome + " " + login + " " + " " + senha;
    }

    public boolean estaLogado()
    {
        return nivel != NivelDeAcesso.VISITANTE;
    }

    public boolean ehAdministrador()
    {
        return nivel == NivelDeAcesso.ADMINISTRADOR;
    }

    public void resetaSenha()
    {
        senha = "padrao@123";
    }
}