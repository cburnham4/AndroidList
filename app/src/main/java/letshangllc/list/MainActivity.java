package letshangllc.list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private AdsHelper adsHelper;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.root_layout);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        prefs = getSharedPreferences(getString(R.string.preferences), MODE_PRIVATE);
        completedItems = new ArrayList<>();
        setOfItems = new HashSet<>(prefs.getStringSet(getString(R.string.item_set), new HashSet<>()));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fillArrayList();

        itemsAdapter = new ItemsAdapter(array_items, this, completedItems);

        final RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemsAdapter);

        final EditText et_item = findViewById(R.id.et_item);
        TextView tv_addItem = findViewById(R.id.tv_addItem);

        tv_addItem.setOnClickListener(v -> {
            String itemName = et_item.getText().toString();
            if (!itemName.isEmpty()) {
                array_items.add(new Item(itemName));
                itemsAdapter.notifyDataSetChanged();
                setOfItems.add(itemName);
                saveTasks();
                et_item.setText("");
            }
        });

        adsHelper = new AdsHelper(
                getWindow().getDecorView(),
                getString(R.string.admob_banner_id),
                this
        );
        adsHelper.setUpAds();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_complete) {
            for (Item listItem : completedItems) {
                array_items.remove(listItem);
                setOfItems.remove(listItem.getItem());
            }
            completedItems.clear();
            itemsAdapter.notifyDataSetChanged();
            saveTasks();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillArrayList() {
        array_items = new ArrayList<>();
        Iterator<String> it = setOfItems.iterator();
        while (it.hasNext()) {
            array_items.add(new Item(it.next()));
        }
    }

    private void saveTasks() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(getString(R.string.item_set), setOfItems);
        editor.apply();
    }

    @Override
    protected void onPause() {
        if (adsHelper != null) {
            adsHelper.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adsHelper != null) {
            adsHelper.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (adsHelper != null) {
            adsHelper.onDestroy();
        }
        super.onDestroy();
    }
}
