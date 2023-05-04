package testers;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import testers.Agenda;
import progetto.Appuntamento;
import progetto.OverlapException;
import org.junit.jupiter.api.BeforeEach;

class AgendaTest {

	// instanza le variabili di default per i test
	private Agenda agenda;
	
	private String dataAppuntamento1 = "01-01-2001";
	private String dataAppuntamento2 = "02-02-2002";
	private String dataAppuntamento3 = "03-03-2003";
	
	private String orarioAppuntamento1 = "01-01";
	private String orarioAppuntamento2 = "12-12";
	private String orarioAppuntamento3 = "23-23";
	
	// si suppone che la classe di test di Appuntamento non verifichi errori
	private Appuntamento appuntamento1 = new Appuntamento(LocalDate.parse(dataAppuntamento1, Appuntamento.getDataFormatter()), LocalTime.parse(orarioAppuntamento1, Appuntamento.getOrarioFormatter()), 10, "luogo1", "persona1");
	private Appuntamento appuntamento2 = new Appuntamento(LocalDate.parse(dataAppuntamento2, Appuntamento.getDataFormatter()), LocalTime.parse(orarioAppuntamento2, Appuntamento.getOrarioFormatter()), 20, "luogo2", "persona2");
	private Appuntamento appuntamento3 = new Appuntamento(LocalDate.parse(dataAppuntamento3, Appuntamento.getDataFormatter()), LocalTime.parse(orarioAppuntamento3, Appuntamento.getOrarioFormatter()), 30, "luogo3", "persona3");

	
	void initAggiunta() {
		agenda.add(appuntamento1);
		agenda.add(appuntamento2);
		agenda.add(appuntamento3);
	}
	
	@BeforeEach
	void initTest() {
		agenda = new Agenda();
	}
	
	@Test
	void testCreazione() {
		assertFalse(agenda == null);
		assertTrue(agenda.size() == 0);
	}
	
	@Test
	void testSize() {
		assertTrue(agenda.size() == 0);
		initAggiunta();
		assertTrue(agenda.size() == 3);
	}
	
	@Test
	void testAggiuntaSingola() {
		assertTrue(agenda.size() == 0);
		assertTrue(agenda.add(appuntamento1));
		assertTrue(agenda.size() == 1);
	}
	
	@Test
	void testAggiuntaMultipla() {
		assertTrue(agenda.size() == 0);
		assertTrue(agenda.add(appuntamento1));
		assertTrue(agenda.add(appuntamento2));
		assertTrue(agenda.add(appuntamento3));
		assertTrue(agenda.size() == 3);
	}
	
