package com.project.auth.service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.auth.config.JWTProperties;
import com.project.auth.keys.RsaKeyManager;

/**
 * Service responsible for issuing JWT access tokens.
 *
 * <p>
 * This service creates and signs JWTs using the currently active RSA key
 * managed by {@link RsaKeyManager}. Tokens are signed with {@code RS256}
 * and include standard JWT registered claims (issuer, audience, subject,
 * issued-at, expiration) plus an application-specific scopes claim.
 * </p>
 */
@Service
public class JWTService {

	@Autowired
	private RsaKeyManager keyManager;
	
	@Autowired
	private JWTProperties props;
	
	/**
	 * Issues a signed JWT access token for the given user and scopes.
	 *
	 * <p>
	 * The token contains:
	 * </p>
	 * <ul>
	 *   <li>{@code iss} from configuration</li>
	 *   <li>{@code aud} from configuration</li>
	 *   <li>{@code sub} set to the provided {@code userId}</li>
	 *   <li>{@code iat} set to current time</li>
	 *   <li>{@code exp} set to current time + configured TTL</li>
	 *   <li>A custom claim containing the provided scopes</li>
	 * </ul>
	 *
	 * <p>
	 * The JWS header includes the active key ID ({@code kid}) so verifiers can
	 * select the correct public key from the JWKS endpoint.
	 * </p>
	 *
	 * @param userId the subject of the token (typically the user identifier)
	 * @param scopes the granted scopes/authorities to embed in the token
	 * @return a serialized, signed JWT (JWS Compact Serialization)
	 * @throws IllegalStateException if token construction or signing fails
	 */
	public String issueAccessToken(String userId, List<String> scopes) {
		Instant now = Instant.now();
		Instant expireTime = now.plusMillis(props.getAccessTokenTtlSeconds());
		
		JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
							.issuer(props.getIssuer())
							.audience(props.getAudience())
							.expirationTime(Date.from(expireTime))
							.subject(userId)
							.issueTime(Date.from(now))
							.claim("claim", scopes)
							.build();
		
		try {
			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
								.keyID(keyManager.getActiveKey().getKeyID())
								.type(JOSEObjectType.JWT).build();
			
			SignedJWT jwt = new SignedJWT(header, claimSet);
			JWSSigner signer = new RSASSASigner(keyManager.getActiveKey().toPrivateKey());
			jwt.sign(signer);
			
			return jwt.serialize();
		} catch (Exception e) {
			throw new IllegalStateException("Failed to issue an access token: "+e);
		}
							
	}
}
