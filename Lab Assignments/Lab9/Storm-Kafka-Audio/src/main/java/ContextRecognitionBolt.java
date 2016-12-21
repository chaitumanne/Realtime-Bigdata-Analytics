import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//import jAudioFeatureExtractor.AudioFeatures.FeatureExtractor;
//import jAudioFeatureExtractor.AudioFeatures.ZeroCrossings;

/**
 * Created by Mayanka on 17-Sep-15.
 */
public class ContextRecognitionBolt extends BaseBasicBolt {
    Map<String, Integer> counts = new HashMap<String, Integer>();
    private static final Logger LOG = LoggerFactory.getLogger(ContextRecognitionBolt.class);
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        try {
            String s = tuple.getString(0);
            String r[] = s.split("_");
            String filename = r[0];
            String features = r[1];
           LOG.info("The features are :"+features);


            double[] feature = fromString(r[1]);
            LOG.info("The sample feature of dogbark recognition is"+feature[2]);
            //String featureDoubles[] = features.split(";");
           // double feature3 = Double.parseDouble(featureDoubles[2]);
            boolean dogbark=false;
            dogbark = checkSiren(feature);

            LOG.info("The decision of dogbark recognition is"+dogbark);
//            double[][] windows = new double[r.length - 3][];
//            if (r.length > 3) {
//
//                for (int i = 3; i < r.length; i++) {
//
//                    String a[] = r[i].split("--");
//                    windows[Integer.parseInt(a[0])] = fromString(a[1]);
//
//                }
//            }
//             insertIntoMongoDB(word, count);
//            FeatureExtractor featureExt = new ZeroCrossings();
//            double[] result = featureExt.extractFeature(sample, sampleRate, otherFeatures);
            Boolean check = checkSiren(feature);
            insertIntoMongoDB(check);
            basicOutputCollector.emit(new Values(filename,dogbark));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        //outputFieldsDeclarer.declare(new Fields("filename","ZeroCrossings"));
        outputFieldsDeclarer.declare(new Fields("context","status"));
    }

    private static double[] fromString(String string) {
        String[] strings = string.split(";");
        double result[] = new double[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Double.parseDouble(strings[i]);
        }
        return result;
    }

    public static void insertIntoMongoDB(Boolean check) {
        String API_KEY = "j1ttIfJ4D9Aol5Ru3bFCtLTAilPI2-v1";
        String DATABASE_NAME = "cs5543";
        String COLLECTION_NAME = "output";
        String urlString = "https://api.mlab.com/api/1/databases/" +
                DATABASE_NAME + "/collections/" + COLLECTION_NAME + "?apiKey=" + API_KEY;
        LOG.info(urlString);

        StringBuilder result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Context", "DogBark");
            jsonObject.put("Decision", check);
            jsonObject.put("Timestamp", System.currentTimeMillis());
            writer.write(jsonObject.toString());
            LOG.info(jsonObject.toString());
            writer.close();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Uploaded data to Mongo");

    }

    public Boolean checkSiren(double[] feature) {
        if (feature[1] <= 6.400095696631952)
            if (feature[4] <= 4.520004716536362E-4)
                if (feature[2] <= 0.0040673828125)
                    if (feature[2] <= 0.00292919921875)
                        if (feature[0] <= 16.394)
                            return true;
                        else if (feature[0] > 16.394)
                            return false;
                        else if (feature[2] > 0.00292919921875)
                            if (feature[0] <= 15.309)
                                return false;
                            else if (feature[0] > 15.309)
                                return false;
                            else if (feature[2] > 0.0040673828125)
                                if (feature[0] <= 17.908)
                                    return false;
                                else if (feature[0] > 17.908)
                                    if (feature[1] <= 6.400003103203128)
                                        return false;
                                    else if (feature[1] > 6.400003103203128)
                                        return false;
                                    else if (feature[4] > 4.520004716536362E-4)
                                        if (feature[5] <= 0.006624262798588733)
                                            return false;
                                        else if (feature[5] > 0.006624262798588733)
                                            if (feature[6] <= 604.2623261129391)
                                                if (feature[0] <= 8.818)
                                                    return false;
                                                else if (feature[0] > 8.818)
                                                    return false;
                                                else if (feature[6] > 604.2623261129391)
                                                    if (feature[0] <= 13.873)
                                                        return true;
                                                    else if (feature[0] > 13.873)
                                                        return false;
                                                    else if (feature[1] > 6.400095696631952)
                                                        if (feature[1] <= 384.97036716863744)
                                                            if (feature[0] <= 63.94)
                                                                if (feature[5] <= 2242.9889983361977)
                                                                    if (feature[2] <= 0.15333333333333327)
                                                                        return false;
                                                                    else if (feature[2] > 0.15333333333333327)
                                                                        return false;
                                                                    else if (feature[5] > 2242.9889983361977)
                                                                        if (feature[3] <= 42.35594583233015)
                                                                            return false;
                                                                        else if (feature[3] > 42.35594583233015)
                                                                            return false;
                                                                        else if (feature[0] > 63.94)
                                                                            if (feature[3] <= 40.436623887682444)
                                                                                if (feature[2] <= 0.2)
                                                                                    return false;
                                                                                else if (feature[2] > 0.2)
                                                                                    return false;
                                                                                else if (feature[3] > 40.436623887682444)
                                                                                    return false;
                                                                                else if (feature[1] > 384.97036716863744)
                                                                                    if (feature[3] <= 30.242772889054116)
                                                                                        return false;
                                                                                    else if (feature[3] > 30.242772889054116)
                                                                                        return false;
    return false;
    }
}
