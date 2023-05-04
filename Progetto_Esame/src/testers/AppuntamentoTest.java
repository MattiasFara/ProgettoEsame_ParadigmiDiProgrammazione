package testers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import progetto.Appuntamento;

class AppuntamentoTest {

	Appuntamento a;
	Appuntamento b;
	
	String data1 = "01-01-2001";
	String data2 = "02-02-2002";
	
	String orario1 = "11-11";
	String orario2 = "22-22";
	
	/**
	 * Inizializza gli appuntamenti con i campi predefiniti all'inizio del test.
	 */
	@BeforeEach
	void init() {
		a = new Appuntamento(LocalDate.parse(data1, Appuntamento.getDataFormatter()), LocalTime.parse(orario1, Appuntamento.getOrarioFormatter()), 30, "luogo1", "persona1");
		b = new Appuntamento(LocalDate.parse(data2, Appuntamento.getDataFormatter()), LocalTime.parse(orario2, Appuntamento.getOrarioFormatter()), 60, "luogo2", "persona2");
	}
	
	/**
	 * Verifica l'esistenza dei campi appuntamento, se sono stati creati e non nulli.
	 */
	@Test
	void testCreazione() {
		assertFalse(a == null);
		assertFalse(b == null);
		assertEquals(a.getData(), LocalDate.parse(data1, Appuntamento.getDataFormatter()));
		assertEquals(b.getData(), LocalDate.parse(data2, Appuntamento.getDataFormatter()));
	}
	
	/**
	 * Verifico che le istanze create ed inizializate con valori differenti, abbiano appunto valori diversi
	 */
	@Test
	void testIstanzeDiverse() {
		assertFalse(a.getData().equals(b.getData()));
		assertFalse(a.getOrario().equals(b.getOrario()));
		assertFalse(a.getDurata() == b.getDurata());
		assertFalse(a.getLuogo().equals(b.getLuogo()));
		assertFalse(a.getPersona().equals(b.getPersona()));
	}
	
	/**
	 * Verifica se i metodi di get sono funzionanti sulla singola variabile di tipo appuntamento.
	 */
	@Test
	void testGetters() {
		assertEquals(a.getData(), LocalDate.parse(data1, Appuntamento.getDataFormatter()));
		assertEquals(a.getOrario(), LocalTime.parse(orario1, Appuntamento.getOrarioFormatter()));
		assertTrue(a.getDurata() == 30);
		assertEquals(a.getLuogo(), "luogo1");
		assertEquals(a.getPersona(), "persona1");
	}
	
	/**
	 * Verifica se i metodi di get sono funzionanti su molteplici (2) variabili di tipo appuntamento. Si ci aspetta che ogni istanza fornisca i valori propri.
	 */
	@Test
	void testGettersMultipleIstanze() {
		// test della prima istanza di appuntamento
		testGetters();
		// test della seconda istanza di appuntamento
		assertEquals(b.getData(), LocalDate.parse(data2, Appuntamento.getDataFormatter()));
		assertEquals(b.getOrario(), LocalTime.parse(orario2, Appuntamento.getOrarioFormatter()));
		assertTrue(b.getDurata() == 60);
		assertEquals(b.getLuogo(), "luogo2");
		assertEquals(b.getPersona(), "persona2");
		// verifica diffenerze tra le due istanze
		testIstanzeDiverse();
	}
	
	/**
	 * Verifica se i metodi di set sono funzionanti sulla singola variabile di tipo appuntamento.
	 * Dopo aver settato i nuovi campi si verifca tramite i metodi di get.
	 */
	@Test
	void testSetters() {
		String newData = "02-02-2001";
		String newOrario = "12-12";
		// chiamo i metodi setters sulla prima istanza di appuntamento.
		a.setData(LocalDate.parse(newData, Appuntamento.getDataFormatter()));
		a.setOrario(LocalTime.parse(newOrario, Appuntamento.getOrarioFormatter()));
		a.setDurata(35);
		a.setLuogo("nuovo luogo1");
		a.setPersona("nuova persona1");
		// verifico che i metodi setters hanno cambiato i campi dell'istanza di appuntamento.
		assertEquals(a.getData(), LocalDate.parse(newData, Appuntamento.getDataFormatter()));
		assertEquals(a.getOrario(), LocalTime.parse(newOrario, Appuntamento.getOrarioFormatter()));
		assertTrue(a.getDurata() == 35);
		assertEquals(a.getLuogo(), "nuovo luogo1");
		assertEquals(a.getPersona(), "nuova persona1");
	}
	
	/**
	 * Verifica se i metodi di set sono funzionanti su molteplici (2) variabili di tipo appuntamento.
	 * Si ci aspetta che ad essere modificati siano solo i campi dell'istanza su cui vengono chiamati i metodi.
	 */
	@Test
	void testSettersMultipleIstanze() {
		String newData = "03-03-2002";
		String newOrario = "11-11"; // imposto la stessa orario della prima istanza di base, per verificare che al ritorno dal metodo testSetters, la prima istanza abbia modificato e mantenuto le modifiche ai propri campi.
		// chiamo i metodi setters su entrambe le istanze, per ciascuna di esse inserisco dati diversi.
		// verifico che i metodi setters hanno cambiato i campi delle istanze di appuntamento.
		// istanza 1.
		testSetters(); // con la chiamata di questo metodo ho anche testato con i getters
		// istanza 2.
		// test setters
		b.setData(LocalDate.parse(newData, Appuntamento.getDataFormatter()));
		b.setOrario(LocalTime.parse(newOrario, Appuntamento.getOrarioFormatter()));
		b.setDurata(73);
		b.setLuogo("nuovo luogo2");
		b.setPersona("nuova persona2");
		// test getters
		assertEquals(b.getData(), LocalDate.parse(newData, Appuntamento.getDataFormatter()));
		assertEquals(b.getOrario(), LocalTime.parse(newOrario, Appuntamento.getOrarioFormatter()));
		assertTrue(b.getDurata() == 73);
		assertEquals(b.getLuogo(), "nuovo luogo2");
		assertEquals(b.getPersona(), "nuova persona2");
		// verifico che le istanze non contengano campi conteneti gli stessi valori.
		testIstanzeDiverse();
	}
	
	/**
	 * Verifica se il metodo toString sia funzionante e che venga chiamato quello relativo alla classe appuntamento e non il metodo fornito da Object.class.
	 */
	@Test
	void testToString() {
		assertEquals(a.toString(), "01-01-2001, 11-11, 30, luogo1, persona1");
	}
	
	/**
	 * Verifica se il metodo toString sia funzionante su molteplici istanze (2) della classe appuntamento e che non ritorni lo stesso risultato per diverse istanze.
	 */
	@Test
	void testToStringMultipleIstanze() {
		// verifico la prima istanza.
		testToString();
		// verifico la seconda istanza.
		assertEquals(b.toString(), "02-02-2002, 22-22, 60, luogo2, persona2");
		// verifico che le diverse istanze non ritornano le stesse stringhe.
		assertFalse(a.toString().equals(b.toString()));
	}

}
