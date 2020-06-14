import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StaggeredRecyclerAdapter extends RecyclerView.Adapter<StaggeredRecyclerAdapter.ImageViewHolder> {

    Context context;
    int logos[];
    String txt[];
    String logo_txt[];
    Boolean b;
    LayoutInflater inflater;
    //constractor for drawable images
    public StaggeredRecyclerAdapter(Context applicationContext, int[] logos,String[] txt,Boolean b) {
        this.context = applicationContext;
        this.logos = logos;
        this.txt=txt;
        this.txt=txt;
        this.b=b;
        inflater = (LayoutInflater.from(applicationContext));
    }
    //constractor for url images
    public StaggeredRecyclerAdapter(Context applicationContext, String[] logo_txt,String[] txt,Boolean b) {
        this.context = applicationContext;
        this.logo_txt = logo_txt;
        this.txt=txt;
        this.b=b;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        if(b==false)
        {return logos.length;}
        if(b==true)
        {return logo_txt.length;}
        return 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



}
