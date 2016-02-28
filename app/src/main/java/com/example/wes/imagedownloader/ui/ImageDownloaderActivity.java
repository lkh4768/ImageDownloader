package com.example.wes.imagedownloader.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wes.imagedownloader.R;
import com.example.wes.imagedownloader.util.ImageFetcher;

public class ImageDownloaderActivity extends AppCompatActivity {
    private ImageFetcher imageFetcher;
    private EditText imageURLEditText;
    private Button imageLoaderBtn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_downloader);

        imageURLEditText = (EditText) findViewById(R.id.imageURLEditText);
        imageLoaderBtn = (Button) findViewById(R.id.imageLoaderBtn);
        imageView = (ImageView) findViewById(R.id.imageView);

        imageFetcher = new ImageFetcher(this.getApplicationContext());

        imageLoaderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFetcher.imageFetchControler(imageURLEditText.getText().toString(), imageView);
            }
        });
    }
}
