package persistencia;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.*;

/** Faz a leitura dos arquivos de texto usados como dados iniciais. */
public class GerenciarDadosIniciais {

    public static void carregarUsuarios(String caminho, ArrayList<Usuario> usuarios) {
        try (Scanner arquivo = new Scanner(new File(caminho))) {
            while (arquivo.hasNextLine()) {
                String linha = arquivo.nextLine();
                if (linha.trim().isEmpty() || linha.startsWith("#"))
                    continue;
                String[] dados = linha.split(";", -1);
                try {
                    Usuario usuario;
                    if (dados[0].equalsIgnoreCase("P")) {
                        usuario = new Proprietario(dados[1], dados[2], dados[3]);
                    } else {
                        usuario = new Cliente(dados[1], dados[2], dados[3]);
                    }
                    usuarios.add(usuario);
                } catch (RuntimeException e) {
                    System.out.println("Usuário ignorado no arquivo: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de usuários não encontrado: " + caminho);
        }
    }

    public static void carregarPropriedades(String caminho, ArrayList<Usuario> usuarios,
            ArrayList<Propriedade> propriedades) {
        try (Scanner arquivo = new Scanner(new File(caminho))) {
            while (arquivo.hasNextLine()) {
                String linha = arquivo.nextLine();
                if (linha.trim().isEmpty() || linha.startsWith("#"))
                    continue;
                String[] d = linha.split(";", -1);
                try {
                    Proprietario dono = buscarProprietario(usuarios, d[7]);
                    if (dono == null)
                        throw new IllegalArgumentException("Proprietário não encontrado.");
                    int codigo = Integer.parseInt(d[0]);
                    String tipo = d[1];
                    Propriedade p;
                    if (tipo.equalsIgnoreCase("CASA")) {
                        p = new Casa(codigo, d[2], d[3], d[4], Integer.parseInt(d[5]),
                                Double.parseDouble(d[6]), dono, Boolean.parseBoolean(d[8]), Double.parseDouble(d[9]));
                    } else if (tipo.equalsIgnoreCase("APARTAMENTO")) {
                        p = new Apartamento(codigo, d[2], d[3], d[4], Integer.parseInt(d[5]),
                                Double.parseDouble(d[6]), dono, Integer.parseInt(d[8]), Double.parseDouble(d[9]));
                    } else {
                        p = new Sitio(codigo, d[2], d[3], d[4], Integer.parseInt(d[5]),
                                Double.parseDouble(d[6]), dono, Double.parseDouble(d[8]));
                    }
                    propriedades.add(p);
                    dono.cadastrarPropriedade(p);
                } catch (RuntimeException e) {
                    System.out.println("Propriedade ignorada no arquivo: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de propriedades não encontrado: " + caminho);
        }
    }

    public static void carregarReservas(String caminho, ArrayList<Usuario> usuarios,
            ArrayList<Propriedade> propriedades) {
        try (Scanner arquivo = new Scanner(new File(caminho))) {
            while (arquivo.hasNextLine()) {
                String linha = arquivo.nextLine();
                if (linha.trim().isEmpty() || linha.startsWith("#"))
                    continue;
                String[] d = linha.split(";", -1);
                try {
                    Propriedade p = buscarPropriedade(propriedades, Integer.parseInt(d[0]));
                    Cliente c = buscarCliente(usuarios, d[1]);
                    if (p == null || c == null)
                        throw new IllegalArgumentException("Cliente ou propriedade não encontrado.");
                    c.realizarReserva(p, LocalDate.parse(d[2]), LocalDate.parse(d[3]), Integer.parseInt(d[4]));
                } catch (Exception e) {
                    System.out.println("Reserva ignorada no arquivo: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de reservas não encontrado: " + caminho);
        }
    }

    private static Proprietario buscarProprietario(ArrayList<Usuario> usuarios, String email) {
        for (Usuario u : usuarios)
            if (u instanceof Proprietario && u.getEmail().equalsIgnoreCase(email))
                return (Proprietario) u;
        return null;
    }

    private static Cliente buscarCliente(ArrayList<Usuario> usuarios, String email) {
        for (Usuario u : usuarios)
            if (u instanceof Cliente && u.getEmail().equalsIgnoreCase(email))
                return (Cliente) u;
        return null;
    }

    // é necessário utilizar instanceof pois o ArrayList<Usuario> usuarios; é tanto
    // Proprietario quanto Cliente, então é necessário verificar se o usuário é do
    // tipo correto antes.
    private static Propriedade buscarPropriedade(ArrayList<Propriedade> propriedades, int codigo) {
        for (Propriedade p : propriedades)
            if (p.getCodigo() == codigo)
                return p;
        return null;
    }
}
