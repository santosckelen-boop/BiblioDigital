package br.edu.atitus.bibliodigital.repositories;

import br.edu.atitus.bibliodigital.entities.Livro;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LivroRepository {
    private final List<Livro> acervo = new ArrayList<>();

    public List<Livro> findAll() {
        return new ArrayList<>(acervo);
    }

    public void inserir(Livro livro) {
        acervo.add(livro);
    }

    public List<Livro> findByTitulo(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        String pesquisa = parametro.toLowerCase(Locale.ROOT);

        for (Livro livro : acervo) {
            if (livro.getTitulo().toLowerCase(Locale.ROOT).contains(pesquisa)) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public List<Livro> findByAutor(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        String pesquisa = parametro.toLowerCase(Locale.ROOT);

        for (Livro livro : acervo) {
            if (livro.getAutor().toLowerCase(Locale.ROOT).contains(pesquisa)) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public void alterar(int indice, Livro livro) {
        acervo.set(indice, livro);
    }

    public void excluir(int indice) {
        acervo.remove(indice);
    }
}
