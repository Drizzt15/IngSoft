package es.unizar.eina.gestionPedidosProductos;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PedidoEdit extends AppCompatActivity {

    private EditText mClientText;
    private EditText mNumberText;
    private EditText mDateText;
    private Long mRowId;
    private PedidosDbAdapter mDbHelper;

    private void populateFields() {
        if ( mRowId != null ) {
            Cursor order = mDbHelper.fetchOrder(mRowId);
            startManagingCursor(order);
            mClientText.setText(order.getString (
                    order.getColumnIndexOrThrow( PedidosDbAdapter.KEY_CLIENT )));
            mNumberText.setText(order.getString (
                    order.getColumnIndexOrThrow ( PedidosDbAdapter.KEY_NUMBER )));
            mDateText.setText(order.getString (
                    order.getColumnIndexOrThrow ( PedidosDbAdapter.KEY_DATE )));
        }
    }

    private void saveState() {
        String client  = mClientText.getText().toString();
        int number = Integer.valueOf(mNumberText.getText().toString());
        String date = mDateText.getText().toString();
        if ( mRowId == null ) {
            long id = mDbHelper.createOrder(client, number, date);
            if ( id > 0) {
                mRowId = id ;
            }
        } else {
            mDbHelper.updateOrder(mRowId, client, number, date);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable( PedidosDbAdapter.KEY_ROWID, mRowId );
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new PedidosDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.activity_pedido_edit);
        setTitle(R.string.edit_order);

        mClientText = (EditText) findViewById(R.id.client);
        mNumberText = (EditText) findViewById(R.id.number);
        mDateText = (EditText) findViewById(R.id.date);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long)savedInstanceState.getSerializable(PedidosDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ?
                    extras.getLong(PedidosDbAdapter.KEY_ROWID) : null ;
        }
        populateFields();
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}

