import bot.botType.Command;
import bot.botType.WaitingCommand;
import bot.collections.Commands;
import bot.collections.Help;

/**
 * Created by Paolo on 09/11/2015.
 */
public class Main implements addons.Addon
{
	/**
	 * Carica il comando nel bot
	 */
	public void load()
	{
		WaitingCommand img = new WaitingCommand("img", "ImageUploader.jar", "ImageUploader", "upload");
		img.addParam("random");
		img.addParam("nasa");
		Commands.addCommand(img);
		Help.addHelp("img", "Get random image: %0A/img random%0AGet nasa pic of the day:%0A/img nasa");
	}
}
