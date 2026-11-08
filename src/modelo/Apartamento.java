package modelo;

public class Apartamento extends Propriedade {
    private int andar;
    private double taxa;

    public Apartamento(String titulo, String descricao, String localizacao, int capacidade, double preco,
            Proprietario proprietario, int andar, double taxa) {
        super(titulo, descricao, localizacao, capacidade, preco, proprietario);
        setAndar(andar);
        setTaxa(taxa);
    }

    public Apartamento(int codigo, String titulo, String descricao, String localizacao, int capacidade, double preco,
            Proprietario proprietario, int andar, double taxa) {
        super(codigo, titulo, descricao, localizacao, capacidade, preco, proprietario);
        setAndar(andar);
        setTaxa(taxa);
    }

    public int getAndar() {
        return andar;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setAndar(int andar) {
        if (andar < 0)
            throw new IllegalArgumentException("O andar não pode ser negativo.");
        this.andar = andar;
    }

    public void setTaxa(double taxa) {
        if (taxa < 0)
            throw new IllegalArgumentException("A taxa não pode ser negativa.");
        this.taxa = taxa;
    }

    @Override
    public double calcularPrecoTotal(int noites) {
        return taxa + getPrecoPorNoite() * noites;
    } // taxa + preço por noite × noites

    @Override
    public void imprimirDados() {
        System.out.println("APARTAMENTO - " + super.toString());
        System.out.println("Descrição: " + getDescricao() + " | Capacidade: " + getCapacidade());
        System.out.println("Andar: " + andar + " | Taxa: R$ " + taxa);
        System.out.println("Proprietário: " + getProprietario().getNome());
    }

    @Override
    public String toString() {
        return "Apartamento - " + super.toString();
    }
}
