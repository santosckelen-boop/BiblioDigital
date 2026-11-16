package br.edu.atitus.bibliodigital.services;

import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

import java.util.List;

public class LivroService {
    private static final int ANO_PUBLICACAO_MINIMO = 1900;

    private final LivroRepository repository = new LivroRepository();

    public void inserir(Livro livro) {
        validarLivro(livro, null);
        repository.inserir(livro);
    }

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public List<Livro> findByTitulo(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new IllegalArgumentException("Informe um título ou parte dele para pesquisar.");

        return repository.findByTitulo(parametro.trim());
    }

    public List<Livro> findByAutor(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new IllegalArgumentException("Informe um autor ou parte do nome para pesquisar.");

        return repository.findByAutor(parametro.trim());
    }

    public void alterar(int indice, Livro livro) {
        List<Livro> livros = repository.findAll();
        if (indice < 0 || indice >= livros.size())
            throw new IllegalArgumentException("Índice de livro inválido.");

        Livro livroAtual = livros.get(indice);
        validarLivro(livro, livroAtual);
        repository.alterar(indice, livro);
    }

    public void excluir(int indice) {
        List<Livro> livros = repository.findAll();
        if (indice < 0 || indice >= livros.size())
            throw new IllegalArgumentException("Índice de livro inválido.");

        repository.excluir(indice);
    }

    // As mesmas regras são utilizadas no cadastro e na alteração.
    private void validarLivro(Livro livro, Livro livroAtual) {
        if (livro == null)
            throw new IllegalArgumentException("Objeto de livro inválido.");

        if (livro.getTitulo() == null || livro.getTitulo().isBlank())
            throw new IllegalArgumentException("Título inválido. O título não pode ficar vazio.");

        if (livro.getAutor() == null || livro.getAutor().isBlank())
            throw new IllegalArgumentException("Autor inválido. O autor não pode ficar vazio.");

        if (livro.getAnoPublicacao() < ANO_PUBLICACAO_MINIMO)
            throw new IllegalArgumentException("Ano inválido. O ano de publicação deve ser igual ou superior a 1900.");

        // Remove espaços desnecessários no início e no final.
        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());

        for (Livro existente : repository.findAll()) {
            boolean eOutroLivro = existente != livroAtual;
            boolean mesmoTitulo = existente.getTitulo().equalsIgnoreCase(livro.getTitulo());

            if (eOutroLivro && mesmoTitulo)
                throw new IllegalArgumentException("Já existe um livro cadastrado com esse título.");
        }
    }
}
