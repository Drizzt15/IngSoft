package es.unizar.eina.gestionPedidosProductos;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProductEdit extends AppCompatActivity {

    private EditText mIdText;
    private EditText mNameText;
    private EditText mDescriptionText;
    private EditText mPriceText;
    private EditText mWeightText;
    private Long mRowId;
    private ProductosDbAdapter mDbHelper;

    private void populateFields() {
        if ( mRowId != null ) {
            Cursor product = mDbHelper.fetchProduct(mRowId);
            startManagingCursor(product);
            mIdText.setText(product.getString (
                    product.getColumnIndexOrThrow( ProductosDbAdapter.KEY_ROWID)));
            mNameText.setText(product.getString (
                    product.getColumnIndexOrThrow( ProductosDbAdapter.KEY_NAME )));
            mDescriptionText.setText(product.getString (
                    product.getColumnIndexOrThrow ( ProductosDbAdapter.KEY_DESCRIPTION )));
            mPriceText.setText(product.getString (
                    product.getColumnIndexOrThrow( ProductosDbAdapter.KEY_PRICE )));
            mWeightText.setText(product.getString (
                    product.getColumnIndexOrThrow( ProductosDbAdapter.KEY_WEIGHT )));
        }
    }

    private void saveState() {
        String name = mNameText.getText().toString();
        String description = mDescriptionText.getText().toString();
        float price = Float.parseFloat(mPriceText.getText().toString());
        float weight = Float.parseFloat(mWeightText.getText().toString());
        if ( mRowId == null ) {
            long id = mDbHelper.createProduct(name, description, price, weight);
            if ( id > 0) {
                mRowId = id ;
            }
        } else {
            mDbHelper.updateProduct(mRowId, name, description, price, weight);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable( ProductosDbAdapter.KEY_ROWID, mRowId );
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
        mDbHelper = new ProductosDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.product_edit);
        setTitle(R.string.edit_product);

        mIdText = (EditText) findViewById(R.id.eID);
        mNameText = (EditText) findViewById(R.id.name);
        mDescriptionText = (EditText) findViewById(R.id.description);
        mPriceText = (EditText) findViewById(R.id.price);
        mWeightText = (EditText) findViewById(R.id.weight);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
                (Long)savedInstanceState.getSerializable(ProductosDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = (extras != null) ?
                    extras.getLong(ProductosDbAdapter.KEY_ROWID) : null ;
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
