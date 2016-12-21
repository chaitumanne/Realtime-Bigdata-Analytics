import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
/**
 * Created by Naga on 05-10-2016.
 */
public class KafkaProducer {

    public String SendMessage(String topic, String msg){

//        new KafkaProducer(); //Setting properties for kafka producer
        Producer<Integer, String> producer;
        Properties properties = new Properties();
        properties.put("metadata.broker.list", "localhost:9092");
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        properties.put("message.max.bytes", "10000000");
        producer = new Producer<Integer, String>(new ProducerConfig(properties));
        System.out.println("Topic: "+topic);
        System.out.println("Message: "+msg);
        KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic, msg);//Encoding the Video
        producer.send(data);
        System.out.println("Message Sent");
        producer.close();
        return "Message Sent";
    }
}
