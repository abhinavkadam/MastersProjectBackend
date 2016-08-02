package com.edu.spark.rest;

/* 
** 	@author Abhinav Kadam 
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
import edu.data.model.DisasterDamage;
import edu.data.model.ResourceNeed;
import spark.LossPrediction;

/* 
** 	This class exposed the REST services for client. 
*/
public class ClientAPI extends ServerResource {

	@Get
	public String readMethod() throws Exception {

		String requestedKey = (String) this.getRequestAttributes().get(
				SystemConstants.KEY);
		double magnitude = Double.parseDouble(requestedKey);
		List<DisasterDamage> damages = LossPrediction.getInstance()
				.getRecommendationForDamages(magnitude);
		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		for (DisasterDamage d : damages) {
			jsonArray.add(HelperUtils.damageTOJSON(d));
		}
		responseDetailsJson.put("forms", jsonArray);
													
		return responseDetailsJson.toJSONString();

	}

	@Post
	public String writeMethod(String jsonStr) {

		List<ResourceNeed> resources = LossPrediction.getInstance()
				.getNeedForResources();
		JSONObject responseDetailsJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		for (ResourceNeed r : resources) {
			jsonArray.add(HelperUtils.resourceNeedTOJSON(r));
		}
		responseDetailsJson.put("forms", jsonArray);
		return responseDetailsJson.toJSONString();
	}

	@Delete
	public String deleteMethod() {
		return "Delete";
	}
	
	@Put
	public String updateMethod(String jStr) throws Exception {
		return "Put";
	}
}
