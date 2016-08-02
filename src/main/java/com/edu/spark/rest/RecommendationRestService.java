package com.edu.spark.rest;
/* 
** 	@author Abhinav Kadam 
*/
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/* 
** 	This class creates the REST service application and defines routes which exposes the spark functionality 
*   for prediction and TFIDF as web services. 
*/
public class RecommendationRestService  extends Application{

	@Override
    public synchronized Restlet createInboundRoot() {
		Router clientRouter = new Router(getContext());
		clientRouter.attach("/test", ClientAPI.class);
		clientRouter.attach("/test/{key}", ClientAPI.class);
		clientRouter.attach("/tfidf",TfIdfClientAPI.class);
		return clientRouter;
	}
}
