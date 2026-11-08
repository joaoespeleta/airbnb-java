package modelo;

import java.util.ArrayList;

public class Proprietario extends Usuario {
    private ArrayList<Propriedade> propriedades;

    public Proprietario(String nome, String email, String senha) {
        super(nome, email, senha);
        propriedades = new ArrayList<>();
    }

    public ArrayList<Propriedade> getPropriedades() { return propriedades; }

    public void cadastrarPropriedade(Propriedade propriedade) {
        propriedades.add(propriedade);
    }

    public void listarPropriedades() {
        if (propriedades.isEmpty()) System.out.println("Você ainda não possui propriedades.");
        for (Propriedade p : propriedades) p.imprimirDados();
    }

    public void listarPropriedadesAlugadas() {
        boolean encontrou = false;
        for (Propriedade p : propriedades) {
            if (p.estaAlugada()) {
                encontrou = true;
                System.out.println("\n" + p.getTitulo() + " (código " + p.getCodigo() + ")");
                for (Reserva r : p.getReservas()) r.imprimirDados();
            }
        }
        if (!encontrou) System.out.println("Nenhuma propriedade possui reservas.");
    }

    @Override
    public void imprimirDados() {
        System.out.println("PROPRIETÁRIO");
        System.out.println(super.toString());
        System.out.println("Quantidade de propriedades: " + propriedades.size());
    }

    @Override
    public String toString() {
        return "Proprietário - " + super.toString();
    }
}
