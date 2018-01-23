package me.philcali.oauth.google;

import java.util.Map;

import me.philcali.oauth.api.model.IProfile;

public class Profile implements IProfile {
    private final Map<String, String> data;

    public Profile(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String getEmail() {
        return data.get("email");
    }

    @Override
    public String getFirstName() {
        return data.get("given_name");
    }

    @Override
    public String getId() {
        return data.get("id");
    }

    @Override
    public String getImage() {
        return data.get("picture");
    }

    @Override
    public String getLastName() {
        return data.get("family_name");
    }

}
