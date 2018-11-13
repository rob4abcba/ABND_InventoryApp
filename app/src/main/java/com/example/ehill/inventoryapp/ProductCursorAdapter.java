package com.example.ehill.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ehill.inventoryapp.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {

    private Context mContext;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        final Button sale_Button = view.findViewById(R.id.sale_Btn);
        // Find the columns of products attributes that we're interested in
        int productNameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the products attributes from the Cursor for the current product
        String productName = cursor.getString(productNameColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        final String quantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(price);
        quantityTextView.setText(quantity);
        final int id = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));

        // Sale Button logic
        sale_Button.setText(R.string.sale);

        sale_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int updatedQuantity = Integer.parseInt(quantityTextView.getText().toString().trim());
                if (updatedQuantity > 0) {

                    Uri newUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, id);
                    ContentValues values = new ContentValues();
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, updatedQuantity - 1);
                    context.getContentResolver().update(newUri, values, null, null);
                } else {

                    sale_Button.setEnabled(false);
                    Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
