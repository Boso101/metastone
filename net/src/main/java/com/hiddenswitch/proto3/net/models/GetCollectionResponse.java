package com.hiddenswitch.proto3.net.models;

import com.hiddenswitch.proto3.net.impl.util.CardRecord;
import net.demilich.metastone.game.entities.heroes.HeroClass;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bberman on 1/22/17.
 */
public class GetCollectionResponse implements Serializable {
	private List<CardRecord> cardRecords;
	private CollectionTypes collectionType;
	private HeroClass heroClass;
	private String name;
	private String collectionId;
	private String userId;

	public static GetCollectionResponse user(String collectionId, List<CardRecord> cardRecords) {
		return new GetCollectionResponse()
				.withCollectionId(collectionId)
				.withCardRecords(cardRecords)
				.withUserId(collectionId)
				.withCollectionType(CollectionTypes.USER);
	}

	public static GetCollectionResponse deck(String userId, String deckId, String name, HeroClass heroClass, List<CardRecord> cardRecords) {
		return new GetCollectionResponse()
				.withCollectionId(deckId)
				.withCardRecords(cardRecords)
				.withHeroClass(heroClass)
				.withUserId(userId)
				.withName(name);
	}

	public List<CardRecord> getCardRecords() {
		return cardRecords;
	}

	public void setCardRecords(List<CardRecord> cardRecords) {
		this.cardRecords = cardRecords;
	}

	public HeroClass getHeroClass() {
		return heroClass;
	}

	public void setHeroClass(HeroClass heroClass) {
		this.heroClass = heroClass;
	}

	public GetCollectionResponse withCardRecords(List<CardRecord> cardRecords) {
		this.cardRecords = cardRecords;
		return this;
	}

	public GetCollectionResponse withHeroClass(final HeroClass heroClass) {
		this.heroClass = heroClass;
		return this;
	}

	public CollectionTypes getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(CollectionTypes collectionType) {
		this.collectionType = collectionType;
	}

	public GetCollectionResponse withCollectionType(final CollectionTypes collectionType) {
		this.collectionType = collectionType;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GetCollectionResponse withName(final String name) {
		this.name = name;
		return this;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	private GetCollectionResponse withCollectionId(String collectionId) {
		this.collectionId = collectionId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public GetCollectionResponse withUserId(String userId) {
		this.userId = userId;
		return this;
	}
}


