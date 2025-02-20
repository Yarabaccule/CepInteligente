import com.google.gson.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String buscar = "";
        List<Lugar> lista = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        while (!buscar.equals("0")) {
            System.out.print("Digite o CEP (ou 0 para sair): ");
            buscar = scanner.nextLine();

            if (buscar.equals("0")) {
                System.out.println("Programa encerrado.");
                break;
            }

            try {
                String cepEncoded = URLEncoder.encode(buscar, StandardCharsets.UTF_8);
                String url = "https://viacep.com.br/ws/" + cepEncoded + "/json/";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();

                System.out.println("🔍 Buscando dados...");
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String json = response.body();

                JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                if (jsonObject.has("erro")) {
                    System.out.println("⚠️ CEP não encontrado! Verifique os números digitados.");
                    continue;
                }

                Lugar lugar = gson.fromJson(json, Lugar.class);
                lista.add(lugar);
                System.out.println("✅ CEP encontrado: " + lugar);

            } catch (Exception e) {
                System.out.println("⚠️ Algo deu errado ao buscar o CEP. Tente novamente.");
            }
        }

        // Exibe os dados coletados antes de encerrar
        if (!lista.isEmpty()) {
            System.out.println("\n📌 Lista de CEPs pesquisados:");
            for (Lugar lugar : lista) {
                System.out.println("- " + lugar);
            }
        }

        // Salva a lista de CEPs em um arquivo JSON
        FileWriter escrita = new FileWriter("cep.json");
        escrita.write(gson.toJson(lista));
        System.out.println("📁 CEPs salvos no arquivo 'cep.json'.");
        escrita.close();


        scanner.close();
    }
}

