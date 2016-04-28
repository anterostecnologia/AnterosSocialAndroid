package br.com.anteros.social.core.component.tipo;

import br.com.anteros.social.core.R;

/**
 * @author Eduardo Albertini (albertinieduardo@hotmail.com)
 */
public enum SocialNetwork {
    FACEBOOK {
        @Override
        public int getDefaultColor() {
            return R.color.colorFacebook;
        }

        @Override
        public int getLightColor() {
            return R.color.colorWhite;
        }

        @Override
        public int getDefaultColorPressed() {
            return R.color.colorFacebookPressed;
        }

        @Override
        public int getLightColorPressed() {
            return R.color.colorLightGrey;
        }

        @Override
        public int getLogotipo() {
            return R.drawable.ic_facebook;
        }

        @Override
        public String getName() {
            return "Facebook";
        }
    },

    GOOGLE {
        @Override
        public int getDefaultColor() {
            return R.color.colorGooglePlus;
        }

        @Override
        public int getLightColor() {
            return R.color.colorWhite;
        }

        @Override
        public int getDefaultColorPressed() {
            return R.color.colorGooglePlusPressed;
        }

        @Override
        public int getLightColorPressed() {
            return R.color.colorLightGrey;
        }

        @Override
        public int getLogotipo() {
            return R.drawable.ic_google_plus;
        }

        @Override
        public String getName() {
            return "Google";
        }
    },

    INSTAGRAM {
        @Override
        public int getDefaultColor() {
            return R.color.colorInstagram;
        }

        @Override
        public int getLightColor() {
            return R.color.colorWhite;
        }

        @Override
        public int getDefaultColorPressed() {
            return R.color.colorInstagramPresssed;
        }

        @Override
        public int getLightColorPressed() {
            return R.color.colorLightGrey;
        }

        @Override
        public int getLogotipo() {
            return R.drawable.ic_instagram;
        }

        @Override
        public String getName() {
            return "Instagram";
        }
    },

    LINKEDIN {
        @Override
        public int getDefaultColor() {
            return R.color.colorLinkedin;
        }

        @Override
        public int getLightColor() {
            return R.color.colorWhite;
        }

        @Override
        public int getDefaultColorPressed() {
            return R.color.colorLinkedinPressed;
        }

        @Override
        public int getLightColorPressed() {
            return R.color.colorLightGrey;
        }

        @Override
        public int getLogotipo() {
            return R.drawable.ic_linkedin;
        }

        @Override
        public String getName() {
            return "LinkedIn";
        }
    },

    TWITTER {
        @Override
        public int getDefaultColor() {
            return R.color.colorTwitter;
        }

        @Override
        public int getLightColor() {
            return R.color.colorWhite;
        }

        @Override
        public int getDefaultColorPressed() {
            return R.color.colorTwitterPressed;
        }

        @Override
        public int getLightColorPressed() {
            return R.color.colorLightGrey;
        }

        @Override
        public int getLogotipo() {
            return R.drawable.ic_twitter;
        }

        @Override
        public String getName() {
            return "Twitter";
        }
    },

    YOUTUBE {
        @Override
        public int getDefaultColor() {
            return R.color.colorYoutube;
        }

        @Override
        public int getLightColor() {
            return R.color.colorWhite;
        }

        @Override
        public int getDefaultColorPressed() {
            return R.color.colorYoutubePressed;
        }

        @Override
        public int getLightColorPressed() {
            return R.color.colorLightGrey;
        }

        @Override
        public int getLogotipo() {
            return R.drawable.ic_youtube;
        }

        @Override
        public String getName() {
            return "YouTube";
        }
    },

    SNAPCHAT {
        @Override
        public int getDefaultColor() {
            return R.color.colorSnapchat;
        }

        @Override
        public int getLightColor() {
            return R.color.colorWhite;
        }

        @Override
        public int getDefaultColorPressed() {
            return R.color.colorSnapchatPressed;
        }

        @Override
        public int getLightColorPressed() {
            return R.color.colorLightGrey;
        }

        @Override
        public int getLogotipo() {
            return R.drawable.ic_snapchat;
        }

        @Override
        public String getName() {
            return "Snapchat";
        }
    };

    public abstract int getDefaultColor();

    public abstract int getLightColor();

    public abstract int getDefaultColorPressed();

    public abstract int getLightColorPressed();

    public abstract int getLogotipo();

    public abstract String getName();
}
