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

public class ActivityPedidos extends AppCompatActivity {

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int ADD_PRODUCT_ID = Menu.FIRST;
    private static final int INSERT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    private static final int EDIT_ID = Menu.FIRST + 3;
    private static final int SEND_ORDER_ID = Menu.FIRST + 4;


    private PedidosDbAdapter mDbHelper;
    private ProductosDbAdapter mDbHelper_product;
    private ListView mList;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        mDbHelper = new PedidosDbAdapter(this);
        mDbHelper.open();
        mList = (ListView) findViewById(R.id.list);
        fillData();

        registerForContextMenu(mList);

    }

    private void fillData() {
        // Get all of the notes from the database and
        // create the item list
        Cursor ordersCursor = mDbHelper.fetchAllOrders() ;
        // Create an array to specify the fields we want to
        // display in the list ( only TITLE )
        String [] from = new
                String [] { PedidosDbAdapter.KEY_CLIENT };
        // and an array of the fields we want to bind
        // those fields to (in this case just text1 )
        int [] to = new int [] { R.id.text1 };
        // Now create an array adapter and set it to
        // display using our row
        SimpleCursorAdapter orders =
                new SimpleCursorAdapter(this , R.layout.
                        notes_row, ordersCursor, from, to);
        mList.setAdapter(orders);
    }

    public void fillProductsData() {
        // Get all of the notes from the database and
        // create the item list
        Cursor productsCursor = mDbHelper_product.fetchAllProducts() ;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_insert_order);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case INSERT_ID:
                createOrder();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, ADD_PRODUCT_ID, Menu.NONE, R.string.menu_insert_product);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete_order);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit_order);
        menu.add(Menu.NONE, SEND_ORDER_ID, Menu.NONE, R.string.send_order);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case ADD_PRODUCT_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                addProduct(info.id);
                return true;

            case DELETE_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteOrder(info.id);
                fillData();
                return true;
            case EDIT_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                editOrder(info.position, info.id);
                return true;
            case SEND_ORDER_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                sendOrder(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void addProduct(long id){
        setContentView(R.layout.activity_notepadv3);

        mDbHelper_product = new ProductosDbAdapter(this);
        mDbHelper_product.open();
        mList = (ListView) findViewById(R.id.list);
        fillProductsData();

        registerForContextMenu(mList);


    }

    private void createOrder() {
        Intent i = new Intent(this, PedidoEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    protected void editOrder(int position, long id) {
        Intent i = new Intent(this, PedidoEdit.class);
        i.putExtra(PedidosDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private void sendOrder(long id) {
        /*Cursor order = mDbHelper.fetchOrder(id);
        SendAbstraction send;
        String client = order.getString (order.getColumnIndexOrThrow(PedidosDbAdapter.KEY_CLIENT));
        int mobile_number = Integer.valueOf(order.getString (order.getColumnIndexOrThrow(PedidosDbAdapter.KEY_NUMBER)));
        if(body.length() >= 100){
            send = new SendAbstractionImpl(this,"MAIL");
        }
        else{
            send = new SendAbstractionImpl(this,"SMS");
        }
        send.send(client, mobile_number);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

}