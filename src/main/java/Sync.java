import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


public class Sync {
    private static Retrofit retrofit;

    public static void main(String[] args) throws IOException {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AtletaService atletaService = retrofit.create(AtletaService.class);
        Response<List<Atleta>> response = atletaService.getTodosAtletas().execute();
        if(response.isSuccessful()){
            List<Atleta> atletaList = response.body();
            System.out.println("Codigo Status: " +response.code() +System.lineSeparator() + "Obtener Todos atletas: "+
            atletaList);

        }else{
            System.out.println("Status codigo: " + response.code() + "Error: " + response.errorBody());
        }

        Response<List<Atleta>> responseErrorUrl = atletaService.getError().execute();

        if(!responseErrorUrl.isSuccessful()){
            System.out.println("Codigo Status: " + responseErrorUrl.code() + "Error Mensaje: " + responseErrorUrl.raw());
        }

        Atleta atleta = new Atleta();
        atleta.setNombre("Pepe");
        atleta.setApellido("Adrian");
        atleta.setNacionalidad("Catalunya");
        atleta.setNacimiento(LocalDate.of(1996, 02,15));
        Response<Atleta> Atletastot = atletaService.createAtletas(atleta).execute();

        if(Atletastot.isSuccessful()) {
            Atleta atletaR = Atletastot.body();
            System.out.println("Codigo status: " + Atletastot.code() + System.lineSeparator() + "Post jugador: " +
                    atletaR);

            Response<Atleta> response1atleta = atletaService.getAtletas(atletaR.getId()).execute();
            if(response1atleta.isSuccessful()){
                System.out.println("GET 1- Codigo Status: " + response1atleta.code() + "Atleta: "
                + response1atleta.body());
            }else{
                System.out.println("Codigo status: " + response1atleta.code() + "Mensaje error: "
                        + response1atleta.errorBody());
            }

            atletaR.setNacionalidad("Barcelona");
            Response<Atleta> insertarAtleta = atletaService.updateAtleta(atletaR).execute();
            if(response1atleta.isSuccessful()){
                System.out.println("Codigo status: " + insertarAtleta.code() + System.lineSeparator() +
                "Insertar jugador: " + insertarAtleta.body());
            }else{
                System.out.println("Codigo status: " + insertarAtleta.code() + "Mensaje error: " +
                insertarAtleta.errorBody());
            }

            Response<Void> borraratleta = atletaService.deleteAtleta(atletaR.getId()).execute();
            System.out.println("Borrar codigo status: " + borraratleta.code());

            response = atletaService.getTodosAtletas().execute();

            System.out.println("Comprobacion borraratleta: " + "Codigo status: " + response.code()
            + System.lineSeparator() + "Obtener jugadores: " + response.body());

        }

    }

}
