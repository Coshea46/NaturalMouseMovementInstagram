import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;

public class JsonWriter {
    public static void segmentToJson(ArrayList<Segment> segments) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("SegmentObjects.json"), segments);
    }
}
