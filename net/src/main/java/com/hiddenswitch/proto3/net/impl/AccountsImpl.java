package com.hiddenswitch.proto3.net.impl;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import com.hiddenswitch.proto3.net.*;
import com.hiddenswitch.proto3.net.amazon.*;
import com.hiddenswitch.proto3.net.models.*;
import com.hiddenswitch.proto3.net.util.Broker;
import com.lambdaworks.crypto.SCryptUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.hiddenswitch.proto3.net.util.QuickJson.fromJson;
import static com.hiddenswitch.proto3.net.util.QuickJson.json;
import static com.hiddenswitch.proto3.net.util.QuickJson.toJson;
import static io.vertx.ext.sync.Sync.awaitResult;

public class AccountsImpl extends Service<AccountsImpl> implements Accounts {
	private static String USERS = "accounts.users";
	private Pattern usernamePattern = Pattern.compile("[A-Za-z0-9_]+");

	@Override
	@Suspendable
	public void start() throws SuspendExecution {
		super.start();
		Broker.of(this, Accounts.class, vertx.eventBus());
		List<String> collections = awaitResult(h -> getMongo().getCollections(h));
		if (!collections.contains(USERS)) {
			Void r1 = awaitResult(h -> getMongo().createCollection(USERS, h));
			Void r2 = awaitResult(h -> getMongo().createIndex(USERS, json("profile.emailAddress", 1), h));
		}
	}

	public CreateAccountResponse createAccount(String emailAddress, String password, String username) throws SuspendExecution, InterruptedException {
		CreateAccountRequest request = new CreateAccountRequest();
		request.emailAddress = emailAddress;
		request.password = password;
		request.name = username;
		return this.createAccount(request);
	}

	public LoginResponse login(String email, String password) throws SuspendExecution, InterruptedException {
		LoginRequest request = new LoginRequest();
		request.setPassword(password);
		request.setEmail(email);
		return this.login(request);
	}

	@Override
	public CreateAccountResponse createAccount(CreateAccountRequest request) throws SuspendExecution, InterruptedException {
		CreateAccountResponse response = new CreateAccountResponse();

		if (!isValidName(request.name)) {
			response.invalidName = true;
			return response;
		}

		if (!isValidEmailAddress(request.emailAddress)
				|| emailExists(request.emailAddress)) {
			response.invalidEmailAddress = true;
			return response;
		}

		final String password = request.password;
		if (!isValidPassword(password)) {
			response.invalidPassword = true;
			return response;
		}

		String userId = RandomStringUtils.randomAlphanumeric(36).toLowerCase();
		UserRecord record = new UserRecord(userId);
		Profile profile = new Profile();
		profile.setEmailAddress(request.emailAddress);
		profile.setDisplayName(request.name);
		record.setProfile(profile);

		final String scrypt = hashedPassword(password);
		LoginToken token = LoginToken.createSecure(userId);

		final AuthorizationRecord auth = new AuthorizationRecord();
		auth.setScrypt(scrypt);
		auth.setTokens(Collections.singletonList(new HashedLoginSecret(token)));
		record.setAuth(auth);

		String ignored = awaitResult(h -> getMongo().insert(USERS, toJson(record), h));

		response.userId = userId;
		response.loginToken = token;

		return response;
	}

	private String hashedPassword(String password) {
		return SCryptUtil.scrypt(password, 16384, 8, 1);
	}

	@Override
	@Suspendable
	public LoginResponse login(LoginRequest request) {
		if (request.getEmail() == null) {
			return new LoginResponse(true, false);
		}
		if (request.getPassword() == null) {
			return new LoginResponse(false, true);
		}

		final String email = request.getEmail();
		UserRecord userRecord = getWithEmail(email);

		if (userRecord == null) {
			return new LoginResponse(true, false);
		}

		if (request.getPassword() != null
				&& !SCryptUtil.check(request.getPassword(), userRecord.getAuth().getScrypt())) {
			return new LoginResponse(false, true);
		}

		// Since we don't store the tokens unhashed, we have to add this token always. We slice down five tokens.
		LoginToken token = LoginToken.createSecure(userRecord.getId());
		HashedLoginSecret hashedLoginSecret = new HashedLoginSecret(token);
		final int sliceLastFiveElements = -5;

		// Update the record with the new token.
		MongoClientUpdateResult updateResult = awaitResult(h -> getMongo().updateCollection(USERS, json("_id", userRecord.getId()),
				json("$push", json("auth.tokens", json("$each", Collections.singletonList(toJson(hashedLoginSecret)), "$slice", sliceLastFiveElements))), h));

		if (updateResult.getDocModified() == 0) {
			throw new RuntimeException();
		}

		return new LoginResponse(token, userRecord);
	}

	@Override
	@Suspendable
	public UserRecord getWithToken(String token) {
		final String[] components = token.split(":");
		final String userId = components[0];
		final String secret = components[1];

		UserRecord record = get(userId);
		if (isAuthorizedWithToken(record, secret)) {
			return record;
		} else {
			return null;
		}
	}

	@Suspendable
	public UserRecord get(String userId) {
		List<JsonObject> records = awaitResult(h -> getMongo().find(USERS, json("_id", userId), h));
		return fromJson(records.get(0), UserRecord.class);
	}

	@Suspendable
	public UserRecord getWithEmail(String email) {
		List<JsonObject> records = awaitResult(h -> getMongo().find(USERS, json("profile.emailAddress", email), h));

		if (records.size() == 0) {
			return null;
		}

		return fromJson(records.get(0), UserRecord.class);
	}

	@SuppressWarnings("unchecked")
	public boolean isAuthorizedWithToken(String userId, String secret) throws SuspendExecution, InterruptedException {
		if (secret == null
				|| userId == null) {
			return false;
		}
		JsonObject result = awaitResult(h -> getMongo().findOne(USERS, json("_id", userId), json("auth.tokens", 1), h));

		if (result == null
				|| result.isEmpty()) {
			return false;
		}

		List<HashedLoginSecret> tokens = fromJson(result.getJsonObject("auth").getJsonArray("tokens"), HashedLoginSecret.class);

		return isTokenInList(secret, tokens);
	}

	public boolean isAuthorizedWithToken(UserRecord record, String secret) {
		return isTokenInList(secret, record.getAuth().getTokens());
	}

	private boolean isTokenInList(String secret, List<HashedLoginSecret> hashedSecrets) {
		for (HashedLoginSecret loginToken : hashedSecrets) {
			if (SCryptUtil.check(secret, loginToken.getHashedLoginToken())) {
				return true;
			}
		}

		return false;
	}


	private boolean isValidPassword(String password) {
		return password != null && password.length() >= 1;
	}

	private boolean emailExists(String emailAddress) throws SuspendExecution, InterruptedException {
		Long count = awaitResult(h -> getMongo().count(USERS, json("profile.emailAddress", emailAddress), h));
		return count != 0;
	}

	private boolean isValidEmailAddress(String emailAddress) {
		return EmailValidator.getInstance().isValid(emailAddress);
	}

	private boolean isValidName(String name) {
		return getUsernamePattern().matcher(name).matches()
				&& !isVulgar(name);
	}

	private boolean isVulgar(String name) {
		return false;
	}

	private Pattern getUsernamePattern() {
		return usernamePattern;
	}
}