	@Test
	void testCheckOverlapEccezioneSollevata() {
		boolean eccezioneSollevata = false;
		Appuntamento appuntamentoOverlap = new Appuntamento(LocalDate.parse("01-01-2001", Appuntamento.getDataFormatter()), LocalTime.parse("01-05", Appuntamento.getOrarioFormatter()), 20, "luogoOverlap", "personaOverlap");
		try {
			agenda.checkOverlap(appuntamento1, appuntamentoOverlap);
		} catch(OverlapException e) {
			eccezioneSollevata = true;
			assertEquals(e.getMessage(), "New [01-01-2001 01-05, 20 min] overlaps with [01-01-2001 01-01, 10 min]");
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testCheckOverlapEccezioneNonSollevata() {
		boolean eccezioneSollevata = false;
		try {
			agenda.checkOverlap(appuntamento1, appuntamento2);
		} catch(OverlapException e) {
			eccezioneSollevata = true;
		} finally {
			assertFalse(eccezioneSollevata);
		}
	}
	
	@Test
	void testCheckOverlapListEccezioneSollevata() {
		initAggiunta();
		boolean eccezioneSollevata = false;
		Appuntamento appuntamentoOverlap = new Appuntamento(LocalDate.parse("01-01-2001", Appuntamento.getDataFormatter()), LocalTime.parse("01-05", Appuntamento.getOrarioFormatter()), 20, "luogoOverlap", "personaOverlap");
		try {
			agenda.checkOverlapList(appuntamentoOverlap);
		} catch(OverlapException e) {
			eccezioneSollevata = true;
			assertEquals(e.getMessage(), "New [01-01-2001 01-05, 20 min] overlaps with [01-01-2001 01-01, 10 min]");
		} finally {
			assertTrue(eccezioneSollevata);
		}
	}
	
	@Test
	void testCheckOverlapListEccezioneNonSollevata() {
		initAggiunta();
		boolean eccezioneSollevata = false;
		Appuntamento appuntamentoOverlap = new Appuntamento(LocalDate.parse("05-05-2005", Appuntamento.getDataFormatter()), LocalTime.parse("01-05", Appuntamento.getOrarioFormatter()), 20, "luogoOverlap", "personaOverlap");
		try {
			agenda.checkOverlapList(appuntamentoOverlap);
		} catch(OverlapException e) {
			eccezioneSollevata = true;
		} finally {
			assertFalse(eccezioneSollevata);
		}
	}
	
	@Test
	void testCheckOverlapListSaltaUnAppuntamentoEccezioneNonSollevata() {
		initAggiunta();
		boolean eccezioneSollevata = false;
		Appuntamento appuntamentoOverlap = new Appuntamento(LocalDate.parse("01-01-2001", Appuntamento.getDataFormatter()), LocalTime.parse("01-05", Appuntamento.getOrarioFormatter()), 20, "luogoOverlap", "personaOverlap");
		try {
			agenda.checkOverlapList(appuntamentoOverlap, 0);
		} catch(OverlapException e) {
			eccezioneSollevata = true;
		} finally {
			assertFalse(eccezioneSollevata);
		}
	}
	
	@Test
	void testAggiuntaEccezioneSollevata() {
		initAggiunta();
		assertTrue(agenda.size() == 3);
		Appuntamento appuntamentoOverlap = new Appuntamento(LocalDate.parse("01-01-2001", Appuntamento.getDataFormatter()), LocalTime.parse("01-05", Appuntamento.getOrarioFormatter()), 20, "luogoOverlap", "personaOverlap");
		assertFalse(agenda.add(appuntamentoOverlap));
		assertTrue(agenda.size() == 3);
	}
	
	@Test
	void testRimozioneSuccesso() {
		initAggiunta();
		assertTrue(agenda.size() == 3);
		assertTrue(agenda.remove(appuntamento1));
		assertTrue(agenda.size() == 2);
	}
	
	@Test
	void testRimozioneFallimento() {
		initAggiunta();
		String data = "04-04-2004";
		String orario = "04-04";
		Appuntamento appuntamentoDaRimuovere = new Appuntamento(LocalDate.parse(data, Appuntamento.getDataFormatter()), LocalTime.parse(orario, Appuntamento.getOrarioFormatter()), 100, "luogoDaRimuovere", "personaDaRimuovere");
		assertTrue(agenda.size() == 3);
		assertFalse(agenda.remove(appuntamentoDaRimuovere));
		assertTrue(agenda.size() == 3);
	}
	
	@Test
	void testModifica() {
		initAggiunta();
		String[] parMod = {"10-10-2010", "10-10", "30", "luogoModificato", "personaMdificata"};
		assertTrue(agenda.modify(agenda.getList().indexOf(agenda.getList().get(0)), new Appuntamento(LocalDate.parse(parMod[0], Appuntamento.getDataFormatter()), LocalTime.parse(parMod[1], Appuntamento.getOrarioFormatter()), Integer.parseInt(parMod[2]), parMod[3], parMod[4])));
		ArrayList<Appuntamento> list = agenda.getList();
		assertEquals(list.get(0).getData(), LocalDate.parse(parMod[0], Appuntamento.getDataFormatter()));
		assertEquals(list.get(0).getOrario(), LocalTime.parse(parMod[1],Appuntamento.getOrarioFormatter()));
		assertEquals(list.get(0).getDurata(), Integer.parseInt(parMod[2]));
		assertEquals(list.get(0).getLuogo(), parMod[3]);
		assertEquals(list.get(0).getPersona(), parMod[4]);
		assertFalse(list.get(0).getData().equals(list.get(1).getData()));
	}
	
	@Test
	void testModificaEccezioneSollevata() {
		initAggiunta();
		String[] parMod = {"02-02-2002", "12-10", "30", "luogoModificato", "personaMdificata"};
		Appuntamento appuntamentoPreModifica = agenda.getList().get(0);
		assertFalse(agenda.modify(agenda.getList().indexOf(agenda.getList().get(0)), new Appuntamento(LocalDate.parse(parMod[0], Appuntamento.getDataFormatter()), LocalTime.parse(parMod[1], Appuntamento.getOrarioFormatter()), Integer.parseInt(parMod[2]), parMod[3], parMod[4])));
		ArrayList<Appuntamento> list = agenda.getList();
		assertEquals(list.get(0).getData(), appuntamentoPreModifica.getData());
		assertEquals(list.get(0).getOrario(), appuntamentoPreModifica.getOrario());
		assertEquals(list.get(0).getDurata(), appuntamentoPreModifica.getDurata());
		assertEquals(list.get(0).getLuogo(), appuntamentoPreModifica.getLuogo());
		assertEquals(list.get(0).getPersona(), appuntamentoPreModifica.getPersona());
	}
	
	@Test
	void testGetList() {
		initAggiunta();
		ArrayList<Appuntamento> results = agenda.getList();
		assertEquals(results.get(0), appuntamento1);
		assertEquals(results.get(1), appuntamento2);
		assertEquals(results.get(2), appuntamento3);
	}
	
	@Test
	void testCercaPerNomeSuccesso() {
		initAggiunta();
		ArrayList<Appuntamento> results = new ArrayList<Appuntamento>();
		results = agenda.search(agenda.getList(), "perso", (String s, Appuntamento a) -> a.getPersona().startsWith(s));
		assertTrue(results.size() == 3);
	}
	
	@Test
	void testCercaPerDataSuccesso() {
		initAggiunta();
		LocalDate dataRicerca = LocalDate.parse("01-01-2001", Appuntamento.getDataFormatter());
		ArrayList<Appuntamento> results = new ArrayList<Appuntamento>();
		results = agenda.search(agenda.getList(), dataRicerca, (LocalDate data, Appuntamento a) -> a.getData().equals(data));
		assertTrue(results.size() == 1);
	}
	
	@Test
	void testCercaTraDateSuccesso() {
		initAggiunta();
		LocalDate dataInizioRicerca = LocalDate.parse("01-01-2002", Appuntamento.getDataFormatter());
		LocalDate dataFineRicerca = LocalDate.parse("01-01-2005", Appuntamento.getDataFormatter());
		ArrayList<Appuntamento> results = new ArrayList<Appuntamento>();
		results = agenda.search(agenda.search(agenda.getList(),
					dataInizioRicerca,
					(LocalDate dataInizio, Appuntamento a) ->a.getData().isAfter(dataInizio) || a.getData().equals(dataInizio)), 
				dataFineRicerca, 
				(LocalDate dataFine, Appuntamento a) -> a.getData().isBefore(dataFine)||a.getData().equals(dataFine));
		assertTrue(results.size() == 2);
	}
	
	@Test
	void testCercaPerNomeFallimento() {
		initAggiunta();
		ArrayList<Appuntamento> results = new ArrayList<Appuntamento>();
		results = agenda.search(agenda.getList(), "luogo", (String s, Appuntamento a) -> a.getPersona().startsWith(s));
		assertTrue(results.size() == 0);
	}
	
	@Test
	void testCreazioneIteratore() {
		Iterator<Appuntamento> itr = agenda.iterator();
		assertFalse(itr == null);
	}
	
	@Test
	void testForEachIteratore() {
		initAggiunta();
		int counter = 0;
		for(Appuntamento a : agenda) {
			assertFalse(a == null);
			counter++;
		}
		assertTrue(counter == 3);
	}
	
	@Test
	void testHasNextIteratore() {
		Iterator<Appuntamento> itr = agenda.iterator();
		assertFalse(itr.hasNext());
		initAggiunta();
		itr = agenda.iterator();
		assertTrue(itr.hasNext());
	}
	
	@Test
	void testNextIteratore() {
		initAggiunta();
		Iterator<Appuntamento> itr = agenda.iterator();
		for(int i = 0; i < 3; i++) {
			assertEquals(agenda.getList().get(i), itr.next());
		}
	}
	
	@Test
	void testRemoveIteratore() {
		initAggiunta();
		Iterator<Appuntamento> itr = agenda.iterator();
		ArrayList<Appuntamento> agendaPreRimozione = agenda.getList();
		assertTrue(agenda.size() == 3);
		itr.remove();
		assertTrue(agenda.size() == 2);
		for(int i = 0; i < 2; i++) {
			assertEquals(agendaPreRimozione.get(i+1), agenda.getList().get(i));
		}
	}
	
}
