package com.asloob.zappos;

import java.util.List;

import com.asloob.zappos.sugar.SavedProduct;
import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<Product>{

	private Context mContext;
	private int mLayoutId;
	private List<Product> mProducts;
	private ImageLoader mImageLoader;

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

		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(mLayoutId, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.mProductNameTV = (TextView) row.findViewById(R.id.productNameTV);
			viewHolder.mBrandNameTV = (TextView) row.findViewById(R.id.brandNameTV);
			viewHolder.mProductImage = (ImageView) row.findViewById(R.id.productImageView);
			viewHolder.mSaveImage = (ImageButton) row.findViewById(R.id.saveImageButton);
			viewHolder.mSaveImage.setOnClickListener(mSaveListener);
			row.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) row.getTag();
		viewHolder.mSaveImage.setTag(Integer.valueOf(position));

		viewHolder.mProductNameTV.setText(mProducts.get(position).getProductName());
		viewHolder.mBrandNameTV.setText(mProducts.get(position).getBrandName());

		String productId = mProducts.get(position).getProductId();
		if(!SavedProduct.isProductSaved(productId)) {
			viewHolder.mSaveImage.setImageResource(android.R.drawable.btn_star_big_off);
		} else {
			viewHolder.mSaveImage.setImageResource(android.R.drawable.btn_star_big_on);
		}

		mImageLoader.DisplayImage(mProducts.get(position).getThumbnailUrl(), viewHolder.mProductImage);
		Log.v(ProductListAdapter.class.getName(), "position = " + position);
		return row;
	}

	private ImageButton.OnClickListener mSaveListener  = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer position = (Integer) v.getTag();
			//Get product id
			String productId = mProducts.get(position).getProductId();
			if(SavedProduct.isProductSaved(productId)) {
				//Remove from saved objects
				SavedProduct.removeProduct(productId);
				((ImageButton)v).setImageResource(android.R.drawable.btn_star_big_off);
			} else {
				//Add to saved objects
				(new SavedProduct(mContext, productId, mProducts.get(position).getProductName())).save();
				((ImageButton)v).setImageResource(android.R.drawable.btn_star_big_on);
			}
		}
	};

	private static class ViewHolder {
		public TextView mProductNameTV;
		public TextView mBrandNameTV;
		public ImageView mProductImage;
		public ImageButton mSaveImage;
	}


	@Override
	public int getCount() {
		return mProducts.size();
	}

}
