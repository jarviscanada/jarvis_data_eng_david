# TwitterCLI Project
## Introduction
Purpose of this application is to allow a user to write a tweet, find a tweet based on id, and delete tweets based on id.
From working on this application, I have learned to use MVC (Model-View-Controller) architecture, which splits layers of 
work into a Controller layer, a Service layer, and a Data Access layer, and allows Model tweet data to be displayed in JSON format. 
In addition to this, I have learned how to apply HTTP Mehtods: POST/GET to write/find/delete tweets via Twitter's REST API platform.

## Design
![my_architecture](./assets/twitterArchitecture.svg)

### app/main
Determines if user request is to POST/SHOW/DELETE or none. If the user request is valid, then send the rest of the user input
to the respective Controller method depending on the user request.

### controller
Checks whether the given user arguments for the respective Controller method is valid. If not will throw an IllegalArgumentException. 
Otherwise will continue sending data to service layer.

### service
Further checks whether user arguments are inputted correctly, throwing IllegalArugmentException if so. For Finding a tweet/deleting 
a tweet will further process the Tweet by filtering out requested fields only/order deleted tweets in a list.

### DAO
Finally use data to send HTTP request to the Twitter REST API to perform the operation desired. Displays the outputted data in the 
form of JSON.

## Quick Start
To first clean and build the project, we run:
```mvn clean package```

To run the application, open up Intellij IDE, and add arguments via 'Edit Configurations...' options. <br /> 
Then run the application via TwitterCLIApp/TwitterCLIBean/TwitterCLIComponentScan

Arguments Possible: <br /> 
**Writing a Tweet:** <br /> 
```POST <input text> <latitude>:<longitude>```<br /> 
Posts tweet with given text, and assigned above coordinates.

**Finding a Tweet:** <br /> 
```SHOW <tweet_id> <tweet_field> [<tweet_field>...]```<br /> 
Find a tweet of given <tweet_id>. Will filter the found tweet via the <tweet_field> given by user.
It is mandatory for the user to give at least 1 <tweet_field> or an IllegalArgumentException will be thrown.


**Deleting a Tweet:**<br /> 
```DElETE <tweet_id> [<tweet_id>...]```<br /> 
Delete all given given <tweet_id>. At least 1 <tweet_id> must be given by user or an IllegalArgumentException 
will be thrown.


## Model
Tweet Model is represented by Tweet class, which contains private variables with getters and setters to retrieve 
and set the data. As this project involved a simplified Tweet Model, in this project we were concerned about the
following fields:<br /> 
```
'created at'
'id'
'id_str'
'text'
'entities'
'coordinates'
'retweet_count'
'favorite_count'
'favorited'
'retweeted'
```

Because 'entities' and 'Coordinates' cannot be represented by given Java wrapper classes. I created a separate Coordinates
and Entities class to represent the data. Because Entities contained additional non-scalar data-types, Entities had to 
be defined by additional Hashtag and UserMention objects.


## Improvements
Things I think can be added to the project:<br /> 
1) Add more tweet fields to the Tweet Model.<br /> 
2) Use GET Requests to organize tweets of a user via screen_name<br /> 
3) Allow option to Retweet.
