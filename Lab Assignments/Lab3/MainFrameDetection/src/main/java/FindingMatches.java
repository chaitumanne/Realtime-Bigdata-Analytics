import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

import java.io.File;
import java.io.IOException;

/**
 * Created by Naga on 07-09-2016.
 */
public class FindingMatches {
    public static void main(String args[]) throws IOException {
        MBFImage image1 = ImageUtilities.readMBF(new File("data/1.jpg"));
        MBFImage image2 = ImageUtilities.readMBF(new File("data/3.jpg"));
        DoGSIFTEngine engine = new DoGSIFTEngine();
        LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(image1.flatten());
        LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(image2.flatten());
        LocalFeatureMatcher<Keypoint> matcher1 = new BasicMatcher<Keypoint>(80);
        matcher1.setModelFeatures(queryKeypoints);
        matcher1.findMatches(targetKeypoints);
        MBFImage basicMatches = MatchingUtilities.drawMatches(image1, image2, matcher1.getMatches(), RGBColour.RED);
        DisplayUtilities.display(basicMatches);

        RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5.0, 1500,
                new RANSAC.PercentageInliersStoppingCondition(0.5));
        LocalFeatureMatcher<Keypoint> matcher2 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);
        matcher2.setModelFeatures(queryKeypoints);
        matcher2.findMatches(targetKeypoints);
        MBFImage consistentMatches = MatchingUtilities.drawMatches(image1, image2, matcher2.getMatches(),
                RGBColour.RED);
        DisplayUtilities.display(consistentMatches);

    }
}
