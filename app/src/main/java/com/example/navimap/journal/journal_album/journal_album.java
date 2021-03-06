package com.example.navimap.journal.journal_album;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navimap.R;

import java.io.File;

public class journal_album extends AppCompatActivity {

    private Intent resultintent = new Intent();
    private String markerName = null;

    private journal_album_adapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_album);
        getSupportActionBar().setTitle("圖片庫");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultintent = getIntent();
        markerName = resultintent.getStringExtra("Name");
        File photoPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/AppCameraPhoto/" + markerName);
        try {
            final File[] path = photoPath.listFiles();

            RecyclerView recyclerViewLayout = (RecyclerView) findViewById(R.id.journal_custom_album);
            //        為了避免內容物(圖片) 拉展到外面的recylerView
            recyclerViewLayout.setHasFixedSize(true);
            //        設定onClickListener
            recyclerViewLayout.addOnItemTouchListener(
                    new journal_album_onitemclicklistener(getApplicationContext(), recyclerViewLayout, new journal_album_onitemclicklistener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (!resultintent.getStringExtra("isShowOnly").equals("yes")) {

                                //                        點一下獲得圖片的檔案名
                                resultintent.putExtra("getJournal_album_photoName", path[position].getPath().substring(path[position].getPath().lastIndexOf("/") + 1, path[position].getPath().length()));
                                resultintent.putExtra("album_result_markerName", markerName);
                                resultintent.putExtra("IsEmpty", "no");
                                setResult(RESULT_OK, resultintent);
                                finish();
                            }
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            ;
                        }
                    })
            );

            //        設定一列多少個圖片
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            //        設定recyclerview 到該管理器, 管理器已經設定好一列多少個元素了
            recyclerViewLayout.setLayoutManager(layoutManager);
            //        Adapter 來調適配置內部框架(內容物)
            adapter = new journal_album_adapter(path);//如果path有非圖片就會爆炸
            recyclerViewLayout.setAdapter(adapter);
        } catch (Exception e) {
            System.out.println("crash");
            resultintent.putExtra("IsEmpty", "yes");
            setResult(RESULT_OK, resultintent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
