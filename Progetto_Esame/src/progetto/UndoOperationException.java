package progetto;

/**
 * Eccezione sollevata per annullare un'esecuzione di un metodo.
 */

public class UndoOperationException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1094192761074928240L;

	/**
	 * Eccezione sollevata quando c'&egrave; bisogno di alterare il normale flusso di esecuzione del programma su richiesta dell'utente.
	 * <p>
	 * L'eccezione serve per fermare un metodo in corso, annullandone l'esecuzione.
	 * @param msg
	 */
	public UndoOperationException(String msg) {
		super(msg);
	}
}
