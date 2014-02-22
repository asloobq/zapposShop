package com.asloob.zappos;

import java.util.List;

import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<Product>{

	Context mContext;
	int mLayoutId;
	List<Product> mProducts;
	ImageLoader mImageLoader;

	public ProductListAdapter(Context context, int resource,
			List<Product> objects) {
		super(context, resource, objects);
		mContext = context;
		mLayoutId = resource;
		mProducts = objects;

		mImageLoader = new ImageLoader(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(mLayoutId, null);
		TextView productNameTV = (TextView) row.findViewById(R.id.productNameTV);
		TextView brandNameTV = (TextView) row.findViewById(R.id.brandNameTV);
		ImageView productImage = (ImageView) row.findViewById(R.id.productImageView);

		productNameTV.setText(mProducts.get(position).getProductName());
		brandNameTV.setText(mProducts.get(position).getBrandName());

		mImageLoader.DisplayImage(mProducts.get(position).getThumbnailUrl(), productImage);
		Log.v(ProductListAdapter.class.getName(), "position = " + position);
		return row;
	}



	@Override
	public int getCount() {
		return mProducts.size();
	}

}
