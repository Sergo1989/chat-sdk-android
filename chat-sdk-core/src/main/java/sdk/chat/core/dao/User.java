package sdk.chat.core.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sdk.chat.core.base.AbstractEntity;
import sdk.chat.core.events.NetworkEvent;
import sdk.chat.core.interfaces.UserListItem;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.ConnectionType;
import sdk.chat.core.utils.StringChecker;


// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS
// KEEP INCLUDES - put your token includes here

@Entity
public class User extends AbstractEntity implements UserListItem {

    @Id
    private Long id;
    private String entityID;
    private Date lastOnline;
    private Boolean isOnline;

    @ToMany(referencedJoinProperty = "userId")
    private List<UserMetaValue> metaValues;
    
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;


    @Generated(hash = 1471069357)
    public User(Long id, String entityID, Date lastOnline, Boolean isOnline) {
        this.id = id;
        this.entityID = entityID;
        this.lastOnline = lastOnline;
        this.isOnline = isOnline;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public List<User> getContacts() {
        return getContacts(ConnectionType.Contact);
    }

    public List<User> getContacts(ConnectionType type) {
        List<User> contactList = new ArrayList<>();

        // For some reason the default ContactLinks do not persist, have to find in DB
        List<ContactLink> contactLinks = DaoCore.fetchEntitiesWithProperty(ContactLink.class,
                ContactLinkDao.Properties.LinkOwnerUserDaoId, this.getId());

        for (ContactLink contactLink : contactLinks){
            if (contactLink.getConnectionType().equals(type)) {
                User user = contactLink.getUser();
                if (user != null) {
                    contactList.add(contactLink.getUser());
                }
            }
        }

        return contactList;
    }

    public void addContact(User user, ConnectionType type) {
        addContact(user, type, true);
    }

    public void addContact(User user, ConnectionType type, boolean notify) {
        if (!user.isMe()) {
            // Retrieve contacts
            List contacts = getContacts(type);

            if (!contacts.contains(user)) {
                ContactLink contactLink = new ContactLink();
                contactLink.setConnectionType(type);
                // Set link owner
                contactLink.setLinkOwnerUser(this);
                contactLink.setLinkOwnerUserDaoId(this.getId());
                // Set contact
                contactLink.setUser(user);
                contactLink.setUserId(user.getId());
                // insert contact link entity into DB
                daoSession.insertOrReplace(contactLink);

                if (notify) {
                    ChatSDK.events().source().accept(NetworkEvent.contactAdded(user));
                }
            }
        }
    }

    public void deleteContact(User user) {
        deleteContact(user, ConnectionType.Contact);
    }

    public void deleteContact(User user, ConnectionType type) {
        deleteContact(user, type, true);
    }

    public void deleteContact(User user, ConnectionType type, boolean notify) {
        if (!user.isMe()) {
            List contacts = getContacts(type);
            if (contacts.contains(user)) {
                Property [] properties = {
                        ContactLinkDao.Properties.LinkOwnerUserDaoId,
                        ContactLinkDao.Properties.UserId,
                        ContactLinkDao.Properties.Type
                };

                List<ContactLink> contactLinks = DaoCore.fetchEntitiesWithProperties(ContactLink.class,
                        properties, this.getId(), user.getId(), type.ordinal());

                for(ContactLink link : contactLinks) {
                    ChatSDK.db().delete(link);
                }

                if (notify) {
                    ChatSDK.events().source().accept(NetworkEvent.contactDeleted(user));
                }
            }
        }
    }

    public void addContact(User user) {
        addContact(user, ConnectionType.Contact);
    }

    public void setAvatarURL(String imageUrl, String hash) {
        setAvatarURL(imageUrl, hash, true);
    }

    public void setAvatarURL(String imageUrl, String hash, boolean notify) {
        setAvatarURL(imageUrl, notify);
        setAvatarHash(hash);
    }

    public void setAvatarURL(String imageUrl) {
        setAvatarURL(imageUrl, true);
    }

    public void setAvatarURL(String imageUrl, boolean notify) {
        setMetaString(Keys.AvatarURL, imageUrl, notify);
    }

    public void setHeaderURL(String imageUrl) {
        setHeaderURL(imageUrl, true);
    }

    public void setHeaderURL(String imageUrl, boolean notify) {
        setMetaString(Keys.HeaderURL, imageUrl, notify);
    }

    public String getHeaderURL() {
        return metaStringForKey(Keys.HeaderURL);
    }

    public void setPresenceSubscription(String presence) {
        setPresenceSubscription(presence, true);
    }

    public void setPresenceSubscription(String presence, boolean notify) {
        setMetaString(Keys.PresenceSubscription, presence, notify);
    }

    public String getPresenceSubscription() {
        return metaStringForKey(Keys.PresenceSubscription);
    }

    public String getAvatarURL() {
        String url = metaStringForKey(Keys.AvatarURL);
        if (StringChecker.isNullOrEmpty(url)) {
            return ChatSDK.ui().getAvatarGenerator().getAvatarURL(this);
        }
        return url;
    }

    public void setAvatarHash(String hash) {
        setMetaString(Keys.AvatarHash, hash, false);
    }

    public String getAvatarHash() {
        return metaStringForKey(Keys.AvatarHash);
    }

    public void setName(String name) {
        setName(name, true);
    }

    public void setName(String name, boolean notify) {
        setMetaString(Keys.Name, name, notify);
    }

    public String getName() {
        String name = metaStringForKey(Keys.Name);
        return name;
//        return getName(true);
    }

//    public String getName(boolean orId) {
//        String name = metaStringForKey(Keys.Name);
//        if (orId && (name == null || name.isEmpty())) {
//            name = getEntityID();
//        }
//        return name;
//
//    }

    public void setEmail(String email) {
        setEmail(email, true);
    }

    public void setEmail(String email, boolean notify) {
        setMetaString(Keys.Email, email, notify);
    }

    public String getEmail() {
        return metaStringForKey(Keys.Email);
    }

    public void setStatus(String status) {
        setStatus(status, true);
    }

    public void setStatus(String status, boolean notify) {
        setMetaString(Keys.Status, status, notify);
    }

    public String getStatus() {
        return metaStringForKey(Keys.Status);
    }

    public String getPhoneNumber() {
        return metaStringForKey(Keys.Phone);
    }

    public void setPhoneNumber(String phoneNumber) {
        setPhoneNumber(phoneNumber, true);
    }

    public void setPhoneNumber(String phoneNumber, boolean notify) {
        setMetaString(Keys.Phone, phoneNumber, notify);
    }

    public void setAvailability(String availability) {
        setAvailability(availability, true);
    }

    public void setAvailability(String availability, boolean notify) {
        if (setMetaString(Keys.Availability, availability, false) && notify) {
            ChatSDK.events().source().accept(NetworkEvent.userPresenceUpdated(this));
        }
    }

    public Boolean getIsOnline() {
        if (this.isOnline != null) {
            return this.isOnline;
        }
        return false;
    }

    public String getState() {
        return metaStringForKey(Keys.State);
    }

    public void setState(String state) {
        setState(state, true);
    }

    public void setState(String state, boolean notify) {
        setMetaString(Keys.State, state, notify);
    }

    public String getAvailability() {
        if (!getIsOnline()) {
            return null;
        }
//        String availability = metaStringForKey(Keys.Availability);
//        if (availability != null) {
//            return availability;
//        } else {
//            return Availability.Available;
//        }
        return metaStringForKey(Keys.Availability);
    }

    public String getLocation() {
        return metaStringForKey(Keys.Location);
    }

    public void setLocation(String location) {
        setLocation(location, true);
    }

    public void setLocation(String location, boolean notify) {
        setMetaString(Keys.Location, location, notify);
    }

    public String metaStringForKey(String key) {
        return metaMap().get(key);
    }

    public Boolean metaBooleanForKey(String key) {
        return metaValueForKey(key) != null && metaValueForKey(key).getValue().toLowerCase().equals("true");
    }

    public void setMetaString(String key, String value) {
        setMetaValue(key, value, true);
    }

    public boolean setMetaString(String key, String value, boolean notify) {
        return setMetaValue(key, value, notify);
    }

    /**
     * Setting the metaData, The Map will be converted to a Json String.
     **/
    public void setMetaMap(Map<String, String> metadata) {
        setMetaMap(metadata, true);
    }

    public void setMetaMap(Map<String, String> meta, boolean notify) {
        if (meta != null && !metaMap().entrySet().equals(meta.entrySet())) {
            for (String key : meta.keySet()) {
                setMetaValue(key, meta.get(key), false);
            }
            if (notify) {
                ChatSDK.events().source().accept(NetworkEvent.userMetaUpdated(this));
            }
        }
    }

    /**
     * Converting the metaData json to a map object
     **/
    public Map<String, String> metaMap() {
        HashMap<String, String> map = new HashMap<>();

        for(UserMetaValue v : getMetaValues()) {
            map.put(v.getKey(), v.getValue());
        }

        return map;
    }

    public void setMetaValue(String key, String value) {
        setMetaValue(key, value, true);
    }

    @Keep
    public boolean setMetaValue(String key, String value, boolean notify) {
        if (value != null) {
            UserMetaValue metaValue = metaValueForKey(key);

            if (metaValue == null || metaValue.getValue() == null || !metaValue.getValue().equals(value)) {
                if (metaValue == null) {
                    metaValue = ChatSDK.db().createEntity(UserMetaValue.class);
                    metaValue.setUserId(this.getId());
                    getMetaValues().add(metaValue);
                }

                metaValue.setValue(value);
                metaValue.setKey(key);

                metaValue.update();
                update();

                if (notify) {
                    ChatSDK.events().source().accept(NetworkEvent.userMetaUpdated(this));
                }
                return true;
            }
        }
        return false;
    }

    @Keep
    public UserMetaValue metaValueForKey(String key) {
        return MetaValueHelper.metaValueForKey(key, getMetaValues());
    }

    public boolean hasThread(Thread thread){
        UserThreadLink data = DaoCore.fetchEntityWithProperties(
                UserThreadLink.class,
                new Property[] {UserThreadLinkDao.Properties.ThreadId, UserThreadLinkDao.Properties.UserId},
                thread.getId(),
                getId()
        );

        return data != null;
    }

    public String getPushChannel() {
        return entityID;
    }

    public boolean isMe() {
        return equalsEntity(ChatSDK.currentUser());
    }

    public String toString() {
        return String.format("User, id: %s meta: %s", id, metaMap().toString());
    }

    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getEntityID() {
        return this.entityID;
    }


    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public java.util.Date getLastOnline() {
        return this.lastOnline;
    }


    public void setLastOnline(java.util.Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    /**
     * To-many relationship, resolved on first access(and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1849453054)
    public List<UserMetaValue> getMetaValues() {
        if (metaValues == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserMetaValueDao targetDao = daoSession.getUserMetaValueDao();
            List<UserMetaValue> metaValuesNew = targetDao._queryUser_MetaValues(id);
            synchronized (this) {
                if (metaValues == null) {
                    metaValues = metaValuesNew;
                }
            }
        }
        return metaValues;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 365870950)
    public synchronized void resetMetaValues() {
        metaValues = null;
    }

    public void setIsOnline(Boolean isOnline) {
        setIsOnline(isOnline, true);
    }

    public void setIsOnline(Boolean isOnline, boolean notify) {
        if (this.isOnline != isOnline) {
            this.isOnline = isOnline;
            update();
            if (notify) {
                ChatSDK.events().source().accept(NetworkEvent.userPresenceUpdated(this));
            }
        }
    }

    public static List<User> convertIfPossible(List<UserListItem> items) {
        List<User> users = new ArrayList<>();
        for (UserListItem item: items) {
            if (item instanceof User) {
                users.add((User) item);
            }
        }
        return users;
    }

}
