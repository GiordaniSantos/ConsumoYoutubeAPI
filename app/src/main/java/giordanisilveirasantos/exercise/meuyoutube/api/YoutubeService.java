package giordanisilveirasantos.exercise.meuyoutube.api;

import giordanisilveirasantos.exercise.meuyoutube.model.Resultado;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {

    /*
    https://www.googleapis.com/youtube/v3/
    search
    ?part=snippet
    &order=date
    &maxResult=20
    &key=AIzaSyCI6LtQb02ULlp9Jin8Brxz6mKYZ0fYbEE
    &channelId=UCzGwyAyWLB2Si6VDFpq8rjw

   https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResult=20&key=AIzaSyCI6LtQb02ULlp9Jin8Brxz6mKYZ0fYbEE&channelId=UCzGwyAyWLB2Si6VDFpq8rjw


     */


    @GET("search")
    Call<Resultado> recuperarVideos(@Query("part") String part, @Query("order") String order, @Query("maxResult") String maxResult, @Query("key") String key, @Query("channelId") String channelId, @Query("q") String q);
}
