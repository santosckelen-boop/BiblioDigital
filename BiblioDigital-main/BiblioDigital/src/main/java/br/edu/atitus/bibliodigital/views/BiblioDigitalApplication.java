package br.edu.atitus.bibliodigital.views;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;

import java.util.List;
import java.util.Scanner;

public class BiblioDigitalApplication {
    private final LivroService service = new LivroService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new BiblioDigitalApplication().iniciar();
    }

    private void iniciar() {
        boolean executando = true;

        while (executando) {
            mostrarMenu();

            try {
                int opcao = lerInteiro("Escolha uma das opções acima: ");

                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> pesquisarPorTitulo();
                    case 3 -> pesquisarPorAutor();
                    case 4 -> alterar();
                    case 5 -> excluir();
                    case 0 -> {
                        System.out.println("Até logo!!! Obrigada por utilizar o BiblioDigital.");
                        executando = false;
                    }
                    default -> System.out.println("Opção inválida. Informe uma opção do menu.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Erro: " + ex.getMessage());
            }

            System.out.println();
        }
    }

    private void mostrarMenu() {
        System.out.println("""
                ===== Bem vindo ao BiblioDigital =====

                Menu:

                1 - Cadastrar novo Livro
                2 - Pesquisar Livro por título
                3 - Pesquisar Livro por autor
                4 - Alterar dados do Livro
                5 - Excluir Livro
                0 - Sair
                """);
    }

    private void cadastrar() {
        System.out.println("===== Cadastro de Livro =====");
        int opcao = lerInteiro("""
                Qual tipo de livro deseja cadastrar?
                1 - Livro físico
                2 - Livro digital
                3 - Audiolivro
                Escolha uma opção: """);

        Livro novoLivro = lerDadosLivro(opcao);
        service.inserir(novoLivro);
        System.out.println("Livro cadastrado com sucesso!");
    }

    private Livro lerDadosLivro(int opcao) {
        if (opcao < 1 || opcao > 3)
            throw new IllegalArgumentException("Opção de tipo de livro inválida.");

        String titulo = lerTexto("Digite o título do livro: ");
        String autor = lerTexto("Digite o autor do livro: ");
        int anoPublicacao = lerInteiro("Digite o ano de publicação do livro: ");

        if (opcao == 1) {
            int quantidadeExemplares = lerInteiro("Digite o número de exemplares: ");
            String localizacao = lerTexto("Digite a localização: ");
            return new LivroFisico(titulo, autor, anoPublicacao, quantidadeExemplares, localizacao);
        } else if (opcao == 2) {
            String formato = lerTexto("Digite o formato do arquivo: ");
            double tamanhoArquivo = lerDouble("Digite o tamanho do arquivo (em MB): ");
            return new LivroDigital(titulo, autor, anoPublicacao, formato, tamanhoArquivo);
        } else {
            int duracao = lerInteiro("Digite a duração do audiolivro (em minutos): ");
            String narrador = lerTexto("Digite o nome do narrador: ");
            return new AudioLivro(titulo, autor, anoPublicacao, duracao, narrador);
        }
    }

    private void pesquisarPorTitulo() {
        String pesquisa = lerTexto("Digite o título ou parte dele: ");
        List<Livro> livrosEncontrados = service.findByTitulo(pesquisa);
        imprimirLivrosEncontrados(livrosEncontrados);
    }

    private void pesquisarPorAutor() {
        String pesquisa = lerTexto("Digite o nome do autor ou parte dele: ");
        List<Livro> livrosEncontrados = service.findByAutor(pesquisa);
        imprimirLivrosEncontrados(livrosEncontrados);
    }

    private void imprimirLivrosEncontrados(List<Livro> livrosEncontrados) {
        if (livrosEncontrados.isEmpty()) {
            System.out.println("Nenhum livro encontrado para essa pesquisa.");
            return;
        }

        System.out.println("===== Resultado da pesquisa =====");
        for (Livro livro : livrosEncontrados) {
            System.out.println(livro);
            System.out.println("Tipo: " + livro.getTipo());
            System.out.println();
        }
    }

    private void alterar() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            System.out.println("Não existe nenhum livro cadastrado para alterar.");
            return;
        }

        imprimirListaComIndices(livros);
        int indice = lerInteiro("Informe o índice do livro que deseja alterar: ") - 1;

        if (indice < 0 || indice >= livros.size())
            throw new IllegalArgumentException("Índice inválido. Escolha um número da lista.");

        Livro livroAtual = livros.get(indice);
        System.out.println("===== Dados atuais do livro =====");
        System.out.println(livroAtual);
        System.out.println("Tipo: " + livroAtual.getTipo());
        System.out.println("===== Informe os novos dados =====");

        int tipoLivro = obterTipo(livroAtual);
        Livro livroAlterado = lerDadosLivroParaAlteracao(tipoLivro);
        service.alterar(indice, livroAlterado);

        System.out.println("Livro alterado com sucesso!");
    }

    private int obterTipo(Livro livro) {
        if (livro instanceof LivroFisico)
            return 1;
        if (livro instanceof LivroDigital)
            return 2;
        if (livro instanceof AudioLivro)
            return 3;

        throw new IllegalArgumentException("Tipo de livro não reconhecido.");
    }

    private Livro lerDadosLivroParaAlteracao(int tipoLivro) {
        // Na alteração, o tipo do livro é mantido; os dados podem ser modificados.
        String titulo = lerTexto("Digite o novo título: ");
        String autor = lerTexto("Digite o novo autor: ");
        int anoPublicacao = lerInteiro("Digite o novo ano de publicação: ");

        if (tipoLivro == 1) {
            int quantidadeExemplares = lerInteiro("Digite a nova quantidade de exemplares: ");
            String localizacao = lerTexto("Digite a nova localização: ");
            return new LivroFisico(titulo, autor, anoPublicacao, quantidadeExemplares, localizacao);
        } else if (tipoLivro == 2) {
            String formato = lerTexto("Digite o novo formato do arquivo: ");
            double tamanhoArquivo = lerDouble("Digite o novo tamanho do arquivo (em MB): ");
            return new LivroDigital(titulo, autor, anoPublicacao, formato, tamanhoArquivo);
        } else {
            int duracao = lerInteiro("Digite a nova duração do audiolivro (em minutos): ");
            String narrador = lerTexto("Digite o novo nome do narrador: ");
            return new AudioLivro(titulo, autor, anoPublicacao, duracao, narrador);
        }
    }

    private void excluir() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            System.out.println("Não existe nenhum livro cadastrado para excluir.");
            return;
        }

        imprimirListaComIndices(livros);
        int indice = lerInteiro("Informe o índice do livro que deseja excluir: ") - 1;

        if (indice < 0 || indice >= livros.size())
            throw new IllegalArgumentException("Índice inválido. Escolha um número da lista.");

        Livro livro = livros.get(indice);
        System.out.println("Livro selecionado: " + livro.getTitulo());
        String confirmacao = lerTexto("Confirma a exclusão? (S/N): ");

        if (confirmacao.equalsIgnoreCase("s") || confirmacao.equalsIgnoreCase("sim")) {
            service.excluir(indice);
            System.out.println("Livro excluído com sucesso!");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private void imprimirListaComIndices(List<Livro> livros) {
        System.out.println("===== Livros cadastrados =====");
        for (int i = 0; i < livros.size(); i++) {
            System.out.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private int lerInteiro(String mensagem) {
        String valor = lerTexto(mensagem);
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Informe um número inteiro válido.");
        }
    }

    private double lerDouble(String mensagem) {
        String valor = lerTexto(mensagem).replace(',', '.');
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Informe um número válido.");
        }
    }
}
