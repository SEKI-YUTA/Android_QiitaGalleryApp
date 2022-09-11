package com.example.qiitagalleryapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.qiitagalleryapp.Adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recycler_img;
    private List<String> pathList;
    private ImageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycler_img = findViewById(R.id.recycler_img);
        recycler_img.setHasFixedSize(true);
        recycler_img.setLayoutManager(new GridLayoutManager(this, 3));

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // パーミッションがまだ許可されていなかった場合
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            // パーミッションが既に許可されていた場合
            appSetUP();
        }
    }

    @SuppressLint("Range")
    private void appSetUP() {
        // リストの初期化
        pathList = new ArrayList<>();

        // UIスレッドで処理すると画面が一瞬固まるので別スレッド
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                // 画像の情報を取得してリストに追加する処理
                Cursor cursor = getApplicationContext().getContentResolver()
                        .query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                null,
                                null,
                                null,
                                MediaStore.Images.Media.DATE_ADDED + " DESC"
                        );

                while(cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    pathList.add(path);
                }
                // 画像の情報を取得してリストに追加する処理　ここまで

                // RecyclerViewのアダプターのインスタンスを作成
                adapter = new ImageAdapter(MainActivity.this, pathList);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // UIスレッド以外からUIを更新するとエラーが出るのでHandlerを使ってUIスレッドで処理する
                        recycler_img.setAdapter(adapter);
                    }
                });
            }
        });

    }

    // パーミッションをリクエストする処理
    // requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE); のようにしてリクエストする
    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if(result) {
            appSetUP();
        } else {
            Toast.makeText(this, "Read Storage permission is required", Toast.LENGTH_SHORT).show();
        }
    });
}