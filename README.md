# sfdc-generate-package-metadata-generator

Generate metadata.js file for sfdc-generate-package package from all available Salesforce APIs by using Salesforce SOAP Metadata API<br /><br />
Could be also useful for just listing all Salesforce metadata directories

## Getting Started

You need to generate an enterprise, metadata and a force-wsc jar files from your ORG's WSDLs.<br />
I recommend using [this](http://www.asagarwal.com/step-by-step-guide-to-get-started-with-salesforce-soap-api-using-java/) guide.<br />
you will also have to generate a metadata.jar using the Metadata WSDL in the same way that the enterprise.jar is created in the guide.

## Usage

Use your own ORG credentials in MetadataLoginUtil.java

## Built With

* [gson-2.8.5](https://github.com/google/gson) - To create json strings
* [wsc](https://github.com/forcedotcom/wsc) - generated together with enterprise and metadata jars - for the connection to the SOAP API
* enterprise.jar - generated from Salesforce's Enterprise WSDL
* metadata.jar - generated from Salesforce's Metadata WSDL

## Authors

* **Ariel Siler** - *Initial work* - [ArielS1](https://github.com/ArielS1)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
