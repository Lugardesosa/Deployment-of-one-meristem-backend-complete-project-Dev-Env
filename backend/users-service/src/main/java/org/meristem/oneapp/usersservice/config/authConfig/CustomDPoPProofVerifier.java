
package org.meristem.oneapp.usersservice.config.authConfig;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.StringUtils;


final class CustomDPoPProofVerifier {

	private static final JwtDecoderFactory<DPoPProofContext> dPoPProofVerifierFactory = new DPoPProofJwtDecoderFactory();

	private CustomDPoPProofVerifier() {
	}

	static Jwt verifyIfAvailable(OAuth2AuthorizationGrantAuthenticationToken authorizationGrantAuthentication) {
		String dPoPProof = (String) authorizationGrantAuthentication.getAdditionalParameters().get("dpop_proof");
		if (!StringUtils.hasText(dPoPProof)) {
			return null;
		}

		String method = (String) authorizationGrantAuthentication.getAdditionalParameters().get("dpop_method");
		String targetUri = (String) authorizationGrantAuthentication.getAdditionalParameters().get("dpop_target_uri");

		Jwt dPoPProofJwt;
		try {
			// @formatter:off
			DPoPProofContext dPoPProofContext = DPoPProofContext.withDPoPProof(dPoPProof)
					.method(method)
					.targetUri(targetUri)
					.build();
			// @formatter:on
			JwtDecoder dPoPProofVerifier = dPoPProofVerifierFactory.createDecoder(dPoPProofContext);
			dPoPProofJwt = dPoPProofVerifier.decode(dPoPProof);
		}
		catch (Exception ex) {
			throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_DPOP_PROOF), ex);
		}

		return dPoPProofJwt;
	}

}
