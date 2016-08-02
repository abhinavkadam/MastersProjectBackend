package com.edu.util;
/* 
* @author abhinav kadam
*/
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
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
* Test class that covers the test cases for the HelperUtils class.
*/
public class HelperUtilsTest extends Test { 
	 @Before
    public void setup() {
       
    }
	  /**
     * The test is to make sure the string is converted to json correct. For any invalid string this method
     * throws VALUE_ALREADY_IN_USE validation error.
     */
    @Test(expected = ParseException.class)
    public void testToJSONObj() {
	
        JSONObject jsonObj = null;
		String jsonString = null;
        try {
				jsonObj = HelperUtils.toJSONObj(jsonString);
        } catch (Exception e) {
            assertEquals(ParseException.class, e.getClass());
            throw e;
        }
    }

}
