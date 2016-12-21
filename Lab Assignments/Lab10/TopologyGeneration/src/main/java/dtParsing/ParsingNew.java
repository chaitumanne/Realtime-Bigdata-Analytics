package dtParsing;

import com.google.common.base.Joiner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naga on 02-11-2016.
 */
public class ParsingNew {

    public static void main(String args[]) throws IOException {
        String label = "1.0";
        String inputPath = "data/data.txt"; //Input path of decision tree model from Spark-ML-Lib
        String outputPath = "data/Class1.txt"; //Output path for each class label
        GeneratePathForClass(inputPath, outputPath, label);
    }

    public ParsingNew(String model, String outputPath, String label){
        try {
            GeneratePathForClass(model, outputPath, label);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void GeneratePathForClass(String model, String outputPath, String label) throws IOException {
        Boolean check = false;
        List<String> tree = new ArrayList<String>();
        String[] modelArray = model.toString().split("\n");
        for(int i=0; i<modelArray.length;i++) {

            if (modelArray[i].contains("Predict")) {

                if (modelArray[i].contains("Predict: " + label)) {
                    String labelChange = modelArray[i].replaceAll(label, "true");
                    labelChange = labelChange.replace("Predict: ", "return ");
                    tree.add(labelChange + ";");
                }


                else {
                    String newStr = modelArray[i].substring(0, modelArray[i].indexOf(":"));
                    newStr = newStr.replace("Predict", "return");
                    String t = newStr + " false;";
//                    String t = "return false;";
                    tree.add(t);
                }
            }
            else if (modelArray[i].contains("If") || modelArray[i].contains("Else")) {
                String ifElse = "";
                if (modelArray[i].contains("If")) {
                    ifElse = modelArray[i].replace("If ", "if ");
                }
                if (modelArray[i].contains("Else ")) {
                    ifElse = modelArray[i].replace("Else ", "else if ");
                }

                String[] parseIf = ifElse.split(" ");
                String parsedIf = "";
                for(int j=0;j<=parseIf.length-1;j++) {
                    if(parseIf[j].contains("feature")){
                        parsedIf = parsedIf + parseIf[j] + "[" + parseIf[j+1] + "] ";
                        j = j+1;
                    }
                    else {
                        parsedIf = parsedIf + parseIf[j] + " ";
                    }
//                    parsedIf = parseIf[0] + parseIf [1] + "[" + parseIf[2] + "] " + parseIf[3] + parseIf[4];

                }
                tree.add(parsedIf);
            }
//            else if (modelArray[i].contains("Else")) {
//                String ifElse = modelArray[i].replace("Else", "else if");
//                String[] parseElseIf = ifElse.split(" ");
//                String parsedIf = "";
//                for(int j=0;j<=parseElseIf.length-1;j++) {
//                    if(parseElseIf[j].contains("feature")){
//                        parsedIf = parsedIf + parseElseIf[j] + "[" + parseElseIf[j+1] + "]";
//                        j = j+1;
//                    }
//                    else {
//                        parsedIf = parsedIf + parseElseIf[j];
//                    }
////                    parsedIf = parseIf[0] + parseIf [1] + "[" + parseIf[2] + "] " + parseIf[3] + parseIf[4];
//
//                }
//                tree.add(parsedIf);
//            }
            else {
                tree.add(modelArray[i]);
            }
        }


        Joiner joiner = Joiner.on("\n").useForNull("null");
        String output = joiner.join(tree);

        /*
        Saving output to a File
         */
        FileWriter fw = new FileWriter(new File(outputPath));
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(output);
        bw.close();
    }
}
