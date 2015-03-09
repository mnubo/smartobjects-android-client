## Available http operations (used internally to perform request to the platform)

The SDK allows theses operations :
* UsersSdkOperations
  * `void delete(String username);`
  * `void update(String username, User user);`
  * `void updatePassword(String username, UpdatePassword password);`
  * `User getUser();` // TO IMPLEMENT ON THE PLATFORM
  * `User getUser(String username);`
  * `User getUser(String username, List<String> attributes);`
  * `UserObjects findUserObjects(String username, boolean details, String model, boolean showHistory);`
* ObjectsSdkOperations
  * `void delete(SdkId objectId);`
  * `UserObject create(UserObject object, boolean updateIfExists);`
  * `void update(SdkId objectId, UserObject object);`
  * `UserObject findOne(SdkId objectId, List<String> attributes);`
  * `User findOne(String username, List<String> attributes);`
  * `void addSamples(SdkId objectId, Samples samples);`
  * `Users listOwnersHistory(SdkId objectid, boolean details);`
  * `Samples searchSamples(SdkId objectId, String sensorName, ValueType value,
                               String from, String to, int resultSizeLimit);`
* GroupsSdkOperations
  * `void delete(SdkId groupId);`
  * `Group create(Group group);`
  * `Group findOne(SdkId groupId);`
  * `Users listAllUsers(SdkId groupId, int resultSizeLimit);`
  * `void addUser(SdkId groupId, String username);`
  * `void removeUser(SdkId groupId, String username);`
* CollectionsSdkOperations
  * `void delete(SdkId collectionId);`
  * `Collection create(Collection collection);`
  * `Collection findOne(SdkId collectionId);`
  * `UserObjects listAllObjects(SdkId collectionId, int resultSizeLimit);`
  * `void addObject(SdkId collectionId, SdkId objectId);`
  * `void removeObject(SdkId collectionId, SdkId objectId);`
* ClientSdkOperations //available when user is not logged in
  * `User create(User user);`
  * `void confirmUserCreation(String username, UserToken token);`
  * `void resetPassword(String username, String password);`
  * `void confirmPasswordReset(String username, String password);`