package topology;

import java.io.*;

/**
 * Created by Harsha on 02-11-2016.
 */
public class CreateBolt {

//    private String boltPath = "D:\\Realtime\\Kafka1\\Storm-Kafka\\src\\main\\java\\";


    public CreateBolt(String boltPath, String name, String inputPath){

        String boltName = name + "Bolt";

        String filePath = boltPath + boltName + ".java";

        try {
            FileWriter fw = new FileWriter(filePath);
            BufferedWriter bw = new BufferedWriter(fw);

            BufferedReader br = new BufferedReader(new FileReader(inputPath));
            String sCurrentLine = "";
            StringBuilder treePath = new StringBuilder();
            while ((sCurrentLine = br.readLine()) != null) {
                treePath.append(sCurrentLine);
                treePath.append("\n");
            }
            br.close();

            String classCode =
                    "import org.apache.storm.topology.BasicOutputCollector;\n" +
                    "import org.apache.storm.topology.OutputFieldsDeclarer;\n" +
                    "import org.apache.storm.topology.base.BaseBasicBolt;\n" +
                    "import org.apache.storm.tuple.Fields;\n" +
                    "import org.apache.storm.tuple.Tuple;\n" +
                    "import org.apache.storm.tuple.Values;\n" +
                    "import org.json.JSONObject;\n" +
                    "import org.slf4j.Logger;\n" +
                    "import org.slf4j.LoggerFactory;\n" +
                    "\n" +
                    "import java.io.*;\n" +
                    "import java.net.HttpURLConnection;\n" +
                    "import java.net.MalformedURLException;\n" +
                    "import java.net.ProtocolException;\n" +
                    "import java.net.URL;\n" +
                    "\n" +
                    "public class " + boltName + " extends BaseBasicBolt {\n" +
                    "    private static final Logger LOG = LoggerFactory.getLogger(" + boltName +".class);\n" +
                    "    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {\n" +
                    "        try {\n" +
                    "            String s = tuple.getString(0);\n" +
                    "            String r[] = s.split(\":\");\n" +
                    "            String filename = r[0];\n" +
                    "            String features = r[1];\n" +
                    "\n" +
                    "            double[] feature = fromString(r[1]);\n" +
                    "            Boolean check = check" + name + "(feature);\n" +
                    "            insertIntoMongoDB(check);\n" +
                    "            basicOutputCollector.emit(new Values(filename,check));\n" +
                    "        }\n" +
                    "        catch (Exception e)\n" +
                    "        {\n" +
                    "            e.printStackTrace();\n" +
                    "        }\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {\n" +
                    "        outputFieldsDeclarer.declare(new Fields(\"context\",\"status\"));\n" +
                    "    }\n" +
                    "\n" +
                    "    private static double[] fromString(String string) {\n" +
                    "        String[] strings = string.split(\";\");\n" +
                    "        double result[] = new double[strings.length];\n" +
                    "        for (int i = 0; i < result.length; i++) {\n" +
                    "            result[i] = Double.parseDouble(strings[i]);\n" +
                    "        }\n" +
                    "        return result;\n" +
                    "    }\n" +
                    "\n" +
                    "    public static void insertIntoMongoDB(Boolean check) {\n" +
                    "        String API_KEY = \"-Q-ydWDJ6ULbB2_e5yLZaloAOMkh9lax\";\n" +
                    "        String DATABASE_NAME = \"lab9_10\";\n" +
                    "        String COLLECTION_NAME = \"c_prediction_lab10\";\n" +
                    "        String urlString = \"https://api.mlab.com/api/1/databases/\" +\n" +
                    "                DATABASE_NAME + \"/collections/\" + COLLECTION_NAME + \"?apiKey=\" + API_KEY;\n" +
                    "        LOG.info(urlString);\n" +
                    "\n" +
                    "        StringBuilder result = null;\n" +
                    "        try {\n" +
                    "            URL url = new URL(urlString);\n" +
                    "            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();\n" +
                    "            urlConnection.setDoOutput(true);\n" +
                    "            urlConnection.setRequestMethod(\"POST\");\n" +
                    "            urlConnection.setRequestProperty(\"Content-Type\", \"application/json\");\n" +
                    "            urlConnection.setRequestProperty(\"Accept\", \"application/json\");\n" +
                    "            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), \"UTF-8\"));\n" +
                    "            JSONObject jsonObject = new JSONObject();\n" +
                    "            jsonObject.put(\"Context\", \""+ name+ "\");\n" +
                    "            jsonObject.put(\"Decision\", check);\n" +
                    "            jsonObject.put(\"Timestamp\", System.currentTimeMillis());\n" +
                    "            writer.write(jsonObject.toString());\n" +
                    "            LOG.info(jsonObject.toString());\n" +
                    "            writer.close();\n" +
                    "            BufferedReader in = new BufferedReader(\n" +
                    "                    new InputStreamReader(urlConnection.getInputStream()));\n" +
                    "            String inputLine;\n" +
                    "            StringBuffer response = new StringBuffer();\n" +
                    "\n" +
                    "            while ((inputLine = in.readLine()) != null) {\n" +
                    "                response.append(inputLine);\n" +
                    "            }\n" +
                    "            in.close();\n" +
                    "\n" +
                    "        } catch (MalformedURLException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        } catch (ProtocolException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        } catch (IOException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        }\n" +
                    "\n" +
                    "        System.out.println(\"Uploaded data to Mongo\");\n" +
                    "\n" +
                    "    }\n" +
                    "\n" +
                    "    public Boolean check" + name +"(double[] feature) {\n" +
                    "        \n" +
                            treePath +
                    "    return false;\n" +
                    "    }\n" +
                    "}\n";



            bw.write(classCode);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
