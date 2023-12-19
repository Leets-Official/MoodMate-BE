package com.moodmate.moodmatebe.global.oauth.application;

import com.moodmate.moodmatebe.domain.user.domain.User;
import com.moodmate.moodmatebe.domain.user.repository.UserRepository;
import com.moodmate.moodmatebe.global.error.ErrorCode;
import com.moodmate.moodmatebe.global.error.exception.ServiceException;
import com.moodmate.moodmatebe.global.oauth.domain.GoogleOAuthAttribute;
import com.moodmate.moodmatebe.global.oauth.domain.OAuthAttribute;
import com.moodmate.moodmatebe.global.oauth.domain.OAuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuthDetailService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Optional<OAuthAttribute> attribute = Optional.of(new GoogleOAuthAttribute(oAuth2User.getAttributes()));

        OAuthAttribute attr = attribute.orElseThrow(() -> new ServiceException(ErrorCode.INTERNAL_SERVER_ERROR));
        String email = attr.getEmail();
        String name = attr.getName();

        User user = userRepository.findByUserEmail(email).orElse(null);
        if (user == null) {
            User createdUser = User.builder()
                    .userEmail(email)
                    .userMatchActive(true)
                    .build();

            user = userRepository.save(createdUser);
        }

        return new OAuthDetails(user.getUserId(), user.getUserEmail(), attr.getAttributes());
    }
}