package letshangllc.list;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by cvburnha on 11/3/2015.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    public ArrayList<Item> items;
    private final ArrayList<Item> completedItems;

    public ItemsAdapter(ArrayList<Item> items, android.content.Context context, ArrayList<Item> completedItems) {
        this.items = items;
        this.completedItems = completedItems;
    }

    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final Item item = items.get(position);

        viewHolder.bx_complete.setOnClickListener(v -> {
            CheckBox cb = (CheckBox) v;
            Item taggedItem = (Item) cb.getTag(R.string.TAG_ITEM);
            TextView tv = (TextView) cb.getTag(R.string.TAG_TEXTVIEW);
            taggedItem.setIsSelected(cb.isChecked());
            if (taggedItem.isSelected()) {
                completedItems.add(taggedItem);
                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                completedItems.remove(taggedItem);
                tv.setPaintFlags(0);
            }
        });

        viewHolder.tv_item.setText(item.getItem());
        if (item.isSelected()) {
            viewHolder.tv_item.setPaintFlags(
                    viewHolder.tv_item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.tv_item.setPaintFlags(0);
        }

        viewHolder.bx_complete.setChecked(item.isSelected());
        viewHolder.bx_complete.setTag(R.string.TAG_ITEM, item);
        viewHolder.bx_complete.setTag(R.string.TAG_TEXTVIEW, viewHolder.tv_item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_item;
        public CheckBox bx_complete;

        public ViewHolder(View view) {
            super(view);
            tv_item = view.findViewById(R.id.tv_task);
            bx_complete = view.findViewById(R.id.bx_complete_task);
        }
    }
}
