import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Naga on 05-10-2016.
 */
public class SimpleConsumer {

    String API_KEY = "-Q-ydWDJ6ULbB2_e5yLZaloAOMkh9lax";
    String DATABASE_NAME = "lab7";
    String COLLECTION_NAME = "c_lab7";
    String urlString = "https://api.mlab.com/api/1/databases/" +
            DATABASE_NAME + "/collections/" + COLLECTION_NAME + "?apiKey=" + API_KEY;

    private final ConsumerConnector consumer;
    private final String topic;

    public SimpleConsumer(String zookeeper, String groupId, String topic) {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "500");
        props.put("zookeeper.sync.time.ms", "250");
        props.put("auto.commit.interval.ms", "1000");

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = topic;
    }

    public void testConsumer() throws UnsupportedEncodingException {
        Map<String, Integer> topicCount = new HashMap<String, Integer>();
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                String value = new String(it.next().message(), "UTF-8");

                /*
                Mongo Driver Call
                 */
                /*
                MongoClientURI uri = new MongoClientURI("mongodb://user:password@ds053164.mlab.com:53164/kafkaconsumer");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection collection = db.getCollection("consumerdata");
                BasicDBObject document = new BasicDBObject();
                document.put("data", value);
                collection.insert(document);
                System.out.println(value);
                */

                /*
                Mongo API Call
                 */
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
                    jsonObject.put("data", value);
                    writer.write(jsonObject.toString());
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
                System.out.println(value);
                System.out.println("Uploaded data to Mongo");
            }
        }
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String topic = args[0]; //Topic Name
        SimpleConsumer simpleConsumer = new SimpleConsumer("localhost:2181", "testgroup", topic);
        simpleConsumer.testConsumer();
    }
}
