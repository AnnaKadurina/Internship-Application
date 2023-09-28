package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.AccessToken;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}