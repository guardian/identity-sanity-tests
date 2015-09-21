# Identity API Sanity Tests

Sanity tests of Identity API REST endpoints. 

Tests can be targeted at different stages (LOCAL, CODE, PROD) using either:

* environmental variable `IDENTITY_API_HOST`
* property `identity.api.host` in `application.conf`

The environmental variable has priority over the config file.

To target the tests in TeamCity use the environmental variable. 

Host Environment | Domain
---------------- | --------------------------------------
CODE             | https://idapi.code.dev-theguardian.com
PROD             | https://idapi.theguardian.com
