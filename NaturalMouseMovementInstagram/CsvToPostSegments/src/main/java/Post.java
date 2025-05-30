import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Post {
    ArrayList<ArrayList<Double>> pointsWithTimeStamps;
    int clickCount;
    Double duration;
    double thresholdAngle = 0.2;
    Integer numOfSegments;
    Integer numOfPoints;

    ArrayList<ArrayList<ArrayList<Double>>> segments;

    Post(ArrayList<ArrayList<Double>> pointsWithTimeStamps, int clickCount) {
        this.pointsWithTimeStamps = pointsWithTimeStamps;
        this.clickCount = clickCount;
    }

    public Double getDuration() {
        return pointsWithTimeStamps.getLast().getLast() - pointsWithTimeStamps.getFirst().getLast();
    }



    // COULD BE ERROR PRONE SO IF SOMETHING GOES WRONG CHECK HERE
    public static ArrayList<ArrayList<ArrayList<Double>>> getSegmentedList(Post post) {
        ArrayList<ArrayList<ArrayList<Double>>> segments = new ArrayList<>();
        ArrayList<ArrayList<Double>> pointList = post.pointsWithTimeStamps;

        int i = 0;
        while (i < pointList.size() - 1) {
            ArrayList<ArrayList<Double>> segment = new ArrayList<>();
            segment.add(pointList.get(i));
            ArrayList<Double> prevPoint = pointList.get(i);
            ArrayList<Double> nextPoint = pointList.get(i + 1);
            Double[] prevVec = VectorMethods.getVectorFromRows(prevPoint, nextPoint);

            i++;
            segment.add(nextPoint);

            while (i < pointList.size() - 1) {
                prevPoint = nextPoint;
                nextPoint = pointList.get(i + 1);
                Double[] currVec = VectorMethods.getVectorFromRows(prevPoint, nextPoint);

                double angle = VectorMethods.getAngle(prevVec, currVec);
                if (angle < post.thresholdAngle) {
                    i++;
                    segment.add(nextPoint);
                    prevVec = currVec;
                } else {
                    break;
                }
            }

            segments.add(segment);
            i++; // Move to the next point to start a new segment
        }

        return segments;
    }

    public Integer getNumOfSegments() {
        numOfSegments = segments.size();
        return numOfSegments;
    }

    public Integer getNumOfPoints() {
        numOfPoints = pointsWithTimeStamps.size();
        return numOfPoints;
    }

    public static void postAnalyticsToCsv(ArrayList<Post> posts){
        String postCsvFile = "postAnalytics";

        try (CSVWriter writer = new CSVWriter(new FileWriter(postCsvFile))) {
            // Write header
            String[] header = {"clickcount", "duration", "numOfSegments", "numOfPoints"};
            writer.writeNext(header);

            // Write data rows
            ArrayList<ArrayList<String>> data = new ArrayList<>();
            for(Post post : posts) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(post.clickCount));
                row.add(String.valueOf(post.duration));
                row.add(String.valueOf(post.numOfSegments));
                row.add(String.valueOf(post.numOfPoints));
                data.add(row);
            }


            for (ArrayList<String> row : data) {
                String[] rowArray = row.toArray(new String[row.size()]);
                writer.writeNext(rowArray);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
