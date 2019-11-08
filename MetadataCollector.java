import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sforce.soap.metadata.DescribeMetadataObject;
import com.sforce.soap.metadata.DescribeMetadataResult;
import com.sforce.soap.metadata.MetadataConnection;


public class MetadataCollector {
	private static Map<String, List<Map<Double, DescribeMetadataObject>>> versionedMetadataObjects = new HashMap<String, List<Map<Double, DescribeMetadataObject>>>();
	
	public static Map<String, List<Map<Double, DescribeMetadataObject>>> getMetadata(Double apiVersion, MetadataConnection metadataConnection) {
		while(describeMetadata(metadataConnection, apiVersion++)); // run until apiVersion doesn't return result
		System.out.println("finished on apiVersion " + (apiVersion - 1));
		
		return versionedMetadataObjects;
	}
	
	// most of the code from https://developer.salesforce.com/docs/atlas.en-us.api_meta.meta/api_meta/meta_describe.htm
    private static boolean describeMetadata(MetadataConnection metadataConnection, double apiVersion) {
    	try {
    		System.out.println("describeMetadata(\"" + apiVersion + "\")");
    	    // Assuming that the SOAP binding has already been established.
    		DescribeMetadataResult res;
    		try {
    			res = metadataConnection.describeMetadata(apiVersion);
    		} catch(Exception e) {
    			return false;
    		}
    	    
    	    StringBuffer sb = new StringBuffer();
    	    DescribeMetadataObject[] describeMetadataObjects = res.getMetadataObjects();
    	    if (res != null && describeMetadataObjects.length > 0) {
    	    	for (DescribeMetadataObject obj : describeMetadataObjects) {
    	    		String xmlName = obj.getXmlName();
    	    		sb.append("***************************************************\n");
    	    		sb.append("XMLName: " + xmlName + "\n");
    	    		sb.append("DirName: " + obj.getDirectoryName() + "\n");
    	    		sb.append("Suffix: " + obj.getSuffix() + "\n");
    	    		
    	    		String[] childNames = obj.getChildXmlNames();
    	    		if(childNames != null && childNames.length > 0) {
    	    			sb.append("ChildNames:\n");
    	    			for(String childName : childNames) {
    	    				sb.append("Child: " + childName + "\n");
    	    			}
    	    		}
    	    		
    	    		List<Map<Double, DescribeMetadataObject>> metadataObjectVersions;
    	    		Map<Double, DescribeMetadataObject> versionedMetadataObject = new HashMap<Double, DescribeMetadataObject>(){{ put(apiVersion, obj); }};
    	    		if(versionedMetadataObjects.containsKey(xmlName)) {
    	    			metadataObjectVersions = versionedMetadataObjects.get(xmlName);
    	    			// compare current object to the previous api object
    	    			compareDescribeMetadataObjects(obj, metadataObjectVersions.get(metadataObjectVersions.size() - 1).get(apiVersion - 1));
    	    		} else {
    	    			metadataObjectVersions = new ArrayList<Map<Double, DescribeMetadataObject>>();
    	    			versionedMetadataObjects.put(xmlName, metadataObjectVersions);
    	    		}
    	    		
    	    		metadataObjectVersions.add(new HashMap<Double, DescribeMetadataObject>(){{ put(apiVersion, obj); }});
    	    		
    	    		sb.append("***************************************************\n");
    	    	}
    	    	
    	    	sb.append("\nTotal of " + describeMetadataObjects.length + " objects\n");
    	    } else {
    	    	sb.append("Failed to obtain metadata types.");
    	    }
    	    System.out.println(sb.toString());
    	} catch (Exception ce) {
    		ce.printStackTrace();
    	}
    	
    	return true;
    }
    
    private static void compareDescribeMetadataObjects(DescribeMetadataObject first, DescribeMetadataObject second) {
    	String name = first.getXmlName();
    	assert name.equals(second.getXmlName()) : "Name between versions was changed: first: " + name + ", second: " + second.getXmlName();
    	assert first.getDirectoryName().equals(second.getDirectoryName()) : "DirName between versions of " + name + " was changed: first: " + first.getDirectoryName() + ", second: " + second.getDirectoryName();
    	String firstSuffix = first.getSuffix();
    	String secondSuffix = second.getSuffix();
    	if(firstSuffix == null) {
    		assert secondSuffix == null : name + "Suffix has been added between versions:\nfirst:\n" + first.toString() + "\nsecond:\n" + second.toString();
    	} else {
    		assert firstSuffix.equals(secondSuffix) : "Suffix between versions of " + name + " was changed: first: " + firstSuffix + ", second: " + secondSuffix;
    	}
    	//child names are always overwritten by the latest API
    }
}