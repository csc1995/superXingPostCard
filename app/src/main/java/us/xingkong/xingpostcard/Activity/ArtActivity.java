package us.xingkong.xingpostcard.Activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import us.xingkong.xingpostcard.Contants.State;
import us.xingkong.xingpostcard.R;
import us.xingkong.xingpostcard.Utils.IOFile;

/**
 * Created by Garfield on 5/15/16. 11:38PM
 * OverWritten by hugeterry(http://hugeterry.cn)
 * Date: 2016/5/14 03:37
 * 用户编辑明信片页
 */
public class ArtActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView tv, tv2;
    private ScrollView sv;
    private LinearLayout ll;

    private int styleCode = 0;
    private int myphoto;
    private String myphotopath;
    int x = 0, y = 0;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);

        initToolbar();
        getStyle();//获取intent的值做初始化
        initViews();//根据获得的板式种类，初始化界面，绑定控件监听等...

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
        getSupportActionBar().setTitle("编辑明信片");

    }

    private void getStyle() {
        if (getIntent().getIntExtra("styleCode", -1) != -1) {
            styleCode = getIntent().getIntExtra("styleCode", styleCode);
            System.out.println("styleCode=" + styleCode);
        }

    }

    private void getPhoto(ImageView iv) {
        myphoto = getIntent().getIntExtra("myphoto", -1);
        myphotopath = getIntent().getStringExtra("myphotopath");
        if (myphoto != -1) {
            iv.setImageResource(myphoto);
        } else if (myphotopath != null) {
            iv.setImageBitmap(BitmapFactory.decodeFile(myphotopath));
            System.out.println(myphoto + "ssssssss" + myphotopath);
        }

    }

    private void initViews() {
        System.out.println("stylecode" + styleCode);
        sv = (ScrollView) findViewById(R.id.art_picsarea);

        Snackbar.make(sv, "点击文字进行修改", Snackbar.LENGTH_LONG).show();

        switch (styleCode) {
            case 0:
                View view = LayoutInflater.from(this).inflate(R.layout.pattern_1, sv, true);
                break;
            case 1:
                View v = LayoutInflater.from(this).inflate(R.layout.pattern_2, sv, true);
                break;
            case 2:
                LayoutInflater.from(this).inflate(R.layout.pattern_3, sv, true);
                break;
            case 3:
                LayoutInflater.from(this).inflate(R.layout.pattern_4, sv, true);
                break;
            case 4:
                LayoutInflater.from(this).inflate(R.layout.pattern_5, sv, true);
                break;
            case 5:
                LayoutInflater.from(this).inflate(R.layout.pattern_6, sv, true);
                break;
            case 6:
                LayoutInflater.from(this).inflate(R.layout.pattern_7, sv, true);
                break;
            case 7:
                LayoutInflater.from(this).inflate(R.layout.pattern_8, sv, true);
                break;

        }

        init();//初始化控件
        if (State.tv1_text == "") {
            State.tv1_text = tv.getText().toString();
            State.tv2_text = tv2.getText().toString();
        }

        if (getIntent().getStringExtra("words") != null) {

            int changeViewID = getIntent().getIntExtra("viewId", -1);
            if (tv.getId() == changeViewID) {
                State.tv1_text = getIntent().getStringExtra("words");

            } else if (tv2.getId() == changeViewID) {
                State.tv2_text = getIntent().getStringExtra("words");

            } else {
                System.out.println("ID会变");
            }
            tv.setText(State.tv1_text);
            tv2.setText(State.tv2_text);
        }


        tv.setOnClickListener(new textOnClickListener());
        tv2.setOnClickListener(new textOnClickListener());

    }

    private void init() {
        ll = (LinearLayout) findViewById(R.id.ll);
        ll.setDrawingCacheEnabled(true);
        iv = (ImageView) findViewById(R.id.iv1);
        iv.setDrawingCacheEnabled(true);
        tv = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv_date);
        getPhoto(iv);
    }

    private class textOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ArtActivity.this, EditCardActivity.class);
            if (!((TextView) view).getText().toString().isEmpty()) {
                intent.putExtra("words", ((TextView) view).getText().toString());
            }
            intent.putExtra("styleCode", 0);
            intent.putExtra("myphotopath", myphotopath);
            intent.putExtra("myphoto", myphoto);
            intent.putExtra("viewId", view.getId());
            intent.putExtra("styleCode", getIntent().getIntExtra("styleCode", -1));
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_art, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_done:
                Intent intent = new Intent(ArtActivity.this, ResultActivity.class);
                Bitmap bmp = ll.getDrawingCache();
                String path = IOFile.toSaveFile(bmp);
                IOFile.scanPhotos(path, this);
                intent.putExtra("resultPath", path);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
