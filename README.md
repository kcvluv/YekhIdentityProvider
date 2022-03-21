###Install the docker and pull the docker image from repository
docker pull amexdocker2019/yekhprovider
[OR]
docker build -t yekhprovider .

###Run the docker
docker run -p 8081:8081 -e PORT=8081 yekhprovider

####1. OPENID DISCOVERY DOCUMENT
```
curl --location --request GET 'http://localhost:8001/.well-known/openid-configuration'
```
####Sample Response
```
{
    "issuer": "http://localhost:8001",
    "authorization_endpoint": "http://localhost:8001/authorize",
    "token_endpoint": "http://localhost:8001/token",
    "jwks_uri": "http://localhost:8001/jwks.json",
    "scopes_supported": [
        "openid"
    ]
}
```
####2. JSON Web KeySet document
```
curl --location --request GET 'http://localhost:8001/jwks.json'
```
######Sample Response
```{
    "keys": [
        {
            "crv": "P-256",
            "d": "8yWxWgX3LGiO641rSIH4WGUGrHRS_5oEktu1qymMbnk",
            "ext": true,
            "key_ops": [
                "sign"
            ],
            "kid": "n03y0bARhO7erWorMe2xISiXtmTNioESwiwuo4RM6dw",
            "kty": "EC",
            "x": "tMhegImqwNRZIjV3sB4P1YYDqCOGfaXFCFy7hB5uUJE",
            "y": "g0LBSkHtM7LmoB0KilZ2NX1UVJDeJI6SJcrfkcdzNyE"
        }
    ]
}
```
##### 3. Authorization Endpoint
Open the below url in browser.
http://localhost:8001/authorize?redirect_uri=http%3A%2F%2Flocalhost%3A8001%2Fcallback&response_type=code&response_mode=query&code_challenge=“MTU3Yjg2NDBlZDgzMDYyYTM5ZDhhY2VkZWMzOWNmZmYxZjU3Y2I4ZTk2MTYwYjJiM2MxODBiZTc1MmU1Njg0Ng==“&code_challenge_method=S256&scope=openid

Sample redirect url:
```
http://localhost:8001/callback?code=sStQODECgj
```

##### 4. Token End point
```json
curl --location --request POST 'http://localhost:8001/token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "code": "sStQODECgj",
    "code_verifier": "sfasdfwr3445",
    "redirect_uri": "http://localhost:8001/callback",
    "grant_type": "authorization_code"
}'
```

######Sample Response:
```json
{
    "id_token": "eyJraWQiOiJuMDN5MGJBUmhPN2VyV29yTWUyeElTaVh0bVROaW9FU3dpd3VvNFJNNmR3IiwiYWxnIjoiRVMyNTYifQ.eyJzdWIiOiJrY3ZsdXZAZ21haWwuY29tIiwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0OjgwMDEiLCJleHAiOjE2NDc4NTAwNzg5OTQsImlhdCI6MTY0Nzg0OTQ3ODk5NH0.IbX81rga7df8PTp_tkbr71MvZou6mLwU_fu5riFbgJLHOevYJg6-IGUVDUiG_XrlVybWZCHOphigmcky_ZFk0A"
}
```# YekhIdentityProvider
