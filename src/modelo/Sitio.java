package modelo;

public class Sitio extends Propriedade {
    private double areaTotal;

    public Sitio(String titulo, String descricao, String localizacao, int capacidade, double preco,
            Proprietario proprietario, double areaTotal) {
        super(titulo, descricao, localizacao, capacidade, preco, proprietario);
        setAreaTotal(areaTotal);
    }

    public Sitio(int codigo, String titulo, String descricao, String localizacao, int capacidade, double preco,
            Proprietario proprietario, double areaTotal) {
        super(codigo, titulo, descricao, localizacao, capacidade, preco, proprietario);
        setAreaTotal(areaTotal);
    }

    public double getAreaTotal() { return areaTotal; }
    public void setAreaTotal(double area) {
        if (area <= 0) throw new IllegalArgumentException("A área total deve ser maior que zero.");
        areaTotal = area;
    }

    @Override
    public double calcularPrecoTotal(int noites) { return getPrecoPorNoite() * noites; }

    @Override
    public void imprimirDados() {
        System.out.println("SÍTIO - " + super.toString());
        System.out.println("Descrição: " + getDescricao() + " | Capacidade: " + getCapacidade());
        System.out.println("Área total: " + areaTotal + " m²");
        System.out.println("Proprietário: " + getProprietario().getNome());
    }

    @Override
    public String toString() { return "Sítio - " + super.toString(); }
}
