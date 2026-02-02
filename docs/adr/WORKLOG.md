# Work Log – Auth Server

## Implemented the KeyManager (RsaKeyManager)

This class is responsible for generating RSA Key Pair
- Private Key (Used for signing the tokens)
- Public Key (Exposed through API, used to validate the token)

## Implemented the Token Generator (JWTService)

This class is responsible for creating new tokens by following below steps:
- Create Claim part of JWT (JWTClaimSet)
- Create the Header part of JWT (JWSHeader)
- Sign the created JWT token using the Private Key

## Expose the Public Key (JwksController)

- Expose the api (/auth/api/.well-known/jwks.json)
