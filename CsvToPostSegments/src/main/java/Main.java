import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileWriter;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String filePath = "path/to/your/file.csv";
        ArrayList<Post> posts = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                ArrayList<Double> rowVals = new ArrayList<>();
                ArrayList<ArrayList<Double>> objVals = new ArrayList<>();
                while(!line[0].contains("clickcount")) {
                        for(int i = 0; i < line.length; i++) {
                            rowVals.add(Double.parseDouble(line[i]));
                        }
                        objVals.add(rowVals);
                        rowVals.clear();
                }
                int clickcount = Integer.parseInt(line[0]);
                posts.add(new Post(objVals, clickcount));
                reader.readNext();

            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        ArrayList<Segment> segments = new ArrayList<>();

        for(Post post : posts) {
            post.duration = post.getDuration();
            post.segments = Post.getSegmentedList(post);
            post.numOfSegments = post.getNumOfSegments();
            post.numOfPoints = post.getNumOfPoints();

            // construct segments
            for (int i = 0; i < post.segments.size(); i++) {
                segments.add(new Segment(post.segments.get(i)));
            }


        }

        // update segment vars

        for(Segment segment : segments) {
            segment.segmentDuration = segment.getSegmentDuration();
            segment.numOfPoints = segment.getNumOfPoints();
            segment.directionVector = segment.getDirectionVector();
            segment.displacement = segment.getDisplacement();
        }


        // write post analytics data and segment analytics data to their own csv's

        Post.postAnalyticsToCsv(posts);
        Segment.writeAnalyticsToCsv(segments);

        // make a csv of all points recorded

        ArrayList<ArrayList<Double>> allPointsList = new ArrayList<>();
        for(Post post : posts) {
            for(ArrayList<Double> point : post.pointsWithTimeStamps){
                allPointsList.add(point);
            }
        }

        String pointsCsvFile = "segmentAnalytics";

        try (CSVWriter writer = new CSVWriter(new FileWriter(pointsCsvFile))) {
            // Write header
            String[] header = {"x_coordinate", "y_coordinate", "timestamp"};
            writer.writeNext(header);

            // Write data rows
            ArrayList<ArrayList<String>> pointStringRow = new ArrayList<>();
            for(ArrayList<Double> point : allPointsList) {
                ArrayList<String> row = new ArrayList<>();
                row.add(point.get(0).toString());
                row.add(point.get(1).toString());
                row.add(point.get(2).toString());

                pointStringRow.add(row);
            }

            for (ArrayList<String> row : pointStringRow) {
                String[] rowArray = row.toArray(new String[row.size()]);
                writer.writeNext(rowArray);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonWriter.segmentToJson(segments);


    }

}
