package com.project.auth.tokens;

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

@Service
public class JWTService {

	@Autowired
	private RsaKeyManager keyManager;
	
	@Autowired
	private JWTProperties props;
	
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
