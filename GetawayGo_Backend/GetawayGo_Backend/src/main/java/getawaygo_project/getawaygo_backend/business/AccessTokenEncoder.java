package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.AccessToken;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}