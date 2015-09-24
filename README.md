# Identity API Sanity Tests

Sanity tests of Identity API REST endpoints. 

Tests execute against hosts in different stages (CODE, PROD). Stage targeting is 
done via an environmental variable:

```
ID_API_TARGET_STAGE := CODE | PROD
```
