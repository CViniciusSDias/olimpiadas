package modelo;

import java.util.*;
import java.util.stream.*;
import dao.PaisBdDao;

public class Quadro
{
    private List<Pais> paises;
    private PaisBdDao bd;

    public Quadro()
    {
        bd = new PaisBdDao();
        paises = bd.getPaises();
    }

    public void adiciona(Pais pais)
    {
        /**
         * Clona o objeto para que as alterações exteriores não
         * afetem o quadro
         */
        Pais novoPais = new Pais();
        novoPais.setSigla(pais.getSigla()).setNome(pais.getNome())
            .setOuro(pais.getOuro()).setPrata(pais.getPrata())
            .setBronze(pais.getBronze()).setBandeira(pais.getBandeira());

        paises.add(novoPais);
        rankPaises();
        bd.inserir(novoPais);
    }

    public void remove(String sigla)
    {
        bd.deletar(sigla);
        int i = indexOf(sigla);
        paises.remove(i);
        if (paises.size() > 0)
            rankPaises();
    }

    public void atualiza(String sigla, Pais pais)
    {
        Pais novoPais = new Pais();
        novoPais.setSigla(pais.getSigla()).setNome(pais.getNome())
            .setOuro(pais.getOuro()).setPrata(pais.getPrata())
            .setBronze(pais.getBronze()).setBandeira(pais.getBandeira());

        paises.set(indexOf(sigla), novoPais);
        rankPaises();
        bd.atualizar(sigla, novoPais);
    }

    protected int indexOf(String sigla)
    {
        int i = 0;
        boolean encontrado = false;
        Iterator<Pais> it = paises.iterator();
        Pais pais = null;
        while(it.hasNext() && !encontrado) {
            pais = it.next();
            if (pais.getSigla().equals(sigla)) {
                encontrado = true;
            } else {
                i++;
            }
        }
        
        return encontrado ? i : -1;
    }

    public List<Pais> getPaises()
    {
        /** Devolve um cópia, e não a lista interna */
        List<Pais> copiaPaises = new ArrayList<>(paises);
        
        return copiaPaises;
    }

    public List<Pais> filtraPorNome(String nome)
    {
        return paises.stream()
            .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
            .collect(Collectors.toList()) /** Coleta o stream usando o coletor retornado pela factory toList */;
    }

    protected void rankPaises()
    {
        int nPaises = paises.size();
        if (nPaises > 1) {
            paises.sort((p1, p2) -> p1.compareTo(p2));

            Pais paisAnterior = paises.get(0);
            paisAnterior.setRanking(1);
            bd.atualizar(paisAnterior.getSigla(), paisAnterior);
            
            int i = 1;
            while (i < nPaises) {
                Pais pais = paises.get(i);
                if (pais.compareTo(paisAnterior) == 0) {
                    pais.setRanking(paisAnterior.getRanking());
                } else {
                    pais.setRanking(i + 1);
                }
                bd.atualizar(pais.getSigla(), pais);
                paisAnterior = pais;
                ++i;
            }
        } else {
            paises.get(0).setRanking(1);
        }
    }

    public void limpar()
    {
        paises.clear();
        bd.limpar();
    }
}