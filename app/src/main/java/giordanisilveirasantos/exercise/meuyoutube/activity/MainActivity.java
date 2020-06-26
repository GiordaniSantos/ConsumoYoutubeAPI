package giordanisilveirasantos.exercise.meuyoutube.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import giordanisilveirasantos.exercise.meuyoutube.R;
import giordanisilveirasantos.exercise.meuyoutube.adapter.AdapterVideo;
import giordanisilveirasantos.exercise.meuyoutube.api.YoutubeService;
import giordanisilveirasantos.exercise.meuyoutube.helper.RetrofitConfig;
import giordanisilveirasantos.exercise.meuyoutube.helper.YoutubeConfig;
import giordanisilveirasantos.exercise.meuyoutube.listener.RecyclerItemClickListener;
import giordanisilveirasantos.exercise.meuyoutube.model.Item;
import giordanisilveirasantos.exercise.meuyoutube.model.Resultado;
import giordanisilveirasantos.exercise.meuyoutube.model.Video;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerVideos;


    private AdapterVideo adapterVideo;
    private List<Item> videos= new ArrayList<>();
    private Resultado resultado;
    private MaterialSearchView searchView;
    //Retrofit
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerVideos = findViewById(R.id.recyclerVideos);
        searchView = findViewById(R.id.searchView);
        //configurações iniciais
        retrofit= RetrofitConfig.getRetrofit();

        //configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);

        //Recupera vídeos
        recuperarVideos("");


        //configura métodos para searchView
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recuperarVideos("");
            }
        });


    }

    private void recuperarVideos(String pesquisa){

        String q= pesquisa.replaceAll(" ", "+");

        YoutubeService youtubeService= retrofit.create(YoutubeService.class);
        youtubeService.recuperarVideos(
                "snippet", "date", "30", YoutubeConfig.CHAVE_YOUTUBE_API, YoutubeConfig.CANAL_ID, q
        ).enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Log.d("resultado", "resultado: "+ response.toString());
                if( response.isSuccessful()){
                    resultado = response.body();
                    videos= resultado.items;
                    configurarRecyclerView();

                }

            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

            }
        });

    }

    public void configurarRecyclerView(){
        adapterVideo=new AdapterVideo(videos, this);
        recyclerVideos.setHasFixedSize( true );
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo );
        //configurar evento de clique
        recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerVideos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Item video= videos.get(position);
                String idVideo= video.id.videoId;
                Intent i =new Intent(MainActivity.this, PlayerActivity.class);
                i.putExtra("idVideo", idVideo);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item= menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);
        return true;
    }
}
