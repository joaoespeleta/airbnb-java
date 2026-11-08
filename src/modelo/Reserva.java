package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import excecoes.ReservaInvalidaException;

public class Reserva {
    private static int proximoCodigo = 1;
    private int codigo;
    private Propriedade propriedade;
    private Cliente cliente;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int quantidadeHospedes;
    private double custoTotal;

    public Reserva(Propriedade propriedade, Cliente cliente, LocalDate checkIn,
            LocalDate checkOut, int hospedes) throws ReservaInvalidaException {
        this(proximoCodigo++, propriedade, cliente, checkIn, checkOut, hospedes);
    }

    public Reserva(int codigo, Propriedade propriedade, Cliente cliente, LocalDate checkIn,
            LocalDate checkOut, int hospedes) throws ReservaInvalidaException {
        if (codigo <= 0) throw new ReservaInvalidaException("Código de reserva inválido.");
        if (propriedade == null || cliente == null) throw new ReservaInvalidaException("Cliente e propriedade são obrigatórios.");
        if (checkIn == null || checkOut == null || !checkOut.isAfter(checkIn))
            throw new ReservaInvalidaException("O check-out deve ser posterior ao check-in.");
        if (hospedes < 1 || hospedes > propriedade.getCapacidade())
            throw new ReservaInvalidaException("Quantidade de hóspedes fora da capacidade do imóvel.");
        if (!propriedade.verificarDisponibilidade(checkIn, checkOut))
            throw new ReservaInvalidaException("A propriedade já possui reserva nesse período.");
        this.codigo = codigo;
        if (codigo >= proximoCodigo) proximoCodigo = codigo + 1;
        this.propriedade = propriedade;
        this.cliente = cliente;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.quantidadeHospedes = hospedes;
        calcularCustoTotal();
    }

    public int getCodigo() { return codigo; }
    public Propriedade getPropriedade() { return propriedade; }
    public Cliente getCliente() { return cliente; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public int getQuantidadeHospedes() { return quantidadeHospedes; }
    public double getCustoTotal() { return custoTotal; }

    public long calcularNumeroNoites() { return ChronoUnit.DAYS.between(checkIn, checkOut); }

    public void calcularCustoTotal() {
        custoTotal = propriedade.calcularPrecoTotal((int) calcularNumeroNoites(), quantidadeHospedes);
    }

    public void imprimirDados() {
        System.out.println("Reserva código: " + codigo);
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("Propriedade: " + propriedade.getTitulo() + " (código " + propriedade.getCodigo() + ")");
        System.out.println("Check-in: " + checkIn + " | Check-out: " + checkOut);
        System.out.println("Hóspedes: " + quantidadeHospedes + " | Noites: " + calcularNumeroNoites());
        System.out.printf("Custo total: R$ %.2f%n", custoTotal);
    }
}
