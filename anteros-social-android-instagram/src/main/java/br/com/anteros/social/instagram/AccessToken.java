package br.com.anteros.social.instagram;

/**
 * Created by edson on 05/04/16.
 */
public class AccessToken {

    public String token;
    public String secret;

    public AccessToken(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                ", secret='" + secret + '\'' +
                '}';
    }
}
