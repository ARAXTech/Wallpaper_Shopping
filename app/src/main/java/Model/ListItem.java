
package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListItem {

    String imgLink;
    String description;
    String name;
    String id;
    List<JSONArray> image_series;
    JSONArray image_json;
    public ArrayList<String> img_src;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<JSONArray> getImage_series() {
        return image_series;
    }

    public void setImage_series(List<JSONArray> image_series) {
        this.image_series = image_series;
    }



    public ListItem(String imgLink,String name,String id, String description, JSONArray image_json,ArrayList img_src) {
        this.imgLink = imgLink;
        this.description = description;
        this.image_json = image_json;
        this.name = name;
        this.id = id;
        this.img_src = img_src;
    }

    public List<String> getImg_src() {
        return img_src;
    }

    public void setImg_src(String src) {
        this.img_src.add(src);
    }

    public int get_img_src_size(){
         return img_src.size();
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /* public void setImage_series( List image_series_item){

         for(int i = 0; i < image_series_item.size(); i++)
             this.image_series.set(i,image_series_item.get(i));

     }
     public List getImage_series(){

         return this.image_series;
     }
 */
    public JSONArray getImage_json() {
        return image_json;
    }

    public void setImage_json(JSONArray image_json) {
        this.image_json = image_json;
    }

}