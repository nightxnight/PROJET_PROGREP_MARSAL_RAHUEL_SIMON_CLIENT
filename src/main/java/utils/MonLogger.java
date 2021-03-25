package utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MonLogger {

    /*
     * Permet de recenser tous les loggeurs cree lorsqu'on utilise la methode : `getLoggerParNom`
     */
    private static ArrayList<String> listeNomLoggerCreer = new ArrayList<String>();

    private Logger logger;
    private FileHandler fileHandler;

    private static final DateTimeFormatter dateFormatteur = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");

    public MonLogger(String nomLogger, String nomDossier, String nomFichier) throws SecurityException, IOException{
        this.logger = Logger.getLogger(nomLogger);

        File fichierLog = new File("logs/" + nomDossier + "/" + dateFormatteur.format(LocalDateTime.now()) + " " + nomFichier + ".log");
        fichierLog.createNewFile();

        this.fileHandler = new FileHandler(fichierLog.getPath(), true);
        this.fileHandler.setFormatter(new SimpleFormatter());
        this.logger.addHandler(fileHandler);

        this.logger.setLevel(Level.INFO);
        this.logger.setUseParentHandlers(false);

        listeNomLoggerCreer.add(nomLogger);
    }

    private MonLogger(Logger logger) {
        this.logger = logger;
    }

    public static MonLogger getLoggerParNom(String nomLogger) throws IllegalArgumentException {
        if(!listeNomLoggerCreer.contains(nomLogger)) throw new IllegalArgumentException("Ce logger n'existe pas.");
        else return new MonLogger(Logger.getLogger(nomLogger));
    }

    public void log(Level level, String message) {
        this.logger.log(level, message);
    }
}
