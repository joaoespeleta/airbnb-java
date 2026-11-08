/*
INTEGRANTES:
GUILHERME LIMA RIOS SANTOS
ISABELA DECARLI
LUCAS ANDRADE DE ALMEIDA
JOÃO VICTOR DE SOUZA SILVA
JOÃO PEDRO ESPELETA STURARI DE CASTRO
 */
package app;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import excecoes.ReservaInvalidaException;
import modelo.*;
import persistencia.GerenciarDadosIniciais;

public class AppPrincipal {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private static ArrayList<Propriedade> listaPropriedades = new ArrayList<>();

    public static void main(String[] args) {
        carregarDados();
        System.out.println("\n===== PLATAFORMA DE LOCAÇÃO =====");
        System.out.println("Dados iniciais carregados: " + listaUsuarios.size() + " usuários e "
                + listaPropriedades.size() + " propriedades.");
        int opcao;
        do {
            System.out.println("\n1 - Área do Proprietário");
            System.out.println("2 - Área do Cliente");
            System.out.println("3 - Listar todas as propriedades");
            System.out.println("4 - Listar todos os usuários");
            System.out.println("0 - Sair");
            opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1: acessarProprietario(); break;
                case 2: acessarCliente(); break;
                case 3: listarPropriedades(); break;
                case 4: listarUsuarios(); break;
                case 0: System.out.println("Obrigado por utilizar a plataforma!"); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
        scanner.close();
    }

    private static void carregarDados() {
        GerenciarDadosIniciais.carregarUsuarios("dados/usuarios.txt", listaUsuarios);
        GerenciarDadosIniciais.carregarPropriedades("dados/propriedades.txt", listaUsuarios, listaPropriedades);
        GerenciarDadosIniciais.carregarReservas("dados/reservas.txt", listaUsuarios, listaPropriedades);
    }

    private static void acessarProprietario() {
        Proprietario proprietario = loginProprietario();
        if (proprietario == null) return;
        int opcao;
        do {
            System.out.println("\n--- ÁREA DO PROPRIETÁRIO: " + proprietario.getNome() + " ---");
            System.out.println("1 - Cadastrar propriedade");
            System.out.println("2 - Exibir minhas propriedades");
            System.out.println("3 - Ver propriedades alugadas e reservas");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Escolha: ");
            switch (opcao) {
                case 1: cadastrarPropriedade(proprietario); break;
                case 2: proprietario.listarPropriedades(); break;
                case 3: proprietario.listarPropriedadesAlugadas(); break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static void acessarCliente() {
        Cliente cliente = loginCliente();
        if (cliente == null) return;
        int opcao;
        do {
            System.out.println("\n--- ÁREA DO CLIENTE: " + cliente.getNome() + " ---");
            System.out.println("1 - Consultar propriedades disponíveis");
            System.out.println("2 - Fazer reserva pelo código");
            System.out.println("3 - Listar minhas reservas");
            System.out.println("0 - Voltar");
            opcao = lerInteiro("Escolha: ");
            switch (opcao) {
                case 1: consultarDisponibilidade(cliente); break;
                case 2: fazerReserva(cliente); break;
                case 3: cliente.listarReservas(); break;
                case 0: break;
                default: System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private static Proprietario loginProprietario() {
        System.out.println("1 - Entrar | 2 - Cadastrar-se | 0 - Voltar");
        int opcao = lerInteiro("Escolha: ");
        if (opcao == 2) {
            Usuario novo = cadastrarUsuario(true);
            return novo == null ? null : (Proprietario) novo;
        }
        if (opcao != 1) return null;
        String email = lerTexto("E-mail: ");
        String senha = lerTexto("Senha: ");
        for (Usuario u : listaUsuarios) {
            if (u instanceof Proprietario && u.autenticar(email, senha)) return (Proprietario) u;
        }
        System.out.println("E-mail ou senha incorretos, ou usuário não é proprietário.");
        return null;
    }

    private static Cliente loginCliente() {
        System.out.println("1 - Entrar | 2 - Cadastrar-se | 0 - Voltar");
        int opcao = lerInteiro("Escolha: ");
        if (opcao == 2) {
            Usuario novo = cadastrarUsuario(false);
            return novo == null ? null : (Cliente) novo;
        }
        if (opcao != 1) return null;
        String email = lerTexto("E-mail: ");
        String senha = lerTexto("Senha: ");
        for (Usuario u : listaUsuarios) {
            if (u instanceof Cliente && u.autenticar(email, senha)) return (Cliente) u;
        }
        System.out.println("E-mail ou senha incorretos, ou usuário não é cliente.");
        return null;
    }

    private static Usuario cadastrarUsuario(boolean proprietario) {
        try {
            String nome = lerTexto("Nome: ");
            String email = lerTexto("E-mail: ");
            for (Usuario u : listaUsuarios) {
                if (u.getEmail().equalsIgnoreCase(email)) throw new IllegalArgumentException("Este e-mail já está cadastrado.");
            }
            String senha = lerTexto("Senha (mínimo 6 caracteres): ");
            Usuario novo;
            if (proprietario) novo = new Proprietario(nome, email, senha);
            else novo = new Cliente(nome, email, senha);
            listaUsuarios.add(novo);
            System.out.println("Cadastro realizado com sucesso!");
            return novo;
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível cadastrar: " + e.getMessage());
            return null;
        }
    }

    private static void cadastrarPropriedade(Proprietario dono) {
        try {
            System.out.println("1 - Casa | 2 - Apartamento | 3 - Sítio");
            int tipo = lerInteiro("Tipo da propriedade: ", 1, 3);
            String titulo = lerTexto("Título: ");
            String descricao = lerTexto("Descrição: ");
            String localizacao = lerTexto("Localização: ");
            int capacidade = lerInteiro("Capacidade de hóspedes: ", 1, Integer.MAX_VALUE);
            double preco = lerDouble("Preço por noite: ");
            Propriedade p;
            if (tipo == 1) {
                boolean piscina = lerTexto("Possui piscina? (s/n): ").equalsIgnoreCase("s");
                double porPessoa = lerDouble("Preço por pessoa: ");
                p = new Casa(titulo, descricao, localizacao, capacidade, preco, dono, piscina, porPessoa);
            } else if (tipo == 2) {
                int andar = lerInteiro("Andar: ", 0, Integer.MAX_VALUE);
                double taxa = lerDouble("Taxa: ");
                p = new Apartamento(titulo, descricao, localizacao, capacidade, preco, dono, andar, taxa);
            } else {
                double area = lerDouble("Área total em m²: ");
                p = new Sitio(titulo, descricao, localizacao, capacidade, preco, dono, area);
            }
            listaPropriedades.add(p);
            dono.cadastrarPropriedade(p);
            System.out.println("Propriedade cadastrada! Código: " + p.getCodigo());
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível cadastrar a propriedade: " + e.getMessage());
        }
    }

    private static void consultarDisponibilidade(Cliente cliente) {
        try {
            String local = lerTexto("Localização (deixe vazio para todas): ");
            LocalDate entrada = lerData("Check-in (AAAA-MM-DD): ");
            LocalDate saida = lerData("Check-out (AAAA-MM-DD): ");
            if (entrada.isBefore(LocalDate.now())) throw new IllegalArgumentException("O check-in não pode ser anterior à data atual.");
            if (!saida.isAfter(entrada)) throw new IllegalArgumentException("O check-out deve ser posterior ao check-in.");
            cliente.listarPropriedadesDisponiveis(listaPropriedades, local, entrada, saida);
        } catch (RuntimeException e) {
            System.out.println("Não foi possível consultar: " + e.getMessage());
        }
    }

    private static void fazerReserva(Cliente cliente) {
        try {
            int codigo = lerInteiro("Código da propriedade: ");
            Propriedade propriedade = buscarPropriedade(codigo);
            if (propriedade == null) throw new IllegalArgumentException("Código de propriedade não encontrado.");
            LocalDate entrada = lerData("Check-in (AAAA-MM-DD): ");
            LocalDate saida = lerData("Check-out (AAAA-MM-DD): ");
            if (entrada.isBefore(LocalDate.now())) throw new IllegalArgumentException("O check-in não pode ser anterior à data atual.");
            int hospedes = lerInteiro("Quantidade de hóspedes: ");
            Reserva reserva = cliente.realizarReserva(propriedade, entrada, saida, hospedes);
            System.out.println("Reserva realizada com sucesso!");
            reserva.imprimirDados();
        } catch (ReservaInvalidaException | IllegalArgumentException e) {
            System.out.println("Não foi possível realizar a reserva: " + e.getMessage());
        }
    }

    private static Propriedade buscarPropriedade(int codigo) {
        for (Propriedade p : listaPropriedades) if (p.getCodigo() == codigo) return p;
        return null;
    }

    private static void listarPropriedades() {
        if (listaPropriedades.isEmpty()) System.out.println("Nenhuma propriedade cadastrada.");
        Collections.sort(listaPropriedades);
        for (Propriedade p : listaPropriedades) p.imprimirDados();
    }

    private static void listarUsuarios() {
        if (listaUsuarios.isEmpty()) System.out.println("Nenhum usuário cadastrado.");
        for (Usuario u : listaUsuarios) u.imprimirDados();
    }

    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try { return Integer.parseInt(lerTexto(mensagem)); }
            catch (NumberFormatException e) { System.out.println("Digite um número inteiro válido."); }
        }
    }

    private static int lerInteiro(String mensagem, int min, int max) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= min && valor <= max) return valor;
            System.out.println("Digite um valor entre " + min + " e " + max + ".");
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try { return Double.parseDouble(lerTexto(mensagem).replace(',', '.')); }
            catch (NumberFormatException e) { System.out.println("Digite um número válido."); }
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            try { return LocalDate.parse(lerTexto(mensagem)); }
            catch (DateTimeParseException e) { System.out.println("Data inválida. Utilize AAAA-MM-DD."); }
        }
    }
}
