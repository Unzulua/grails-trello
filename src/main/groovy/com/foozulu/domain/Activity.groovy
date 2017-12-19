package com.foozulu.domain

class Activity {
    ActivityType type
    String user
    String cardName
    String project
    Date date

}


//https://developers.trello.com/reference#action-types
enum ActivityType {
    CREATE_CARD,
    UPDATE_CARD
}
