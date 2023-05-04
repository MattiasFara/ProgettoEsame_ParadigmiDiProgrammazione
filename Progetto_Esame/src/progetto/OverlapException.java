package progetto;

/**
 * Eccezione sollevata per sovrapposizione appuntamenti.
 */

public class OverlapException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2006539153988011449L;

	/**
	 * Eccezione sollevata quando due istanze della classe Appuntamento si sovrappongono in termini di tempo.
	 * @param oldA appuntamento pre esistente.
	 * @param newA appuntamento nuovo.
	 */
	public OverlapException(Appuntamento oldA, Appuntamento newA) {
		super("New [" + newA.getData().format(Appuntamento.getDataFormatter()) + " " + newA.getOrario().format(Appuntamento.getOrarioFormatter()) + ", " + Integer.toString(newA.getDurata()) 
		+ " min] overlaps with [" + 
		oldA.getData().format(Appuntamento.getDataFormatter()) + " " + oldA.getOrario().format(Appuntamento.getOrarioFormatter()) + ", " + Integer.toString(oldA.getDurata()) + " min]");
	}

}
