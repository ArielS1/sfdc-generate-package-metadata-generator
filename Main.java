import java.util.List;
import java.util.Map;
import com.sforce.soap.metadata.*;
import com.sforce.soap.metadata.MetadataConnection;


public class Main {
    public static void main(String[] args) {
    	try {
    		MetadataConnection metadataConnection = MetadataLoginUtil.login();
    		Map<String, List<Map<Double, DescribeMetadataObject>>> versionedMetadataObjects = MetadataCollector.getMetadata(3.0, metadataConnection);
    		MetadataToFile.generateFile(versionedMetadataObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}