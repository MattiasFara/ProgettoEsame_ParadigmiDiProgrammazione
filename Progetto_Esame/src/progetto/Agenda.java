package progetto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe Agenda che si occupa di gestire una agenda di tipo ArrayList<Appuntamento>.
 */

public class Agenda implements Iterable<Appuntamento> {

	private ArrayList<Appuntamento> agenda;
	
	/**
	 * Costruttore della classe Agenda.
	 * Inizializza una agenda di tipo ArrayList<Appuntamento> vuota.
	 */
	public Agenda() {
		agenda = new ArrayList<Appuntamento>();
	}
	
	/**
	 * Iteratore per Agenda.
	 * Fornisce i metodi: hasNext(), next() e remove() che rimuove l'elemento a cui punta l'iteratore.
	 */
	@Override
	public Iterator<Appuntamento> iterator() {
		// bisogna inserire le funzioni principali dell'iteratore, per farlo funzionare almeno.
		Iterator<Appuntamento> itr = new Iterator<Appuntamento>() {
			
			private int index = 0;
			private int maxSize = agenda.size();
			
			@Override
			public boolean hasNext() {
				return index < maxSize && agenda.get(index) != null;
			}
			
			@Override
			public Appuntamento next() {
				return agenda.get(index++);
			}
			
			@Override
			public void remove() {
				if(maxSize > 0) {
					agenda.remove(index);
					maxSize = agenda.size();
				}
			}
		};
		return itr;
	}
	
