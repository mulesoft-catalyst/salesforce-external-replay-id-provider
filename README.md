## Salesforce Replay ID External Source Provider with Encrypted Client ID and Secret


### Checkout and Install the module

  - Checkout Code
    - `git clone git@github.com:mulesoft-catalyst/salesforce-external-replay-id-provider.git`
  - This will locally install to local M2 repository
    - `mvn clean install -DorganizationId=<REPLACE_THIS_WITH_ANYPOINT_ORG_ID>`
  - This will Deploy to your Anypoint Organization
    - `mvn deploy -DorganizationId=<REPLACE_THIS_WITH_ANYPOINT_ORG_ID>`


### Add the dependency to your application pom.xml

```xmln
  <dependency>
    <groupId>[REPLACE_THIS_WITH_ANYPOINT_ORG_ID]</groupId>
    <artifactId>salesforce-replay-id-external-provider</artifactId>
    <version>1.0.0</version>
  <dependency>
```


### Required YAML configuration

  - Add the YAML config to your YAML file **_(ie: src/main/resources/config_<env>.yaml)_**
  ```YAML
    sfdc_replay_provider:
      url: "http://localhost:8081/api"
      key: "![Y4li8QslP5WBInIRaPq/nfFWvZAlONnQYy+gCWTTbkE=]"
      secret: "![Y4li8QslP5WBInIRaPq/nfFWvZAlONnQYy+gE2WzHyegI7gYMEcwEQ==]"
      platform_event_name: "Account_Update__c"
      ignore_error: "false"
      default_replay_id: "-1"  
  ```

  ### Property description:


| Key                 | Description | Default Value |
| ------------------- | ------------------------------------------------------------------------------------------------- | ----- |
| url                 | HTTP URL location to extract the Last Replay ID | |
| key                 | Client ID and must be (ie: **![<Encrypted Value>]**) ENCRYPTED | |
| secret              | Client Secret and must be ENCRYPTED **![<Encrypted Value>]** | |
| platform_event_name | Platform Event Name | |
| ignore_error        | Boolean. If encountered an error it will set to default -1 | false |
| default_replay_id   | (Optional) The value will be set to **${custom-sfdc-property::last_replay_id}** when external return empty| -1 |
