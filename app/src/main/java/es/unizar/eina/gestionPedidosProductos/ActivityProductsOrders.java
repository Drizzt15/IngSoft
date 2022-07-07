package es.unizar.eina.gestionPedidosProductos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ActivityProductsOrders extends AppCompatActivity {

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;


    private static final int ADD_ID = Menu.FIRST;

    private  ProductosDbAdapter mDbHelper;
    private ListView mList;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepadv3);

        mDbHelper = new ProductosDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        fillData();

        registerForContextMenu(mList);

    }

    public void fillData() {
        // Get all of the notes from the database and
        // create the item list
        Cursor productsCursor = mDbHelper.fetchAllProducts() ;
        // Create an array to specify the fields we want to
        // display in the list ( only TITLE )
        String [] from = new
                String [] { ProductosDbAdapter.KEY_NAME };
        // and an array of the fields we want to bind
        // those fields to (in this case just text1 )
        int [] to = new int [] { R.id.text1 };
        // Now create an array adapter and set it to
        // display using our row
        SimpleCursorAdapter products =
                new SimpleCursorAdapter(this , R.layout.
                        notes_row, productsCursor, from, to);
        mList.setAdapter(products);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, ADD_ID, Menu.NONE, R.string.menu_add_units);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ADD_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                addUnits(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createProduct() {
        Intent i = new Intent(this, ProductEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    protected void editProduct(int position, long id) {
        Intent i = new Intent(this, ProductEdit.class);
        i.putExtra(ProductosDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

}
