package modelo;

public class Casa extends Propriedade {
    private boolean possuiPiscina;
    private double precoPorPessoa;

    public Casa(String titulo, String descricao, String localizacao, int capacidade, double preco,
            Proprietario proprietario, boolean piscina, double precoPorPessoa) {
        super(titulo, descricao, localizacao, capacidade, preco, proprietario);
        setPossuiPiscina(piscina);
        setPrecoPorPessoa(precoPorPessoa);
    }

    public Casa(int codigo, String titulo, String descricao, String localizacao, int capacidade, double preco,
            Proprietario proprietario, boolean piscina, double precoPorPessoa) {
        super(codigo, titulo, descricao, localizacao, capacidade, preco, proprietario);
        setPossuiPiscina(piscina);
        setPrecoPorPessoa(precoPorPessoa);
    }

    public boolean isPossuiPiscina() { return possuiPiscina; }
    public double getPrecoPorPessoa() { return precoPorPessoa; }
    public void setPossuiPiscina(boolean possuiPiscina) { this.possuiPiscina = possuiPiscina; }
    public void setPrecoPorPessoa(double preco) {
        if (preco < 0) throw new IllegalArgumentException("O preço por pessoa não pode ser negativo.");
        precoPorPessoa = preco;
    }

    @Override
    public double calcularPrecoTotal(int noites) { return calcularPrecoTotal(noites, 1); }

    @Override
    public double calcularPrecoTotal(int noites, int hospedes) {
        if (hospedes < 1 || hospedes > getCapacidade()) {
            throw new IllegalArgumentException("A quantidade de hóspedes deve estar entre 1 e " + getCapacidade() + ".");
        }
        return (getPrecoPorNoite() + precoPorPessoa * hospedes) * noites;
    }

    @Override
    public void imprimirDados() {
        System.out.println("CASA - " + super.toString());
        System.out.println("Descrição: " + getDescricao() + " | Capacidade: " + getCapacidade());
        System.out.println("Piscina: " + (possuiPiscina ? "Sim" : "Não") + " | Preço por pessoa: R$ " + precoPorPessoa);
        System.out.println("Proprietário: " + getProprietario().getNome());
    }

    @Override
    public String toString() { return "Casa - " + super.toString(); }
}
