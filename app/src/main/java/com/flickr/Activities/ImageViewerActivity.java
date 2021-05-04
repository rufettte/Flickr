package com.flickr.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.flickr.Objects.Image;
import com.flickr.flickr.R;
import android.os.Bundle;

/*This activity simply helps to show the image selected from the search result list.*/
public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);

        /* Gets the Serializable object - Image - passed from the
           context of the SearchableActivity (see ImageAdapter class for details) via the intent. */
        Image image = (Image) getIntent().getSerializableExtra("Image");
        ImageView iw = (ImageView) findViewById(R.id.imageViewer);

        /*External 3rd party library, which is open source, developer friendly and quite fast.
        * Displays the image of the selected search list item.
        *
        * Alternatives: 1) Manually, by using AsyncTask, Volley, etc.
                        2) Using the other 3rd part libraries, such as Picasso, UIL, etc.
        * */
        Glide.with(this)
                .load(image.getUrl())
                .into(iw);
    }
}