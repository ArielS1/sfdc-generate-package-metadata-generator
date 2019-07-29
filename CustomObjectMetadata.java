import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sforce.soap.metadata.DescribeMetadataObject;
import com.sun.xml.internal.ws.util.StringUtils;


public class CustomObjectMetadata {
	public String xmlName, requiredField;
	public Children children;
	public Double minApiVersion, maxApiVersion;

	private static final Map<String, String> requiredFields = new HashMap<String, String>() {{
		put("Workflow", "label");
		put("CustomObject", "label");
	}};

	private static final Map<String, Object> differentBehaviorChildren = new HashMap<String, Object>() {{
		put("CustomLabels", new DifferentChildren("CustomLabels", "CustomLabel"));
	}};

	//manually add children that for some reason are not retrieved by the api
	private static final Map<String, String> apiMissingChildren = new HashMap<String, String>() {{
		put("SharingRules", "sharingTerritoryRules");
		put("CustomObject", "NamedFilter");
	}};

	private static final Map<String, String> customChildren = new HashMap<String, String>() {{
		put("CustomField", "fields");
		// special plural ending:
		put("BusinessProcess", "businessProcesses");
		put("Index", "indexes");
		put("WorkflowKnowledgePublish", "knowledgePublishes");
	}};

	public CustomObjectMetadata(DescribeMetadataObject describeMetadataObject, Double minApiVersion,
			Double maxApiVersion) {
		xmlName = describeMetadataObject.getXmlName();
		if (requiredFields.containsKey(xmlName)) {
			requiredField = requiredFields.get(xmlName);
		}

		this.minApiVersion = minApiVersion;
		this.maxApiVersion = maxApiVersion;

		if (differentBehaviorChildren.containsKey(xmlName)) {
			children = (DifferentChildren) differentBehaviorChildren.get(xmlName);
		} else {
			List<String> childNames = new ArrayList<String>();
			for (String childName : describeMetadataObject.getChildXmlNames()) {
				childNames.add(childName);
			}

			if (apiMissingChildren.containsKey(xmlName)) {
				childNames.add(apiMissingChildren.get(xmlName));
			}

			NormalChildren normalChildren = new NormalChildren();
			if (!childNames.isEmpty()) {
				normalChildren = new NormalChildren();
				children = normalChildren;
			}

			for (String childName : childNames) {
				String keyName = null;
				if (customChildren.containsKey(childName)) {
					keyName = customChildren.get(childName);
				} else {
					// first option handles workflows
					keyName = childName.startsWith(xmlName) ? StringUtils.decapitalize(childName.replace(xmlName, ""))
							: StringUtils.decapitalize(childName);
					if (keyName.charAt(keyName.length() - 1) != 's') {
						keyName += 's';
					}
				}

				System.out.println("keyName: " + keyName + ", childName: " + childName);
				normalChildren.put(keyName, new NormalChildrenObject(childName, "fullName"));
			}
		}
	}
}