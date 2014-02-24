package com.asloob.zappos;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Parser {

static Product parseProduct(JSONObject jsonObj) {
	String styleId 			= "";
	String price 			= "";
	String originalPrice 	= "";
	String productUrl 		= "";
	String colorId 			= "";
	String productName 		= "";
	String brandName 		= "";
	String thumbnailUrl 	= "";
	String percentOff 		= "";
	String productId 		= "";

	try {
		styleId 		= jsonObj.getString("styleId");
		price 			= jsonObj.getString("price");
		originalPrice 	= jsonObj.getString("originalPrice");
		productUrl	 	= jsonObj.getString("productUrl");
		colorId 		= jsonObj.getString("colorId");
		productName 	= jsonObj.getString("productName");
		brandName 		= jsonObj.getString("brandName");
		thumbnailUrl 	= jsonObj.getString("thumbnailImageUrl");
		percentOff	 	= jsonObj.getString("percentOff");
		productId 		= jsonObj.getString("productId");

	} catch (JSONException e) {
		e.printStackTrace();
	}

	Product product = new Product(styleId, price, originalPrice, productUrl, colorId,
			productName, brandName, thumbnailUrl, percentOff, productId);
	return product;
}

static ArrayList<Product> parseProducts(String coded) {
	ArrayList<Product> products = new ArrayList<Product>(0);

	try {
		JSONObject codedObj = new JSONObject(coded);
		JSONArray results = codedObj.getJSONArray("results");

		for(int index = 0; index < results.length(); index++) {
			JSONObject jsonObj = results.getJSONObject(index);

			Product product = parseProduct(jsonObj);
			products.add(product);

		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return products;
}

}
