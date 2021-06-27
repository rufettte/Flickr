package com.flickr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.flickr.Objects.Image;
import com.flickr.flickr.R;
import android.os.Bundle;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);

        Image image = (Image) getIntent().getSerializableExtra(getBaseContext().getString(R.string.img_url));
        ImageView iw = findViewById(R.id.imageViewer);

        Glide.with(this)
                .load(image.getUrl())
                .into(iw);
    }
}