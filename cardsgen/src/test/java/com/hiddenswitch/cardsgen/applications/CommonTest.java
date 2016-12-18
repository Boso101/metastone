package com.hiddenswitch.cardsgen.applications;

import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.decks.Deck;
import net.demilich.metastone.game.decks.DeckCatalogue;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CommonTest {
	@Test
	public void testGetDefaultDecks() throws Exception {
		assertTrue(Common.getDefaultDecks().size() > 0);
	}

	@Test
	public void testAllDecksValid() throws Exception {
		CardCatalogue.loadCardsFromPackage();
		DeckCatalogue.loadDecksFromPackage();
		Common.getDefaultDecks().stream().map(d -> {
			Deck d1 = DeckCatalogue.getDeckByName(d);
			assertNotNull(d1);
			return d1;
		}).forEach(d -> {
			d.getCards().toList().forEach(Assert::assertNotNull);
		});
	}
}