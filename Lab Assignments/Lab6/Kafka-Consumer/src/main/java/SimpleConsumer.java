import com.migcomponents.migbase64.Base64;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SimpleConsumer {

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

    public void DecodeVideo(String encodedString){
        byte[] decodedBytes = Base64.decodeFast(encodedString.getBytes());

        try {
            FileOutputStream out = new FileOutputStream("decode.mkv");
            out.write(decodedBytes);
            out.close();
            System.out.println("Created File");
        } catch (Exception e) {
            // TODO: handle exception
//            Log.e("Error", e.toString());

        }
    }

    public void testConsumer() {
        Map<String, Integer> topicCount = new HashMap<String, Integer>();
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);
        StringBuilder sb = new StringBuilder();
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
//            int i=0;
            while (it.hasNext()) {
                System.out.println("Inside Consumer Iterator");
                byte[] message = it.next().message();
                try {
                    String value = new String(message, "UTF-8");
                    sb.append(value);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                i++;
//                if(i == 21){
//                    System.out.println("Creating Video");
//                    DecodeVideo(sb.toString());
//                }
            }
            System.out.println("Creating Video");
                    DecodeVideo(sb.toString());
        }
        if (consumer != null) {
            consumer.shutdown();
        }
    }

    public static void main(String[] args) {
        String topic = args[0]; //Topic Name
        SimpleConsumer simpleConsumer = new SimpleConsumer("127.0.0.1:2181", "testgroup", topic);
        simpleConsumer.testConsumer();
    }

}