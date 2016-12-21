import org.apache.commons.codec.binary.Base64;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.io.*;
import java.util.Map;

/**
 * Created by harsha on 10/19/16.
 */
public class KFDecoderBolt extends BaseBasicBolt {

    private OutputCollector collector;
//    static String msg = "";
//    private static Writer output;

//    @Override
//    public void prepare(Map stormConf, TopologyContext context,
//                        OutputCollector collector) {
//        this.collector = collector;
//
//    }
//
//    @Override
//    public void execute(Tuple input) {
//        String sentence = input.getString(0);
//        String[] words = sentence.split(" ");
//        for(String word: words){
//            word = word.trim();
//            if(!word.isEmpty()){
//                word = word.toLowerCase();
//                collector.emit(new Values(word));
//            }
//        }
//        collector.ack(input);
//    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        try {
            String s = tuple.getString(0);
            String msg = null;
            String fileName = null;
            fileName = s.split(" ")[0];
            msg = s.split(" ")[1];

            byte[] decodedMsg = Base64.decodeBase64(msg);

            try {
                FileOutputStream fos = new FileOutputStream("/home/harsha/Desktop/outputFiles/" + fileName);
                fos.write(decodedMsg);
                fos.close();
            }
            catch (Exception e) {
                System.out.print("Exception during decoding the file");
            }

//            if (s.substring(0, 11) == "*@#Harsha*@#") {
//
//                msg = "";
//                msg = msg + s.substring(13);
//            } else
//                msg = msg + s;
//            if (s.contains("#@*Sri#@*")) {
//                msg = msg + s.substring(0, s.indexOf("#@*Sri#@*")-1);
                PrintWriter out = null;
                try {
//                    out = new PrintWriter(new BufferedWriter(new FileWriter("/home/harsha/Desktop/messages.txt", true)));
                    out = new PrintWriter(new BufferedWriter(new FileWriter("/home/harsha/Desktop/output/" + fileName + ".txt", true)));
                    out.println(msg);
                    out.println("Harsha");
//                    msg = "";
                } catch (IOException e) {
                    System.err.println(e);
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
//            }

//            PrintWriter out = null;
//            try {
//                out = new PrintWriter(new BufferedWriter(new FileWriter("/home/harsha/Desktop/messages.txt", true)));
//                out.println(s);
//                out.println("Harsha");
//            }catch (IOException e) {
//                System.err.println(e);
//            }finally{
//                if(out != null){
//                    out.close();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanup() {
        // TODO Auto-generated method stub

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("fileName"));

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }
}
