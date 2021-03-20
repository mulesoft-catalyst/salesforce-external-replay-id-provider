## Salesforce Replay ID External Source Provider with Encrypted Client ID and Secret


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
      platform_event_name: "Disbursement_Event__c"
      ignore_error: "false"
      default_replay_id: "-1"  
  ```

  ### Property description:


| -----               | -----                                                                                                                                            |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| Key                 | Description                                                                                                                                      |
| url                 | HTTP URL location to extract the Last Replay ID                                                                                                  |
| key                 | Client ID and must be ENCRYPTED                                                                                                                  |
| secret              | Client Secret and must be ENCRYPTED                                                                                                              |
| platform_event_name | Platform Event Name for filtering                                                                                                                |
| ignore_error        | If encountered an error it will set to default -1                                                                                                |
| default_replay_id   | (Optional) If the external provider encountered an ERROR, the default_replay_id value will be set to **${custom-sfdc-property::last_replay_id}** |
|                     |                                                                                                                                                  |
