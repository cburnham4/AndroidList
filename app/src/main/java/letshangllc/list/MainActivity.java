package letshangllc.list;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Item> array_items;
    private ArrayList<Item> completedItems;

    private Set<String> setOfItems;

    private ItemsAdapter itemsAdapter;

    private SharedPreferences prefs;

    AdsHelper adsHelper;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(getString(R.string.preferences), 0);
        completedItems = new ArrayList<>();
        setOfItems = new HashSet<String>(prefs.getStringSet(getString(R.string.item_set), new HashSet<String>()));

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        this.fillArrayList();

        itemsAdapter = new ItemsAdapter(array_items, this, completedItems);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setAdapter(itemsAdapter);

        final EditText et_item = (EditText) findViewById(R.id.et_item);
        TextView tv_addItem = (TextView) findViewById(R.id.tv_addItem);

        tv_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = et_item.getText().toString();
                if(!itemName.isEmpty()){
                    array_items.add(new Item(itemName));
                    itemsAdapter.notifyDataSetChanged();
                    setOfItems.add(itemName);
                    saveTasks();
                    et_item.setText("");
                }

            }
        });

        adsHelper = new AdsHelper(getWindow().getDecorView(), getResources().getString(R.string.admob_banner_id), this);
        adsHelper.runAds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_complete:
                for(Item listItem : completedItems){
                    array_items.remove(listItem);
                    //itemsAdapter.remove(listItem);
                    setOfItems.remove(listItem.getItem());

                }
                completedItems.clear();
                itemsAdapter.notifyDataSetChanged();
                saveTasks();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillArrayList(){
        array_items = new ArrayList<>();
        Iterator<String> it = setOfItems.iterator();
        while(it.hasNext()){
            array_items.add(new Item(it.next()));
        }
    }

    private void saveTasks(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(getString(R.string.item_set), setOfItems);
        editor.commit();
    }
}
