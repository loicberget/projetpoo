package com.myrpg.game.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Manages user profiles, including saving, loading, and managing profile data.
 */
public class ProfileManager extends ProfileSubject {

    private static final String SAVEGAME_SUFFIX = ".sav";
    public static final String DEFAULT_PROFILE = "default";

    private Json json;
    private static ProfileManager profileManager;
    private Hashtable<String, FileHandle> profiles;
    private ObjectMap<String, Object> profileProperties;
    private String profileName;
    private boolean isNewProfile;

    private ProfileManager() {
        json = new Json();
        profiles = new Hashtable<>();
        profileProperties = new ObjectMap<>();
        profileName = DEFAULT_PROFILE;
        isNewProfile = false;
        storeAllProfiles();
    }

    public static synchronized ProfileManager getInstance() {
        if (profileManager == null) {
            profileManager = new ProfileManager();
        }
        return profileManager;
    }

    public void setIsNewProfile(boolean isNewProfile) {
        this.isNewProfile = isNewProfile;
    }

    public boolean getIsNewProfile() {
        return this.isNewProfile;
    }

    public Array<String> getProfileList() {
        Array<String> profileList = new Array<>();
        for (Enumeration<String> e = profiles.keys(); e.hasMoreElements();) {
            profileList.add(e.nextElement());
        }
        return profileList;
    }

    public FileHandle getProfileFile(String profile) {
        return profiles.getOrDefault(profile, null);
    }

    public void storeAllProfiles() {
        if (Gdx.files.isLocalStorageAvailable()) {
            FileHandle[] files = Gdx.files.local(".").list(SAVEGAME_SUFFIX);
            for (FileHandle file : files) {
                profiles.put(file.nameWithoutExtension(), file);
            }
        }
    }

    public boolean doesProfileExist(String profileName) {
        return profiles.containsKey(profileName);
    }

    public void writeProfileToStorage(String profileName, String fileData, boolean overwrite) {
        String fullFilename = profileName + SAVEGAME_SUFFIX;
        FileHandle file = Gdx.files.local(fullFilename);

        if (file.exists() && !overwrite) {
            return;
        }

        if (Gdx.files.isLocalStorageAvailable()) {
            String encodedString = Base64Coder.encodeString(fileData);
            file.writeString(encodedString, !overwrite);
            profiles.put(profileName, file);
        }
    }

    public void setProperty(String key, Object object) {
        profileProperties.put(key, object);
    }

    public <T> T getProperty(String key, Class<T> type) {
        return type.cast(profileProperties.get(key));
    }

    public void saveProfile() {
        notify(this, ProfileObserver.ProfileEvent.SAVING_PROFILE);
        String text = json.prettyPrint(json.toJson(profileProperties));
        writeProfileToStorage(profileName, text, true);
    }

    public void loadProfile() {
        if (isNewProfile) {
            notify(this, ProfileObserver.ProfileEvent.CLEAR_CURRENT_PROFILE);
            saveProfile();
        }

        String fullProfileFileName = profileName + SAVEGAME_SUFFIX;
        if (!Gdx.files.local(fullProfileFileName).exists()) {
            return;
        }

        FileHandle encodedFile = profiles.get(profileName);
        String decodedFile = Base64Coder.decodeString(encodedFile.readString());
        profileProperties = json.fromJson(ObjectMap.class, decodedFile);
        notify(this, ProfileObserver.ProfileEvent.PROFILE_LOADED);
        isNewProfile = false;
    }

    public void setCurrentProfile(String profileName) {
        this.profileName = doesProfileExist(profileName) ? profileName : DEFAULT_PROFILE;
    }
}
