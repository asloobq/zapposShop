package com.asloob.zappos;

public class Product {

	private String mStyleId;
	private String mPrice;
	private String mOriginalPrice;
	private String mProductUrl;
	private String mColorId;
	private String mProductName;
	private String mBrandName;
	private String mThumbnailUrl;
	private String mPercentOff;
	private String mProductId;

	public Product(String mStyleId, String mPrice, String mOriginalPrice,
			String mProductUrl, String mColorId, String mProductName,
			String mBrandName, String mThumbnailUrl, String mPercentOff,
			String mProductId) {
		super();
		this.mStyleId = mStyleId;
		this.mPrice = mPrice;
		this.mOriginalPrice = mOriginalPrice;
		this.mProductUrl = mProductUrl;
		this.mColorId = mColorId;
		this.mProductName = mProductName;
		this.mBrandName = mBrandName;
		this.mThumbnailUrl = mThumbnailUrl;
		this.mPercentOff = mPercentOff;
		this.mProductId = mProductId;
	}

	public String getStyleId() {
		return mStyleId;
	}

	public String getPrice() {
		return mPrice;
	}

	public String getOriginalPrice() {
		return mOriginalPrice;
	}

	public String getProductUrl() {
		return mProductUrl;
	}

	public String getColorId() {
		return mColorId;
	}

	public String getProductName() {
		return mProductName;
	}

	public String getBrandName() {
		return mBrandName;
	}

	public String getThumbnailUrl() {
		return mThumbnailUrl;
	}

	public String getPercentOff() {
		return mPercentOff;
	}

	public String getProductId() {
		return mProductId;
	}

}
