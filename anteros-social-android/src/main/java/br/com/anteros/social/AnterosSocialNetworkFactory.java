package br.com.anteros.social;

import br.com.anteros.social.facebook.AnterosFacebook;
import br.com.anteros.social.google.AnterosGoogle;
import br.com.anteros.social.instagram.AnterosInstagram;
import br.com.anteros.social.linkedin.AnterosLinkedIn;
import br.com.anteros.social.twitter.AnterosTwitter;

/**
 * Created by edson on 07/04/16.
 */
public class AnterosSocialNetworkFactory {

    public static AnterosSocialNetwork createSocialNetwork(AnterosSocialConfiguration configuration) throws AnterosSocialNetworkException {
        if (configuration.getSocialNetworkType() == SocialNetworkType.FACEBOOK) {
            return AnterosFacebook.create(configuration);
        } else if (configuration.getSocialNetworkType() == SocialNetworkType.GOOGLE) {
            return AnterosGoogle.create(configuration);
        } else if (configuration.getSocialNetworkType() == SocialNetworkType.INSTAGRAM) {
            return AnterosInstagram.create(configuration);
        } else if (configuration.getSocialNetworkType() == SocialNetworkType.LINKEDIN) {
            return AnterosLinkedIn.create(configuration);
        } else if (configuration.getSocialNetworkType() == SocialNetworkType.TWITTER) {
            return AnterosTwitter.create(configuration);
        }
        throw new AnterosSocialNetworkException("Configuração inválida. Não foi possível identificar o tipo de Rede Social.");
    }
}