	/**
	 * Aggiunge un appuntamento alla lista agenda.
	 * <p>
	 * Alla chiamata del metodo viene verificato che l'appuntamento non si sovrapponga con gli elementi presenti nella agenda.
	 * Se si verifica sovrapposizione viene catturata e gestita l'eccezione <OverlapException>, stampando a schermo il messaggio di errore, e ritornando false, indicante una non riuscita dell'aggiunta.
	 * @return true se l'oggetto &egrave; stato aggiunto, false altrimenti
	 */
	public boolean add(Appuntamento a) {
		try {
			checkOverlapList(a);
		} catch(OverlapException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return agenda.add(a);
	}
	
	/**
	 * Verifica sovrapposizioni.
	 * <p>
	 * Verifica se l'appuntamento passato come argomento si sovrappone con almeno un appuntamento appartenente all'agenda.
	 * Se si verifca sovrapposizione viene sollevata l'eccezione <OverlapException> da dover gestire.
	 * @param a appuntamento da verificare
	 * @throws OverlapException se l'appuntamento si sovrappone con un altro appuntamento della lista
	 */
	public void checkOverlapList(Appuntamento a) throws OverlapException {
		for(Appuntamento e: agenda) {
			checkOverlap(e, a);
		}
	}
	
	/**
	 * Verifica sovrapposizioni.
	 * <p>
	 * Verifica se l'appuntamento passato come argomento si sovrappone con almeno un appuntamento appartenente all'agenda(escludendo l'appuntamento cui indice &egrave; stato passato come parametro).
	 * Se si verifca sovrapposizione viene sollevata l'eccezione <OverlapException> da dover gestire.
	 * @param a appuntamento da verificare
	 * @throws OverlapException se l'appuntamento si sovrappone con un altro appuntamento della lista
	 */
	private void checkOverlapList(Appuntamento a, int i) throws OverlapException {
		int counter = 0;
		for(Appuntamento e: agenda) {
			if(counter != i) checkOverlap(e, a);
			counter++;
		}
	}
	
	/**
	 * Verifica sovrapposizione.
	 * <p>
	 * Verifica che i due appuntamenti passati come argomento non si sovrappongono. Se collidono allora viene sollevata l'eccezione <OverlapException>.
	 * @param a primo appuntamento
	 * @param b secondo appuntamento
	 * @throws OverlapException se gli appuntamenti si sovrappongono
	 */
	private void checkOverlap(Appuntamento a, Appuntamento b) throws OverlapException {
		// Trasformo la data ed orario in un unica variabile localDateTime in modo da poter usare i metodi isAfter() e isBefore().
		LocalDateTime dataInizioPrimoAppuntamento = a.getData().atTime(a.getOrario());
		LocalDateTime dataInizioSecondoAppuntamento = b.getData().atTime(b.getOrario());
		
		if(dataInizioPrimoAppuntamento.isAfter(dataInizioSecondoAppuntamento) && dataInizioPrimoAppuntamento.isBefore(dataInizioSecondoAppuntamento.plusMinutes(b.getDurata()))
				|| dataInizioSecondoAppuntamento.isAfter(dataInizioPrimoAppuntamento) && dataInizioSecondoAppuntamento.isBefore(dataInizioPrimoAppuntamento.plusMinutes(a.getDurata()))) {
			throw new OverlapException(a, b);
		}
	}
	
	/**
	 * Rimuove un oggetto dalla agenda.
	 * <p>
	 * L'oggetto deve essere passato come argomento.
	 * @param a appuntamento da rimuovere
	 * @return true se l'oggetto &egrave; stato rimosso, false altrimenti
	 */
	public boolean remove(Appuntamento a) {
		return agenda.remove(a);
	}
	
	/**
	 * Modifica un appuntamento.
	 * <p>
	 * Sostituisce l'appuntamento all'indice passato come argomento, con l'appuntamento passato come argomento.
	 * Verifica, chiamando checkOverlapList() che l'appuntamento sostituente non si sovrapponga con gli elementi della lista, escludendo l'elemnto da sostituire.
	 * Se si sovrappone allora cattura e gestisce l'eccezione OverlapException ritornando false
	 * @param index indice dell'appuntamento da modificare
	 * @param appuntamentoModificato appuntamento che sostituisce l'appuntamento passato come indice
	 * @return true se l'appuntamento &egrave; stato modificato, false altrimenti
	 */
	public boolean modify(int index, Appuntamento appuntamentoModificato) {
		try {
			checkOverlapList(appuntamentoModificato, index);
		} catch (OverlapException e) {
			System.out.println(e.getMessage());
			return false;
		}
		agenda.set(index, appuntamentoModificato);
		return true;
	}
	
	/**
	 * Ritorna una copia della agenda.
	 * @return una copia della agenda di tipo ArrayList<Appuntamento>
	 */
	public ArrayList<Appuntamento> getList() {
		return new ArrayList<Appuntamento>(agenda);
	}
	
	/**
	 * Ritorna le dimensioni della agenda.
	 * @return le dimensioni dell'agenda
	 */
	public int size() {
		return agenda.size();
	}
	
	/**
	 * Interfaccia funzionale per la ricerca di un elemento all'interno dell'agenda.
	 * <p>
	 * Possiede il metodo searchMethod che prende come argomenti una chiave di ricerca ed un appuntamento.
	 * @param <T> il tipo di dato della chiave di ricerca
	 */
	public interface Search<T> {
		boolean searchMethod(T s, Appuntamento a);
	}
	
	/**
	 * Ricerca nella lista.
	 * <p>
	 * Metodo che si occupa di generare un ArrayList di appuntamenti che, secondo il criterio di ricerca, passato come argomento, restituiscono true.
	 * Viene chiamata l'espressione passata come argomento, su ogni appuntamento appartenente alla lista. Se l'espressione ritorna true, allora viene aggiunto all'ArrayList<Appuntamenti> degli elementi corrispondetni alla ricerca.
	 * @param <T> il tipo di dato della chiave di ricerca
	 * @param list ArrayList<Appuntamento> su cui cercare
	 * @param key chiave di ricerca
	 * @param searchCriteria criterio di ricerca, espressione lambda, un'espresione che ritorni true o false
	 * @return ArrayList<Appuntamento> contenente gli elementi risultati positivi al criterio di ricerca
	 */
	public <T> ArrayList<Appuntamento> search(ArrayList<Appuntamento> list, T key, Search<T> searchCriteria) {
		ArrayList<Appuntamento> results = new ArrayList<Appuntamento>();
		for(Appuntamento a: list) {
			if(searchCriteria.searchMethod(key, a)) {
				results.add(a);
			}
		}
		return results;
	}
}
