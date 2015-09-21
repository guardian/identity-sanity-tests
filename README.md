# Identity API Sanity Tests

Sanity tests of Identity API REST endpoints. 

The tests can be targeted at different environments (LOCAL, CODE, PROD) using either:

* environemental variable `IDENTITY_API_HOST`
* property `identity.api.host` in `application.conf`

The environemental variable has priority over the config file.

To target the tests in TeamCity use the environmental variable. 

Host Environment | Domain
---------------- | --------------------------------------
CODE             | https://idapi.code.dev-theguardian.com
PROD             | https://idapi.theguardian.com


