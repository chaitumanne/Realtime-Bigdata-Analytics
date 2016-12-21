import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.StormSubmitter;

/**
 * Created by harsha on 10/19/16.
 */
public class RTBDATopologyStorm {
    private static final String KAFKA_TOPIC ="lab8";

        public static void main(String[] args) throws Exception{

            Config config = new Config();
            config.put("inputFile", args[0]);
            config.setDebug(true);
            config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

            SpoutConfig kafkaConf = new SpoutConfig(
                    new ZkHosts("localhost:2181"),
                    KAFKA_TOPIC,
                    "/kafka",
                    "KafkaSpout");
            kafkaConf.scheme = new SchemeAsMultiScheme(new StringScheme());
//            TopologyBuilder topology = new TopologyBuilder();
            TopologyBuilder builder = new TopologyBuilder();

            builder.setSpout("Keyframe_Collect_Spout", new KafkaSpout(kafkaConf), 4);

//            builder.setSpout("line-reader-spout", new MsgCollectSpout());
            builder.setBolt("KF_Decoder_Bolt", new KFDecoderBolt()).shuffleGrouping("Keyframe_Collect_Spout");
            builder.setBolt("KF_Mongo_Bolt", new MongoDBBolt()).shuffleGrouping("Keyframe_Collect_Spout");

            if (args[1] != null && args[1].length() > 0) {
                config.setNumWorkers(3);
                StormSubmitter.submitTopology(args[1], config, builder.createTopology());
            }
            else {
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("Lab8Storm", config, builder.createTopology());
                Thread.sleep(10000);
                cluster.shutdown();
            }
//            LocalCluster cluster = new LocalCluster();
//            cluster.submitTopology("HelloStorm", config, builder.createTopology());
//            Thread.sleep(10000);

//            cluster.shutdown();
        }

}
