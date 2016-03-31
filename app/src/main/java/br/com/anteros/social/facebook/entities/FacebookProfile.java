package br.com.anteros.social.facebook.entities;


import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import br.com.anteros.social.facebook.utils.Attributes;
import br.com.anteros.social.facebook.utils.FacebookUtils;

/**
 * Created by edson on 23/03/16.
 */
public class FacebookProfile extends FacebookUser {

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

    private Bitmap imageBitmap;

    /**
     * Returns the ID of the user. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the ID of the user
     */
    public String getId() {
        return super.getId();
    }

    /**
     * Returns the name of the user. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the name of the user
     */
    public String getName() {
        return super.getName();
    }

    /**
     * Returns the first name of the user. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the middle name of the user. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the middle name of the user
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Returns the last name of the user. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the gender of the user. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the gender of the user
     */
    public String getGender() {
        return gender;
    }

    /**
     * Returns the Facebook URL of the user. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the Facebook URL of the user
     */
    public String getLink() {
        return link;
    }

    /**
     * The user's age range. <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return the user's age range
     */
    public FacebookAgeRange getAgeRange() {
        return ageRange;
    }

    /**
     * Returns the birthday of the user. <b>MM/DD/YYYY</b> format <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_BIRTHDAY} <br>
     *
     * @return the birthday of the user
     */
    public String getBirthday() {
        return birthday;
    }





    /**
     * Return the email of the user.<br>
     * <br>
     * <b> Permissions:</b> <br>
     * {@link br.com.anteros.social.facebook.actions.Permission#EMAIL}
     * To get the details about the place, use GetPage with this id.
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * The user's profile pic <br>
     * <br>
     * <b> Permissions:</b><br>
     * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
     *
     * @return The user's profile pic
     */
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

        /**
         * <b>Description:</b><br>
         * The user's Facebook ID<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String ID = "id";

        /**
         * <b>Description:</b><br>
         * The user's full name<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String NAME = "name";

        /**
         * <b>Description:</b><br>
         * The user's first name<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String FIRST_NAME = "first_name";

        /**
         * <b>Description:</b><br>
         * The user's middle name<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String MIDDLE_NAME = "middle_name";

        /**
         * <b>Description:</b><br>
         * The user's last name<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String LAST_NAME = "last_name";

        /**
         * <b>Description:</b><br>
         * The user's gender: female or male<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String GENDER = "gender";

        /**
         * <b>Description:</b><br>
         * The URL of the profile for the user on Facebook<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String LINK = "link";

        /**
         * <b>Description:</b><br>
         * The user's age range<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String AGE_RANGE = "age_range";

        /**
         * <b>Description:</b><br>
         * The user's birthday<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_BIRTHDAY}
         *
         */
        public static final String BIRTHDAY = "birthday";

        /**
         * <b>Description:</b><br>
         * The email address granted by the user<br>
         * <br>
         *
         * <b>Note:</b> There is no way for apps to obtain email addresses for a
         * user's friends.<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#EMAIL}
         */
        public static final String EMAIL = "email";


        /**
         * <b>Description:</b><br>
         * The user's profile pic<br>
         * <br>
         *
         * <b>Permissions:</b><br>
         * {@link br.com.anteros.social.facebook.actions.Permission#USER_ABOUT_ME}
         *
         */
        public static final String PICTURE = "picture";


        public static class Builder {
            Set<String> properties;

            public Builder() {
                properties = new HashSet<String>();
            }

            /**
             * Add property you need
             *
             * @param property
             *            The property of the user profile<br>
             *            For example: {@link FacebookProfile.Properties#FIRST_NAME}
             * @return {@link Properties.Builder}
             */
            public Builder add(String property) {
                properties.add(property);
                return this;
            }

            /**
             * Add property and attribute you need
             *
             * @param property
             *            The property of the user profile<br>
             *            For example: {@link FacebookProfile.Properties#PICTURE}
             * @param attributes
             *            For example: picture can have type,width and height<br>
             *
             * @return {@link FacebookProfile.Properties.Builder}
             */
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
