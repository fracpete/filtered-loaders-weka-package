# filtered-loaders-weka-package

Meta-loaders that apply a filter to the data loader by the base-loader.

Available converters:

* `weka.core.converters.FilteredBatchLoader` - can use batch and incremental loaders and filters
* `weka.core.converters.FilteredIncrementalLoader` - requires incremental loader and filter


## Releases

* [2020.11.29](https://github.com/fracpete/filtered-loaders-weka-package/releases/download/v2020.11.29/common-csv-2020.11.29.zip)


## Maven

Use the following dependency in your `pom.xml`:

```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>filtered-loaders-weka-package</artifactId>
      <version>2020.11.29</version>
      <type>jar</type>
      <exclusions>
        <exclusion>
          <groupId>nz.ac.waikato.cms.weka</groupId>
          <artifactId>weka-dev</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
```


## How to use packages

For more information on how to install the package, see:

https://waikato.github.io/weka-wiki/packages/manager/


