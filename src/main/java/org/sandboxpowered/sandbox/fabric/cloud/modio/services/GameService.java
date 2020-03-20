package org.sandboxpowered.sandbox.fabric.cloud.modio.services;

import org.sandboxpowered.sandbox.fabric.cloud.modio.objects.DataWrap;
import org.sandboxpowered.sandbox.fabric.cloud.modio.objects.GameObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface GameService {
    @GET("games")
    Call<DataWrap<List<GameObject>>> listGames(@Query("api_key") String api_key);
    @GET("games/{game}")
    Call<GameObject> getGame(@Path("game") int game_id,@Query("api_key") String api_key);
}
