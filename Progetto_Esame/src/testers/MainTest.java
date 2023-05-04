package testers;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import progetto.Appuntamento;
import progetto.UndoOperationException;

class MainTest {

	
	/**
	 * I test sono stati generati intorno al fatto che in agenda.txt siano presenti:
		01-01-2021, 11-11, 10, luogo1, persona1
		06-05-2022, 17-55, 10, luogo2, persona2
	 */
	
	
	
	boolean eccezioneSollevata;
	
	@BeforeEach
	void init() {
		eccezioneSollevata = false;
		try {
			Main.init();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	@Test
	void testMain() {
		App.main();
	}
	*/
	
	@Test
	void testPrintList() {
		Main.printList(Main.agenda.getList());
	}
	
	@Test
	void testSaveAgendaOnFile() {
		ArrayList<Appuntamento> listPreReload = Main.agenda.getList();
		Main.saveAgendaOnFile();
		init();
		int i = 0;
		for(Appuntamento a: Main.agenda) {
			testEqualsAppuntamento(a, listPreReload.get(i));
			i++;
		}
	}
	
	@Test
	void testInputCampo() {
		assertEquals(Main.inputCampo("[inputCampo] Inserisci mela: "), "mela");
	}
	
	@Test
	void testInputCampoEccezioneSollevata() {
		try {
			Main.inputCampo("[inputCampo] Inserisci back: ");
		} catch(UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testInputCampoNum() {
		assertEquals(Main.inputCampoNum("[inputCampoNum] Inserisci 1: "), 1);
	}
	
	@Test
	void testInputCampoNumInputNonValido() {
		assertEquals(Main.inputCampoNum("[inputCampoNum] Inserisci lettere, poi 1: "), 1);
	}
	
	@Test
	void testInputCampoNumUndoOperationExceptionSollevata() {
		try {
			Main.inputCampoNum("[inputCampoNum] Inserisci back: ");
		} catch (UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testInputData() {
		System.out.println("[inputData] Inserisci giorno: 1\n mese: 1\n anno: 2021");
		assertEquals(Main.inputData(), LocalDate.parse("2021-01-01"));
	}
	
	@Test
	void testInputDataOperazioneAnnullata() {
		System.out.println("[inputData] annulla l'operazione: ");
		try {
			Main.inputData();
		} catch(UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testInputOrario() {
		System.out.println("[inputOrario] Inserisci ora: 1\n minuti: 10");
		assertEquals(Main.inputOrario(), LocalTime.parse("01:10"));
	}
	
	@Test
	void testInputOrarioOperazioneAnnullata() {
		System.out.println("[inputOrario] annulla l'operazione: ");
		try {
			Main.inputOrario();
		} catch(UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testGeneraAppuntamento() {
		System.out.println("[generaAppuntamento] Expercted: 01-01-2021, 01-10, 10, luogo1, persona1");
		testEqualsAppuntamento(Main.generaAppuntamento(), new Appuntamento(LocalDate.parse("2021-01-01"), LocalTime.parse("01:10"), 10, "luogo1", "persona1"));
	}
	
	@Test
	void tesGeneraAppuntamentoOperazioneCancellata() {
		System.out.println("[generaAppuntamento] cancella l'operazione: ");
		try {
			Main.generaAppuntamento();
		} catch(UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testAggiungiSuccesso() {
		System.out.println("[aggiungi]: ");
		assertTrue(Main.aggiungi());
	}
	
	@Test
	void testAggiungiFallimentoPerOverlap() {
		System.out.println("[aggiungi] inserisci 1-1-2021, 11-15, ...");
		assertFalse(Main.aggiungi());
	}
	
	@Test
	void testAggiuntaOperazioneAnnullata() {
		System.out.println("[aggiungi] annulla operazione: ");
		try {
			Main.aggiungi();
		} catch(UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testRimuoviSuccesso() {
		System.out.println("[rimuovi] rimuovi 1: ");
		assertTrue(Main.rimuovi());
	}
	
	@Test
	void testRimuoviRiprovaIndexOutOfBounds() {
		System.out.println("[rimuovi] rimuovi 4, 3, 65, 1: ");
		assertTrue(Main.rimuovi());
	}
	
	@Test
	void testRimuoviOperazioneAnnullata() {
		System.out.println("[rimuovi] annulla operazione: ");
		try {
			Main.rimuovi();
		} catch(UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testModificaSuccesso() {
		System.out.println("[modifica] modifica il primo indice: ");
		try {
			assertTrue(Main.modifica());
		} catch (UndoOperationException e) {
			eccezioneSollevata = true;
		} finally {
			assertFalse(eccezioneSollevata);
		}
	}
	
	@Test
	void testModificaOverlap() {
		System.out.println("[modifica] modifica il secondo indice con:\n[01-01-2021, 11-15, 10, ...] : ");
		try {
			assertFalse(Main.modifica());
		} catch (UndoOperationException e) {
			eccezioneSollevata = false;
		} finally {
			assertFalse(eccezioneSollevata);
		}
	}
	
	@Test
	void testModificaOperazioneAnnullata() {
		System.out.println("[modifica] annulla l'operazione: ");
		try {
			Main.modifica();
		} catch (UndoOperationException e) {
			eccezioneSollevata = true;
			assertEquals(e.getMessage(), "Operazione annullata");
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testCercaOperaioneAnnullata() {
		System.out.println("[cerca] annulla l'operazione: ");
		try {
			Main.cerca();
		} catch (UndoOperationException e) {
			assertEquals(e.getMessage(), "Operazione annullata");
			eccezioneSollevata = true;
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testCercaPerNome() {
		System.out.println("[cerca] cerca per nome (Opzione 1) <pers>: ");
		ArrayList<Appuntamento> results = Main.cerca();
		assertTrue(results.size() == 2);
	}
	
	@Test
	void testCercaPerData() {
		System.out.println("[cerca] cerca per data (Opzione 2) <01-01-2021>: ");
		ArrayList<Appuntamento> results = Main.cerca();
		assertTrue(results.size() == 1);
	}
	
	@Test
	void testCercaTraDate() {
		System.out.println("[cerca] cerca tra date (Opzione 3) <01-01-2021 - 01-01-2023>: ");
		ArrayList<Appuntamento> results = Main.cerca();
		assertTrue(results.size() == 2);
	}
	
	void testEqualsAppuntamento(Appuntamento a, Appuntamento b) {
		assertEquals(a.getData(), b.getData());
		assertEquals(a.getOrario(), b.getOrario());
		assertEquals(a.getDurata(), b.getDurata());
		assertEquals(a.getLuogo(), b.getLuogo());
		assertEquals(a.getPersona(), b.getPersona());
	}
}
