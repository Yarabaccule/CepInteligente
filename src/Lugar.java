public class Lugar {
    private String cep;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;

    @Override
    public String toString() {
        return logradouro + ", " + bairro + ", " + localidade + " - " + uf + " (CEP: " + cep + ")";
    }
}
