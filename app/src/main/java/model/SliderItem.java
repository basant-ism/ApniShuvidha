package model;

import androidx.annotation.VisibleForTesting;

public class SliderItem
{
    String image_url,des;
            int image_id;

    public SliderItem() {
    }

    public SliderItem(String image_url, int image_id, String des) {
        this.image_url = image_url;
        this.image_id = image_id;
        this.des = des;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
