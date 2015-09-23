# Identity API Sanity Tests

Sanity tests of Identity API REST endpoints. 

Tests execute against Web and Mobile Apps hosts in different stages (CODE, PROD). 
Stage targeting is done via an environmental variable:

```
ID_API_TARGET_STAGE := CODE | PROD
```
Domain names of targets are set in `application.conf`

List of targets:

Consumer | Stage | Domain
---------|-------|--------------------------------------
Web      | CODE  | https://idapi.code.dev-theguardian.com
Web      | PROD  | https://idapi.theguardian.com
Mobile   | CODE  | https://id.code.dev-guardianapis.com
Mobile   | PROD  | https://id.guardianapis.com
