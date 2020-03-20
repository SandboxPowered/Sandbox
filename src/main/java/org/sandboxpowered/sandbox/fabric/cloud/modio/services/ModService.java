package org.sandboxpowered.sandbox.fabric.cloud.modio.services;

import org.sandboxpowered.sandbox.fabric.cloud.modio.objects.DataWrap;
import org.sandboxpowered.sandbox.fabric.cloud.modio.objects.ModObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ModService {
    @GET("games/{game}/mods")
    Call<DataWrap<List<ModObject>>> listRepos(@Path("game") int game, @Query("api_key") String api_key);
}
