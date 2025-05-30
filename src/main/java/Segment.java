import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Segment {
    ArrayList<ArrayList<Double>> segmentPoints;
    Double segmentDuration;  // can use method in Post class
    Integer numOfPoints;
    Double displacement;
    double[] directionVector = new double[2];

    // For json writer (not actually used)

    public Segment() {
    }

    Segment(ArrayList<ArrayList<Double>> segmentPoints){
        this.segmentPoints = segmentPoints;
    }


    public Integer getNumOfPoints(){
        numOfPoints = segmentPoints.size();
        return numOfPoints;
    }

    public double[] getDirectionVector(){
        directionVector[0] = segmentPoints.getLast().get(0) - segmentPoints.getFirst().get(0);
        directionVector[1] = segmentPoints.getLast().get(1) - segmentPoints.getFirst().get(1);
        return directionVector;
    }


    public Double getDisplacement(){
        displacement = Math.sqrt(directionVector[0]*directionVector[0] + directionVector[1]*directionVector[1]);
        return displacement;
    }

    public Double getSegmentDuration(){
        segmentDuration = segmentPoints.getLast().getLast() - segmentPoints.getFirst().getLast();
        return segmentDuration;
    }

    public static void writeAnalyticsToCsv(ArrayList<Segment> segments){
        String segmentCsvFile = "segmentAnalytics";

        try (CSVWriter writer = new CSVWriter(new FileWriter(segmentCsvFile))) {
            // Write header
            String[] header = {"numOfPoints", "duration", "displacement", "directionVector_x", "directionVector_y"};
            writer.writeNext(header);

            // Write data rows

            ArrayList<ArrayList<String>> data = new ArrayList<>();
            for(Segment segment : segments) {
                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(segment.numOfPoints));
                row.add(String.valueOf(segment.segmentDuration));
                row.add(String.valueOf(segment.displacement));
                row.add(String.valueOf(segment.directionVector[0]));
                row.add(String.valueOf(segment.directionVector[1]));
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

    public static void segmentsToJson(ArrayList<Segment> segments){

    }
}
