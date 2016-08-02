package spark;

/**
 * 	@author abhinav kadam
 **/
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.mllib.feature.IDF;
import org.apache.spark.mllib.feature.IDFModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.recommendation.ALS;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.apache.spark.rdd.JdbcRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.storage.StorageLevel;

import com.edu.util.ConfigurationService;
import com.edu.util.HelperUtils;

import edu.data.model.DisasterDamage;
import edu.data.model.LocRating;
import edu.data.model.Location;
import edu.data.model.ResourceNeed;
import edu.data.model.User;
import scala.Tuple2;
import scala.reflect.ClassManifestFactory$;
import scala.runtime.AbstractFunction0;
import scala.runtime.AbstractFunction1;

/**
 * LossPrediction class is used to perform all the important functions related to prediction of disaster effects in all the form. 
 * Apache Spark is used with Machine Learning Libraries MLIB and leverage the developer friendly machine learning functionality.
 * This class is responsible for creating the model based on static form history data, saving the model and using the model to predict
 * the values based on the user input.
 **/

@SuppressWarnings("serial")
public class LossPrediction implements Serializable {
	private Properties properties = new Properties();
	private String dataSetPath = "";
	private static LossPrediction instance = null;
	private DatabaseConnection conn = null;
	private String path = "/home/abhinav/spark-1.1.0/data/mllib/ridge-data";

	// Singleton instance creation for LossPrediction class
	public static LossPrediction getInstance() {
		if (instance == null) {
			instance = new LossPrediction();
		}

		return instance;

	}

	private LossPrediction() {
		init();
	}
	
	// Initial set up for operation
	public void init() {
		conn = new DatabaseConnection("com.mysql.jdbc.Driver",
				ConfigurationService.getInstance().getUrl(),
				ConfigurationService.getInstance().getUsername(),
				ConfigurationService.getInstance().getPassword());

		loadDamagesFromDB();
		getPredictionModel();

	}

	// Build the prediction model based on historical data 
	public void getPredictionModel() {

		JavaSparkContext sc = DataService.getInstance().getSc();
		// $example on$
		// Load and parse the data
		JavaRDD<String> data = sc.textFile(path);
		JavaRDD<LabeledPoint> parsedData = data
				.map(new Function<String, LabeledPoint>() {
					public LabeledPoint call(String line) {
						String[] parts = line.split(",");
						String[] features = parts[1].split(" ");
						double[] v = new double[features.length];
						for (int i = 0; i < features.length - 1; i++) {
							if (features[i] == "")
								v[i] = Double.parseDouble("0.0");

							else
								v[i] = Double.parseDouble(features[i]);

						}
						return new LabeledPoint(Double.parseDouble(parts[0]),
								Vectors.dense(v));
					}
				});
		parsedData.cache();

		// Building the model
		int numIterations = 100;
		double stepSize = 0.00000001;
		final LinearRegressionModel model = LinearRegressionWithSGD.train(
				JavaRDD.toRDD(parsedData), numIterations, stepSize);

		// Evaluate model on training examples and compute training error and
		// save model
		JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData
				.map(new Function<LabeledPoint, Tuple2<Double, Double>>() {
					public Tuple2<Double, Double> call(LabeledPoint point) {
						double prediction = model.predict(point.features());
						return new Tuple2<>(prediction, point.label());
					}
				});
		double MSE = new JavaDoubleRDD(valuesAndPreds.map(
				new Function<Tuple2<Double, Double>, Object>() {
					public Object call(Tuple2<Double, Double> pair) {
						return Math.pow(pair._1() - pair._2(), 2.0);
					}
				}).rdd()).mean();

		LinearRegressionModel sameModel = LinearRegressionModel.load(sc.sc(),
				"target/tmp/javaLinearRegressionWithSGDModel");
		// $example off$
		MatrixFactorizationModel bestModel = DataService.getInstance()
				.getBestModel();
	}

	// Predict the values based on prediction model created in the last step
	public void predict() {
		// $example on$
		// Load and parse the data
		JavaSparkContext sc = DataService.getInstance().getSc();
		String path = "/home/abhinav/cmpe295b/data";
		JavaRDD<String> data = sc.textFile(path);
		JavaRDD<LabeledPoint> parsedData = data
				.map(new Function<String, LabeledPoint>() {
					public LabeledPoint call(String line) {
						String[] parts = line.split(",");
						String[] features = parts[1].split(" ");
						double[] v = new double[features.length];
						System.out.println("The data is loaded now. ");
						for (int i = 0; i < features.length - 1; i++) {
							v[i] = Double.parseDouble(features[i]);
						}
						return new LabeledPoint(Double.parseDouble(parts[0]),
								Vectors.dense(v));
					}
				});
		parsedData.cache();

		// Building the model
		int numIterations = 100;
		double stepSize = 0.00000001;
		final LinearRegressionModel model = LinearRegressionWithSGD.train(
				JavaRDD.toRDD(parsedData), numIterations, stepSize);

		// Evaluate model on training examples and compute training error
		JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData
				.map(new Function<LabeledPoint, Tuple2<Double, Double>>() {
					public Tuple2<Double, Double> call(LabeledPoint point) {
						double prediction = model.predict(point.features());
						return new Tuple2<>(prediction, point.label());
					}
				});
		double MSE = new JavaDoubleRDD(valuesAndPreds.map(
				new Function<Tuple2<Double, Double>, Object>() {
					public Object call(Tuple2<Double, Double> pair) {
						return Math.pow(pair._1() - pair._2(), 2.0);
					}
				}).rdd()).mean();
		System.out.println("training Mean Squared Error = " + MSE);

		// Save and load model
		model.save(sc.sc(), "target/tmp/javaLinearRegressionWithSGDModel");
		LinearRegressionModel sameModel = LinearRegressionModel.load(sc.sc(),
				"target/tmp/javaLinearRegressionWithSGDModel");
		// $example off$
		MatrixFactorizationModel bestModel = DataService.getInstance()
				.getBestModel();
	}

