import bot.collections.Help;
import bot.log.Log;
import bot.botType.Message;
import bot.functions.Sender;
import bot.functions.FileDownloader;
import bot.functions.FileUploader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import bot.botType.Command;
import bot.collections.Commands;

public class ImageUploader
{
	/**
	 * Carica il comando nel bot
	 */
	public void load()
	{
		Commands.addCommand(new Command("img", "ImageUploader.jar", "ImageUploader", "upload"));
		Help.addHelp("img", "Get random image: %0A/img random%0AGet nasa pic of the day:%0A/img nasa");
	}

	/**
	 * Quando viene invocato il comando, sceglie una delle 3 opzioni oppure se non riconosce le opzioni non fa nulla
	 *
	 * @param message
	 */
	public static void upload(Message message)
	{
		String param[] = message.getText().split(" ");
		if (param.length > 1)
		{
			//FUNZIONE NASA
			if (param[1].equals("nasa"))
			{
				if ((ImageUploader.nasa(message)) == null)
				{
					Log.error("Errore funzione nasa");
					if (message.getChat().getType().equals("group"))
						Sender.sendMessage(message.getChat().getId(), "Error function nasa", message.getMessageId());
					else
						Sender.sendMessage(message.getChat().getId(), "Error function nasa");
				}
			}


			//FUNZIONE RANDOM
			else if (param[1].equals("random"))
			{
				if ((ImageUploader.random(message)) == null)
				{
					Log.error("Errore funzione random");
					if (message.getChat().getType().equals("group"))
						Sender.sendMessage(message.getChat().getId(), "Errore funzione random", message.getMessageId());
					else
						Sender.sendMessage(message.getChat().getId(), "Errore funzione random");
				}
			}
			//SE NON RICONOSCE IL COMANDO
			else if (message.getChat().getType().equals("group"))
				Sender.sendMessage(message.getChat().getId(), "Not enough parameters(valid params: nasa, random)", message.getMessageId());
			else
				Sender.sendMessage(message.getChat().getId(), "Not enough parameters(valid params: nasa, random)");
		}
	}

	/**
	 * Scarica l'immagine astronomica del giorno e la invia
	 *
	 * @param message
	 */
	public static String nasa(Message message)
	{
		ArrayList<String> apod = getNasaPic();
		if (apod.get(0) != null && apod.get(0) != null)
		{
			//DOWNLOAD
			File file = new File(""+FileDownloader.downloadFile(apod.get(0)));

			Log.info("Download immagine Apod effettuato");

			//Upload
			FileUploader.uploadFile(file, message.getChat().getId(), FileUploader.FileType.PHOTO);

			Log.info("Caricamento effettuato");
			return apod.get(1);
		}
		return null;
	}

	/**
	 * Scarica un immagine random del web e la invia
	 *
	 * @param message
	 */
	public static String random(Message message)
	{
		ArrayList<String> random = getRandomPic();
		if (random.get(0) != null && random.get(0) != null)
		{
			//DOWNLOAD
			File file = new File(""+FileDownloader.downloadFile(random.get(0)));
			Log.info("Download immagine Random effettuato");

			//UPLOAD
			FileUploader.uploadFile(file, message.getChat().getId(), FileUploader.FileType.PHOTO);
			Log.info("Caricamento effettuato");
			return random.get(1);
		}
		return null;
	}


	/**
	 * Ottiene il link dell'immagine nasa del giorno
	 *
	 * @return ArrayList - at 0 there is link to image, at 1 there is name of file
	 */
	//Ottiene il link della pic del giorno della nasa andando ad analizzare il sito in html sfruttando il fatto che la pagina web ha una sola immagine
	public static ArrayList<String> getNasaPic()
	{
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line = "";

		try
		{
			url = new URL("http://apod.nasa.gov/apod/astropix.html");
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			boolean stop = false;

			//array contenente le singole linee della pagina
			while (stop == false)
			{
				line = br.readLine();
				if (line.length() > 4)
				{
					if (line != null && line.charAt(1) == 'I' && line.charAt(2) == 'M' && line.charAt(3) == 'G')
						stop = true;
				}
			}
			if (line.length() > 10)
			{
				line = line.substring(10);
				line = line.substring(0, line.length() - 1);
				line = "http://apod.nasa.gov/apod/" + line;
				//cerca il link dell'immagine
				ArrayList<String> apod = new ArrayList<String>(10);
				apod.add(line);
				String name[] = line.split("apod");
				apod.add(name[2].split("/")[3]);
				return apod;
			}
			else
				return null;

		}
		catch (MalformedURLException mue)
		{
			mue.printStackTrace();
			return new ArrayList<String>(0);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return new ArrayList<String>(0);
		}
	}

	/**
	 * Ottiene il link dell'immagine random
	 *
	 * @return ArrayList - at 0 there is link to image, at 1 there is name of file
	 */
	public static ArrayList<String> getRandomPic()
	{
		ArrayList<String> picRandom = new ArrayList<String>(10);
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line = "";

		try
		{
			url = new URL("http://www.funcage.com/?");
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			int contatore = 0;

			//array contenente le singole linee della pagina
			while (contatore < 56)
			{
				contatore++;
				line = br.readLine();
			}
			if (line.length() > 10)
			{
				String name = line.split("photos")[1];
				name = name.split("\"")[0];
				picRandom.add("http://www.funcage.com/photos" + name);
				picRandom.add(name.split("/")[1]);
				return picRandom;
			}
			else
			{
				return null;
			}
		}
		catch (MalformedURLException mue)
		{
			mue.printStackTrace();
			return null;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return null;
		}

	}
}
