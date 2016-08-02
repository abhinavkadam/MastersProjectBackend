package com.edu.spark.rest;

/**
 * @author abhinav
 */
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import com.edu.util.HelperUtils;
import com.edu.util.SystemConstants;
import edu.data.model.ResourceNeed;
import spark.LossPrediction;

/**
 * This class is responsible for creating and handling the REST calls for the TF-IDF operation.
 */
public class TfIdfClientAPI extends ServerResource{	
	
	@Post
	public String writeMethod(String jsonStr) {
		String requestedKey = (String) this.getRequestAttributes().get(SystemConstants.KEY);		
		double magnitude = Double.parseDouble(requestedKey);
		LossPrediction.getInstance().getPredictionModel();
		List<ResourceNeed> resources = LossPrediction.getInstance().getNeedForResources();
		JSONObject responseDetailsJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    
	    for(ResourceNeed r : resources) {
	       jsonArray.add(HelperUtils.resourceNeedTOJSON(r));
	    }
	    responseDetailsJson.put("forms", jsonArray);//Here you can see the data in json format
	    return responseDetailsJson.toJSONString();    
	}
		
	@Delete
	public String deleteMethod() {
		return "Delete";
	}	
	
	
	@Get
	public String readMethod() throws Exception{
		
		List<ResourceNeed> resources = LossPrediction.getInstance().getNeedForResources();
		JSONObject responseDetailsJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    
	    for(ResourceNeed r : resources) {
	       jsonArray.add(HelperUtils.resourceNeedTOJSON(r));
	    }
	    responseDetailsJson.put("forms", jsonArray);
	    return responseDetailsJson.toJSONString();   
	}
	
	@Put
	public String updateMethod(String jStr) throws Exception {
		return "Put";
	}	
}

