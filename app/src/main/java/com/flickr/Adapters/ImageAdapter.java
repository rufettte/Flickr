package com.flickr.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.flickr.Activities.ImageViewerActivity;
import com.flickr.Objects.Image;
import com.flickr.flickr.R;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    /*
    * images - List of the items
    * context - Current context of the application; note that this is important when clicking on an item.
    * */
    private final List<Image> images;
    private final Context context;

    /*
     * Constructs the ImageAdapter
     * */
    public ImageAdapter(List<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    /*
     * adds block of items at the same time to the end of the item list.
     * */
    public void addAll(List<Image> packOfImages) {
        images.addAll(packOfImages);
        notifyItemRangeChanged(images.size()-1, packOfImages.size());
    }

    /*
     * The number of currently existing data in the item list.
     * */
    @Override
    public int getItemCount() {
        return images.size();
    }

    /*
    * This class is important for keeping item VISIBLE to the user.
    * */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /*
        * imageView - representator of the image(s) of the current item(s).
        * titleView - representator of the title(s) of the current item(s).
        * */
        public ImageView imageView;
        public TextView titleView;

        /*
        * Initializes imageView and titleView for the currently visible item(s).
        * */
        public ViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.image);
            titleView = v.findViewById(R.id.title);
        }
    }

    /*
    * This function creates the View (e.g., CardView),
    * which is hold by the view holder to be presentable (visible) to the user.
    * */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    /*
    * The function below gets the item(s) at the current position
    * and helps to show their details (image and title) to the user.
    * */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        /*External 3rd party library, which is open source, developer friendly and quite fast.
        * Displays the image at the current "position".
        *
        * Alternatives: 1) Manually, by using AsyncTask, Volley, etc.
                        2) Using the other 3rd part libraries, such as Picasso, UIL, etc.
        * */
        Glide.with(viewHolder.itemView)
                .load(images.get(position).getUrl())
                .centerCrop()
                .into(viewHolder.imageView);

        /*
        * Displays the title of the current image
        * */
        viewHolder.titleView.setText(images.get(position).getTitle());

        /*
        * When an item is clicked, another activity is opened, where the image
        * of the clicked item is demonstrated to the user with up-scaled view.
        */
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageViewerActivity.class);
            intent.putExtra("Image", images.get(position));
            context.startActivity(intent);
        });
    }
}
