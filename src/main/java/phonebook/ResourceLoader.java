package phonebook;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class ResourceLoader {

    private final String[] resourceNames={
            "logo",
            "add",
            "delete",
            "edit",
            "find",
            "telegram",
            "vk",
            "ok",
            "facebook",
            "twitter",
            "mail"
    };

    private final String resourcePrefix = "images/";
    private final String resourceSuffix = ".png";

    private final HashMap<String, Image> resourceMap = new HashMap<>();

    public void loadResource() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Image image;
        for (String name: resourceNames){
            image = ImageIO.read(classLoader.getResourceAsStream(resourcePrefix + name + resourceSuffix));
            resourceMap.put(name, image);
        }
    }

    public Image getResource(String name){
        return resourceMap.get(name);
    }

}
