# client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import com.hiddenswitch.proto3.net.client.*;
import com.hiddenswitch.proto3.net.client.auth.*;
import com.hiddenswitch.proto3.net.client.model.*;
import com.hiddenswitch.proto3.net.client.api.DefaultApi;

import java.io.File;
import java.util.*;

public class DefaultApiExample {

    public static void main(String[] args) {
        
        DefaultApi apiInstance = new DefaultApi();
        CreateAccountRequest request = new CreateAccountRequest(); // CreateAccountRequest | 
        try {
            CreateAccountResponse result = apiInstance.createAccount(request);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#createAccount");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://api-us-east-2.hiddenswitch.com:8080/v1*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**createAccount**](docs/DefaultApi.md#createAccount) | **PUT** /accounts | 
*DefaultApi* | [**decksDelete**](docs/DefaultApi.md#decksDelete) | **DELETE** /decks/{deckId} | 
*DefaultApi* | [**decksGet**](docs/DefaultApi.md#decksGet) | **GET** /decks/{deckId} | 
*DefaultApi* | [**decksPut**](docs/DefaultApi.md#decksPut) | **PUT** /decks | 
*DefaultApi* | [**decksUpdate**](docs/DefaultApi.md#decksUpdate) | **POST** /decks/{deckId} | 
*DefaultApi* | [**getAccount**](docs/DefaultApi.md#getAccount) | **GET** /accounts/{targetUserId} | 
*DefaultApi* | [**getAccounts**](docs/DefaultApi.md#getAccounts) | **GET** /accounts | 
*DefaultApi* | [**login**](docs/DefaultApi.md#login) | **POST** /accounts | 
*DefaultApi* | [**matchmakingConstructedQueueDelete**](docs/DefaultApi.md#matchmakingConstructedQueueDelete) | **DELETE** /matchmaking/constructed/queue | 
*DefaultApi* | [**matchmakingConstructedQueuePut**](docs/DefaultApi.md#matchmakingConstructedQueuePut) | **PUT** /matchmaking/constructed/queue | 


## Documentation for Models

 - [Account](docs/Account.md)
 - [CardRecord](docs/CardRecord.md)
 - [CreateAccountRequest](docs/CreateAccountRequest.md)
 - [CreateAccountResponse](docs/CreateAccountResponse.md)
 - [DecksGetResponse](docs/DecksGetResponse.md)
 - [DecksPutRequest](docs/DecksPutRequest.md)
 - [DecksPutResponse](docs/DecksPutResponse.md)
 - [DecksUpdateCommand](docs/DecksUpdateCommand.md)
 - [DecksUpdateCommandPushInventoryIds](docs/DecksUpdateCommandPushInventoryIds.md)
 - [GetAccountsRequest](docs/GetAccountsRequest.md)
 - [GetAccountsResponse](docs/GetAccountsResponse.md)
 - [InventoryCollection](docs/InventoryCollection.md)
 - [JavaSerializationObject](docs/JavaSerializationObject.md)
 - [LoginRequest](docs/LoginRequest.md)
 - [LoginResponse](docs/LoginResponse.md)
 - [MatchCancelResponse](docs/MatchCancelResponse.md)
 - [MatchmakingDeck](docs/MatchmakingDeck.md)
 - [MatchmakingQueuePutRequest](docs/MatchmakingQueuePutRequest.md)
 - [MatchmakingQueuePutResponse](docs/MatchmakingQueuePutResponse.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### TokenSecurity

- **Type**: API key
- **API key parameter name**: X-Auth-Token
- **Location**: HTTP header


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issue.

## Author

benjamin.s.berman@gmail.com

