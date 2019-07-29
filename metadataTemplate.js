//this file was generated using https://github.com/ArielS1/sfdc-generate-package-metadata-generator
module.exports = apiVersion => {
    apiVersion = parseInt(apiVersion);
    const apiMetadata = {};

    Object.keys(allMetadata).forEach(metadataName => {
        const { minApiVersion, maxApiVersion, xmlName, requiredField, children = {} } = allMetadata[metadataName];
        if(apiVersion >= minApiVersion && apiVersion <= maxApiVersion) {
            apiMetadata[metadataName] = { xmlName, requiredField, children };
        }
    });

    return apiMetadata;
}

const allMetadata =