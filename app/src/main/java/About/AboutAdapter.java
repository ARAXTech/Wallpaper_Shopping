package About;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.qhs.wallpapershopping.R;

public class AboutAdapter extends BaseAdapter {
    private Context context;
    private String nameList[];
    private int imgList[];
    private LayoutInflater inflter;

    public AboutAdapter(Context context, String[] nameList,int[] imgList) {
        this.context = context;
        this.nameList = nameList;
        this.imgList=imgList;
        //  this.flags = flags;
        inflter = (LayoutInflater.from(context));
    }
    public int getCount() {
        return nameList.length;
    }


    public Object getItem(int i) {
        return null;
    }


    public long getItemId(int i) {
        return 0;
    }


    public View getView(int i, View view, ViewGroup viewGroup) {
//        Typeface face=Typeface.createFromAsset(context.getAssets(), "yekan/homa.ttf");
        view = inflter.inflate(R.layout.about_list, null);
       //TextView name = (TextView) view.findViewById(R.id.txtA);
        //name.setText(nameList[i]);
    // name.setTypeface(face);
        ImageView image = view.findViewById(R.id.imgA);
        //ImageView image = (ImageView)view.findViewById(R.id.imgA);
        //Picasso.with(context).load(imgList[i])
        //   .into(image);
        Glide.with(context)
                .load(imgList[i])
                .apply(new RequestOptions().override(500, 500))

                //.override(800,300)
               // .thumbnail(1f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)

                .into(image);
        return view;
    }
}
