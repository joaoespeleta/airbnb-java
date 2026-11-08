package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

/** Classe base dos tipos de propriedade. */
public abstract class Propriedade implements Comparable<Propriedade> {
    private static int proximoCodigo = 1;
    private int codigo;
    private String titulo;
    private String descricao;
    private String localizacao;
    private int capacidade;
    private double precoPorNoite;
    private boolean disponivel;
    private Proprietario proprietario;
    private ArrayList<Reserva> reservas;

    public Propriedade(String titulo, String descricao, String localizacao, int capacidade,
            double precoPorNoite, Proprietario proprietario) {
        this(proximoCodigo++, titulo, descricao, localizacao, capacidade, precoPorNoite, proprietario);
    }

    public Propriedade(int codigo, String titulo, String descricao, String localizacao, int capacidade,
            double precoPorNoite, Proprietario proprietario) {
        if (codigo <= 0) throw new IllegalArgumentException("Código da propriedade inválido.");
        this.codigo = codigo;
        if (codigo >= proximoCodigo) proximoCodigo = codigo + 1;
        setTitulo(titulo);
        setDescricao(descricao);
        setLocalizacao(localizacao);
        setCapacidade(capacidade);
        setPrecoPorNoite(precoPorNoite);
        setProprietario(proprietario);
        disponivel = true;
        reservas = new ArrayList<>();
    }

    public int getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public String getLocalizacao() { return localizacao; }
    public int getCapacidade() { return capacidade; }
    public double getPrecoPorNoite() { return precoPorNoite; }
    public boolean isDisponivel() { return disponivel; }
    public Proprietario getProprietario() { return proprietario; }
    public ArrayList<Reserva> getReservas() { return reservas; }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) throw new IllegalArgumentException("O título não pode ficar vazio.");
        this.titulo = titulo.trim();
    }
    public void setDescricao(String descricao) {
        if (descricao == null) throw new IllegalArgumentException("Descrição inválida.");
        this.descricao = descricao.trim();
    }
    public void setLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty()) throw new IllegalArgumentException("A localização não pode ficar vazia.");
        this.localizacao = localizacao.trim();
    }
    public void setCapacidade(int capacidade) {
        if (capacidade <= 0) throw new IllegalArgumentException("A capacidade deve ser maior que zero.");
        this.capacidade = capacidade;
    }
    public void setPrecoPorNoite(double preco) {
        if (preco <= 0) throw new IllegalArgumentException("O preço por noite deve ser maior que zero.");
        this.precoPorNoite = preco;
    }
    public void setProprietario(Proprietario proprietario) {
        if (proprietario == null) throw new IllegalArgumentException("A propriedade precisa ter um proprietário.");
        this.proprietario = proprietario;
    }

    public boolean verificarDisponibilidade() {
        return disponivel;
    }

    public boolean verificarDisponibilidade(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn)) return false;
        for (Reserva r : reservas) {
            boolean conflito = checkIn.isBefore(r.getCheckOut()) && checkOut.isAfter(r.getCheckIn());
            if (conflito) return false;
        }
        return true;
    }

    public void adicionarReserva(Reserva reserva) { reservas.add(reserva); }
    public boolean estaAlugada() { return !reservas.isEmpty(); }

    public abstract double calcularPrecoTotal(int noites);

    public double calcularPrecoTotal(int noites, int hospedes) {
        if (hospedes < 1 || hospedes > capacidade) {
            throw new IllegalArgumentException("A quantidade de hóspedes deve estar entre 1 e " + capacidade + ".");
        }
        return calcularPrecoTotal(noites);
    }

    public abstract void imprimirDados();

    @Override
    public int compareTo(Propriedade outra) {
        return Double.compare(this.precoPorNoite, outra.precoPorNoite);
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | " + titulo + " | " + localizacao
                + " | R$ " + precoPorNoite + " por noite";
    }
}
