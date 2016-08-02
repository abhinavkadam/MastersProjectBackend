package com.edu.util;

/* 
** 	@author Abhinav Kadam 
*/

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.data.model.DisasterDamage;
import edu.data.model.Location;
import edu.data.model.Product;
import edu.data.model.ResourceNeed;
import edu.data.model.User;

/* 
** This class acts as a utility class that provides helper functions like JSON to object conversion. 
*/

public class HelperUtils {

	public static int httpPOST(String nodeURL, String jStr) {
		int postStatus = 404;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(nodeURL);
			StringEntity postParams = new StringEntity(jStr);
			httpPost.setEntity(postParams);
			HttpResponse httpResponse = client.execute(httpPost);

			postStatus = httpResponse.getStatusLine().getStatusCode();
		} catch (Exception e) {
			System.out.println("Error in HTTP POST Request");
		}
		return postStatus;
	}

	public static String httpDELETE(String nodeURL) {
		String json = null;
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpDelete hDelete = new HttpDelete(nodeURL);
			HttpResponse hResponse = httpClient.execute(hDelete);
			if (hResponse.getEntity() != null) {
				HttpEntity hEntity = hResponse.getEntity();
				InputStream inputStream = hEntity.getContent();

				StringWriter strWriter = new StringWriter();
				IOUtils.copy(inputStream, strWriter, "UTF-8");
				json = strWriter.toString();
			}
		} catch (Exception e) {
			System.out.println("Error in HTTP DELETE Request");
		}
		return json;
	}

	public static String httpGET(String nodeURL) {
		String getResponse = null;
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet hGET = new HttpGet(nodeURL);
			HttpResponse hResponse = client.execute(hGET);
			if (hResponse.getEntity() != null) {
				HttpEntity hEntity = hResponse.getEntity();
				InputStream inputStream = hEntity.getContent();

				StringWriter strWriter = new StringWriter();
				IOUtils.copy(inputStream, strWriter, "UTF-8");
				return strWriter.toString();
			}
		} catch (Exception e) {
			System.out.println("Error in HTTP GET Request");
		}
		return getResponse;
	}

	public static JSONObject toJSONObj(String jStr) {
		JSONObject jsonObj = null;
		try {
			JSONParser parser = new JSONParser();
			jsonObj = (JSONObject) parser.parse(jStr);
		} catch (ParseException e) {
			System.out.println("Error in parsing JSON String to JSON Object");
		}
		return jsonObj;
	}

	public static boolean pingNode(String hostIP, int hostPort, int pingTimeout) {
		try (Socket pingSocket = new Socket()) {
			pingSocket.connect(new InetSocketAddress(hostIP, hostPort),
					pingTimeout);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static String locationTOJSON(Location location){		
		Gson gson = new Gson();
		return gson.toJson(location); 
	}

	public static int httpPUT(String nodeURL, String jStr) {
		int defaultStatus = 404;
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPut hPut = new HttpPut(nodeURL);
			StringEntity hParams = new StringEntity(jStr);
			hPut.setEntity(hParams);
			HttpResponse pResponse = httpClient.execute(hPut);

			return pResponse.getStatusLine().getStatusCode();
		} catch (Exception e) {
			System.out.println("Error in HTTP PUT Request");
		}
		return defaultStatus;
	}

	public static String mapToJSONStr(Map<String, String> keyValues) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(keyValues);
	}

	public static String damageTOJSON(DisasterDamage damage) {
		Gson gson = new Gson();
		return gson.toJson(damage);
	}

	public static String resourceNeedTOJSON(ResourceNeed r) {
		Gson gson = new Gson();
		return gson.toJson(r);
	}

	public static int generateRandomNumber() {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);

		return randomInt;
	}

}
