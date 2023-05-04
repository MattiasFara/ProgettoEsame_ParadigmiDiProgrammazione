package progetto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe Appuntamento che serve per generare il tipo a riferimento Appuntamento.
 */

public class Appuntamento {
	
	private LocalDate data;
	private LocalTime orario;
	private int durata;
	private String luogo;
	private String persona;
	
	/**
	 * Formattatore della data [dd-MM-yyyy]
	 */
	private static DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	/**
	 * Formattatore dell'orario [HH-mm]
	 */
	private static DateTimeFormatter orarioFormatter = DateTimeFormatter.ofPattern("HH-mm");
	
	/**
	 * Costruttore della classe Appuntamento.
	 * @param data data dell'appuntamento
	 * @param orario orario inizio appuntamento
	 * @param durata durata dell'appuntamento, espressa in minuti
	 * @param luogo luogo dove si tiene l'appuntamento
	 * @param persona persona con cui si svolge l'appuntamento
	 */
	public Appuntamento (LocalDate data, LocalTime orario, int durata, String luogo, String persona) {
		this.data = data;
		this.orario = orario;
		this.durata = durata;
		this.luogo = luogo;
		this.persona = persona;
	}
	
	/**
	 * Ritorna il valore del campo data.
	 * @return data
	 */
	public LocalDate getData() {
		return data;
	}
	
	/**
	 * Ritorna il valore del campo orario.
	 * @return orario
	 */
	public LocalTime getOrario() {
		return orario;
	}
	
	/**
	 * Ritorna il valore del campo durata.
	 * @return durata
	 */
	public int getDurata() {
		return durata;
	}
	
	/**
	 * Ritorna il valore del campo luogo.
	 * @return luogo
	 */
	public String getLuogo() {
		return luogo;
	}
	
	/**
	 * Ritorna il valore del campo persona.
	 * @return persona
	 */
	public String getPersona() {
		return persona;
	}
	
	/**
	 * Ritorna il formattatore della data.
	 * @return dataFormatter
	 */
	public static DateTimeFormatter getDataFormatter() {
		return dataFormatter;
	}
	
	/**
	 * Ritorna il formattatore dell'orario.
	 * @return orarioFormatter
	 */
	public static DateTimeFormatter getOrarioFormatter() {
		return orarioFormatter;
	}

	/**
	 * Cambia il campo data dell'appuntamento con il parametro fornito di tipo LocalDate.
	 * @param data data dell'appuntamento
	 */
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	/**
	 * Cambia il campo orario dell'appuntamento con il parametro fornito di tipo LocalTime.
	 * @param orario orario di inizio dell'appuntamento
	 */
	public void setOrario(LocalTime orario) {
		this.orario = orario;
	}
	
	/**
	 * Cambia il campo durata dell'appuntamento con il parametro fornito di tipo int.
	 * @param durata darata dell'appuntamento, espressa in minuti
	 */
	public void setDurata(int durata) {
		this.durata = durata;
	}
	
	/**
	 * Cambia il campo luogo dell'appuntamento con il parametro fornito di tipo String.
	 * @param luogo luogo dove si tiene l'appuntamento
	 */
	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}
	
	/**
	 * Cambia il campo persona dell'appuntamento con il parametro fornito di tipo String.
	 * @param persona persona con cui si svolge l'appuntamento
	 */
	public void setPersona(String persona) {
		this.persona = persona;
	}
	
	/**
	 * Ritorna in formato String l'appuntamento:
	 * <p> Fa sovrascrittura del metodo toString della classe Object.
	 * @return "dd-MM-yyyy HH-mm, durata, luogo, persona"
	 */
	@Override
	public String toString() {
		return data.format(dataFormatter) + ", " + orario.format(orarioFormatter) + ", " + Integer.toString(durata) + ", " + luogo + ", " + persona;
	}
}
