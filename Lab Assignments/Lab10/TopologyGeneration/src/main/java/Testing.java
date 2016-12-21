import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naga on 03-11-2016.
 */
public class Testing {
    public static void main(String args[]) throws IOException {
        List<Double> label = new ArrayList<Double>();
        List<double[]> feature = new ArrayList<double[]>();
        BufferedReader br = new BufferedReader(new FileReader("data/features.txt"));
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null) {
            String s[] = sCurrentLine.split(",");
            label.add(Double.parseDouble(s[0]));
            String s1[] = s[1].split(" ");
            double[] nums = new double[s1.length];
            for (int i = 0; i < nums.length; i++) {
                nums[i] = Double.parseDouble(s1[i]);
            }
            feature.add(nums);
        }

        double count = 0.0;

        for(int i=0; i<label.size(); i++){
            if(temp.check1(feature.get(i)) == label.get(i)){
                count++;
            }
        }


        System.out.println(count);
        System.out.println(label.size());
        double accuracy = count/(label.size());
        System.out.println(accuracy);
    }
}
