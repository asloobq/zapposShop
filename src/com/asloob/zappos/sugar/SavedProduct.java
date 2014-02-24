package com.asloob.zappos.sugar;

import java.util.List;

import android.content.Context;

import com.orm.SugarRecord;

public class SavedProduct extends SugarRecord<SavedProduct>{

	String mProductId;
	String mProductName;
	Boolean mRead;

	public SavedProduct(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SavedProduct(Context context, String productId, String productName) {
		super(context);
		this.mProductId = productId;
		this.mProductName = productName;
		this.mRead = false;
	}

	public String getProductId() {
		return mProductId;
	}

	public void setProductId(String mProductId) {
		this.mProductId = mProductId;
	}

	public String getProductName() {
		return mProductName;
	}

	public void setProductName(String mPproductName) {
		this.mProductName = mPproductName;
	}

	public Boolean getRead() {
		return mRead;
	}

	public void setRead(Boolean mRead) {
		this.mRead = mRead;
	}
	
	public static boolean isProductSaved(String productId) {
		List<SavedProduct> productsList = SavedProduct.listAll(SavedProduct.class);
		for(SavedProduct prod : productsList) {
			if(productId.equals(prod.getProductId())) {
				return true;
			}
		}
		return false;
	}
	
	public static void removeProduct(String productId) {
		List<SavedProduct> productsList = SavedProduct.listAll(SavedProduct.class);
		for(SavedProduct prod : productsList) {
			if(productId.equals(prod.getProductId())) {
				prod.delete();
				return;
			}
		}
	}
}
