package com.beverage.beverageapiauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("beverage-web")
                .secret(passwordEncoder.encode("web123"))
                .authorizedGrantTypes("password", "refresh_token") // Type of flow used = password
                .scopes("write", "read")
                .accessTokenValiditySeconds(60 * 60 * 6)
                .refreshTokenValiditySeconds(60 * 24 * 60 * 60)
                    .and()
                .withClient("beverage-analytics")
                .secret(passwordEncoder.encode("beverage23"))
                .authorizedGrantTypes("authorization_code") // It is here that we authorize the client with the scopes
                .scopes("write", "read")
                .redirectUris("http://client-application")
//                .redirectUris("http:/www.beverage-analytics.local:8082") // Class 22.19
// http://localhost:8081/oauth/authorize?response_type=code&client_id=beverage-analytics&state=abc&redirect_uri=http://
// client-application
                    .and()
                .withClient("webadmin")
                .authorizedGrantTypes("implicit")
                .scopes("write", "read")
                .redirectUris("http://client-web-application")
// http://localhost:8081/oauth/authorize?response_type=token&client_id=webadmin&state=abc&redirect_uri=http://
// client-web-application
                .and()
                .withClient("invoice")
                .secret(passwordEncoder.encode("invoice123"))
                .authorizedGrantTypes("client_credentials") // Type of flow used = password
                .scopes("write", "read")
                    .and()
                .withClient("checktoken")
                .secret(passwordEncoder.encode("check123")); // The standard time is 12 hours
    }

// http://localhost:8081/oauth/authorize?response_type=code&client_id=beverage-analytics&redirect_uri=http://client-application
// &code_challenge=xxxxxxxxxxx&code_challenge_method=s256
// https://tonyxu-io.github.io/pkce-generator/

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
//        security.checkTokenAccess("permitAll()"); // It permits all access
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // It needs this, otherwise the 'password flow' does not work
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false) // It generates another refresh token
                .tokenStore(redisTokenStore())
                .tokenGranter(tokenGranter(endpoints));
    }

    private TokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());

        var granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());

        return new CompositeTokenGranter(granters);
    }
}
