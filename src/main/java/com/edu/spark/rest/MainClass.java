package com.edu.spark.rest;

/* 
** 	@author Abhinav Kadam 
*/

import org.restlet.Component;
import org.restlet.data.Protocol;

import com.edu.util.ConfigurationService;

/* 
** 	This class is the main execution point of the project. It reads properties from the propterties file, creates a restlet instance 
*  	and starts the spark instance.  
*/
public class MainClass {

	public static void main(String args[]) {
		try {
			
			ConfigurationService.getInstance().readProperties(args[0]);
			if (args.length > 1) {
				printUsage();
			}
			
			Component clientComponent = new Component();
			clientComponent.getServers().add(Protocol.HTTP, 8081);
			clientComponent.getDefaultHost().attach("/restlet", new RecommendationRestService());
			clientComponent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printUsage() {
		System.out.println("Usage: <configuration-file>");
	}

}
