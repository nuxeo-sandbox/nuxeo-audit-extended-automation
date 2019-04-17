## Description
This plugin contains an Automation operation to log entries in the audit. 
Compared to the native operation, this plugins allows developper to set all the parameters of the audit entry such as the event time, document, current user and extended properties

## Important Note

**These features are not part of the Nuxeo Production platform.**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.

## How to build
Building requires the following software:
- git
- maven

```
git clone https://github.com/nuxeo-sandbox/nuxeo-audit-extended-automation
cd nuxeo-audit-extended-automation
mvn clean install
```

## Deploying
* Install the marketplace package from the admin center or using nuxeoctl
* Reindex the nuxeo repository


## Known limitations
This plugin is a work in progress.

## About Nuxeo
Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands. More information is available at [www.nuxeo.com](http://www.nuxeo.com).
