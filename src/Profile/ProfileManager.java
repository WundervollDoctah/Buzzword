package Profile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import BuzzScene.Home;
import components.AppDataComponent;
import components.AppFileComponent;

//@author Jeremy Chu

public class ProfileManager implements AppFileComponent{
	
	private Profile loadedProfile;
	private static ProfileManager profileManager = null;
	private final char[] ENCRYPT_ALPHABET = new String("0123456789abcdefghijklmnopqrstuvwxyz").toCharArray();
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String GAMEMODE = "gamemode";
	
	public ProfileManager(){
		loadedProfile = null;
	}
	
	public boolean createData(String username, String password) throws IOException{
		// TODO
		JsonFactory factory = new JsonFactory();
		System.out.println(username);
		String encryptedUser = runBestestEncryption(username);
		String encryptedPass = runAmazingEncryption(password);
		
		System.out.println(encryptedPass);
		File f = new File(encryptedUser + ".json");
		if(!f.exists()){
			try(JsonGenerator generator = factory.createGenerator(new FileWriter(f))){
				generator.writeStartObject();
				generator.useDefaultPrettyPrinter();
				generator.writeFieldName(USERNAME);
				generator.writeString(encryptedUser);
				generator.writeFieldName(PASSWORD);
				generator.writeString(encryptedPass);
				generator.writeFieldName(GAMEMODE);
				generator.writeStartObject();
				for(int i = 1; i < Home.GAMEMODES.length; i++){
					generator.writeFieldName(Home.GAMEMODES[i]);
					generator.writeNumber(1);
				}
				generator.writeEndObject();
				generator.writeEndObject();
			}
			loadedProfile = new Profile();
			loadedProfile.username = username;
			loadedProfile.password = password;
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public void saveData(AppDataComponent data, Path filePath) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public boolean deleteProfile(String username){
		String encryptedUser = runBestestEncryption(username);
		File f = new File(encryptedUser + ".json");
		if(f.exists()){
			f.delete();
			return true;
		}
		return false;
	}
	
	public boolean renameProfile(String username) throws IOException{
		JsonFactory factory = new JsonFactory();
		System.out.println(username);
		String encryptedUser = runBestestEncryption(username);
		String encryptedPass = runAmazingEncryption(loadedProfile.password);
		
		System.out.println(encryptedPass);
		System.out.println(encryptedUser);
		File f = new File(encryptedUser + ".json");
		if(!f.exists()){
			try(JsonGenerator generator = factory.createGenerator(new FileWriter(f))){
				generator.writeStartObject();
				generator.useDefaultPrettyPrinter();
				generator.writeFieldName(USERNAME);
				generator.writeString(encryptedUser);
				generator.writeFieldName(PASSWORD);
				generator.writeString(encryptedPass);
				generator.writeFieldName(GAMEMODE);
				generator.writeStartObject();
				for(int i = 1; i < Home.GAMEMODES.length; i++){
					generator.writeFieldName(Home.GAMEMODES[i]);
					generator.writeNumber(loadedProfile.getGamemodeProgress(Home.GAMEMODES[i]));
				}
				generator.writeEndObject();
				generator.writeEndObject();
			}
			loadedProfile.username = username;
			String deleteName = runBestestEncryption(loadedProfile.getUsername());
			System.out.println(deleteName);
			new File(deleteName + ".json").delete();
			return true;
		}
		else
			return false;
	}
	
	public void saveProfile(String username, String password) throws IOException{
		JsonFactory factory = new JsonFactory();
		System.out.println(username);
		String encryptedUser = runBestestEncryption(username);
		String encryptedPass = runAmazingEncryption(password);
		
		System.out.println(encryptedPass);
		File f = new File(encryptedUser + ".json");
		try(JsonGenerator generator = factory.createGenerator(new FileWriter(f))){
			generator.writeStartObject();
			generator.useDefaultPrettyPrinter();
			generator.writeFieldName(USERNAME);
			generator.writeString(encryptedUser);
			generator.writeFieldName(PASSWORD);
			generator.writeString(encryptedPass);
			generator.writeFieldName(GAMEMODE);
			generator.writeStartObject();
			for(int i = 1; i < Home.GAMEMODES.length; i++){
				generator.writeFieldName(Home.GAMEMODES[i]);
				generator.writeNumber(loadedProfile.getGamemodeProgress(Home.GAMEMODES[i]));
			}
			generator.writeEndObject();
			generator.writeEndObject();
		}
		loadedProfile.username = username;
		loadedProfile.password = password;
	}
	
	public boolean loadProfile(String username, String password) throws IOException, ProfileException {
		String encryptedUser = runBestestEncryption(username);
		System.out.println(encryptedUser);
		File f = new File(encryptedUser + ".json");
		loadedProfile = new Profile();
		boolean profileSet = false;
		if(f.exists()){			
			JsonFactory factory = new JsonFactory();
			try(JsonParser parser = factory.createParser(new FileReader(f))){
				while(!parser.isClosed()){
					JsonToken token = parser.nextToken();
	                if(token == null){
	                    break;
	                }
	                if(JsonToken.FIELD_NAME.equals(token) && USERNAME.equals(parser.getCurrentName())){
	                    parser.nextToken();
	                    System.out.println(parser.getText());
	                    if(runBestestEncryption(username).equals(parser.getText())){
	                    	loadedProfile.username = username;
	                    	profileSet = true;
	                    }
	                    else
	                    	break;
	                }
	                if(JsonToken.FIELD_NAME.equals(token) && PASSWORD.equals(parser.getCurrentName())){
	                    parser.nextToken();
	                    System.out.println(parser.getText());
	                    if(runAmazingEncryption(password).equals(parser.getText())){
	                    	loadedProfile.password = password;
	                    	profileSet = true;
	                    }
	                    else
	                    	break;
	                }
	                if(JsonToken.FIELD_NAME.equals(token) && GAMEMODE.equals(parser.getCurrentName())){
	                    token = parser.nextToken();
	                    if(!JsonToken.START_OBJECT.equals(token)){
	                        break;
	                    }
	                    token = parser.nextToken();
	                    while(!JsonToken.END_OBJECT.equals(token)) {
	                    	if(JsonToken.FIELD_NAME.equals(token) && loadedProfile.gamemodeLevels.containsKey(parser.getText())){
	                    		String key = parser.getText();
	                    		token = parser.nextToken();
	                    		loadedProfile.gamemodeLevels.replace(key, (Integer) parser.getNumberValue());
	                   		}
	                    	token = parser.nextToken();
	                    }
	                }
				}
			}
		}
		if(!profileSet){
			loadedProfile = null;
			throw new ProfileException("invalid username or password");
		}
		return profileSet;
	}

	@Override
	public void loadData(AppDataComponent data, Path filePath) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportData(AppDataComponent data, Path filePath) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	private String runAmazingEncryption(String password){ //WAUUU
		String encryptedPass = "";
		int hashValue = 0;
		for(int i = 0; i < password.length(); i++)
			hashValue += password.charAt(i);
		for(int i = 0; i < password.length(); i++){
			int encryptValue = password.charAt(i)*hashValue;
			while(encryptValue > 0){
				int currentVal = 1;
				int tempEncryptVal = encryptValue;
				int itterationCounter = 1;
				while(tempEncryptVal > 0){
					currentVal += Math.max(tempEncryptVal%10, 1) * itterationCounter + itterationCounter;
					tempEncryptVal /= 10;
					itterationCounter++;
				}
				encryptedPass += ENCRYPT_ALPHABET[currentVal % 36];
				encryptValue -= currentVal;
			}
		}
		return encryptedPass;
	}
	
	public String runBestestEncryption(String username){
		String encryptedUser = "";
		int hashValue = 0;
		for(int i = 0; i < username.length(); i++)
			hashValue += username.charAt(i);
		for(int i = 0; i < username.length(); i++){
			int encryptValue = username.charAt(i) * hashValue;
			while(encryptValue > 0){
				encryptedUser += ENCRYPT_ALPHABET[(int)((Math.abs(encryptValue / 100.0 - 1.0) * 100) % 36)];
				encryptValue /= 100;
			}
		}
		return encryptedUser;
		
	}

	public Profile getLoadedProfile() {
		return loadedProfile;
	}
	
	public static ProfileManager getProfileManager(){
		if(profileManager == null)
			profileManager = new ProfileManager();
		return profileManager;
	}
}
