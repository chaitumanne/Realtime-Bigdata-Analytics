package topology;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Naga on 26-10-2016.
 */
public class CreateStromMainClass {

    String topology;
    String path;


    public CreateStromMainClass(String topology, String path){
        this.topology = topology;
        this.path = path;
        CreateSpoutClassFile();
    }

    public void CreateSpoutClassFile(){

        String code = "import org.apache.log4j.BasicConfigurator;\n" +
                "import org.apache.storm.Config;\n" +
                "import org.apache.storm.LocalCluster;\n" +
                "import org.apache.storm.StormSubmitter;\n" +
                "import org.apache.storm.generated.StormTopology;\n" +
                "import org.apache.storm.kafka.KafkaSpout;\n" +
                "import org.apache.storm.kafka.SpoutConfig;\n" +
                "import org.apache.storm.kafka.StringScheme;\n" +
                "import org.apache.storm.kafka.ZkHosts;\n" +
                "import org.apache.storm.spout.SchemeAsMultiScheme;\n" +
                "import org.apache.storm.topology.TopologyBuilder;\n" +
                "\n" +
                "\n" +
                "public class StormKafkaMain {\n" +
                "    private static final String KAFKA_TOPIC =\"lab10\";\n" +
                "    public static void main(String[] args) {\n" +
                "        BasicConfigurator.configure();\n" +
                "\n" +
                "        if (args != null && args.length > 0)\n" +
                "        {\n" +
                "            try {\n" +
                "                StormSubmitter.submitTopology(\n" +
                "                        args[0],\n" +
                "                        createConfig(false),\n" +
                "                        createTopology());\n" +
                "            } catch (Exception e) {\n" +
                "                // TODO Auto-generated catch block\n" +
                "                e.printStackTrace();\n" +
                "            }\n" +
                "        }\n" +
                "        else\n" +
                "        {\n" +
                "            LocalCluster cluster = new LocalCluster();\n" +
                "            cluster.submitTopology(\n" +
                "                    \"Lab10Storm\",\n" +
                "                    createConfig(true),\n" +
                "                    createTopology());\n" +
                "            try {\n" +
                "                Thread.sleep(10000);\n" +
                "            } catch (InterruptedException e) {\n" +
                "                // TODO Auto-generated catch block\n" +
                "                e.printStackTrace();\n" +
                "            }\n" +
                "            cluster.shutdown();\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private static StormTopology createTopology()\n" +
                "    {\n" +
                "        SpoutConfig kafkaConf = new SpoutConfig(\n" +
                "                new ZkHosts(\"localhost:2181\"),\n" +
                "                KAFKA_TOPIC,\n" +
                "                \"/kafka\",\n" +
                "                \"KafkaSpout\");\n" +
                "        kafkaConf.scheme = new SchemeAsMultiScheme(new StringScheme());\n" +
                "        TopologyBuilder topology = new TopologyBuilder();\n"

                + topology +
                "  \n" +
                "        return topology.createTopology();\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    private static Config createConfig(boolean local)\n" +
                "    {\n" +
                "        int workers = 1;\n" +
                "        Config conf = new Config();\n" +
                "        conf.setDebug(true);\n" +
                "        if (local)\n" +
                "            conf.setMaxTaskParallelism(workers);\n" +
                "        else\n" +
                "            conf.setNumWorkers(workers);\n" +
                "        return conf;\n" +
                "    }\n" +
                "}\n";

        BufferedWriter bw;
        try {
            FileWriter fw = new FileWriter(path + "StormKafkaMain.java");
            bw = new BufferedWriter(fw);
            bw.write(code);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