	// Loading the damages related historical data from the database
	public void loadDamagesFromDB() {

		JdbcRDD<Object[]> disasterDamageJdbcRDD = new JdbcRDD<>(
				DataService.getInstance().getSc().sc(),
				conn,
				"select * from DisasterDamage where DisasterDamage.Magnitude > ? and DisasterDamage.Magnitude < ?",
				-1, 499999999, 10, new MapResult(),
				ClassManifestFactory$.MODULE$.fromClass(Object[].class));
		JavaRDD<Object[]> disasterDamageRDD = JavaRDD.fromRDD(
				disasterDamageJdbcRDD,
				ClassManifestFactory$.MODULE$.fromClass(Object[].class));

		Map<Integer, DisasterDamage> damages = disasterDamageRDD.mapToPair(
				new PairFunction<Object[], Integer, DisasterDamage>() {
					public Tuple2<Integer, DisasterDamage> call(
							final Object[] record) throws Exception {
						DisasterDamage dam = new DisasterDamage(Integer
								.parseInt(record[0] + ""), Integer
								.parseInt(record[1] + ""), Integer
								.parseInt(record[2] + ""), 0);// ,Integer.parseInt(record[3]
																// + ""));
						String str = HelperUtils.damageTOJSON(dam);
						return new Tuple2<Integer, DisasterDamage>((int) Double
								.parseDouble(record[0] + ""), dam);
					}
				}).collectAsMap();
		DataService.getInstance().setDamages(damages);
		printDamages(damages);
	}

	// Reading the system properties from the configurable properties file 
	public void readProperties(String filePath) {
		InputStream is;
		try {
			is = new FileInputStream(filePath);
			properties.load(is);
			dataSetPath = (String) properties.get("datasetPath");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Method to compute Root Mean Squared Value for correction 
	public static Double computeRMSE(MatrixFactorizationModel model,
			JavaRDD<Rating> data) {
		JavaRDD<Tuple2<Object, Object>> userProducts = data
				.map(new Function<Rating, Tuple2<Object, Object>>() {
					public Tuple2<Object, Object> call(Rating r) {
						return new Tuple2<Object, Object>(r.user(), r.product());
					}
				});

		JavaPairRDD<Tuple2<Integer, Integer>, Double> predictions = JavaPairRDD
				.fromJavaRDD(model
						.predict(JavaRDD.toRDD(userProducts))
						.toJavaRDD()
						.map(new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
							public Tuple2<Tuple2<Integer, Integer>, Double> call(
									Rating r) {
								return new Tuple2<Tuple2<Integer, Integer>, Double>(
										new Tuple2<Integer, Integer>(r.user(),
												r.product()), r.rating());
							}
						}));
		JavaRDD<Tuple2<Double, Double>> predictionsAndRatings = JavaPairRDD
				.fromJavaRDD(
						data.map(new Function<Rating, Tuple2<Tuple2<Integer, Integer>, Double>>() {
							public Tuple2<Tuple2<Integer, Integer>, Double> call(
									Rating r) {
								return new Tuple2<Tuple2<Integer, Integer>, Double>(
										new Tuple2<Integer, Integer>(r.user(),
												r.product()), r.rating());
							}
						})).join(predictions).values();

		double mse = JavaDoubleRDD.fromRDD(
				predictionsAndRatings.map(
						new Function<Tuple2<Double, Double>, Object>() {
							public Object call(Tuple2<Double, Double> pair) {
								Double err = pair._1() - pair._2();
								return err * err;
							}
						}).rdd()).mean();

		return Math.sqrt(mse);
	}

	public static void main(String args[]) {
		if (args.length < 1) {
			printUsage();
		}

		ConfigurationService.getInstance().readProperties(args[0]);

	}

	// MapResult Class is used to hold the RDD values 
	static class MapResult extends AbstractFunction1<ResultSet, Object[]>
			implements Serializable {
		public Object[] apply(ResultSet row) {
			return JdbcRDD.resultSetToObjectArray(row);
		}
	}

	// Database connection class that encapsulates the Database connection object holding important properties like
	// username, password, connection url, driver class name. 
	static class DatabaseConnection extends AbstractFunction0<Connection>
			implements Serializable {
		private String driverClassName;
		private String connectionUrl;
		private String username;
		private String password;

		public DatabaseConnection(String driverClassName, String connectionUrl,
				String username, String password) {
			this.driverClassName = driverClassName;
			this.connectionUrl = connectionUrl;
			this.username = username;
			this.password = password;
		}

		@Override
		public Connection apply() {
			try {
				Class.forName(driverClassName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			Properties props = new Properties();
			props.setProperty("user", username);
			props.setProperty("password", password);
			Connection conn = null;

			try {
				conn = DriverManager.getConnection(connectionUrl, props);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conn;
		}
	}
}
