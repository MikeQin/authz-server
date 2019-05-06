# OAuth2 Authorization Server

OAuth2 Authorization Server and Resource Server Demo.

### Password Grant Type
```
# To get access_token using grant_type: password
curl -X POST SampleClientId:secret@localhost:8081/auth/oauth/token \
  -d grant_type=password -d username=mike -d password=123

# To get User Info from User Info endpoint
curl -X GET SampleClientId:secret@localhost:8081/auth/user/me \
  -d grant_type=password -d access_code=xxxxxxx

# To get User Extra from User Extra endpoint
curl -X GET SampleClientId:secret@localhost:8081/auth/user/extra \
  -d grant_type=password -d access_code=xxxxxxx
```

### Client Credentials Grant Type
```
curl SampleClientId:secret@localhost:8081/auth/oauth/token \
  -d grant_type=client_credentials
```