//this file was generated using https://github.com/ArielS1/sfdc-generate-package-metadata-generator

module.exports = apiVersion => {
    apiVersion = parseInt(apiVersion);
    return Object.keys(allMetadata).reduce((acc,metadataName) => {
        const { minApiVersion, maxApiVersion, xmlName, requiredField, children = {} } = allMetadata[metadataName];
        if(apiVersion >= minApiVersion && apiVersion <= maxApiVersion) {
            acc[metadataName] = { xmlName, requiredField, children };
        }
        return acc;
    },{});    
}

const allMetadata =
