package com.hiddenswitch.proto3.net.models;

import net.demilich.metastone.game.cards.CardSet;
import net.demilich.metastone.game.entities.heroes.HeroClass;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bberman on 1/19/17.
 */
public class CreateCollectionRequest implements Serializable {
	private CollectionTypes type;
	private String userId;
	private List<String> cardIds;
	private List<String> inventoryIds;
	private String name;
	private QueryCardsRequest queryCardsRequest;
	private HeroClass heroClass;

	protected CreateCollectionRequest() {
	}

	public static CreateCollectionRequest deck(String userId, String name, HeroClass heroClass, List<String> inventoryIds) {
		return new CreateCollectionRequest()
				.withType(CollectionTypes.DECK)
				.withName(name)
				.withUserId(userId)
				.withHeroClass(heroClass)
				.withInventoryIds(inventoryIds);
	}

	public static CreateCollectionRequest startingCollection(String userId) {
		return new CreateCollectionRequest()
				.withType(CollectionTypes.USER)
				.withUserId(userId)
				.withName(userId + "'s Collection")
				.withOpenCardPack(new OpenCardPackRequest()
						.withUserId(userId)
						.withSets(CardSet.MINIONATE)
						.withNumberOfPacks(5)
						.withCardsPerPack(5))
				.withCardsQuery(new QueryCardsRequest()
						.withSets(CardSet.BASIC, CardSet.CLASSIC));
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public OpenCardPackRequest getOpenCardPackRequest() {
		return openCardPackRequest;
	}

	public void setOpenCardPackRequest(OpenCardPackRequest openCardPackRequest) {
		this.openCardPackRequest = openCardPackRequest;
	}

	private OpenCardPackRequest openCardPackRequest;

	public QueryCardsRequest getQueryCardsRequest() {
		return queryCardsRequest;
	}

	public void setQueryCardsRequest(QueryCardsRequest queryCardsRequest) {
		this.queryCardsRequest = queryCardsRequest;
	}

	public CreateCollectionRequest withType(CollectionTypes type) {
		this.type = type;
		return this;
	}

	public CreateCollectionRequest withUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public CollectionTypes getType() {
		return type;
	}

	public void setType(CollectionTypes type) {
		this.type = type;
	}

	public CreateCollectionRequest withCardIds(List<String> cardIds) {
		this.cardIds = cardIds;
		return this;
	}

	public CreateCollectionRequest withCardsQuery(QueryCardsRequest queryCardsRequest) {
		this.queryCardsRequest = queryCardsRequest;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public CreateCollectionRequest withOpenCardPack(OpenCardPackRequest openCardPackRequest) {
		this.openCardPackRequest = openCardPackRequest;
		return this;
	}

	public List<String> getCardIds() {
		return cardIds;
	}

	public void setCardIds(List<String> cardIds) {
		this.cardIds = cardIds;
	}

	public List<String> getInventoryIds() {
		return inventoryIds;
	}

	public void setInventoryIds(List<String> inventoryIds) {
		this.inventoryIds = inventoryIds;
	}

	public CreateCollectionRequest withInventoryIds(final List<String> inventoryIds) {
		this.inventoryIds = inventoryIds;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CreateCollectionRequest withName(final String name) {
		this.name = name;
		return this;
	}

	public HeroClass getHeroClass() {
		return heroClass;
	}

	public void setHeroClass(HeroClass heroClass) {
		this.heroClass = heroClass;
	}

	public CreateCollectionRequest withHeroClass(HeroClass heroClass) {
		this.heroClass = heroClass;
		return this;
	}
}
