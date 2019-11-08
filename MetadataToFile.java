import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sforce.soap.metadata.DescribeMetadataObject;


public class MetadataToFile {
	public static void generateFile(Map<String, List<Map<Double, DescribeMetadataObject>>> versionedMetadataObjects) {
		String fileContent = getFileTemplate("src/metadataTemplate.js").trim() + ' ' + generateJson(versionedMetadataObjects) + ";\n";
		System.out.println(fileContent);
		
		try {
			Files.write(Paths.get("./metadata.js"), fileContent.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String generateJson(Map<String, List<Map<Double, DescribeMetadataObject>>> versionedMetadataObjects) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    Map<String, CustomObjectMetadata> jsonStructure = new HashMap<String, CustomObjectMetadata>();
	    Double[] highestApiVersion = new Double[] {0.0};
	    
		versionedMetadataObjects.forEach((xmlName, versionsList) -> {
			DescribeMetadataObject latestDescribeMetadataObject = null;
		    Double minApiVersion = 0.0;
		    Double maxApiVersion = 0.0;
		    for(Map<Double, DescribeMetadataObject> versionedMetadata : versionsList) {
		    	Map.Entry<Double, DescribeMetadataObject> versionedEntry = versionedMetadata.entrySet().iterator().next();
		    	Double currentApiVersion = versionedEntry.getKey();
		    	minApiVersion = minApiVersion == 0.0 ? currentApiVersion : minApiVersion;
		    	maxApiVersion = currentApiVersion;
		    	latestDescribeMetadataObject = versionedEntry.getValue();
			}
		    
		    CustomObjectMetadata metadataObject = new CustomObjectMetadata(latestDescribeMetadataObject, minApiVersion, maxApiVersion);
		    if(metadataObject.children != null) {
			    System.out.println("objChildren: " + metadataObject.children.toString());
		    }
		    
		    jsonStructure.put(latestDescribeMetadataObject.getDirectoryName(), metadataObject);
		    if(maxApiVersion > highestApiVersion[0]) {
		    	highestApiVersion[0] = maxApiVersion;
		    }
		});
		

		// handle directories that for some reason are not returned in the api
		new ArrayList<String[]>() {{
			add(new String[] {"translations", "Translations"});
			add(new String[] {"portals", "Portal"});
			add(new String[] {"networks", "Network"});
			add(new String[] {"topicsforobjects", "TopicsForObjects"});
		}}.forEach(xmlObject -> {
			String directory = xmlObject[0];
			if(!jsonStructure.containsKey(directory)) {
				jsonStructure.put(directory, new CustomObjectMetadata(xmlObject[1], 3.0, highestApiVersion[0]));
			}
		});

		
	    return gson.toJson(jsonStructure);
	}
	
    private static String getFileTemplate(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
}