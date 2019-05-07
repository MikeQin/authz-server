# OAuth2 Authorization Server

OAuth2 Authorization Server and Resource Server Demo.

### Password Grant Type
```
# To get access_token using grant_type: password
curl -X POST SampleClientId:secret@localhost:8081/auth/oauth/token \
  -d grant_type=password -d username=mike -d password=123

# To get User Info from User Info endpoint using access_token
curl -X GET localhost:8081/auth/user/me \
  -H "Authorization: Bearer acadbb31-f126-411d-ae5b-6a278cee2ed6"

# To get User Extra from User Extra endpoint using access_token
curl -X GET localhost:8081/auth/user/extra \
  -H "Authorization: Bearer acadbb31-f126-411d-ae5b-6a278cee2ed6"
```

### Client Credentials Grant Type
```
curl SampleClientId:secret@localhost:8081/auth/oauth/token \
  -d grant_type=client_credentials
```