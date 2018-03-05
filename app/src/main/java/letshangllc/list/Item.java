package letshangllc.list;

/**
 * Created by cvburnha on 11/3/2015.
 */
public class Item {
    private boolean selected;
    private String item;
    public Item(String item) {
        this.item = item;
        this.selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setIsSelected(boolean isSelected) {
        this.selected = isSelected;
    }



    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }


}
