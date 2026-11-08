package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import excecoes.ReservaInvalidaException;

public class Cliente extends Usuario {
    private ArrayList<Reserva> reservasRealizadas;

    public Cliente(String nome, String email, String senha) {
        super(nome, email, senha);
        reservasRealizadas = new ArrayList<>();
    }

    public ArrayList<Reserva> getReservasRealizadas() { return reservasRealizadas; }

    public Reserva realizarReserva(Propriedade p, LocalDate checkIn, LocalDate checkOut)
            throws ReservaInvalidaException {
        return realizarReserva(p, checkIn, checkOut, 1);
    }

    public Reserva realizarReserva(Propriedade p, LocalDate checkIn, LocalDate checkOut, int hospedes)
            throws ReservaInvalidaException {
        Reserva reserva = new Reserva(p, this, checkIn, checkOut, hospedes);
        p.adicionarReserva(reserva);
        reservasRealizadas.add(reserva);
        return reserva;
    }

    public void listarReservas() {
        if (reservasRealizadas.isEmpty()) System.out.println("Você ainda não possui reservas.");
        for (Reserva r : reservasRealizadas) r.imprimirDados();
    }

    public void listarPropriedadesDisponiveis(ArrayList<Propriedade> propriedades,
            String localizacao, LocalDate entrada, LocalDate saida) {
        ArrayList<Propriedade> disponiveis = new ArrayList<>();
        for (Propriedade p : propriedades) {
            boolean mesmaLocalizacao = localizacao == null || localizacao.trim().isEmpty()
                    || p.getLocalizacao().toLowerCase().contains(localizacao.toLowerCase());
            if (mesmaLocalizacao && p.verificarDisponibilidade(entrada, saida)) disponiveis.add(p);
        }
        Collections.sort(disponiveis);
        if (disponiveis.isEmpty()) System.out.println("Nenhuma propriedade disponível para esse período.");
        for (Propriedade p : disponiveis) p.imprimirDados();
    }

    @Override
    public void imprimirDados() {
        System.out.println("CLIENTE");
        System.out.println(super.toString());
        System.out.println("Quantidade de reservas: " + reservasRealizadas.size());
    }

    @Override
    public String toString() {
        return "Cliente - " + super.toString();
    }
}
