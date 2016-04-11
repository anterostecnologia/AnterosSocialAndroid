package br.com.anteros.social.facebook.entities;


import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import br.com.anteros.social.core.AgeRange;
import br.com.anteros.social.core.SocialNetworkType;
import br.com.anteros.social.core.SocialProfile;
import br.com.anteros.social.facebook.utils.Attributes;
import br.com.anteros.social.facebook.utils.FacebookUtils;

/**
 * Created by edson on 23/03/16.
 */
public class FacebookProfile extends FacebookUser implements SocialProfile {

    @SerializedName(Properties.FIRST_NAME)
    private String firstName;

    @SerializedName(Properties.MIDDLE_NAME)
    private String middleName;

    @SerializedName(Properties.LAST_NAME)
    private String lastName;

    @SerializedName(Properties.GENDER)
    private String gender;

    @SerializedName(Properties.LINK)
    private String link;

    @SerializedName(Properties.AGE_RANGE)
    private FacebookAgeRange ageRange;

    @SerializedName(Properties.BIRTHDAY)
    private String birthday;

    @SerializedName(Properties.EMAIL)
    private String email;

    @SerializedName(Properties.PICTURE)
    private FacebookUtils.SingleDataResult<FacebookImage> image;

    private String userName = "";

    private Bitmap imageBitmap;

    public String getId() {
        return super.getId();
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public String getName() {
        return super.getName();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String getFullName() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFirstName());
        if (getMiddleName()!=null){
            sb.append(" ");
            sb.append(getMiddleName());
        }
        sb.append(" ");
        sb.append(getLastName());
        return sb.toString();
    }


    public String getGender() {
        return gender;
    }

    public String getLink() {
        return link;
    }

    public AgeRange getAgeRange() {
        return ageRange;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getImageUrl() {
        return getImage();
    }

    public String getImage() {
        if (image == null || image.data == null) {
            return null;
        }
        return image.data.getUrl();
    }


    public static class Properties {
        private final Bundle mBundle;

        private Properties(Builder builder) {
            mBundle = new Bundle();
            Iterator<String> iterator = builder.properties.iterator();
            String fields = FacebookUtils.join(iterator, ",");
            mBundle.putString("fields", fields);
        }

        public Bundle getBundle() {
            return mBundle;
        }

        public static final String ID = "id";

        public static final String NAME = "name";

        public static final String FIRST_NAME = "first_name";

        public static final String MIDDLE_NAME = "middle_name";

        public static final String LAST_NAME = "last_name";

        public static final String GENDER = "gender";

        public static final String LINK = "link";

        public static final String AGE_RANGE = "age_range";

        public static final String BIRTHDAY = "birthday";

        public static final String EMAIL = "email";

        public static final String PICTURE = "picture";


        public static class Builder {
            Set<String> properties;

            public Builder() {
                properties = new HashSet<String>();
            }

            public Builder add(String property) {
                properties.add(property);
                return this;
            }

            public Builder add(String property, Attributes attributes) {
                Map<String, String> map = attributes.getAttributes();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(property);
                stringBuilder.append('.');
                stringBuilder.append(FacebookUtils.join(map, '.', '(', ')'));
                properties.add(stringBuilder.toString());
                return this;
            }

            public Properties build() {
                return new Properties(this);
            }

        }
    }

    public Bitmap getImageBitmap(){
        return this.imageBitmap;
    }

    @Override
    public SocialNetworkType getProfileType() {
        return SocialNetworkType.FACEBOOK;
    }

    @Override
    public String getProfileName() {
        return "Facebook";
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.imageBitmap = bitmap;
    }

    @Override
    public String toString() {
        return "FacebookProfile{" +
                "firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", link='" + link + '\'' +
                ", ageRange=" + ageRange +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", image=" + image.data.getUrl() +
                '}';
    }
}
