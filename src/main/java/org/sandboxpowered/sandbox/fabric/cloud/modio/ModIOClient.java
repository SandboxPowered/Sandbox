package org.sandboxpowered.sandbox.fabric.cloud.modio;

import org.sandboxpowered.sandbox.fabric.cloud.modio.objects.DataWrap;
import org.sandboxpowered.sandbox.fabric.cloud.modio.objects.GameObject;
import org.sandboxpowered.sandbox.fabric.cloud.modio.objects.ModObject;
import org.sandboxpowered.sandbox.fabric.cloud.modio.services.ModService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModIOClient {
    private final String apiKey, apiUrl;
    private final int gameId;
    private final Retrofit retrofit;
    private GameObject game;

    public ModIOClient(String apiKey, int gameId, boolean test) {
        this.apiKey = apiKey;
        this.apiUrl = test ? "https://api.test.mod.io/v1/" : "https://api.mod.io/v1/";
        this.gameId = gameId;
        this.retrofit = new Retrofit.Builder().baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    public CompletableFuture<List<ModObject>> getMods() {
        CompletableFuture<List<ModObject>> completableFuture = new CompletableFuture<>();
        retrofit.create(ModService.class).listRepos(gameId, apiKey).enqueue(callbackToCompletableFutureWithoutWrap(completableFuture));
        return completableFuture;
    }

    private <T> Callback<DataWrap<T>> callbackToCompletableFutureWithoutWrap(CompletableFuture<T> completableFuture) {
        return new Callback<DataWrap<T>>() {
            @Override
            public void onResponse(@Nonnull Call<DataWrap<T>> call, @Nonnull Response<DataWrap<T>> response) {
                if (response.isSuccessful())
                    completableFuture.complete(Objects.requireNonNull(response.body()).data);
                else
                    completableFuture.cancel(false);
            }

            @Override
            public void onFailure(@Nonnull Call<DataWrap<T>> call, @Nonnull Throwable t) {
                completableFuture.completeExceptionally(t);
            }
        };
    }

    private <T> Callback<T> callbackToCompletableFuture(CompletableFuture<T> completableFuture) {
        return new Callback<T>() {
            @Override
            public void onResponse(@Nonnull Call<T> call, @Nonnull Response<T> response) {
                if (response.isSuccessful())
                    completableFuture.complete(response.body());
                else
                    completableFuture.cancel(false);
            }

            @Override
            public void onFailure(@Nonnull Call<T> call, @Nonnull Throwable t) {
                completableFuture.completeExceptionally(t);
            }
        };
    }
}