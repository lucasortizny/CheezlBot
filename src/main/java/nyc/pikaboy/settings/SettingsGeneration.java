package nyc.pikaboy.settings;

import com.google.gson.Gson;
import nyc.pikaboy.Main;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * This class will be responsible for generating the Settings files and checking for validation.
 */
public class SettingsGeneration {

    public static boolean checkValidity(Gson gson, String settingDestination){
        try {
            File fileopen = new File(settingDestination);
            FileReader readFile = new FileReader(fileopen);
            Settings attemptedSettings = gson.fromJson(readFile, Settings.class);
            readFile.close();
            return true;

        } catch (Exception e){
            Main.logger.error("Unable to read file. Check file permissions or destination.");
            e.printStackTrace();
            return false;

        }
    }

    public static boolean createNewSettings(Gson gson, String settingDestination){
        try {
            File fileopen = new File(settingDestination);
            fileopen.delete();
            // At this point original file will be deleted. Generate new file off blank Settings.json.
            Settings blankSettings = new Settings();
            FileWriter fileWriter = new FileWriter(fileopen);
            fileWriter.write(gson.toJson(blankSettings));
            fileWriter.close();
            return true;


        } catch (Exception e){
            Main.logger.error("Unable to use file. Check file permissions or destination.");
            e.printStackTrace();
            return false;
        }

    }

    public static Settings getExistingSettings(Gson gson, String settingDestination){
        try {
            FileReader readFile = new FileReader(new File(settingDestination));
            Settings retSetting = gson.fromJson(readFile, Settings.class);
            readFile.close();
            return retSetting;
        } catch (Exception e){
            Main.logger.error("Unable to get existing settings (is it corrupted?) Generating new settings file...");
            createNewSettings(gson, settingDestination);
            return new Settings();
        }
    }
    public static boolean saveExistingSettings(Gson gson, String settingDestination, Settings settings){
        try {
            FileWriter fileWriter = new FileWriter(new File(settingDestination));
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
            return true;
        } catch (Exception e){
            Main.logger.error("Unable to save existing settings. Check file perms.");
            e.printStackTrace();
            return false;
        }
    }


}
