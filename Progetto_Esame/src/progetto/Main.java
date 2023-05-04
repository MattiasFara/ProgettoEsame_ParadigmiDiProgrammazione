package progetto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import jbook.util.Input;

/**
 * Classe che permette di lavorare con la classe Agenda interagendo.
 */

public class Main {

	private static Agenda agenda;
	
	private static String nomeFile = "agenda.txt";
	
	private static ArrayList<Appuntamento> currentList;
	
	private static boolean cercaAttiva = false;
	
	/**
	 * Main del programma
	 * @param args argomenti passati dalla linea dicomandi, non necessari
	 */
	public static void main(String ...args) {
		
		try {
			init();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		while(true) {
			
			printList(currentList);
			
			try {
				switch(inputCampo("Opzioni:\n1)Aggiungi un appuntamento\n2)Rimuovi un appuntamento\n3)Modifica un appuntamento\n4)Cerca nella agenda\n5)Esci\n>>> ")) {
				case "1":
					// aggiungi
					System.out.println("Aggiungi un appuntamento: ");
					try {
						if(aggiungi()) {
							System.out.println("Aggiunta con successo");
							saveAgendaOnFile();
							currentList = agenda.getList();
						} else {
							System.out.println("Aggiunta fallita");
						} 
					} catch(UndoOperationException | DateTimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				case "2":
					// rimuovi
					try {
						if(rimuovi()) {
							System.out.println("Appuntamento rimosso con successo");
							saveAgendaOnFile();
							currentList = agenda.getList();
						} else {
							System.out.println("Nessun appuntamento è stato rimosso");
						}
					} catch(UndoOperationException e) {
						System.out.println(e.getMessage());
					}
					break;
				case "3":
					// modifica
					System.out.println("Modifica un appuntamento");
					try {
						if(modifica()) {
							System.out.println("Modifica avvenuta");
							saveAgendaOnFile();
							currentList = agenda.getList();
						} else {
							System.out.println("Modifica non avvenuta");
						}
					} catch(UndoOperationException | DateTimeException e) {
						System.out.println(e.getMessage());
						break;
					}
					break;
				case "4":
					// cerca
					try {
						if (!cercaAttiva) {
							currentList = cerca();
							cercaAttiva = true;
						} else {
							currentList = agenda.getList();
							cercaAttiva = false;
						}
					} catch(UndoOperationException | DateTimeException e) {
						System.out.println(e.getMessage());
					}
					break;
				case "5":
					// salva ed esci
					saveAgendaOnFile();
					System.exit(1);
					break;
				default:
					System.out.println("Input non valido");
					break;
				}
			} catch(UndoOperationException e) {
				System.exit(1);
			}
		}
	}
	
	/**
	 * Inizializza l'applicazione.
	 * <p>
	 * Inizializza il campo agenda chiamando il costruttore di Agenda, apre/crea il file per la letture/scrittura e carica, leggendo il file, la agenda, poi carica la lista operativa con la agenda.
	 * @throws IOException se &egrave; presente un'errore con l'appertura del file
	 */
	private static void init() throws IOException {
		agenda = new Agenda();
		File f = new File(nomeFile);
		if(!f.exists()) f.createNewFile();
		try(BufferedReader in = new BufferedReader(new FileReader(f))){
			loadAgendaFromFile(in);
		}
		currentList = agenda.getList();
	}
	
	/**
	 * Carica l'agenda, leggendo da file.
	 * <p>
	 * Carica l'agenda usando i dati letti dal file passato come argomento.
	 * @param in file per la lettura
	 * @throws IOException se &egrave; presente un errore con la lettura del file
	 */
	private static void loadAgendaFromFile(BufferedReader in) throws IOException {
		String l;
		String[] s = new String[5];
		while((l = in.readLine()) != null) {
			s = l.split(",", 5);
			try {
				agenda.add(new Appuntamento(LocalDate.parse(s[0].trim(), Appuntamento.getDataFormatter()), LocalTime.parse(s[1].trim(), Appuntamento.getOrarioFormatter()), Integer.parseInt(s[2].trim()), s[3].trim(), s[4].trim()));
			} catch (NumberFormatException | DateTimeParseException e) {
				System.err.println(e.getMessage());
				System.out.println("Controllare il file di lettura: " + nomeFile);
				System.exit(1);
			}
		}
	}
	
	/**
	 * Salva l'agenda su file.
	 */
	private static void saveAgendaOnFile() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(nomeFile));
			for(Appuntamento a: agenda) {
				out.println(a.toString());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * Stampa la lista a schermo.
	 * <p>
	 * Stampa la lista, passata come argomento, ordinata per data a schermo
	 */
	private static void printList(ArrayList<Appuntamento> list) {
		int index = 1;
		// Ordina la lista
		Collections.sort(list, new Comparator<Appuntamento>() {
			@Override
			public int compare(Appuntamento a, Appuntamento b) {
				return a.getData().atTime(a.getOrario()).compareTo(b.getData().atTime(b.getOrario()));
			}
		});
		for (Appuntamento a : list) {
			System.out.println(Integer.toString(index) + ") " + a.toString());
			index++;
		}
	}

	/**
	 * Aggiunge un appuntamento alla agenda.
	 * <p>
	 * Metodo per l'aggiunta di un nuovo appuntamento generato con generaAppuntamento prendondo i valori dei campi da input da tastiera
	 * @return true se aggiunto correttamente, false altrimenti
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 * @throws DateTimeException se la data o l'orario inseriti in input non sono adeguati per le caratteristiche di LocalDate e LocalTime
	 */
	private static boolean aggiungi() throws UndoOperationException, DateTimeException{
		return agenda.add(generaAppuntamento());
	}
	
	/**
	 * Rimuove un elemento dall'agenda-
	 * @return true se rimosso, false altrimenti
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 */
	private static boolean rimuovi() throws UndoOperationException{
		while(true) {
			try{
				int index = inputCampoNum("Scegli l'indice dell'appuntamento da rimuovere: ");
				return agenda.remove(currentList.get(index-1));
			} catch(IndexOutOfBoundsException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Modifica un appuntamento.
	 * <p>
	 * Metodo che modifica un elemento nell'agenda sostituendolo con uno nuovo creato dai dati ottenuti in input da tastiera
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 * @throws DateTimeException se la data o l'orario inseriti in input non sono adeguati per le caratteristiche di LocalDate e LocalTime
	 */
	private static boolean modifica() throws UndoOperationException, DateTimeException{
		while(true) {
			try {
				int index = inputCampoNum("Scegli l'indice dell'appuntamento da modificare: ");
				Appuntamento appMod = generaAppuntamento();
				int indexApp = agenda.getList().indexOf(currentList.get(index-1));
				if (indexApp == -1) throw new UndoOperationException("L'appuntamento [" + currentList.get(index-1).toString() + "] non è presente nella agenda\nOperazione annullata");
				return agenda.modify(indexApp, appMod);
			} catch(IndexOutOfBoundsException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Cerca nell'agenda gli appuntamento di interesse.
	 * <p> 
	 * Si può scegliere il criterio di scelta tra: per nome, per data, tra date.
	 * @return ArrayList<Appuntamento> contenente gli elementi cercati
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 * @throws DateTimeException se la data o l'orario inseriti in input non sono adeguati per le caratteristiche di LocalDate e LocalTime
	 */
	private static ArrayList<Appuntamento> cerca() throws UndoOperationException, DateTimeException{
		ArrayList<Appuntamento> results = new ArrayList<Appuntamento>();
		// scegliere il criterio di ricerca
		switch(inputCampo("Scegli metodo di ricerca:\n1)Per nome\n2)Per data\n3)Tra due date\n>>> ")) {
		case "1":
			// NOME
			String nome = inputCampo("Inserisci il nome: ");
			results = agenda.search(agenda.getList(),
					nome,
					(String s, Appuntamento a) -> a.getPersona().startsWith(s));
			break;
		case "2":
			// GIORNO
			System.out.println("Cerca per giorno: ");
			LocalDate key = inputData();
			results = agenda.search(currentList,
					key,
					(LocalDate ldl, Appuntamento a) -> a.getData().getDayOfYear() == ldl.getDayOfYear() && a.getData().getYear() == ldl.getYear());
			break;
		case "3":
			// TRA DUE GIORNI
			System.out.println("Inersici il giorno di partenza: ");
			LocalDate dataInizio = inputData();
			System.out.println("Inserisci il giorno finale: ");
			LocalDate dataFine = inputData();
			results = agenda.search(agenda.search(agenda.getList(),
					dataInizio,
					(LocalDate ld, Appuntamento a) -> a.getData().isAfter(ld) || a.getData().isEqual(ld)),
					dataFine,
					(LocalDate ld, Appuntamento a) -> a.getData().isBefore(ld) || a.getData().isEqual(ld));
			break;
		default:
			System.out.println("Non esiste tale criterio di scelta");
			break;
		}
		return results;
	}
	
	/**
	 * Genera un nuovo appuntamento prendendo i dati in input da tastiera.
	 * @return Appuntamento appuntamento generato dai dati ottenuti da input da tastiera
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 * @throws DateTimeException se la data o l'orario inseriti in input non sono adeguati per le caratteristiche di LocalDate e LocalTime
	 */
	private static Appuntamento generaAppuntamento() throws UndoOperationException, DateTimeException{
		return new Appuntamento(inputData(), inputOrario(), inputCampoNum("Inserisci la durata: "), inputCampo("Inserisci il luogo: "), inputCampo("Inserisci la persona: "));
	}
	
	/**
	 * Prende in input tre stringhe e le converte in una data.
	 * @return La data ottenuta da input
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 * @throws DateTimeException se la data o l'orario inseriti in input non sono adeguati per le caratteristiche di LocalDate e LocalTime
	 */
	private static LocalDate inputData() throws UndoOperationException, DateTimeException{
		LocalDate newData = LocalDate.parse("1970-01-01");
		return newData.withDayOfMonth(inputCampoNum("Inserisci il giorno: ")).withMonth(inputCampoNum("Inserisci il mese: ")).withYear(inputCampoNum("Inserisci l'anno: "));
	}
	
	/**
	 * Prende in input due stringhe e le converte in un orario.
	 * @return L'orario ottenuto da input
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 * @throws DateTimeException se la data o l'orario inseriti in input non sono adeguati per le caratteristiche di LocalDate e LocalTime
	 */
	private static LocalTime inputOrario() throws UndoOperationException, DateTimeException{
		LocalTime newOrario = LocalTime.parse("00:00");
		return newOrario.withHour(inputCampoNum("Inserisci l'ora: ")).withMinute(inputCampoNum("Inserisci i minuti: "));
	}
	
	/**
	 * Metodo che prende in input una stringa.
	 * @param msg Messaggio da far apparire a schermo
	 * @return la stringa ottenuta in input
	 * @throws UndoOperationException nel caso che la stringa sia uguale a "back", ossia la scelta di annullare l'operazione in corso
	 */
	private static String inputCampo(String msg) throws UndoOperationException{
		String input = new String();
		if((input = Input.readString(msg)).equals("back")) throw new UndoOperationException("Operazione annullata");
		return input;
	}
	
	/**
	 * Metodo che prende in input (tramite inputCampo()) una stringa e verifica se tale stringa contine solo valori numerici.
	 * <p>
	 * Se si allora ritorna il valore parsificato in int, altrimenti richiede di inserire il valore
	 * @param msg messaggio da far apparire a scehrmo
	 * @return int parsificato dell'input
	 * @throws UndoOperationException se l'operazione &egrave; annullata
	 */
	private static int inputCampoNum(String msg) throws UndoOperationException{
		String input = new String();
		while(!(input = inputCampo(msg)).matches("[0-9]+")) {
			System.out.println("L'input deve contenere solo valori numerici");
		}
		return Integer.parseInt(input);
	}
}